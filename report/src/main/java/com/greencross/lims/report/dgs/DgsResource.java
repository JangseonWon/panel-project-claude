package com.greencross.lims.report.dgs;

import com.gcgenome.lims.report.Resource;
import com.greencross.lims.report.HasSign;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;

public interface DgsResource extends Resource, HasSign {
	PDFont fontDefault();
	PDFont fontTitle();
	PDFont fontHeader();
	PDFont fontText1();
	PDFont fontText2();

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