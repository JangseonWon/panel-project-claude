package com.greencross.lims.report;

import com.gcgenome.lims.report.Template;
import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.AbstractReportDto;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionRevision<T extends Template<?>&Revisionable, D extends AbstractReportDto> implements Painter<T, D> {
	private final static int REVISION_BOX_HEIGHT = 20;
	private final static float REVISION_BOX_POS_X = 540;
	private final static float REVISION_BOX_POS_Y = 750;
	private final static Color COLOR = new Color(135,51,61);
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, T template, D dto) throws IOException {
		if(dto.revision()!=null && dto.revision()) {
			stream.saveGraphicsState();
			stream.setStrokingColor(COLOR);
			TextStyle ts = new TextStyle().color(new Color(135,51,61)).fontSize(10);
			TextBlock msg = new TextBlock(ts, template.revisionComment());

			stream.paragraph(REVISION_BOX_POS_X+2, REVISION_BOX_POS_Y, 800, RIGHT, MIDDLE, msg);
			stream.restoreGraphicsState();
		}
		return stream;
	}
}
