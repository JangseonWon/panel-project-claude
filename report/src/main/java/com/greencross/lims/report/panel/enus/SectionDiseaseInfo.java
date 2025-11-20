package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.Util;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionDiseaseInfo implements Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> {
	private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> newPage;
	public SectionDiseaseInfo(Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> newPage) {
		this.newPage = newPage;
	}
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
		if(dto.report()==null || dto.report().variants() == null || dto.report().variants().length <= 0) return stream;
		List<HereditaryTemplateEnUs.CancerType> types = new LinkedList<>();
		Set<String> genes = Arrays.stream(dto.report().variants()).map(HereditaryEnUsDto.Variant::gene).collect(Collectors.toSet());
		for(HereditaryTemplateEnUs.CancerType type: template.cancerTypes()) if(Arrays.stream(type.genes()).anyMatch(g->genes.contains(g.symbol()))) types.add(type);
		for(var type: types) stream = newPage.and((s, t, d)->paint(s, t, type)).paint(stream, template, dto);
		return stream;
	}

	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryTemplateEnUs.CancerType type) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();
		y-= 25;
		var resource = template.resource();
		var styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(13).paragraph(false);
		stream.paragraph(25, y, 400, new TextBlock(styleTitle, template.lblRelevantDisease()));
		y-= 10;
		var styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(12).paragraph(false);
		float ty = y;
		Util.gradient(stream, s->s.rect(25, ty, 545, 20), resource.gradient(), 25, 545);
		stream.paragraph(35, y-10, 400, MIDDLE, new TextBlock(styleHeader, type.title()));
		y-= 40;

		var styleHeader2 = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitleSmall(), resource.fontDefault()).fontSize(11).paragraph(false);
		stream.paragraph(45, y-10, 400, MIDDLE, new TextBlock(styleHeader2, template.lblDiseaseInformation()));
		y -= 30;
		var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(false);
		y = stream.paragraph(45, y, 520, new TextBlock(styleText, type.information()));
		y -= 30;
		stream.setLineDashPattern(new float[] {2, 2}, 0).setLineWidth(0.5f).setStrokingColor(resource.colorPrimary())
				.line(25, y, 570, y).stroke();
		y -= 20;
		stream.paragraph(45, y-10, 400, MIDDLE, new TextBlock(styleHeader2, template.lblGeneticFeature()));
		y -= 30;
		y = stream.paragraph(45, y, 520, new TextBlock(styleText, type.feature()));

		float ty3 = y - 30;
		Util.gradient(stream, s->s.rect(25, ty3, 545, 0.2f), resource.gradient(), 25, 545);
		stream.restoreGraphicsState();
		return stream.cursorY(ty3);
	}
}
