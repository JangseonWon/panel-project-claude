package com.greencross.lims.report.genomescreen;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.IOException;

public interface RiskScreenResource extends GenomeScreenResource {
	default Color colorSafe() {
		return Color.decode("#274374");
	}
	default Color colorWarn() {
		return Color.decode("#AD1742");
	}
	PDImageXObject icon(RiskScreenTemplate.RiskFactor factor) throws IOException;
	PDImageXObject[] dietGoodIcons() throws IOException;
	PDImageXObject[] dietBadIcons() throws IOException;
	PDImageXObject[] athleticIcons() throws IOException;
}
