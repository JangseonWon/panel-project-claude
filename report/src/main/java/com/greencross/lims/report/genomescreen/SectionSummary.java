package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionSummary implements Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> {
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		return info().and(summary()).paint(stream, template, dto);
	}
	private final static float HEADER_HEIGHT = 20;
	private Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> info() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			var resource = template.resource();
			var y = stream.cursorY();
			y -= 20;
			TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
			TextStyle styleValue = new TextStyle().color(Color.WHITE).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).justify(true).paragraph(true);
			TextBlock blockInfo = new TextBlock(styleValue.clone().color(resource.colorText()), template.lblSummaryInfo());
			stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
			// Util.icon(stream, resource.imgIcons()[0], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
			stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblSummaryTestIntroTitle()));
			stream.paragraph(250, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleValue, template.lblSummaryTestIntroTitleInfo()));
			y -= HEADER_HEIGHT;
			y -= 15;
			y = stream.paragraph(50, y, 497, JUSTIFY, blockInfo);
			y -= 10;
			stream.restoreGraphicsState();
			return stream.cursorY(y);
		};
	}
	private Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> summary() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			var resource = template.resource();
			var y = stream.cursorY();
			y -= 30;
			TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
			TextStyle styleValue = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(12).paragraph(true);
			TextBlock blockInfo = new TextBlock(styleValue.clone().color(resource.colorText()), dto.summary());
			stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
			// Util.icon(stream, resource.imgIcons()[7], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
			stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblSummaryTitle()));
			y -= HEADER_HEIGHT;
			y -= 40;
			y = stream.paragraph(297.5f, y, 497, CENTER, blockInfo);
			y -= 30;
			stream.restoreGraphicsState();
			return stream.cursorY(y);
		};
	}
}
