package com.greencross.lims.report.panels.enus;

import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.panels.PanelResource;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;


@Data
@Accessors(fluent = true)
public class PanelResourceEnUs implements PanelResource {
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

	public PanelResourceEnUs(TestInfo test, PDDocument doc) throws IOException {
		this.doc = doc;
		fontDefault	= font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle	= font(new File(resource, "/font/GC140.ttf"));
		fontHeader	= font(new File(resource, "/font/OpenSans-Bold.ttf"));
		fontText	= font(new File(resource, "/font/OpenSans-Regular.ttf"));
		inspector = person("류해인");
		reporter = person("설창안");
		reviewer = person("이청화");
		medal = img(new File(new File(resource, "img/medal"), "RareDisease.png"));
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
