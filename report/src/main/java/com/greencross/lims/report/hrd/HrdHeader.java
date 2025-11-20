package com.greencross.lims.report.hrd;

import com.greencross.lims.report.builder.Header;

public abstract class HrdHeader extends Header<HrdResource, HrdTemplate, HrdDto> {
	protected HrdHeader(HrdTemplate template) {
		super(template);
	}
	protected static String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}
}
