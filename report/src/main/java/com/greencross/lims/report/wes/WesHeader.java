package com.greencross.lims.report.wes;

import com.greencross.lims.report.builder.Header;

public abstract class WesHeader extends Header<WesResource, WesTemplate, WesDto> {
	protected WesHeader(WesTemplate template) {
		super(template);
	}
	protected static String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}
}
