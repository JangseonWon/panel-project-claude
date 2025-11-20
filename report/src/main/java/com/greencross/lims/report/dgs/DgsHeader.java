package com.greencross.lims.report.dgs;

import com.greencross.lims.report.builder.Header;

public abstract class DgsHeader extends Header<DgsResource, DgsTemplate, DgsDto> {
	protected DgsHeader(DgsTemplate template) {
		super(template);
	}
	protected static String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}
}
