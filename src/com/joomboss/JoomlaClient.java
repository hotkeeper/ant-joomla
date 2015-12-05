/*
 * JoomlaClient.java
 * 
 * Copyright (C) 2011 JoomBoss (http://joomboss.com )
 * Copyright (C) 2012 Harry Klein (http://www.joomla-hklein.de )
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 */
package com.joomboss;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class JoomlaClient {
	private String token = "";
	private final String baseUrl;
	private final DefaultHttpClient httpclient;
	private final boolean debug;
	private final RegEx regEx;

	public JoomlaClient(final boolean debug, final String baseUrl, final RegEx regEx) {
		this.debug = debug;
		this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
		this.httpclient = new DefaultHttpClient();
		this.regEx = regEx;
	}

	public void installPackage(final String adminLogin, final String adminPassword, final File packageSource, final String ftpLogin,
			final String ftpPassword) throws IOException {
		login(baseUrl, adminLogin, adminPassword);
		prepareInstallion();
		install(packageSource, ftpLogin, ftpPassword);
		logout();
	}

	public void deleteComponent(final String extensionName, final String extensionType, final String adminLogin,
			final String adminPassword, final String ftpLogin, final String ftpPassword) throws IOException {
		login(baseUrl, adminLogin, adminPassword);
		final String componentId = getExtentionId(extensionName, extensionType);
		delete(extensionName, componentId, ftpLogin, ftpPassword);
	}

	private void login(final String url, final String user, final String password) throws IOException {
		traceAction("Login");
		final HttpGet httpget = new HttpGet(baseUrl + "/administrator/index.php");
		HttpResponse response;
		response = httpclient.execute(httpget);
		checkHttpStatusCode(response);
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity);
		updateToken(body);
		final HttpPost httpost = new HttpPost(baseUrl + "/administrator/index.php");
		final List<NameValuePair> nvps = getHttpParamerForLogin(user, password);
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		response = httpclient.execute(httpost);
		entity = response.getEntity();
		if (entity != null) {
			body = EntityUtils.toString(entity);
			if (body.indexOf(regEx.getLOGIN_SUCCESSFULL_TEXT_EN()) < 0) {
				checkErrorMessage(body);
				throw new IllegalStateException("Unable to login to " + baseUrl + "/administrator/index.php. ");
			}
		}
		traceAction("Login: successful");
	}

	private void install(final File archieveFile, final String ftpUser, final String ftpPassword) throws IOException {
		traceAction("Installion");
		if (!archieveFile.canRead()) {
			throw new IllegalStateException("Can't read installtion file [" + archieveFile + "]. Please check your configuration.");
		}
		final MultipartEntity reqEntity = getHttpParameterForInstallion(archieveFile, ftpUser, ftpPassword);
		final HttpPost httpost = new HttpPost(this.baseUrl + "/administrator/index.php?option=com_installer&view=install");
		httpost.setEntity(reqEntity);
		final String body = executeHttpRequest(httpost);
		checkErrorMessage(body);
		if (Pattern.compile(regEx.getINSTALLION_WAS_SUCCESSFULL_REGEG_EN(), Pattern.MULTILINE).matcher(body).find()) {
			logHtml(body);
		} else {
			if (Pattern.compile(regEx.getINSTALLION_WAS_FAILED_REGEG_EN(), Pattern.MULTILINE).matcher(body).find()) {
				logHtmlError(body);
				throw new IllegalStateException("Fehler bei der Installation. Fehlermeldung [" + regEx.getINSTALLION_WAS_FAILED_REGEG_EN()
						+ "] in der Antwort gefunden");
			} else {
				logHtmlError(body);
				throw new IllegalStateException("Unbekannter Fehler.");
			}
		}
		traceAction("Installion was successful");
	}

	private void logout() throws IOException {
		traceAction("Logout");
		final HttpGet httpget = new HttpGet(this.baseUrl + "/administrator/index.php?option=com_login&task=logout&" + token + "=1");
		final HttpResponse response = httpclient.execute(httpget);
		final String body = EntityUtils.toString(response.getEntity());
		logHtml(body);
		checkIfLogoutWasSuccessful(body);
	}

	private void delete(final String componentName, final String componentId, final String ftpUser, final String ftpPassword)
			throws IOException {
		traceAction("Delete Extention");
		final List<NameValuePair> nvps = getHttpParameterForDeleteExtention(componentId, ftpUser, ftpPassword);
		final HttpPost httpost = new HttpPost(this.baseUrl + "/administrator/index.php");
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		final HttpResponse response = httpclient.execute(httpost);
		final String body = EntityUtils.toString(response.getEntity());
		logHtml(body);
		checkErrorMessage(body);
		checkIfDeleteWasSuccessful(body, componentName);
		traceAction("Delete Extention: successful");
	}

	private void prepareInstallion() throws IOException, ClientProtocolException {
		traceAction("Prepare Installion Component");
		final HttpGet httpget = new HttpGet(this.baseUrl + "/administrator/index.php?option=com_installer");
		final HttpResponse response = httpclient.execute(httpget);
		final HttpEntity entity = response.getEntity();
		final String body = EntityUtils.toString(entity);
		traceAction("Prepare Installion Component was successful");

		updateToken(body);
	}

	private void checkErrorMessage(final String body) {
		final Pattern pattern = Pattern.compile(regEx.getGET_ERROR_MESSAGE_REGEX(), Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(body);
		if (matcher.find()) {
			logHtmlError(body);
			throw new IllegalStateException(matcher.group(1));
		}
	}

	private void checkIfDeleteWasSuccessful(final String body, final String componentName) {
		final Pattern pattern = Pattern.compile(regEx.getUNINSTALLION_WAS_SUCCESSFULL_REGEG_EN());
		final Matcher matcher = pattern.matcher(body);
		if (matcher.find()) {
			traceAction("The removal of  [" + componentName + "] was successful");
		} else {
			logHtmlError(body);
			throw new IllegalStateException("Can't find the success message. Pleace check the html output.");
		}
	}

	// bei J3.1.5 kommt bei der Abmedlung ein dt. Text: Benutzername
	private void checkIfLogoutWasSuccessful(final String body) {
		final Pattern pattern = Pattern.compile(regEx.getLOGOUT_WAS_SUCCESSFULL_REGEG_EN());
		final Matcher matcher = pattern.matcher(body);
		if (matcher.find()) {
			traceAction("Logout was successful");
		} else {
			logHtmlError(body);
			throw new IllegalStateException("Can't logout.");
		}
	}

	private void checkHttpStatusCode(final HttpResponse response) {
		final int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 404) {
			throw new IllegalStateException("The HTTP status code was 404. Please check your URL.");
		}
	}

	private String getExtentionId(final String name, final String type) throws IOException {
		traceAction("Get ComponentID ");
		final HttpPost httpost = new HttpPost(baseUrl + "/administrator/index.php");
		final List<NameValuePair> nvps = getHttpParameterForGetExtentionId(type);
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		final String body = executeHttpRequest(httpost);
		final Pattern p = Pattern.compile(regEx.getGET_EXTENTION_ID_REGEX().replaceAll("\\{0\\}", name), Pattern.MULTILINE);
		final Matcher m = p.matcher(body);
		updateToken(body);
		String cId = null;
		while (m.find()) {
			cId = m.group(1);
		}
		if (cId == null) {
			logHtmlError(body);
			throw new IllegalStateException("Can't find Extension [" + name + "]");
		}
		traceAction("Get component id: successful [" + cId + "]");
		return cId;

	}

	private List<NameValuePair> getHttpParamerForLogin(final String user, final String password) {
		final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("option", "com_login"));
		nvps.add(new BasicNameValuePair("task", "login"));
		nvps.add(new BasicNameValuePair("username", user));
		nvps.add(new BasicNameValuePair("passwd", password));
		nvps.add(new BasicNameValuePair("lang", "en-GB"));
		nvps.add(new BasicNameValuePair(token, "1"));
		return nvps;
	}

	private List<NameValuePair> getHttpParameterForGetExtentionId(final String extentionType) {
		final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair(token, "1"));
		nvps.add(new BasicNameValuePair("option", "com_installer"));
		nvps.add(new BasicNameValuePair("view", "manage"));
		nvps.add(new BasicNameValuePair("filters[type]", extentionType));
		nvps.add(new BasicNameValuePair("limit", "0"));
		nvps.add(new BasicNameValuePair("limitstart", "0"));
		return nvps;
	}

	private MultipartEntity getHttpParameterForInstallion(final File archieveFile, final String ftpUser, final String ftpPassword)
			throws UnsupportedEncodingException {
		final MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("install_package", new FileBody(archieveFile));
		reqEntity.addPart(token, new StringBody("1"));
		reqEntity.addPart("installtype", new StringBody("upload"));
		reqEntity.addPart("task", new StringBody("install.install"));
		reqEntity.addPart("option", new StringBody("com_installer"));
		if (ftpUser != null && ftpPassword != null) {
			reqEntity.addPart("username", new StringBody(ftpUser));
			reqEntity.addPart("password", new StringBody(ftpPassword));
		}
		return reqEntity;
	}

	private List<NameValuePair> getHttpParameterForDeleteExtention(final String componentId, final String ftpUser, final String ftpPassword) {
		final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair(token, "1"));
		nvps.add(new BasicNameValuePair("option", "com_installer"));
		nvps.add(new BasicNameValuePair("task", "manage.remove"));
		if (ftpUser != null && ftpPassword != null) {
			nvps.add(new BasicNameValuePair("username", ftpUser));
			nvps.add(new BasicNameValuePair("password", ftpPassword));
		}
		nvps.add(new BasicNameValuePair("cid", componentId));
		return nvps;
	}

	private String executeHttpRequest(final HttpPost httpost) throws IOException {
		final HttpResponse response = httpclient.execute(httpost);
		final HttpEntity entity = response.getEntity();
		final String body = EntityUtils.toString(entity);
		return body;
	}

	private void updateToken(final String body) {
		traceAction("Update token");
		final Pattern p = Pattern.compile(regEx.getGET_TOKEN_REGEX(), Pattern.MULTILINE);
		final Matcher m = p.matcher(body);
		String newToken = null;
		while (m.find()) {
			newToken = m.group(1);
		}
		if (newToken == null) {
			logHtmlError(body);
			throw new IllegalStateException("Can't find token.");
		} else {
			token = newToken;
		}
		traceAction("Update token: successful");
	}

	private void traceAction(final String action) {
		System.out.println("==> " + action);
	}

	private void logHtmlError(final String html) {
		System.out.println("==> HTML-Output [ERROR]:" + html);
	}

	private void logHtml(final String html) {
		if (debug) {
			System.out.println("==> HTML-Output [TRACE]:" + html);
		}
	}

}
