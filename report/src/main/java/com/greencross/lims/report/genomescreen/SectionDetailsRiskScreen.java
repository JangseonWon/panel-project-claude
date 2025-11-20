package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import lombok.val;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;
import static com.gcgenome.lims.report.func.AlignVertical.BOTTOM;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionDetailsRiskScreen implements Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> {
	private final static float HEADER_HEIGHT = 20;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage;
	public SectionDetailsRiskScreen(Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage) {
		this.newPage = newPage.and((s, t, d)->header(s, t));
	}
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template, GenomeScreenWithRiskScreenDto dto) throws IOException {
		for(val disease:template.riskscreens())
			stream = newPage.and((s, t, d)->table(s, template, dto, disease)).
							paint(stream, (GenomeScreenTemplate)template, dto);
		return stream;
	}
	private PDPageContentStreamPageAccessible header(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 20;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
		// Util.icon(stream, resource.imgIcons()[7], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, "검사 결과"));
		y -= HEADER_HEIGHT;
		y -= 15;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}

	private final static float RISK_SUMMARY_WIDTH = 180;
	private final static float RISK_GENE_WIDTH = 60;
	private final static float RISK_TYPE_WIDTH = (RISK_SUMMARY_WIDTH-RISK_GENE_WIDTH)/2;
	private final static float RESULT_WIDTH = 100;
	private final static float GENOTYPE_ROW_HEIGHT = 30;
	private PDPageContentStreamPageAccessible table(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template, GenomeScreenWithRiskScreenDto dto, RiskScreenTemplate.DiseaseRiskScreen disease) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497-RESULT_WIDTH, HEADER_HEIGHT).fill();
		stream.setNonStrokingColor(resource.colorText()).rect(547-RESULT_WIDTH, y, RESULT_WIDTH, HEADER_HEIGHT).fill();
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497-RESULT_WIDTH, MIDDLE, new TextBlock(styleHeader, "▶" + template.diseaseRiskScreenName(disease)));
		String risk = template.diseaseSubRiskNegative();
		if(disease.isPositive(dto)) risk = template.diseaseSubRiskPositive();
		stream.paragraph(547-RESULT_WIDTH/2, y-HEADER_HEIGHT/2, RESULT_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, risk));
		stream.cursorY(y-HEADER_HEIGHT);
		float startY = stream.cursorY();
		// 좌측단
		for(RiskScreenTemplate.DiseaseSubRiskScreen sub: disease.subs()) stream = table(stream, template, dto, sub);
		float subBottom = stream.cursorY();
		// 우측단
		stream.cursorY(startY);
		stream = info(stream, template, disease);
		List<RiskScreenTemplate.GeneRiskScreen> genes = Arrays.stream(disease.subs()).map(RiskScreenTemplate.DiseaseSubRiskScreen::genes)
															  .flatMap(Arrays::stream).distinct().collect(Collectors.toList());
		for(RiskScreenTemplate.GeneRiskScreen gene: genes) stream = info(stream, template, gene);
		y = stream.cursorY();
		float height1 = 80;
		float height2 = 50;
		float x = 50+RISK_SUMMARY_WIDTH + 10;
		if(subBottom-height1-height2 > 130) {
			stream.cursorY(subBottom);
			x = 50;
		}
		stream = interpretation(stream, template, x, disease, dto);
		stream = recommendation(stream, template, x, disease, dto);
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible table(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template, GenomeScreenWithRiskScreenDto dto, RiskScreenTemplate.DiseaseSubRiskScreen sub) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 10;
		var n = Arrays.stream(sub.genes()).map(RiskScreenTemplate.GeneRiskScreen::snvs).flatMap(Arrays::stream).count();
		float TABLE_HEIGHT = HEADER_HEIGHT*(4+n);
		TextStyle styleHeader = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8);
		TextStyle styleEtc = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(6);
		stream.setStrokingColor(resource.colorDarkGray()).setNonStrokingColor(Color.WHITE).setLineWidth(0.25f)
			  .rect(50, y, RISK_SUMMARY_WIDTH, HEADER_HEIGHT).fill()
			  .rect(50, y, RISK_SUMMARY_WIDTH, HEADER_HEIGHT).stroke();
		stream.paragraph(50+RISK_SUMMARY_WIDTH/2, y-HEADER_HEIGHT/2, RISK_SUMMARY_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, template.diseaseSubRiskScreenName(sub) + " 위험 유전 요인"));
		stream.setNonStrokingColor(Color.WHITE)
			  .rect(50, y-HEADER_HEIGHT, RISK_SUMMARY_WIDTH, HEADER_HEIGHT)
			  .rect(50, y-HEADER_HEIGHT, RISK_GENE_WIDTH, HEADER_HEIGHT*(3 + n)).fill()
			  .rect(50, y-HEADER_HEIGHT, RISK_SUMMARY_WIDTH, HEADER_HEIGHT)
			  .rect(50, y-HEADER_HEIGHT*2, RISK_GENE_WIDTH, HEADER_HEIGHT*2)
			  .rect(50+RISK_GENE_WIDTH, y-HEADER_HEIGHT*2, RISK_TYPE_WIDTH*2, HEADER_HEIGHT*2)
			  .rect(50+RISK_GENE_WIDTH, y-HEADER_HEIGHT*3, RISK_TYPE_WIDTH, HEADER_HEIGHT)
			  .rect(50+RISK_GENE_WIDTH+RISK_TYPE_WIDTH, y-HEADER_HEIGHT*3, RISK_TYPE_WIDTH, HEADER_HEIGHT).stroke();
		stream.paragraph(50+RISK_GENE_WIDTH/2, y-HEADER_HEIGHT*3, RISK_GENE_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, "유전자"));
		stream.paragraph(50+RISK_GENE_WIDTH+RISK_TYPE_WIDTH, y-HEADER_HEIGHT*2-HEADER_HEIGHT/2, RISK_TYPE_WIDTH*2, CENTER, MIDDLE, new TextBlock(styleHeader, "나의 유전자형"));
		stream.paragraph(50+RISK_GENE_WIDTH+RISK_TYPE_WIDTH/2, y-HEADER_HEIGHT*3-HEADER_HEIGHT/2, RISK_TYPE_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, "표준형"));
		stream.paragraph(50+RISK_GENE_WIDTH+RISK_TYPE_WIDTH/2*3, y-HEADER_HEIGHT*3-HEADER_HEIGHT/2, RISK_TYPE_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, "증가형"));

		long total = Arrays.stream(sub.genes()).flatMap(s-> Arrays.stream(s.snvs())).count();
		long risk = Arrays.stream(sub.genes()).mapToLong(gene->
				Arrays.stream(gene.snvs()).filter(s-> {
					var genotype = dto.variantsRiskScreen().get(s);
					return sub.isPositive(s, genotype);
				}).count()).sum();
		stream.paragraph(50+RISK_SUMMARY_WIDTH/2, y-HEADER_HEIGHT-HEADER_HEIGHT/2, RISK_SUMMARY_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, total + " 개중 " + risk + "개 위험"));
		var row = 4;
		NumberFormat fmtPrecision = new DecimalFormat("0");
		fmtPrecision.setMaximumFractionDigits(4);
		for(int i = 0; i < sub.genes().length; ++i) {
			RiskScreenTemplate.GeneRiskScreen gene = sub.genes()[i];
			RiskScreenTemplate.Snv[] snvs = gene.snvs();
			for(int j = 0; j < snvs.length; ++j) {
				RiskScreenTemplate.Snv snv = snvs[j];
				stream.rect(50, y-HEADER_HEIGHT*row, RISK_GENE_WIDTH, HEADER_HEIGHT)
					  .rect(50+RISK_GENE_WIDTH, y-HEADER_HEIGHT*row, RISK_TYPE_WIDTH, HEADER_HEIGHT)
					  .rect(50+RISK_GENE_WIDTH+RISK_TYPE_WIDTH, y-HEADER_HEIGHT*row, RISK_TYPE_WIDTH, HEADER_HEIGHT)
					  .stroke();
				stream.paragraph(50+RISK_GENE_WIDTH/2, y-HEADER_HEIGHT*row-HEADER_HEIGHT/2, RISK_GENE_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, gene.name()));
				float x = 50+RISK_GENE_WIDTH+RISK_TYPE_WIDTH/2;
				Color color = resource.colorSafe();
				var genotype = dto.variantsRiskScreen().get(snv);
				if(sub.isPositive(snv, genotype)) {
					x += RISK_TYPE_WIDTH;
					color = resource.colorWarn();
				}
				stream.paragraph(x, y-HEADER_HEIGHT*row-HEADER_HEIGHT/2, RISK_GENE_WIDTH, CENTER, MIDDLE, new TextBlock(styleValue.clone().color(color), genotype));
				String genotypeAll = sub.types(snv).stream().map(RiskScreenTemplate.Type::genotype).collect(Collectors.joining("/"));
				RiskScreenTemplate.Type myType = sub.types(snv).stream().filter(t->t.genotype().equalsIgnoreCase(genotype)).findAny().get();
				String label = gene.name() + " " + snv.pos() + " " + genotypeAll + " :";
				stream.paragraph(50, y-TABLE_HEIGHT-10-GENOTYPE_ROW_HEIGHT*(row-4), RISK_SUMMARY_WIDTH+10, new TextBlock(styleEtc, label));
				String label2 = "나의 유전형빈도( " + (myType.lessThen?"<":"") + fmtPrecision.format(myType.percentage*100) + "% )";
				stream.paragraph(50+RISK_SUMMARY_WIDTH, y-TABLE_HEIGHT-10-GENOTYPE_ROW_HEIGHT*(row-4), RISK_SUMMARY_WIDTH+10, RIGHT, new TextBlock(styleEtc, label2));
				double max = sub.types(snv).stream().mapToDouble(RiskScreenTemplate.Type::percentage).sum();
				float x2 = 50;
				Color[] colors = new Color[] {
						Color.decode("#00A4C7"),
						Color.decode("#6DBB53"),
						Color.decode("#A1CC54"),
						Color.decode("#E68B1B"),
						Color.decode("#019DB1"),
						Color.decode("#E73673")
				};
				float cursor = 0;
				boolean hit = false;
				for(int k = 0; k < sub.types(snv).size(); ++k) {
					RiskScreenTemplate.Type type = sub.types(snv).get(k);
					double w = Math.max(1, type.percentage/max*RISK_SUMMARY_WIDTH);
					Color c = colors[k];
					stream.setNonStrokingColor(c).rect(x2, y-TABLE_HEIGHT-15-GENOTYPE_ROW_HEIGHT*(row-4), (float)w, (GENOTYPE_ROW_HEIGHT-18)).fill();
					x2 += w;
					if(type == myType) {
						hit = true;
						cursor += w/2;
					} else if(!hit) cursor += w;
				}
				stream.rect(50, y-TABLE_HEIGHT-15-GENOTYPE_ROW_HEIGHT*(row-4), RISK_SUMMARY_WIDTH, (GENOTYPE_ROW_HEIGHT-18)).stroke();
				float cx = 50+cursor;
				float cy = y-TABLE_HEIGHT-20-GENOTYPE_ROW_HEIGHT*(row-4);
				stream.saveGraphicsState();
				stream.setNonStrokingColor(color).moveTo(cx, cy).lineTo(cx-5, cy+8.66f).lineTo(cx+5, cy+8.66f).fill();
				stream.restoreGraphicsState();
				++row;
			}
		}
		stream.paragraph(50, y-TABLE_HEIGHT-10-GENOTYPE_ROW_HEIGHT*(row-4), RISK_SUMMARY_WIDTH, BOTTOM, new TextBlock(styleEtc, "※ 검사 유전자에서 수검자의 유전형 빈도(동아시아인 기준)"));

		y -= TABLE_HEIGHT+GENOTYPE_ROW_HEIGHT*(row-4)+20;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible info(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template, RiskScreenTemplate.DiseaseRiskScreen disease) throws IOException {
		if(template.diseaseRiskScreenInfo(disease) == null) return stream;
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 10;
		float x = 50+RISK_SUMMARY_WIDTH + 10;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(x, y, 547-x, HEADER_HEIGHT).fill();
		stream.paragraph(x+5, y-HEADER_HEIGHT/2, 547-x, MIDDLE, new TextBlock(styleHeader, "질환 정보 - " + template.diseaseRiskScreenName(disease) + "이란?"));
		y-= HEADER_HEIGHT+10;
		y = stream.paragraph(x, y, 547-x, new TextBlock(styleValue, template.diseaseRiskScreenInfo(disease)));
		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible info(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template, RiskScreenTemplate.GeneRiskScreen gene) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 10;
		float x = 50+RISK_SUMMARY_WIDTH + 10;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(x, y, 547-x, HEADER_HEIGHT).fill();
		stream.paragraph(x+5, y-HEADER_HEIGHT/2, 547-x, MIDDLE, new TextBlock(styleHeader, "검사 유전자 정보 - " + gene.name() + " 유전자"));
		y-= HEADER_HEIGHT+10;
		y = stream.paragraph(x, y, 547-x, new TextBlock(styleValue, template.geneRiskScreenInfo(gene)));
		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible interpretation(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template, float x, RiskScreenTemplate.DiseaseRiskScreen disease, GenomeScreenWithRiskScreenDto dto) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 10;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(x, y, 547-x, HEADER_HEIGHT).fill();
		stream.paragraph(x+5, y-HEADER_HEIGHT/2, 547-x, MIDDLE, new TextBlock(styleHeader, "결과해석"));
		y-= HEADER_HEIGHT+10;
		List<String> interpretations = template.interpretation(disease, dto);
		for(String interpretation: interpretations) {
			stream.paragraph(x, y, 7, new TextBlock(styleValue, "-"));
			y = stream.paragraph(x+7, y, 540-x, new TextBlock(styleValue, interpretation));
			y -= 11;
		}
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible recommendation(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template, float x, RiskScreenTemplate.DiseaseRiskScreen disease, GenomeScreenWithRiskScreenDto dto) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 10;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(x, y, 547-x, HEADER_HEIGHT).fill();
		stream.paragraph(x+5, y-HEADER_HEIGHT/2, 547-x, MIDDLE, new TextBlock(styleHeader, "권고사항"));
		y-= HEADER_HEIGHT+10;
		List<String> recommendations = template.recommendation(disease, dto);
		for(String recommendation: recommendations) {
			stream.paragraph(x, y, 7, new TextBlock(styleValue, "-"));
			y = stream.paragraph(x+7, y, 540-x, new TextBlock(styleValue, recommendation));
			y -= 11;
		}
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
