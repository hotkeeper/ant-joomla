package com.joomboss;

public class RegExFactory {

	public final static RegEx getRegEx(final String joomlaVersion) {
		if ("16".equals(joomlaVersion)) {
			return new RegEx_16();
		}
		return new RegEx_31();
	}

}
