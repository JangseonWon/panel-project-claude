package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.Resource;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.HasSign;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.fontbox.ttf.OTFParser;
import org.apache.fontbox.ttf.OpenTypeFont;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Data
@Accessors(fluent = true)
public abstract class HereditaryResourceEnUs implements Resource, HasSign {
	@Getter
	private final PDDocument doc;
	private final PDFont fontHeader;
	private final PDFont fontHeaderSmall;
	private final PDFont fontHeaderSmall2;
	private final PDFont fontTitle;
	private final PDFont fontTitleSmall;
	private final PDFont fontText;
	private final PDFont fontScientific;
	protected final File resource = new File("/data/lims/resources");
	private final HasSign.Person inspector;
	private final HasSign.Person reporter;
	private final HasSign.Person reviewer;
	public HereditaryResourceEnUs(PDDocument doc) throws IOException {
		this.doc = doc;
		fontTitle		= font(new File(resource, "/font/GC150.ttf"));
		fontTitleSmall	= font(new File(resource, "/font/GC130.ttf"));
		fontHeader		= otf(new File(resource, "font/MyriadPro-Bold.otf"));
		fontHeaderSmall	= otf(new File(resource, "font/MyriadPro-BoldIt.otf"));
		fontHeaderSmall2= otf(new File(resource, "font/MyriadPro-Semibold.otf"));
		fontText		= otf(new File(resource, "font/MyriadPro-Regular.otf"));
		fontScientific	= font(new File(resource, "font/OpenSans-Regular.ttf"));
		inspector = person("류해인");
		reporter = person("설창안");
		reviewer = person("이청화");
	}
	private PDFont otf(File font) throws IOException {
		OTFParser otfParser = new OTFParser();
		OpenTypeFont otf = otfParser.parse(font);
		return PDType0Font.load(this.doc(), otf, false);
	}
	public abstract Color colorPrimary();
	public abstract PDImageXObject icon();
	public abstract PDImageXObject cancer();
	public abstract COSDictionary gradient();
	public Color colorGray() {
		return Color.decode("0xF3F3F4");
	}
	@Override
	public Color colorText() {
		return Color.decode("0x484848");
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
