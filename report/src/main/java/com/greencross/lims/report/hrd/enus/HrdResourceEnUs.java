package com.greencross.lims.report.hrd.enus;

import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.hrd.HrdResource;
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
public class HrdResourceEnUs implements HrdResource {
	private final File resource = new File("/data/lims/resources");
	private final PDDocument doc;
	private final PDFont fontDefault;
	private final PDFont fontTitle;
	private final PDFont fontHeader;
	private final PDFont fontText;
	private final PDImageXObject gene1;
	private final PDImageXObject gene2;
	private final PDImageXObject medal;
	private final HasSign.Person inspector;
	private final HasSign.Person reporter;
	private final HasSign.Person reviewer;
	public HrdResourceEnUs(PDDocument doc) throws IOException {
		this.doc = doc;
		fontDefault	= font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle	= font(new File(resource, "/font/GC140.ttf"));
		fontHeader	= font(new File(resource, "/font/CALIBRI.TTF"));
		fontText	= font(new File(resource, "/font/CALIBRI.TTF"));
		gene1       = img(new File(resource, "/img/hrd/gene1.png"));
		gene2       = img(new File(resource, "/img/hrd/gene2.png"));
		medal = img(new File(new File(resource, "img/medal"), "HRD.png"));
		this.inspector = person("문예솔");
		this.reporter = person("이새미");
		this.reviewer = person("허주영");
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
	public Color colorText() {
		return HrdResource.super.colorText();
	}
}
