package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionMyResult implements Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> {
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 30;
		stream.setLineWidth(0.75f).setLineDashPattern(new float[] {1, 2}, 2)
			  .setStrokingColor(resource.colorDarkGray()).roundRect(50, y-20, 495, 20, 5)
			  .stroke();
		y -= 10;
		TextStyle styleSummary = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8).paragraph(true);
		stream.paragraph(297.5f, y, 500, CENTER, MIDDLE, new TextBlock(styleSummary, template.fmtMyResult(dto)));

		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
