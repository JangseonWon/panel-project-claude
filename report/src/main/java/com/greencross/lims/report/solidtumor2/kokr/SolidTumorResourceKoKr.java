package com.greencross.lims.report.solidtumor2.kokr;

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

import static com.gcgenome.lims.test.solidtumor2.TestInfo.G0022402;
import static com.gcgenome.lims.test.solidtumor2.TestInfo.N198;

@Getter
@Accessors(fluent = true)
public class SolidTumorResourceKoKr implements SolidTumorResource {
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
	public SolidTumorResourceKoKr(TestInfo test, PDDocument doc) throws IOException {
		this.doc = doc;
		fontDefault	= font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle	= font(new File(resource, "/font/GC140.ttf"));
		fontHeader	= font(new File(resource, "/font/OpenSans-Regular.ttf"));
		fontText	= font(new File(resource, "/font/OpenSans-Regular.ttf"));
		inspector = person("문예솔");
		if (N198 == test || G0022402 == test) {
			reporter = person("이청화");
			reviewer = person("이새미");
		} else {
			reporter = person("이청화");
			reviewer = person("허주영");
		}
		medal = img(new File(new File(resource, "img/medal"), "고형암.png"));
	}
	@Override
	public SignLabel[] labels() {
		return new SignLabel[] {
				new SignLabel("검사자:", inspector),
				new SignLabel("보고자/검토자:", reporter, reviewer)
		};
	}
}
