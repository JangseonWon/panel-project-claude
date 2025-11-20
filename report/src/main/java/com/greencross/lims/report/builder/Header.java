package com.greencross.lims.report.builder;

import com.gcgenome.lims.report.Resource;
import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;

public abstract class Header<R extends Resource, T extends AbstractReportTemplate<R>, D extends AbstractReportDto> extends Page<T> {
	protected Header(T template) {
		super(template);
	}
	public abstract Painter<T, D> initialize();
	public abstract Painter<T, D> header();
}
