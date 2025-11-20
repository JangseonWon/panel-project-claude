package com.greencross.lims.report.tmp;

import com.greencross.lims.report.HasSign;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Data
@Accessors(fluent = true)
public class BallondorResourceKoKr implements BallondorResource, HasSign {
	private final File resource = new File("/data/lims/resources");
	private final PDDocument doc;
	private final PDFont fontDefault;
	private final PDFont fontTitle;
	private final PDFont fontHeader;
	private final PDFont fontText;
	private final HasSign.Person inspector;
	private final HasSign.Person reporter;
	private final HasSign.Person reviewer;
	public BallondorResourceKoKr(PDDocument doc) throws IOException {
		this.doc = doc;
		fontDefault	= font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle	= font(new File(resource, "/font/GC140.ttf"));
		fontHeader	= font(new File(resource, "/font/OpenSans-Bold.ttf"));
		fontText	= font(new File(resource, "/font/OpenSans-Regular.ttf"));
		inspector = person("김다솜");
		reporter = person("조은해");
		reviewer = person("설창안");
	}
	@Override
	public SignLabel[] labels() {
		return new SignLabel[] {
				new SignLabel("검사자:", inspector),
				new SignLabel("보고자/검토자:", reporter, reviewer)
		};
	}
	@Override
	public Color colorText() {
		return BallondorResource.super.colorText();
	}
}
