package com.greencross.lims.report.genomescreen.kokr;

import com.greencross.lims.report.genomescreen.GenomeScreenResource;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Data
@Accessors(fluent = true)
public abstract class GenomeScreenResourceKoKr implements GenomeScreenResource {
	@Getter
	private final PDDocument doc;
	private final PDFont fontHeader;
	private final PDFont fontValue;
	private final PDFont fontText;
	private final PDFont fontScientific;

	public GenomeScreenResourceKoKr(PDDocument doc) throws IOException {
		this.doc = doc;
		initialize();
		fontHeader	= font(new File(GenomeScreenResource.resource, "font/SDGothicNeoRound06.ttf"));
		fontValue	= font(new File(GenomeScreenResource.resource, "font/SdGothicNeoRound04.ttf"));
		fontText	= font(new File(GenomeScreenResource.resource, "font/NanumBarunGothic.ttf"));
		fontScientific= font(new File(GenomeScreenResource.resource, "font/OpenSans-Regular.ttf"));
	}
	@Override
	public Color colorText() {
		return GenomeScreenResource.super.colorText();
	}
}
