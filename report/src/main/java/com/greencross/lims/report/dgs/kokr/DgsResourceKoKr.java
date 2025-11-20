package com.greencross.lims.report.dgs.kokr;

import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.dgs.DgsDto;
import com.greencross.lims.report.dgs.DgsResource;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Data
@Accessors(fluent = true)
public class DgsResourceKoKr implements DgsResource {
	private final File resource = new File("/data/lims/resources");
	private final PDDocument doc;
	private final PDFont fontDefault;
	private final PDFont fontTitle;
	private final PDFont fontHeader;
	private final PDFont fontText1;
	private final PDFont fontText2;
	private Color colorPrimary = new Color(213, 224, 248);
	private Color colorSecondary = new Color(43,102,221);
	private Color colorPrimaryLine =  new Color(130,155,187);
	private Color colorSecondaryLine =  new Color(130,155,187);
	private Color colorGray =  Color.decode("0xEFEFEF");
	private Color colorText =  Color.decode("0x484848");
	private Color colorTextWithPrimary = colorText();
	private Color colorTextWithSecondary = Color.WHITE;
	private final HasSign.Person inspector;
	private final HasSign.Person reporter;
	private final HasSign.Person reviewer;
	public DgsResourceKoKr(PDDocument doc, DgsDto dto) throws IOException {
		this.doc = doc;
		fontDefault	= font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle	= font(new File(resource, "/font/GC140.ttf"));
		fontHeader	= font(new File(resource, "/font/OpenSans-Bold.ttf"));
		fontText1	= font(new File(resource, "/font/OpenSans-Regular.ttf"));
		fontText2	= font(new File(resource, "/font/OpenSans-Regular.ttf"));
		inspector = person("류해인");
		reporter = person(dto.reporter());
		reviewer = person(dto.reviewer());
	}

	@Override
	public SignLabel[] labels() {
		return new SignLabel[] {
				new SignLabel("검사자:", inspector),
				new SignLabel("보고자/검토자:", reporter, reviewer)
		};
	}
}
