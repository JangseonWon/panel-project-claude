package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.Util;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionResult implements Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> {
	private final static float[] COLUMN_WIDTHS = new float[] {80, 100, 100, 60, 85, 60, 60};
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();

		y-= 25;
		var resource = template.resource();
		var styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(13).paragraph(false);
		stream.paragraph(25, y, 150, new TextBlock(styleTitle, template.lblResult()));
		y-= 10;
		var styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontText(), resource.fontDefault()).fontSize(11).paragraph(false);
		float ty = y;
		float sx = 25;
		Util.gradient(stream, s->s.rect(25, ty, 545, 20), resource.gradient(), 25, 545);
		y -= 10;
		stream.setStrokingColor(resource.colorGray()).setLineWidth(0.5f);
		for(int i = 0; i < COLUMN_WIDTHS.length; ++i) {
			stream.paragraph(sx + COLUMN_WIDTHS[i]/2, y, 150, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblVariantTable()[i]));
			sx += COLUMN_WIDTHS[i];stream.line(sx, y-10, sx, y+10);
		}
		stream.stroke();
		y-= 10;
		var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).paragraph(false);
		if(dto.report()!=null && dto.report().variants()!=null && dto.report().variants().length > 0) {
			for(HereditaryEnUsDto.Variant variant: dto.report().variants()) {
				y -= 11;
				stream = variant(stream.cursorY(y), styleText, variant);
				float ty2 = stream.cursorY();
				Util.gradient(stream, s->s.rect(25, ty2, 545, 0.2f), resource.gradient(), 25, 545);
				y = ty2;
			}
			y -= 15;
			String reference = dto.report().reference();
			String diseases = dto.report().omimDisease();
			String abbrs = dto.report().abbreviation();
			y = stream.paragraph(35, y, 470, 1.5f,
					new TextBlock(styleText, StringUtils.hasText(reference)?("· " + template.lblReferenceSequence() + ": " + reference + "\n"):""),
					new TextBlock(styleText, StringUtils.hasText(diseases)?("· " + template.lblOmimDisease() + ": " + diseases + "\n"):""),
					new TextBlock(styleText, StringUtils.hasText(abbrs)?("· " + template.lblAbbreviation() + ": " + abbrs):""));
		}  else {
			y -= 11;
			y = stream.paragraph(297.5f, y, 520, CENTER, new TextBlock(styleText, template.noDiseaseVariant()));
			y-= 5;
			float ty2 = y;
			Util.gradient(stream, s->s.rect(25, ty2, 545, 0.2f), resource.gradient(), 25, 545);
		}
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible variant(PDPageContentStreamPageAccessible stream, TextStyle styleText, HereditaryEnUsDto.Variant variant) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();
		float sx = 25;
		float sy = stream.paragraph(sx + COLUMN_WIDTHS[0]/2, y, COLUMN_WIDTHS[0], CENTER,  new TextBlock(styleText, variant.gene())); 				sx += COLUMN_WIDTHS[0];
		sy = Math.min(sy, stream.paragraph(sx + COLUMN_WIDTHS[1]/2, y, COLUMN_WIDTHS[1], CENTER, new TextBlock(styleText, variant.dnaChange())));	sx += COLUMN_WIDTHS[1];
		sy = Math.min(sy, stream.paragraph(sx + COLUMN_WIDTHS[2]/2, y, COLUMN_WIDTHS[2], CENTER, new TextBlock(styleText, variant.predictedAa())));	sx += COLUMN_WIDTHS[2];
		sy = Math.min(sy, stream.paragraph(sx + COLUMN_WIDTHS[3]/2, y, COLUMN_WIDTHS[3], CENTER, new TextBlock(styleText, variant.zygosity())));	sx += COLUMN_WIDTHS[3];
		sy = Math.min(sy, stream.paragraph(sx + COLUMN_WIDTHS[4]/2, y, COLUMN_WIDTHS[4], CENTER, new TextBlock(styleText, variant.omim())));		sx += COLUMN_WIDTHS[4];
		sy = Math.min(sy, stream.paragraph(sx + COLUMN_WIDTHS[5]/2, y, COLUMN_WIDTHS[5], CENTER, new TextBlock(styleText, variant.inherit())));		sx += COLUMN_WIDTHS[5];
		sy = Math.min(sy, stream.paragraph(sx + COLUMN_WIDTHS[6]/2, y, COLUMN_WIDTHS[6], CENTER, new TextBlock(styleText, variant.clazz())));
		y = sy-5;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
