package com.greencross.lims.report.panels;

import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.Header;

public abstract class PanelHeader extends Header<PanelResource, PanelTemplate, PanelDto> {
	protected PanelHeader(PanelTemplate template) {
		super(template);
	}
	protected static String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}
	public final Painter<PanelTemplate, PanelDto> sign() {
		return (s, t, d)->s;
	}
}
