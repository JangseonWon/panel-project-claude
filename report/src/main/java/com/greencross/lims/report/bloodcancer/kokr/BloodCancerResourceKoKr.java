package com.greencross.lims.report.bloodcancer.kokr;

import com.gcgenome.lims.test.bloodcancer.TestInfo;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.bloodcancer.BloodCancerResource;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

@Getter
@Accessors(fluent = true)
public class BloodCancerResourceKoKr implements BloodCancerResource {
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

	public BloodCancerResourceKoKr(TestInfo test, PDDocument doc) throws IOException {
		this.doc = doc;
		fontDefault	= font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle	= font(new File(resource, "/font/GC140.ttf"));
		fontHeader	= font(new File(resource, "/font/OpenSans-Bold.ttf"));
		fontText	= font(new File(resource, "/font/OpenSans-Regular.ttf"));
		medal = img(new File(new File(resource, "img/medal"), "혈액암.png"));
		this.inspector = person("류해인");
		this.reporter = person("이청화");
		this.reviewer = test.isTissueTest() ? person("허주영")  : person("이새미");
	}
	@Override
	public SignLabel[] labels() {
		return new SignLabel[] {
				new SignLabel("검사자:", inspector),
				new SignLabel("보고자/검토자:", reporter, reviewer)
		};
	}
}
