package com.greencross.lims.report.bloodcancer.enus;

import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.bloodcancer.BloodCancerResource;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Getter
@Accessors(fluent = true)
public class BloodCancerResourceEnUs implements BloodCancerResource {
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

	public BloodCancerResourceEnUs(PDDocument doc) throws IOException {
		this.doc = doc;
		fontDefault	= font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle	= font(new File(resource, "/font/GC140.ttf"));
		fontHeader	= font(new File(resource, "/font/OpenSans-Bold.ttf"));
		fontText	= font(new File(resource, "/font/OpenSans-Regular.ttf"));
		medal = img(new File(new File(resource, "img/medal"), "혈액암.png"));
		this.inspector = person("류해인");
		this.reporter = person("이청화");
		this.reviewer = person("이새미");
	}
	@Override
	public Color colorText() {
		return BloodCancerResource.super.colorText();
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
