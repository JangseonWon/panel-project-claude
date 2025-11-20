package com.greencross.lims.report.bloodcancer;

import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.SectionBarcode;

public class ALLPageBuilder extends BloodCancerPageBuilder {
	private final Painter<BloodCancerTemplate, BloodCancerDto> barcode = new SectionBarcode<>();
	private final Painter<BloodCancerTemplate, BloodCancerDto> ldt;
	private final Painter<BloodCancerTemplate, BloodCancerDto> page;
	private final Painter<BloodCancerTemplate, BloodCancerDto> summary = new SectionSummaryAll();
	private final Painter<BloodCancerTemplate, BloodCancerDto> details;
	private final Painter<BloodCancerTemplate, BloodCancerDto> drug;
	private final Painter<BloodCancerTemplate, BloodCancerDto> method;
	private final Painter<BloodCancerTemplate, BloodCancerDto> qc;
	private final Painter<BloodCancerTemplate, BloodCancerDto> limitation;
	private final Painter<BloodCancerTemplate, BloodCancerDto> explanation = new SectionVariantInterpretation();
	private final Painter<BloodCancerTemplate, BloodCancerDto> genes;
	private final Painter<BloodCancerTemplate, BloodCancerDto> empty = (s, t, d)->newPage(s);
	public ALLPageBuilder(BloodCancerTemplate template, Painter<BloodCancerTemplate, BloodCancerDto> header, Painter<BloodCancerTemplate, BloodCancerDto> ldt,
                          Painter<BloodCancerTemplate, BloodCancerDto> footer, Painter<BloodCancerTemplate, BloodCancerDto> sign, Painter<BloodCancerTemplate, BloodCancerDto> page) {
		super(template, header, ldt, footer, sign, page);
		this.ldt = ldt;
		this.page = page;
		this.details = new SectionDetails(empty.and(template()));
		this.drug = new SectionDrugs(empty.and(template()));
		this.method = new SectionMethod(empty.and(template()));
		this.qc = new SectionQc(empty.and(template()));
		this.limitation = new SectionLimitation(empty.and(template()));
		this.genes = new SectionGenes(empty.and(template()));
	}
	public Painter<BloodCancerTemplate, BloodCancerDto> page() {
		Painter<BloodCancerTemplate, BloodCancerDto> initialize = (s, t, d)->{
			s.font(t.resource().fontDefault());
			return s;
		};
		return initialize.and(template()).and(barcode)
				.and(summary).and(details).and(drug).and(method).and(qc).and(limitation)
				.and(empty).and(template()).and(explanation)
				.and(empty).and(template()).and(genes)
				.and(ldt)
				.and(page);
	}
}
