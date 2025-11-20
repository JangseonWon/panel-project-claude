package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.Resource;
import com.greencross.lims.report.HasSign;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public interface GenomeScreenResource extends Resource, HasSign {
	Color colorPrimary();
	default Color colorGray() {
		return Color.decode("0xEFEFEF");
	}
	default Color colorDarkGray() {
		return Color.decode("0xB5B5B5");
	}
	default Color colorText() { return Color.decode("0x484848"); }
	PDFont fontDefault();
	PDFont fontHeader();
	PDFont fontText();
	PDFont fontScientific();
	PDImageXObject imgTitle();
	PDImageXObject imgHeaderLogo();

	/*default PDImageXObject[] imgIcons() {
		return imgIcons;
	}*/
	default PDImageXObject imgCheck() {
		return imgCheck[0];
	}
	default void initialize() throws IOException {
		/*imgIcons[0] = img(new File(resource, "img/genomescreen/icon1.png"));
		imgIcons[1] = img(new File(resource, "img/genomescreen/icon2.png"));
		imgIcons[2] = img(new File(resource, "img/genomescreen/icon3.png"));
		imgIcons[3] = img(new File(resource, "img/genomescreen/icon4.png"));
		imgIcons[4] = img(new File(resource, "img/genomescreen/icon5.png"));
		imgIcons[5] = img(new File(resource, "img/genomescreen/icon6.png"));
		imgIcons[6] = img(new File(resource, "img/genomescreen/icon7.png"));
		imgIcons[7] = img(new File(resource, "img/genomescreen/icon8.png"));*/

		imgCheck[0] = img(new File(resource, "img/genomescreen/check.png"));
	}
	File resource = new File("/data/lims/resources");
	PDImageXObject[] imgIcons = new PDImageXObject[8];
	PDImageXObject[] imgCheck = new PDImageXObject[1];
}
