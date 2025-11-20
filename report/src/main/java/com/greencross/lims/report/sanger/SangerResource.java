package com.greencross.lims.report.sanger;

import com.gcgenome.lims.report.Resource;
import com.greencross.lims.report.HasSign;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;

public interface SangerResource extends Resource, HasSign {
	PDFont fontDefault();
	PDFont fontTitle();
	PDFont fontHeader();
	PDFont fontText();

	default Color colorPrimary() {
		return Color.decode("#333F48");
	}
	default Color colorSecondary() {
		return Color.decode("#333F48");
	}
	default Color colorPrimaryLine() {
		return Color.decode("#C3CDD4");
	}
	default Color colorSecondaryLine() {
		return Color.decode("#C3CDD4");
	}
	default Color colorGray() {
		return Color.decode("0xEFEFEF");
	}
	default Color colorText() {
		return Color.decode("0x484848");
	}
	default Color colorTextWithPrimary() {
		return colorText();
	}
	default Color colorTextWithSecondary() {
		return Color.WHITE;
	}
	PDImageXObject medal();
}