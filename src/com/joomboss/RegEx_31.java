package com.joomboss;

class RegEx_31 extends AbstractRegEx {
	private final static String LOGIN_SUCCESSFULL_TEXT_EN = "Control Panel";
	private final static String INSTALLION_WAS_SUCCESSFULL_REGEG_EN = "Install[\\s\\p{L}0-9]* was successful.";
	private static final String GET_EXTENTION_ID_REGEX = "<input type=\"checkbox\" id=\"cb[0-9]+\" name=\"cid\\[\\]\" value=\"([0-9]+)\" "
			+ "onclick=\".*\" " + "title=\"[\\p{L}0-9\\s]*\" />" + "\\s*</td>\\s*<td>\\s*" + "<span class=\"bold hasTip\" title=\"{0}"
			+ "::";
	private static final String INSTALLION_WAS_FAILED_REGEG_EN = "Component Install: Custom install routine failure";
	
	

	@Override
	public String getLOGIN_SUCCESSFULL_TEXT_EN() {
		return LOGIN_SUCCESSFULL_TEXT_EN;
	}

	@Override
	public String getINSTALLION_WAS_SUCCESSFULL_REGEG_EN() {
		return INSTALLION_WAS_SUCCESSFULL_REGEG_EN;
	}

	@Override
	public String getGET_EXTENTION_ID_REGEX() {
		return GET_EXTENTION_ID_REGEX;
	}

	@Override
	public String getINSTALLION_WAS_FAILED_REGEG_EN() {
		return INSTALLION_WAS_FAILED_REGEG_EN;

	}
}