package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.Util;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionSummary implements Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> {
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();
		y-= 25;
		var resource = template.resource();
		var styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(13).paragraph(false);
		stream.paragraph(25, y, 150, new TextBlock(styleTitle, template.lblSummary()));
		y-= 10;
		var styleTitleSmall = new TextStyle().color(Color.WHITE).fonts(resource.fontHeaderSmall(), resource.fontDefault()).fontSize(12).paragraph(false);
		float ty = y;
		Util.gradient(stream, s->s.rect(25, ty, 545, 20), resource.gradient(), 25, 545);
		if(dto.report()!=null) stream.paragraph(35, y-10, 150, MIDDLE, new TextBlock(styleTitleSmall, template.result(dto.result())));
		y-= 40;
		var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(false);
		if(dto.report()!=null) y = stream.paragraph(50, y, 520, new TextBlock(styleText, dto.report().resultText()));
		y-= 15;
		float ty2 = y;
		Util.gradient(stream, s->s.rect(25, ty2, 545, 0.2f), resource.gradient(), 25, 545);
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
