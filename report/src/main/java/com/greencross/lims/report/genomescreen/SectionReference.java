package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.builder.Util;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionReference implements Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> {
	private final static float HEADER_HEIGHT = 20;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage;
	public SectionReference(Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage) {
		this.newPage = newPage;
	}
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		float height = 55 + 12*template.references().length;
		if(stream.cursorY() - height < 80) stream = newPage.paint(stream, template, dto);
		stream = header(stream, template);
		stream = contents(stream, template);
		return stream;
	}
	private PDPageContentStreamPageAccessible header(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 20;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
		//Util.icon(stream, resource.imgIcons()[5], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblReferenceTitle()));
		y -= HEADER_HEIGHT;
		y -= 15;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible contents(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		var y = stream.cursorY();
		var resource = template.resource();
		stream.saveGraphicsState();
		TextStyle styleHeader = new TextStyle().color(resource.colorText()).fonts(resource.fontScientific(), resource.fontDefault()).fontSize(8).paragraph(true);
		for(int i = 0; i < template.references().length; ++i) {
			TextBlock block = template.references()[i];
			stream.paragraph(55, y, 497, RIGHT, new TextBlock(styleHeader, i + 1 + "."));
			y = stream.paragraph(60, y, 487, JUSTIFY, block);
			y -= 12;
		}
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
