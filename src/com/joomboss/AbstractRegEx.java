package com.joomboss;

abstract class AbstractRegEx implements RegEx {
	private static final String GET_TOKEN_REGEX = "<input type=\"hidden\" name=\"([a-z0-9]+)\" value=\"1\" />";
	private static final String GET_ERROR_MESSAGE_REGEX = "<dd class=\"error message\">\\s*<ul>\\s*<li>(.*?)</li>";
	private final static String UNINSTALLION_WAS_SUCCESSFULL_REGEG_EN = "Uninstalling [\\p{L}0-9]* was successful.";
	private final static String LOGOUT_WAS_SUCCESSFULL_REGEG_EN = "User Name|Benutzername";

	@Override
	public String getGET_TOKEN_REGEX() {
		return GET_TOKEN_REGEX;
	}

	@Override
	public String getGET_ERROR_MESSAGE_REGEX() {
		return GET_ERROR_MESSAGE_REGEX;
	}

	@Override
	public String getUNINSTALLION_WAS_SUCCESSFULL_REGEG_EN() {
		return UNINSTALLION_WAS_SUCCESSFULL_REGEG_EN;
	}
	
	@Override
	public String getLOGOUT_WAS_SUCCESSFULL_REGEG_EN(){
		return LOGOUT_WAS_SUCCESSFULL_REGEG_EN;
	}
}
