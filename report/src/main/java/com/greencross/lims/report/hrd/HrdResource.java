package com.greencross.lims.report.hrd;

import com.gcgenome.lims.report.Resource;
import com.greencross.lims.report.HasSign;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;

public interface HrdResource extends Resource, HasSign {
	PDFont fontDefault();
	PDFont fontTitle();
	PDFont fontHeader();
	PDFont fontText();
	PDImageXObject gene1();
	PDImageXObject gene2();

	default Color colorPrimary() {
		return Color.decode("0X3D97A5");
	}
	default Color colorSecondary() {
		return Color.decode("0x00AEB3");
	}
	default Color colorRed() {
		return new Color(135,51,61);
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
	PDImageXObject medal();
}