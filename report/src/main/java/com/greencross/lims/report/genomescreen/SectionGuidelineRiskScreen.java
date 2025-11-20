package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.builder.Util;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionGuidelineRiskScreen implements Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> {
	private final static float HEADER_HEIGHT = 20;
	private final static float ATHLETIC_GUIDE_HEADER_WIDTH = 110;
	private final static float ATHLETIC_GUIDE_ICON_SIZE = 50;
	private final static float ATHLETIC_GUIDE_ICON_MARGIN = 30;
	private final static float ATHLETIC_GUIDE_KINDS_HEIGHT = ATHLETIC_GUIDE_ICON_SIZE + ATHLETIC_GUIDE_ICON_MARGIN;
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template, GenomeScreenWithRiskScreenDto dto) throws IOException {
		stream = prevent(stream, template);
		stream = diet(stream, template);
		stream = athletic(stream, template);
		stream = reference(stream, template);
		return stream;
	}
	private PDPageContentStreamPageAccessible prevent(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 20;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
		// Util.icon(stream, resource.imgIcons()[6], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblGuidePreventTitle()));
		y -= HEADER_HEIGHT;
		y -= 10;
		stream.cursorY(y);
		//stream.paragraph(50, y, 497, JUSTIFY, new TextBlock(styleValue, template.lblClinicalMeaningInfo()));
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	PDPageContentStreamPageAccessible diet(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template) throws IOException {
		stream.saveGraphicsState();
		var y = stream.cursorY();
		var resource = template.resource();
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, "▶ " + template.lblGuideDietTitle()));
		y -= HEADER_HEIGHT + 12;
		y = stream.paragraph(50, y, 497, JUSTIFY, new TextBlock(styleValue, template.lblGuideDietInfo()));
		y -= 10;
		stream.setNonStrokingColor(resource.colorSafe()).rect(50, y, 247, HEADER_HEIGHT).fill();
		stream.setNonStrokingColor(resource.colorWarn()).rect(300, y, 247, HEADER_HEIGHT).fill();
		stream.paragraph(50+245/2f, y-HEADER_HEIGHT/2, 245, MIDDLE, new TextBlock(styleHeader, template.lblGuideDietGood()));
		stream.paragraph(300+245/2f, y-HEADER_HEIGHT/2, 245, MIDDLE, new TextBlock(styleHeader, template.lblGuideDietBad()));
		y -= HEADER_HEIGHT;
		PDImageXObject[] good = resource.dietGoodIcons();
		float w = 247f/ good.length;
		for(int i = 0; i < good.length; ++i) Util.icon(stream, good[i], 50+w*i+1, y-1, w-1, w-1);
		PDImageXObject[] bad = resource.dietBadIcons();
		w = 247f/ bad.length;
		for(int i = 0; i < bad.length; ++i) Util.icon(stream, bad[i], 300+w*i+1, y-1, w-1, w-1);
		y -= w+10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible athletic(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template) throws IOException {
		stream.saveGraphicsState();
		var y = stream.cursorY();
		var resource = template.resource();
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).justify(true).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, "▶ " + template.lblGuideAthleticTitle()));
		y -= HEADER_HEIGHT+5;
		stream.setStrokingColor(resource.colorDarkGray()).setLineWidth(0.25f)
			  .line(50, y, 547, y)
			  .line(50, y-ATHLETIC_GUIDE_KINDS_HEIGHT, 547, y-ATHLETIC_GUIDE_KINDS_HEIGHT)
			  .line(50, y-ATHLETIC_GUIDE_KINDS_HEIGHT-HEADER_HEIGHT, 547, y-ATHLETIC_GUIDE_KINDS_HEIGHT-HEADER_HEIGHT)
			  .line(50, y-ATHLETIC_GUIDE_KINDS_HEIGHT-2*HEADER_HEIGHT, 547, y-ATHLETIC_GUIDE_KINDS_HEIGHT-2*HEADER_HEIGHT)
			  .line(50+ATHLETIC_GUIDE_HEADER_WIDTH, y, 50+ATHLETIC_GUIDE_HEADER_WIDTH, y-ATHLETIC_GUIDE_KINDS_HEIGHT-2*HEADER_HEIGHT)
			  .stroke();
		stream.paragraph(50+ATHLETIC_GUIDE_HEADER_WIDTH/2, y-ATHLETIC_GUIDE_KINDS_HEIGHT/2, ATHLETIC_GUIDE_HEADER_WIDTH, CENTER, MIDDLE, new TextBlock(styleValue, "운동종류"));
		stream.paragraph(50+ATHLETIC_GUIDE_HEADER_WIDTH/2, y-ATHLETIC_GUIDE_KINDS_HEIGHT-HEADER_HEIGHT/2, ATHLETIC_GUIDE_HEADER_WIDTH, CENTER, MIDDLE, new TextBlock(styleValue, "운동강도"));
		stream.paragraph(50+ATHLETIC_GUIDE_HEADER_WIDTH/2, y-ATHLETIC_GUIDE_KINDS_HEIGHT-HEADER_HEIGHT-HEADER_HEIGHT/2, ATHLETIC_GUIDE_HEADER_WIDTH, CENTER, MIDDLE, new TextBlock(styleValue, "운동빈도"));
		stream.paragraph(50+ATHLETIC_GUIDE_HEADER_WIDTH+(497-ATHLETIC_GUIDE_HEADER_WIDTH)/2, y-ATHLETIC_GUIDE_KINDS_HEIGHT-HEADER_HEIGHT/2, 497, CENTER, MIDDLE, new TextBlock(styleValue, "유산소운동 수준"));
		stream.paragraph(50+ATHLETIC_GUIDE_HEADER_WIDTH+(497-ATHLETIC_GUIDE_HEADER_WIDTH)/2, y-ATHLETIC_GUIDE_KINDS_HEIGHT-HEADER_HEIGHT-HEADER_HEIGHT/2, 497, CENTER, MIDDLE, new TextBlock(styleValue, "5일 이상/주당, 30~60분/1회"));

		PDImageXObject[] icons = template.resource().athleticIcons();
		float w = (497- ATHLETIC_GUIDE_HEADER_WIDTH) / icons.length;
		float sx = 50+ ATHLETIC_GUIDE_HEADER_WIDTH+(w- ATHLETIC_GUIDE_ICON_SIZE)/2;
		y -=  ATHLETIC_GUIDE_ICON_MARGIN/2-5;
		int i = 0;
		for(PDImageXObject icon: icons) {
			float ih = icon.getHeight();
			float iw = icon.getWidth();
			var scaleHeight = ih *  ATHLETIC_GUIDE_ICON_SIZE / icon.getWidth();
			if(scaleHeight <=  ATHLETIC_GUIDE_ICON_SIZE) {
				float dy = ( ATHLETIC_GUIDE_ICON_SIZE-scaleHeight)/2;
				stream.drawImage(icon, sx, y-dy-scaleHeight,  ATHLETIC_GUIDE_ICON_SIZE, scaleHeight);
			} else {
				var scaleWidth = iw *  ATHLETIC_GUIDE_ICON_SIZE / icon.getHeight();
				float dx =  ( ATHLETIC_GUIDE_ICON_SIZE-scaleWidth)/2;
				stream.drawImage(icon, sx+dx, y- ATHLETIC_GUIDE_ICON_SIZE, scaleWidth,  ATHLETIC_GUIDE_ICON_SIZE);
			}
			stream.paragraph(sx+ ATHLETIC_GUIDE_ICON_SIZE/2, y- ATHLETIC_GUIDE_ICON_SIZE-10,  ATHLETIC_GUIDE_ICON_SIZE, CENTER, MIDDLE, new TextBlock(styleValue, template.lblAthletics()[i++]));
			sx += w;
		}
		y -=  ATHLETIC_GUIDE_KINDS_HEIGHT+2*HEADER_HEIGHT- ATHLETIC_GUIDE_ICON_MARGIN/2+5;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible reference(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 20;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
		// Util.icon(stream, resource.imgIcons()[5], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblGuideReferenceTitle()));
		y -= HEADER_HEIGHT;
		y -= 10;
		stream.cursorY(y);
		int n = template.referencesRiskScreen().length;
		for(int i = 0; i <= template.referencesRiskScreen().length/2; ++i) {
			stream.paragraph(50, y-10*i, 240, new TextBlock(styleValue, (i+1) + "."));
			stream.paragraph(65, y-10*i, 240, template.referencesRiskScreen()[i]);
			if(n/2+i+1 < template.referencesRiskScreen().length) {
				stream.paragraph(298.5f, y-10*i, 240, new TextBlock(styleValue, (n/2+i+2) + "."));
				stream.paragraph(313.5f, y-10*i, 240, template.referencesRiskScreen()[n/2+i+1]);
			}
		}
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
