package com.greencross.lims.report.solidtumor2.enus;

import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.test.solidtumor2.TestInfo;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.solidtumor2.SolidTumorResource;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

import static com.gcgenome.lims.test.solidtumor2.TestInfo.ON198;

@Getter
@Accessors(fluent = true)
public class SolidTumorResourceEnUs implements SolidTumorResource {
	private final File resource = new File("/data/lims/resources");
	private final PDDocument doc;
	private final PDFont fontDefault;
	private final PDFont fontTitle;
	private final PDFont fontHeader;
	private final PDFont fontText;
	private final PDImageXObject medal;
	private final HasSign.Person inspector;
	private final HasSign.Person reporter;
	private final HasSign.Person reviewer;
	public SolidTumorResourceEnUs(TestInfo test, PDDocument doc) throws IOException {
		this.doc = doc;
		fontDefault	= font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle	= font(new File(resource, "/font/CALIBRIB.TTF"));
		fontHeader	= font(new File(resource, "/font/CALIBRI.TTF"));
		fontText	= font(new File(resource, "/font/CALIBRI.TTF"));
		inspector = person("문예솔");
		if (ON198 == test) {
			reporter = person("이청화");
			reviewer = person("이새미");
		} else {
			reporter = person("이청화");
			reviewer = person("허주영");
		}
		medal = img(new File(new File(resource, "img/medal"), "고형암.png"));
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
	@Override
	public TextStyle styleHeaderTitle() { return new TextStyle().color(colorText()).fonts(fontHeader(), fontText(), fontDefault()).fontSize(9); }
}
