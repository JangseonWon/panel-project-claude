package com.greencross.lims.report.sanger.enus;

import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.sanger.SangerDto;
import com.greencross.lims.report.sanger.SangerResource;
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
public class SangerResourceEnUs implements SangerResource {
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
	public SangerResourceEnUs(PDDocument doc, SangerDto dto) throws IOException {
		this.doc = doc;
		fontDefault	= font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle	= font(new File(resource, "/font/GC140.ttf"));
		fontHeader	= font(new File(resource, "/font/OpenSans-Bold.ttf"));
		fontText	= font(new File(resource, "/font/OpenSans-Regular.ttf"));
		medal = img(new File(new File(resource, "img/medal"), "Gene.png"));
		inspector = person(dto.inspector());
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
