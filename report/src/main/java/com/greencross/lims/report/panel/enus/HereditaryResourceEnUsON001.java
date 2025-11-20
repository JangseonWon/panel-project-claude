package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.Resource;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Getter
@Accessors(fluent = true)
public class HereditaryResourceEnUsON001 extends HereditaryResourceEnUs {
	private final PDFont fontDefault;
	private final File img						= new File(resource, "img/hereditary/ON001/EnUs");
	private final Color colorPrimary			= Color.decode("0xE1615C");
	private final COSDictionary gradient		= Resource.gradient(Color.decode("0xED726B"), Color.decode("0xCE5668"));
	private final PDImageXObject icon;
	private final PDImageXObject cancer;
	public HereditaryResourceEnUsON001(PDDocument doc) throws IOException {
		super(doc);
		icon			= img(new File(img, "icon.png"));
		cancer			= img(new File(img, "cancer.png"));
		fontDefault	= font(new File(resource, "font/NanumBarunGothic.ttf"));
	}
}
