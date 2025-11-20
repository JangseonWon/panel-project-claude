package com.greencross.lims.report.tmp;

import com.gcgenome.lims.report.Resource;
import com.greencross.lims.report.HasSign;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;

public interface S051Resource extends Resource, HasSign {
	PDFont fontDefault();
	PDFont fontTitle();
	PDFont fontHeader();
	PDFont fontText();
	default Color colorGray() {
		return Color.decode("0xEFEFEF");
	}
	default Color colorText() {
		return Color.decode("0x484848");
	}
	PDImageXObject medal();
}
