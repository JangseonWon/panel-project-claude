package com.greencross.lims.report.genomescreen;

import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.builder.LogoType;
import com.gcgenome.lims.report.func.Painter;

import java.util.function.Function;

public class GenomeScreenN090 extends GenomeScreenPageBuilder<GenomeScreenTemplateN090<GenomeScreenResource>> {
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> barcode = new SectionBarcode<>();
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> ldt;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> sign;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> footer;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> page;

	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> title = new SectionTitle();
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> header;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> result = new SectionMyResult();
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> summary = new SectionSummary();
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> empty = (s, t, d)->newPage(s);
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> positive;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> details;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> guide;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> genes;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> limitations;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> infos;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> references;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> clinicalMeanings;
	public GenomeScreenN090(GenomeScreenTemplateN090 template, GenomeScreenDto dto,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> header, LogoType logoType,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> sign,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> footer,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> page,
							Function<Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto>, Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto>> genes) {
		super(template, dto);
		this.header = header;
		this.ldt = new SectionLDT<>(logoType, 95);
		this.sign = sign;
		this.footer = footer;
		this.page = page;
		positive = new SectionPositive(empty.and(template()).and(result));
		details = new SectionDetails(empty.and(template()).and(result));
		guide = new SectionTestGuide(empty.and(template()));
		this.genes = genes.apply(empty.and(template()));
		limitations = new SectionLimitation();
		infos = new SectionTestInfo(empty.and(template()));
		references = new SectionReference(empty.and(template()));
		clinicalMeanings = new SectionClinicalMeanings(empty.and(template()));
	}
	@Override
	public Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> template() {
		return title.and((stream, template, dto)->{
			stream.font(template.resource().fontDefault());
			return stream;
		}).and(header).and(footer).and(sign);
	}
	@Override
	public Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> pages() {
		return template().and(barcode).and(result).and(summary).and(positive)
		.and((s, t, d)->newPage(s)).and(template()).and(result).and(details)
		.and((s, t, d)->newPage(s)).and(template()).and(guide)
		.and((s, t, d)->newPage(s)).and(template()).and(genes)
		.and((s, t, d)->newPage(s)).and(template()).and(limitations)
		.and((s, t, d)->newPage(s)).and(template()).and(infos).and(references)
		.and((s, t, d)->newPage(s)).and(template()).and(clinicalMeanings).and((stream, template, dto)->{
					stream.font(template.resource().fontDefault());
					return stream;
				}).and(ldt).and(page);
	}
}
