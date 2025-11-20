package com.greencross.lims.report.single.kokr;

import com.gcgenome.lims.test.single.TestInfo;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.single.SingleGeneResource;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static com.gcgenome.lims.test.single.TestInfo.Category;

@Data
@Accessors(fluent = true)
public class SingleGeneResourceKoKr implements SingleGeneResource {
	private final File resource = new File("/data/lims/resources");
	private final PDDocument doc;
	private final PDFont fontDefault;
	private final PDFont fontTitle;
	private final PDFont fontHeader;
	private final PDFont fontText;
	private Color colorPrimary = Color.decode("#333F48");
	private Color colorSecondary = Color.decode("#333F48");
	private Color colorPrimaryLine =  Color.decode("#C3CDD4");
	private Color colorSecondaryLine =  Color.decode("#C3CDD4");
	private Color colorGray =  Color.decode("0xEFEFEF");
	private Color colorText =  Color.decode("0x484848");
	private Color colorTextWithPrimary = Color.WHITE;
	private Color colorTextWithSecondary = Color.WHITE;
	private final PDImageXObject medal;
	private final HasSign.Person inspector;
	private final HasSign.Person reporter;
	private final HasSign.Person reviewer;
	public SingleGeneResourceKoKr(TestInfo test, PDDocument doc) throws IOException {
		this.doc = doc;
		fontDefault = font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle = font(new File(resource, "/font/GC140.ttf"));
		fontHeader = font(new File(resource, "/font/OpenSans-Bold.ttf"));
		fontText = font(new File(resource, "/font/OpenSans-Regular.ttf"));
		inspector = person("류해인");
		if (test.category() == Category.BRCA || test.category() == Category.CANCER) {
			reporter = person("설창안");
			reviewer = person("이청화");
			medal = img(new File(new File(resource, "img/medal"), "Cancer.png"));
		} else if (test.category() == Category.WITH_MLPA){
			reporter = person("이새미");
			reviewer = person("설창안");
			medal = img(new File(new File(resource, "img/medal"), "Gene.png"));
		} else {
			reporter = person("설창안");
			reviewer = person("이청화");
			medal = img(new File(new File(resource, "img/medal"), "Gene.png"));
		}
	}

	@Override
	public SignLabel[] labels() {
		return new SignLabel[] {
				new SignLabel("검사자:", inspector),
				new SignLabel("보고자/검토자:", reporter, reviewer)
		};
	}
}
