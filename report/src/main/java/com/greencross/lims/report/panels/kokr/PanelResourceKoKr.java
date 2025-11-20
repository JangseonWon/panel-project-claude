package com.greencross.lims.report.panels.kokr;

import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.panels.PanelResource;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.gcgenome.lims.test.panel.TestInfo.G2200101;
import static com.gcgenome.lims.test.panel.TestInfo.G2200201;

@Data
@Accessors(fluent = true)
public class PanelResourceKoKr implements PanelResource, HasSign {
	private final File resource = new File("/data/lims/resources");
	private final PDDocument doc;
	private final PDFont fontDefault;
	private final PDFont fontTitle;
	private final PDFont fontHeader;
	private final PDFont fontText;
	private Color colorPrimary = new Color(226, 240, 217);
	private Color colorSecondary = new Color(84,130,53);
	private Color colorPrimaryLine =  new Color(84,130,53);
	private Color colorSecondaryLine =  new Color(226,240,217);
	private Color colorGray =  Color.decode("0xEFEFEF");
	private Color colorText =  Color.decode("0x484848");
	private Color colorTextWithPrimary = colorText();
	private Color colorTextWithSecondary = Color.WHITE;
	private final PDImageXObject medal;
	private final HasSign.Person inspector;
	private final HasSign.Person reporter;
	private final HasSign.Person reviewer;

	public PanelResourceKoKr(TestInfo test, PDDocument doc) throws IOException {
		this.doc = doc;
		fontDefault	= font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle	= font(new File(resource, "/font/GC140.ttf"));
		fontHeader	= font(new File(resource, "/font/OpenSans-Bold.ttf"));
		fontText	= font(new File(resource, "/font/OpenSans-Regular.ttf"));
		inspector = person("류해인");
		reporter = (G2200101 == test || G2200201 == test) ? person("이새미") : person("설창안");
		reviewer = (G2200101 == test || G2200201 == test) ? person("설창안") : person("이청화");
		if(TEST_CANCER.contains(test) ) medal = img(new File(new File(resource, "img/medal"), "Cancer.png"));
		else medal = img(new File(new File(resource, "img/medal"), "RareDisease.png"));
	}
	public PDFont font(File font) throws IOException {
		return PDType0Font.load(this.doc(), new FileInputStream(font), true);
	}

	@Override
	public SignLabel[] labels() {
		return new SignLabel[] {
				new SignLabel("검사자:", inspector),
				new SignLabel("보고자/검토자:", reporter, reviewer)
		};
	}
}
