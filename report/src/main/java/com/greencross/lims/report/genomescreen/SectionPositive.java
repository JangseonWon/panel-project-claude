package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionPositive implements Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> {
	protected final static float HEADER_HEIGHT = 20;
	protected final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage;
	protected Integer mode = 0;
	public SectionPositive(Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage) {
		this.newPage = newPage;
	}
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		if(dto.variants() == null || dto.variants().isEmpty() || dto.variants().values().stream().flatMap(Collection::stream).noneMatch(v->"PV".equals(v.clazz()) || "LPV".equals(v.clazz()) || "VUS".equals(v.clazz()))) {
			if(dto.interpretation()!=null && !dto.interpretation().trim().isEmpty()) stream = interpretation(stream, template, dto);
			return stream;
		}
		stream = table(stream, template, dto);
		//stream = newPage.paint(stream, template, dto);
		stream = interpretation(stream, template, dto);
		mode = 1;
		stream = newPage.and((s, t, d)->header(s, t)).paint(stream, template, dto);

		for(var disease: template.diseases())
			L: for(var sub: disease.subs()) {
				if(dto.diseases().containsKey(sub)) for(var gene: sub.genes()) {
					if(dto.diseases().get(sub).get(gene)) {
						stream = value(stream, template, dto, sub);
						continue L;
					}
				}
			}
		return stream;
	}
	protected PDPageContentStreamPageAccessible header(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 20;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
		// Util.icon(stream, resource.imgIcons()[7], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
		String text = mode==0?template.lblPositiveInterpretationTitle():template.lblPositiveDiseaseInfoTitle();
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, text));
		y -= HEADER_HEIGHT;
		y -= 0.5f;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	protected PDPageContentStreamPageAccessible table(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		TextStyle styleTableHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary())
			  .addRect(50, y-20,497, 20)
			  .fill();
		y -= 10;
		stream.paragraph(100, y, 115, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblVariantTable()[0]));
		stream.paragraph(200, y, 115, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblVariantTable()[1]));
		stream.paragraph(310, y, 115, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblVariantTable()[2]));
		stream.paragraph(420, y, 115, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblVariantTable()[3]));
		stream.paragraph(500, y, 115, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblVariantTable()[4]));
		var values = new LinkedList<GenomeScreenDto.Variant>();
		for(var disease: template.diseases()) for(var sub: disease.subs()) for(var gene: sub.genes()) if(dto.variants().containsKey(gene)) {
			var variants = dto.variants().get(gene);
			for(var variant: variants) if(!values.contains(variant)) values.add(variant);
		}
		y -= 2;
		for(var variant: values) {
			y -= 16;
			stream.paragraph(100, y, 115, CENTER, MIDDLE, new TextBlock(styleValue, variant.gene()));
			stream.paragraph(200, y, 115, CENTER, MIDDLE, new TextBlock(styleValue, variant.dnaChange()));
			stream.paragraph(310, y, 115, CENTER, MIDDLE, new TextBlock(styleValue, variant.predictedAa()));
			stream.paragraph(420, y, 115, CENTER, MIDDLE, new TextBlock(styleValue, variant.zygosity()));
			stream.paragraph(500, y, 115, CENTER, MIDDLE, new TextBlock(styleValue, variant.clazz()));
		}
		y -= 10;
		stream.setStrokingColor(resource.colorPrimary()).setLineWidth(0.25f).line(50, y, 547, y).stroke();
		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	protected PDPageContentStreamPageAccessible interpretation(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		TextBlock blockInfo = new TextBlock(styleValue, dto.interpretation());
		float height = 65+stream.height(497, blockInfo);
		if(y - height < 130) stream = newPage.and((s, t, d)->header(s, t)).paint(stream, template, dto);
		else stream = header(stream, template);
		y = stream.cursorY();
		y -= 14.5f;
		y = stream.paragraph(50, y, 497, JUSTIFY, blockInfo);
		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private final static float VALUE_ROW_HEIGHT = 40;
	protected PDPageContentStreamPageAccessible value(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto, GenomeScreenTemplate.DiseaseSub disease) throws IOException {
		var y = stream.cursorY();
		var resource = template.resource();
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
		TextBlock blockInfo = new TextBlock(styleValue, template.diseaseSubInfo(disease));
		float height = 15+stream.height(497, blockInfo)+5+ VALUE_ROW_HEIGHT +10;
		if(y - height < 130) {
			stream = newPage.and((s, t, d)->header(s, t)).paint(stream, template, dto);
			y = stream.cursorY();
		}
		stream.saveGraphicsState();
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, 20).fill();
		TextStyle styleTableHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		stream.paragraph(165, y-10, 230, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblPositiveDisease()));
		stream.paragraph(413.5f, y-10, 267, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblPositiveDiseaseInfo()));
		y -= 20;
		stream.paragraph(165, y-VALUE_ROW_HEIGHT/2, 230, CENTER, MIDDLE, new TextBlock(styleValue , template.diseaseSubName(disease)));
		stream.paragraph(413.5f, y-VALUE_ROW_HEIGHT/2, 267, CENTER, MIDDLE, new TextBlock(styleValue , template.diseaseSubReference(disease)));
		y -= VALUE_ROW_HEIGHT;
		stream.setStrokingColor(resource.colorPrimary()).setLineWidth(0.25f).line(50, y, 547, y).stroke();
		y -= 12;
		y = stream.paragraph(50, y, 497, JUSTIFY, blockInfo);
		y -= 25;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
