package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.io.IOException;

public class SectionInterpretation implements Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> {
	private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> newPage;
	public SectionInterpretation(Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> newPage) {
		this.newPage = newPage;
	}

	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
		header(stream, template);
		stream.saveGraphicsState();
		float y = stream.cursorY();
		float boxHeight = y - 100;
		stream.setNonStrokingColor(template.resource().colorGray()).rect(25, y, 545, boxHeight).fill();
		stream.cursorY(y-21);
		stream.restoreGraphicsState();
		for(String text: dto.report().interpretation().split("\n", -1)) stream = text(stream, template, dto, text);
		return stream.cursorY(100f);
	}
	private PDPageContentStreamPageAccessible header(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();
		y-= 25;
		var resource = template.resource();
		var styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(13).paragraph(false);
		stream.paragraph(25, y, 150, new TextBlock(styleTitle, template.lblInterpretation()));
		y-= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	public PDPageContentStreamPageAccessible text(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto, String interpretation) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();
		var resource = template.resource();
		var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).justify(false).paragraph(true);
		var block = new TextBlock(styleText, interpretation);
		var height = stream.height(520, block);
		if(y - height - 13 < 100) {
			stream = newPage.and((s, t, d)->header(s, t)).paint(stream, template, dto);
			y = stream.cursorY();
			stream = header(stream, template);
			float boxHeight = y - 100;
			stream.setNonStrokingColor(resource.colorGray()).rect(25, y, 545, boxHeight).fill();
			y-= 21;
		}
		y = stream.paragraph(40, y, 520, new TextBlock(styleText, interpretation));
		y -= 10*1.3f;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
