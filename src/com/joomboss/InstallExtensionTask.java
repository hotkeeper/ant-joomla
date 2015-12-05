/*
 * InstallExtensionTask.java
 * 
 * Copyright (C) 2011 JoomBoss (http://joomboss.com)
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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Ant task implementation for Joomla extension installation
 * 
 * @author Aliaksei Kanstantsinau http://joomboss.com
 * @author Harry Klein http://www.joomla-hklein.de
 * @since March 2011
 * @version 1.0
 * 
 *          This ant task has following attributes:
 * 
 * <pre>
 * source        - path to extension archieve in local filesystem (required)
 * url           - URL to Joomla site (required) 
 * adminLogin    - Joomla Administrator's login (required)
 * adminPassword - Joomla Administrator's password (required)
 * ftpLogin      - Site FTP login. Required on most unix systems to be able to perform file operations (optional)
 * ftpPassword   - Site FTP password (optional)
 * debug         - Debug HTML output (optional)
 * </pre>
 */
public class InstallExtensionTask extends Task {
	@Override
	public void execute() {
		if (source == null) {
			throw new IllegalArgumentException("Package source should be defined");
		}
		if (url == null) {
			throw new IllegalArgumentException("Joomla base URL should be defined");
		}
		if (adminLogin == null) {
			throw new IllegalArgumentException("adminLogin should be defined");
		}
		if (adminPassword == null) {
			throw new IllegalArgumentException("adminPassword should be defined");
		}
		try {
			final JoomlaClient client = new JoomlaClient(debug, url,RegExFactory.getRegEx(joomlaVersion));
			client.installPackage(adminLogin, adminPassword, source, ftpLogin, ftpPassword);
		} catch (final Exception e) {
			throw new BuildException(e);
		}
	}

	public File getSource() {
		return source;
	}

	public void setSource(final File packageSource) {
		this.source = packageSource;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String baseUrl) {
		this.url = baseUrl;
	}

	public String getAdminLogin() {
		return adminLogin;
	}

	public void setAdminLogin(final String adminLogin) {
		this.adminLogin = adminLogin;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(final String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getFtpLogin() {
		return ftpLogin;
	}

	public void setFtpLogin(final String ftpLogin) {
		this.ftpLogin = ftpLogin;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(final String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public boolean getDebug() {
		return debug;
	}

	public void setDebug(final boolean debug) {
		this.debug = debug;
	}
	
	public String getJoomlaVersion() {
		return joomlaVersion;
	}

	public void setJoomlaVersion(final String joomlaVersion) {
		this.joomlaVersion = joomlaVersion;
	}
	
	private File source = null;
	private String url = null;
	private String adminLogin = null;
	private String adminPassword = null;
	private String ftpLogin = null;
	private String ftpPassword = null;
	private boolean debug;
	private String joomlaVersion = "3.1";
}
