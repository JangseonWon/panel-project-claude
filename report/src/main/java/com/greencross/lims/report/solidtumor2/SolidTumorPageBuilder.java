package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.solidtumor2.TestInfo;
import com.greencross.lims.report.SectionBarcode;

public class SolidTumorPageBuilder extends Page<SolidTumorTemplate> {
    private final Painter<SolidTumorTemplate, SolidTumorDto> barcode = new SectionBarcode<>();
    private final Painter<SolidTumorTemplate, SolidTumorDto> ldt;
    private final Painter<SolidTumorTemplate, SolidTumorDto> footer;
    private final Painter<SolidTumorTemplate, SolidTumorDto> sign;
    private final Painter<SolidTumorTemplate, SolidTumorDto> page;
    private final Painter<SolidTumorTemplate, SolidTumorDto> header;
    private final Painter<SolidTumorTemplate, SolidTumorDto> cancerTypes = new SectionCancerTypes();
    private final Painter<SolidTumorTemplate, SolidTumorDto> summary;
    private final SectionQc qc;
    private final Painter<SolidTumorTemplate, SolidTumorDto> details;
    private final Painter<SolidTumorTemplate, SolidTumorDto> variants;
    private final Painter<SolidTumorTemplate, SolidTumorDto> method = new SectionMethod();
    private final Painter<SolidTumorTemplate, SolidTumorDto> limitation = new SectionLimitation();
    private final Painter<SolidTumorTemplate, SolidTumorDto> explanation = new SectionExplanation();
    private final Painter<SolidTumorTemplate, SolidTumorDto> genes;
    private final Painter<SolidTumorTemplate, SolidTumorDto> empty = (s, t, d)->newPage(s);
    public SolidTumorPageBuilder(SolidTumorTemplate template,
                                 Painter<SolidTumorTemplate, SolidTumorDto> header,
                                 Painter<SolidTumorTemplate, SolidTumorDto> ldt,
                                 Painter<SolidTumorTemplate, SolidTumorDto> footer,
                                 Painter<SolidTumorTemplate, SolidTumorDto> sign,
                                 Painter<SolidTumorTemplate, SolidTumorDto> page) {
        super(template);
        this.ldt = ldt;
        this.header = header;
        this.footer = footer;
        this.sign = sign;
        this.page = page;
        this.summary = new SectionSummary(empty.and(template()));
        if(template.testInfo() == TestInfo.N198 || template.testInfo() == TestInfo.ON198) this.qc = new SectionQcN198(empty.and(template()));
        else if(template.testInfo() == TestInfo.N199 || template.testInfo() == TestInfo.ON199 || template.testInfo() == TestInfo.G0022402) this.qc = new SectionQcN199(empty.and(template()));
        else if(template.testInfo() == TestInfo.N200 || template.testInfo() == TestInfo.ON200) this.qc = new SectionQcN200(empty.and(template()));
        else this.qc = null;
        this.details = new SectionDetails(empty.and(template()));
        this.variants = new SectionVariants(empty.and(template()));
        this.genes = new SectionGenes(empty.and(template()));
    }
    public Painter<SolidTumorTemplate, SolidTumorDto> template() {
        return header.and(cancerTypes).and(footer).and(sign);
    }
    public Painter<SolidTumorTemplate, SolidTumorDto> page() {
        Painter<SolidTumorTemplate, SolidTumorDto> initialize = (s, t, d)->{
            s.font(t.resource().fontDefault());
            return s;
        };
        return initialize.and(template()).and(barcode)
                .and(summary).and(qc)
                .and(empty).and(template()).and(details).and(variants)
                .and(empty).and(template()).and(method).and(qc::qc).and(limitation)
                .and(empty).and(template()).and(explanation)
                .and(empty).and(template()).and(genes)
                .and(ldt)
                .and(page);
    }
}
