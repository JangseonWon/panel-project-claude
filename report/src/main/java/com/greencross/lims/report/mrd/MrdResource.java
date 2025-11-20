package com.greencross.lims.report.mrd;

import com.gcgenome.lims.report.Resource;
import com.greencross.lims.report.HasSign;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;

public interface MrdResource extends Resource, HasSign {
	PDFont fontDefault();
	PDFont fontTitle();
	PDFont fontHeader();
	PDFont fontText();

	default Color colorPrimary() {
		return Color.decode("0x8E3A80");
	}
	default Color colorSecondary() {
		return Color.decode("0xE1B6DA");
	}
	default Color colorGray() {
		return Color.decode("0xEFEFEF");
	}
	default Color colorText() {
		return Color.decode("0x484848");
	}
	default Color colorTextWithPrimary() {
		return Color.WHITE;
	}
	default Color colorTextWithSecondary() {
		return colorText();
	}
	default Color colorGraphLinePrimary() {
		return Color.decode("0x36547A");
	}
	default Color colorGraphLineSecondary() {
		return Color.decode("0xE7440B");
	}
	PDImageXObject medal();
}
