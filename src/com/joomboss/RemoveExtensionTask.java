/*
 * RemoveExtensionTask.java
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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Ant task implementation for Joomla extension uninstallation
 * 
 * @author Aliaksei Kanstantsinau http://joomboss.com
 * @author Harry Klein http://www.joomla-hklein.de
 * @since March 2011
 * @version 1.0
 * 
 *          This ant task has following attributes:
 * 
 * <pre>
 * url           - URL to Joomla site (required)
 * name          - Extension name (exactly the same as it displayed in Extension Manager) (required)
 * type          - Extension type - can be component, module, plugin or template. (optional) 
 * adminLogin    - Joomla Administrator's login (required)
 * adminPassword - Joomla Administrator's password (required)
 * ftpLogin      - Site FTP login. Required on most unix systems to be able to perform file operations (optional)
 * ftpPassword   - Site FTP password (optional)
 * debug         - Debug HTML output (optional)
 * </pre>
 */
public class RemoveExtensionTask extends Task {
	@Override
	public void execute() {
		if (baseUrl == null) {
			throw new IllegalArgumentException("baseUrl should be defined");
		}
		if (adminLogin == null) {
			throw new IllegalArgumentException("adminLogin should be defined");
		}
		if (adminPassword == null) {
			throw new IllegalArgumentException("adminPassword should be defined");
		}
		if (name == null) {
			throw new IllegalArgumentException("Extension name should be defined");
		}

		final JoomlaClient client = new JoomlaClient(debug, baseUrl,RegExFactory.getRegEx(joomlaVersion));
		try {
			client.deleteComponent(name, type, adminLogin, adminPassword, ftpLogin, ftpPassword);
		} catch (final Exception e) {
			throw new BuildException(e);
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String extensionType) {
		if (extensionType != null) {
			extensionType = extensionType.toLowerCase();
		}
		if ("component".equals(extensionType) || "module".equals(extensionType) || "plugin".equals(extensionType)
				|| "template".equals(extensionType) || "".equals(extensionType)) {
			this.type = extensionType;
		} else {
			throw new IllegalArgumentException("Extension type should be 'component', 'module', 'plugin' or 'template'");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(final String componentName) {
		this.name = componentName;
	}

	public String getUrl() {
		return baseUrl;
	}

	public void setUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
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

	private String name;
	private String type = "component";
	private String baseUrl;
	private String adminLogin;
	private String adminPassword;
	private String ftpLogin;
	private String ftpPassword;
	private boolean debug;
	private String joomlaVersion = "3.1";
}
