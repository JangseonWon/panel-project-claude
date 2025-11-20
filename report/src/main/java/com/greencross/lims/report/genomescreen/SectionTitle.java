package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.io.IOException;

public class SectionTitle implements Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> {
	private final static float TITLE_HEIGHT = 20;

	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		stream.saveGraphicsState();
		var img = template.resource().imgTitle();
		var width = img.getWidth() * TITLE_HEIGHT / img.getHeight();
		float y = 773;
		stream.drawImage(img, 297.5f - width/2, y, width, TITLE_HEIGHT);
		stream.restoreGraphicsState();
		return stream.cursorY(y - TITLE_HEIGHT);
	}
}
