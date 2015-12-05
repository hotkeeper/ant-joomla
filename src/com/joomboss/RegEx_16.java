package com.joomboss;

class RegEx_16 extends AbstractRegEx{
	private final static String LOGIN_SUCCESSFULL_TEXT_EN = "<strong>Logout</strong>";
	private final static String INSTALLION_WAS_SUCCESSFULL_REGEG_EN = "<dd class=\"message message\">\\s*<ul>\\s*<li>Installing [\\p{L}0-9]* was successful.</li>\\s*</ul>\\s*</dd>";
	private static final String GET_EXTENTION_ID_REGEX = "<td>\\s*"
			+ "<input type=\"checkbox\" id=\"cb[0-9]+\" name=\"cid\\[\\]\" value=\"([0-9]+)\" onclick=\".*\" title=\"[\\p{L}0-9\\s]*\" />"
			+ "\\s*</td>\\s*<td>\\s*" + "<span class=\"bold hasTip\" title=\"" + "{0}" + "::[\\p{L}\\p{Punct}\\p{Digit}\\s]*\">\\s*"
			+ "{0}" + "\\s*</span>\\s*</td>";

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
		return "";
	}

}
