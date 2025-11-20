package com.greencross.lims.report.sanger;

import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.Header;

public abstract class SangerHeader extends Header<SangerResource, SangerTemplate, SangerDto> {
	protected SangerHeader(SangerTemplate template) {
		super(template);
	}
	@Override
	public abstract Painter<SangerTemplate, SangerDto> initialize();
	@Override
	public abstract Painter<SangerTemplate, SangerDto> header();
}
