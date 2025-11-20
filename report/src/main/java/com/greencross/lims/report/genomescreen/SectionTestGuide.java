package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.builder.Util;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionTestGuide implements Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> {
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage;
	public SectionTestGuide(Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage) {
		this.newPage = newPage;
	}
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		return guideTest().and(guideDisease()).paint(stream, template, dto);
	}
	private final static float HEADER_HEIGHT = 20;
	private Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> guideTest() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			var resource = template.resource();
			var y = stream.cursorY();
			y -= 20;
			TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
			stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
			// Util.icon(stream, resource.imgIcons()[1], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
			stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblGuideTestTitle()));
			y -= HEADER_HEIGHT;
			y -= 15;
			y = stream.paragraph(50, y, 497, JUSTIFY, template.guideTest());
			y -= 10;
			stream.restoreGraphicsState();
			return stream.cursorY(y);
		};
	}
	private Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> guideDisease() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			var resource = template.resource();
			var y = stream.cursorY();
			y -= 20;
			TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
			stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
			//Util.icon(stream, resource.imgIcons()[1], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
			stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblGuideDiseaseTitle()));
			y -= HEADER_HEIGHT;
			y -= 15;
			stream.setNonStrokingColor(resource.colorPrimary());
			for(TextBlock guide: template.guideDisease()) {
				stream.circle(53, y+2, 3).fill();
				y = stream.paragraph(60, y, 487, JUSTIFY, guide);
				y -= 20;
			}
			y -= 10;
			stream.restoreGraphicsState();
			return stream.cursorY(y);
		};
	}
}
