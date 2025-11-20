package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

public abstract class GenomeScreenPageBuilder<T extends GenomeScreenTemplate<? extends GenomeScreenResource>> extends Page<T> {
	private final PageBuilder<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> builder;
	public GenomeScreenPageBuilder(T template, GenomeScreenDto dto) {
		super(template);
		builder = new PageBuilder(template, dto);
	}
	public abstract Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> template();
	public abstract Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> pages();
	public PDDocument build() throws IOException {
		return builder.add(pages()).build();
	}
}
