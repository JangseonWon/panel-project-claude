package com.greencross.lims.report.genomescreen.kokr;

import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.genomescreen.GenomeScreenResource;
import com.greencross.lims.report.genomescreen.RiskScreenResourceN088;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;

// 고지혈증 지놈 스크린
@Getter
@Accessors(fluent = true)
public class GenomeScreenResourceN088KoKr extends GenomeScreenResourceKoKr implements RiskScreenResourceN088 {
	private final Color colorPrimary = Color.decode("#EB411D");
	private final PDFont fontDefault;
	private final File img						= new File(GenomeScreenResource.resource, "img/genomescreen/N088/KoKr");
	private final PDImageXObject imgTitle		= img(new File(img, "title.png"));
	private final PDImageXObject imgHeaderLogo	=img(new File(img, "header.png"));
	private final HasSign.Person inspector;
	private final HasSign.Person reporter;
	private final HasSign.Person reviewer;
	public GenomeScreenResourceN088KoKr(PDDocument doc) throws IOException {
		super(doc);
		fontDefault	= font(new File(GenomeScreenResource.resource, "font/NanumBarunGothic.ttf"));
		inspector = person("류해인");
		reporter = person("기창석");
		reviewer = person("설창안");
	}
	@Override
	public PDImageXObject[] dietGoodIcons() throws IOException {
		return new PDImageXObject[] {
				img(new File(img, "diet_good1.png")),
				img(new File(img, "diet_good2.png")),
				img(new File(img, "diet_good3.png")),
				img(new File(img, "diet_good4.png")),
				img(new File(img, "diet_good5.png"))
		};
	}
	@Override
	public PDImageXObject[] dietBadIcons() throws IOException {
		return new PDImageXObject[] {
				img(new File(img, "diet_bad1.png")),
				img(new File(img, "diet_bad2.png")),
				img(new File(img, "diet_bad3.png")),
				img(new File(img, "diet_bad4.png")),
				img(new File(img, "diet_bad5.png"))
		};
	}
	@Override
	public SignLabel[] labels() {
		return new SignLabel[] {
				new SignLabel("검사자:", inspector),
				new SignLabel("보고자/검토자:", reporter, reviewer)
		};
	}
}
