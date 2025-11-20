package com.greencross.lims.report.genomescreen;

import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.SectionLDT;
import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.builder.LogoType;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.genomescreen.kokr.SectionGeneListKoKr;
import com.greencross.lims.report.genomescreen.kokr.SectionHeaderKoKr;

import java.awt.*;
import java.util.Arrays;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.BOTTOM;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class GenomeScreenN088 extends GenomeScreenPageBuilder<GenomeScreenTemplateN088<RiskScreenResource>> {
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> barcode = new SectionBarcode<>();
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> ldt;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> sign;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> footer;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> page;

	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> title = new SectionTitle();
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> header = new SectionHeaderKoKr();
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
	private final Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> summary2;
	private final Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> details2;
	private final Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> lifestyle = new SectionLifestyleRiskScreen();
	private final Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> guideline = new SectionGuidelineRiskScreen();
	private final Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> clinicalMeanings2;

	public GenomeScreenN088(GenomeScreenTemplateN088 template, GenomeScreenDto dto, LogoType logoType,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> sign,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> footer,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> page) {
		super(template, dto);
		this.ldt = new SectionLDT<>(logoType, 85);
		this.sign = sign;
		this.footer = footer;
		this.page = page;
		positive = new SectionPositive(empty.and(template()).and(result));
		details = new SectionDetails(empty.and(template()).and(result));
		guide = new SectionTestGuide(empty.and(template()));
		genes = new SectionGeneListKoKr(empty.and(template()));
		limitations = new SectionLimitation();
		infos = new SectionTestInfo(empty.and(template()));
		references = new SectionReference(empty.and(template()));
		clinicalMeanings = new SectionClinicalMeanings(empty.and(template()));
		summary2 = new SectionSummaryRiskScreen(this::table);
		details2 = new SectionDetailsRiskScreen(empty.and(template()).and(result));
		clinicalMeanings2 = new SectionClinicalMeaningsRiskScreen(empty.and(template()));

	}
	@Override
	public Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> template() {
		return title.and((stream, template, dto)->{
			stream.font(template.resource().fontDefault());
			return stream;
		}).and(header).and(footer).and(sign);
	}
	private Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> cast(Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> painter) {
		return (s, t, d)->painter.paint(s, (RiskScreenTemplate)t, (GenomeScreenWithRiskScreenDto)d);
	}
	@Override
	public Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> pages() {
		return template().and(barcode).and(result).and(summary).and(positive)
		.and((s, t, d)->newPage(s)).and(template()).and(result).and(details)
		.and((s, t, d)->newPage(s)).and(template()).and(guide)
		.and((s, t, d)->newPage(s)).and(template()).and(genes)
		.and((s, t, d)->newPage(s)).and(template()).and(limitations)
		.and((s, t, d)->newPage(s)).and(template()).and(infos).and(references)
		.and((s, t, d)->newPage(s)).and(template()).and(clinicalMeanings)
		.and((s, t, d)->newPage(s)).and(template()).and(result).and(cast(summary2.and(details2)))
		.and((s, t, d)->newPage(s)).and(template()).and(result).and(cast(lifestyle))
		.and((s, t, d)->newPage(s)).and(template()).and(cast(terms().and(guideline)))
		.and((s, t, d)->newPage(s)).and(template()).and(cast(clinicalMeanings2)).and(ldt).and(page);
	}
	private final static float HEADER_HEIGHT = 20;
	private final static float HEADER_WIDTH = 100;
	private final static float GRAPH_HEIGHT = 110;
	private final static float GRAPH_MARGIN = 30;
	private final static float HEIGHT_TOTAL = 4*HEADER_HEIGHT + GRAPH_HEIGHT + 30;
	private Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> table() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			var templateCasted = (GenomeScreenTemplateN088)template;
			var resource = template.resource();
			var y = stream.cursorY();
			TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8).paragraph(true);
			TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8).paragraph(true);
			stream.setNonStrokingColor(resource.colorPrimary())
				  .rect(50, y, HEADER_WIDTH, HEIGHT_TOTAL)
				  .rect(50+HEADER_WIDTH, y, 497-HEADER_WIDTH, HEADER_HEIGHT)
				  .rect(50+HEADER_WIDTH, y-HEADER_HEIGHT*2, 497-HEADER_WIDTH, HEADER_HEIGHT)
				  .fill().setNonStrokingColor(resource.colorGray())
				  .rect(50+HEADER_WIDTH, y-HEADER_HEIGHT, 497-HEADER_WIDTH, HEADER_HEIGHT)
				  .rect(50+HEADER_WIDTH, y-HEIGHT_TOTAL+HEADER_HEIGHT, 497-HEADER_WIDTH, HEADER_HEIGHT)
				  .fill();
			stream.setStrokingColor(Color.WHITE).setLineWidth(0.25f)
				  .line(50, y-HEADER_HEIGHT, 497, y-HEADER_HEIGHT)
				  .line(50, y-2*HEADER_HEIGHT, 50+HEADER_WIDTH, y-2*HEADER_HEIGHT)
				  .line(50, y-3*HEADER_HEIGHT, 50+HEADER_WIDTH, y-3*HEADER_HEIGHT)
				  .line(50, y-HEIGHT_TOTAL+HEADER_HEIGHT, 50+HEADER_WIDTH, y-HEIGHT_TOTAL+HEADER_HEIGHT)
				  .stroke();
			float cntGenes = Arrays.stream(template.riskscreens()).map(c->(GenomeScreenTemplateN088.DiseaseRiskScreenN088)c)
								   .map(GenomeScreenTemplateN088.DiseaseRiskScreenN088::subs).flatMap(Arrays::stream)
								   .map(GenomeScreenTemplateN088.DiseaseSubRiskScreenN088::genes).flatMap(Arrays::stream).count();
			float widthGenes = (497 - HEADER_WIDTH)/cntGenes;
			float x = 50 + HEADER_WIDTH;
			var bottom = y-HEIGHT_TOTAL+HEADER_HEIGHT;
			stream.paragraph(50+HEADER_WIDTH/2, y-HEADER_HEIGHT/2, HEADER_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, templateCasted.lblGraphDiseaseInfo()));
			stream.paragraph(50+HEADER_WIDTH/2, y-HEADER_HEIGHT-HEADER_HEIGHT/2, HEADER_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, templateCasted.lblGraphSummary()));
			stream.paragraph(50+HEADER_WIDTH/2, y-2*HEADER_HEIGHT-HEADER_HEIGHT/2, HEADER_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, templateCasted.lblGraphDisease()));
			stream.paragraph(50+HEADER_WIDTH/2, y-3*HEADER_HEIGHT-(GRAPH_HEIGHT+30)/2, HEADER_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, templateCasted.lblGraphRisk()));
			stream.paragraph(50+HEADER_WIDTH/2, bottom-HEADER_HEIGHT/2, HEADER_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, templateCasted.lblGraphGene()));
			for(var disease: template.riskscreens()) {
				stream.setStrokingColor(Color.WHITE).line(x, y-HEADER_HEIGHT, x, y).stroke();
				if(x > 50 + HEADER_WIDTH) stream.setStrokingColor(resource.colorText()).line(x, y-HEADER_HEIGHT, x, y-2*HEADER_HEIGHT).stroke();
				GenomeScreenTemplateN088.DiseaseRiskScreenN088 cast = (GenomeScreenTemplateN088.DiseaseRiskScreenN088)disease;
				var width = widthGenes * Arrays.stream(cast.subs()).map(s->s.genes()).flatMap(Arrays::stream).count();
				stream.paragraph(x+width/2, y-HEADER_HEIGHT/2, width, CENTER, MIDDLE, new TextBlock(styleHeader, template.diseaseRiskScreenName(cast)));
				Color color = resource.colorSafe();
				String risk = template.diseaseSubRiskNegative();
				if(disease.isPositive(dto)) {
					color = resource.colorWarn();
					risk = template.diseaseSubRiskPositive();
				}
				stream.paragraph(x+width/2, y-HEADER_HEIGHT/2 - HEADER_HEIGHT, width, CENTER, MIDDLE, new TextBlock(styleValue.clone().color(color), risk));
				for(var sub: cast.subs()) {
					stream.setStrokingColor(Color.WHITE).line(x, y-3*HEADER_HEIGHT, x, y-2*HEADER_HEIGHT).stroke();
					var widthSubs = widthGenes * sub.genes().length;
					stream.paragraph(x+widthSubs/2, y-2*HEADER_HEIGHT-HEADER_HEIGHT/2, widthSubs, CENTER, MIDDLE, new TextBlock(styleHeader, template.diseaseSubRiskScreenName(sub)));
					for(var gene: sub.genes()) {
						stream.paragraph(x+widthGenes/2, bottom-HEADER_HEIGHT/2, width, CENTER, MIDDLE, new TextBlock(styleValue, gene.name()));
						if(x > 50 + HEADER_WIDTH) stream.setStrokingColor(resource.colorText()).line(x, bottom, x, bottom-HEADER_HEIGHT).stroke();
						x += widthGenes;
					}
				}
			}
			x = 50 + HEADER_WIDTH+widthGenes/2;
			stream.cursorY(y - 3*HEADER_HEIGHT - 15 - GRAPH_HEIGHT);
			for(var disease: template.riskscreens()) {
				GenomeScreenTemplateN088.DiseaseRiskScreenN088 cast = (GenomeScreenTemplateN088.DiseaseRiskScreenN088)disease;
				for(var sub: cast.subs()) {
					for (var gene : sub.genes()) {
						graph(x, widthGenes - 2 * GRAPH_MARGIN, sub, gene).paint(stream, template, dto);
						x += widthGenes;
					}
				}
			}
			stream.restoreGraphicsState();
			return stream.cursorY(y-HEIGHT_TOTAL);
		};
	}
	private Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenDto> graph(float x, float width, RiskScreenTemplate.DiseaseSubRiskScreen disease, RiskScreenTemplate.GeneRiskScreen gene) {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			var resource = template.resource();
			var y = stream.cursorY();
			var lineBase = y+20;
			TextStyle styleValue = new TextStyle().fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8).paragraph(true);
			var color = resource.colorSafe();
			var height = 5f;
			var risk = disease.risk(gene, (GenomeScreenWithRiskScreenDto) dto);
			if(disease.isPositive(gene, (GenomeScreenWithRiskScreenDto) dto)) {
				color = resource.colorWarn();
				height = Math.min(GRAPH_HEIGHT-20, (float)(5*risk));
			}
			stream.paragraph(x, y, width+GRAPH_MARGIN, CENTER, BOTTOM, new TextBlock(styleValue.color(color), template.risk(risk)));
			stream.setNonStrokingColor(color).rect(x - width / 2 + 5, lineBase, width - 10, -height).fill();
			stream.setStrokingColor(resource.colorText()).setLineWidth(0.25f)
				  .line(x-width/2, lineBase, x+width/2, lineBase).stroke();
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> terms() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			var templateCasted = (GenomeScreenTemplateN088)template;
			var resource = template.resource();
			var y = stream.cursorY();
			y -= 20;
			TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
			TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
			stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
			// Util.icon(stream, resource.imgIcons()[2], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
			stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, templateCasted.lblGuideTermTitle()));
			y -= HEADER_HEIGHT + 15;
			for(String term: templateCasted.lblGuideTerms()) {
				stream.paragraph(50, y, 497, new TextBlock(styleValue, "-"));
				y = stream.paragraph(60, y, 487, JUSTIFY, new TextBlock(styleValue, term));
				y -= 12;
			}
			y += 20;
			stream.cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
}
