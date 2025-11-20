package com.greencross.lims.report.wes.enus;

import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.wes.WesDto;
import com.greencross.lims.report.wes.WesResource;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Getter
@Accessors(fluent = true)
public class WesResourceEnUs implements WesResource {
	private final File resource = new File("/data/lims/resources");
	private final PDDocument doc;
	private final PDFont fontDefault;
	private final PDFont fontTitle;
	private final PDFont fontHeader;
	private final PDFont fontText;
	private Color colorPrimary = new Color(219, 230, 240);
	private Color colorSecondary = new Color(55, 95, 146);
	private Color colorPrimaryLine = new Color(130, 155, 187);
	private Color colorSecondaryLine = new Color(130, 155, 187);
	private Color colorGray = Color.decode("0xEFEFEF");
	private Color colorText = Color.decode("0x484848");
	private Color colorTextWithPrimary = colorText();
	private Color colorTextWithSecondary = Color.WHITE;
	private final HasSign.Person inspector;
	private final HasSign.Person reporter;
	private final HasSign.Person reviewer;

	public WesResourceEnUs(PDDocument doc, WesDto dto) throws IOException {
		this.doc = doc;
		fontDefault = font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle = font(new File(resource, "/font/OpenSans-Bold.ttf"));
		fontHeader = font(new File(resource, "/font/OpenSans-Bold.ttf"));
		fontText = font(new File(resource, "/font/OpenSans-Regular.ttf"));
		inspector = WesResourceEnUs.this.person("류해인") ;
		reporter = person(dto.reporter());
		reviewer = person(dto.reviewer());
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
