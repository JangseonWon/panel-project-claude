package com.greencross.lims.report.genomescreen.enus;

import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.genomescreen.GenomeScreenResource;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;

// 암 지놈 스크린
@Getter
@Accessors(fluent = true)
public class GenomeScreenResourceN090EnUs extends GenomeScreenResourceEnUs {
	private final Color colorPrimary = Color.decode("#2BB8AC");
	private final PDFont fontDefault;
	private final PDImageXObject imgTitle;
	private final PDImageXObject imgHeaderLogo;
	private final HasSign.Person inspector;
	private final HasSign.Person reporter;
	private final HasSign.Person reviewer;
	public GenomeScreenResourceN090EnUs(PDDocument doc) throws IOException {
		super(doc);
		fontDefault	= font(new File(GenomeScreenResource.resource, "/font/NanumBarunGothic.ttf"));

		File img	= new File(GenomeScreenResource.resource, "img/genomescreen/N090/EnUs");
		imgTitle	= img(new File(img, "title.png"));
		imgHeaderLogo= img(new File(img, "header.png"));

		inspector = person("류해인");
		reporter = person("설창안");
		reviewer = person("이청화");
	}
	@Override
	public TextStyle stylePerson() {
		return new TextStyle().fonts(fontDefault()).color(colorText()).fontSize(7).paragraph(false);
	}
	@Override
	public SignLabel[] labels() {
		return new SignLabel[] {
				new SignLabel("Tested by:", inspector),
				new SignLabel("Reported by/Reviewed by:", reporter, reviewer)
		};
	}
}
