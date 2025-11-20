package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

public abstract class HereditaryEnUsPageBuilder<T extends HereditaryTemplateEnUs> extends Page<T> {
	private final PageBuilder<T, HereditaryEnUsDto> builder;
	public HereditaryEnUsPageBuilder(T template, HereditaryEnUsDto dto) {
		super(template);
		builder = new PageBuilder(template, dto);
	}
	public abstract Painter<T, HereditaryEnUsDto> template();
	public abstract Painter<T, HereditaryEnUsDto> pages();
	public PDDocument build() throws IOException {
		return builder.add(pages()).build();
	}
}
