package com.greencross.lims.report.wes;

import com.gcgenome.lims.report.Resource;
import com.greencross.lims.report.HasSign;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;

public interface WesResource extends Resource, HasSign {
	PDFont fontDefault();
	PDFont fontTitle();
	PDFont fontHeader();
	PDFont fontText();

	default Color colorPrimary() {
		return new Color(226, 240, 217);
	}
	default Color colorSecondary() {
		return new Color(84,130,53);
	}
	default Color colorPrimaryLine() {
		return new Color(226,240,217);
	}
	default Color colorSecondaryLine() {
		return new Color(226,240,217);
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
}