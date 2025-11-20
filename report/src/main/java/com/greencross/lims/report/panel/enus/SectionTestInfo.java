package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.Util;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionTestInfo implements Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> {
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();

		y-= 25;
		var resource = template.resource();
		var styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(13).paragraph(false);
		stream.paragraph(25, y, 400, new TextBlock(styleTitle, template.lblTest()));
		stream = method(stream.cursorY(y), template);
		stream = coverage(stream, template, dto);
		stream = genes(stream, template, dto);
		stream = limitation(stream, template);
		stream = reference(stream, template, dto);
		float ty2 = stream.cursorY() - 5;
		Util.gradient(stream, s->s.rect(25, ty2, 545, 0.2f), resource.gradient(), 25, 545);
		stream.restoreGraphicsState();
		return stream.cursorY(ty2);
	}

	final static String bullet = "■";
	private PDPageContentStreamPageAccessible method(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();
		var resource = template.resource();
		var test = template.testInfo();
		y-= 10;
		var styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(12).paragraph(false);
		float ty = y;
		Util.gradient(stream, s->s.rect(25, ty, 545, 20), resource.gradient(), 25, 545);
		stream.paragraph(35, y-10, 400, MIDDLE, new TextBlock(styleHeader, template.lblMethod()));
		y-= 40;
		var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(false);
		var styleBullet = styleText.clone().color(resource.colorPrimary()).fontSize(4);
		stream.paragraph(45, y+2, 520, new TextBlock(styleBullet, bullet));
		stream.paragraph(53, y, 520, new TextBlock(styleText, template.lblTarget() + " : " + test.region()));
		y -= 15;
		stream.paragraph(45, y+2, 520, new TextBlock(styleBullet, bullet));
		stream.paragraph(53, y, 520, new TextBlock(styleText, template.lblProbe() + " : " + test.probe()));
		y -= 15;
		stream.paragraph(45, y+2, 520, new TextBlock(styleBullet, bullet));
		stream.paragraph(53, y, 520, new TextBlock(styleText, template.lblSequencing() + " : " + test.sequencing()));
		y -= 15;
		stream.paragraph(45, y+2, 520, new TextBlock(styleBullet, bullet));
		stream.paragraph(53, y, 520, new TextBlock(styleText, template.lblReferenceGenome() + " : " + test.reference()));
		y -= 15;
		stream.paragraph(45, y+2, 520, new TextBlock(styleBullet, bullet));
		stream.paragraph(53, y, 520, new TextBlock(styleText, template.lblPipeline() + " : " + test.pipeline()));
		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}

	private PDPageContentStreamPageAccessible coverage(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();
		var resource = template.resource();
		y-= 10;
		var styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(12).paragraph(false);
		float ty = y;
		Util.gradient(stream, s->s.rect(25, ty, 545, 20), resource.gradient(), 25, 545);
		stream.paragraph(35, y-10, 400, MIDDLE, new TextBlock(styleHeader, template.lblCoverage()));
		y-= 40;
		var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(false);
		var styleBullet = styleText.clone().color(resource.colorPrimary()).fontSize(4);
		stream.paragraph(45, y+2, 520, new TextBlock(styleBullet, bullet));
		stream.paragraph(53, y, 520, new TextBlock(styleText, template.lblMeanDepth() + " : " + dto.meanDepth()));
		y -= 15;
		stream.paragraph(45, y+2, 520, new TextBlock(styleBullet, bullet));
		stream.paragraph(53, y, 520, new TextBlock(styleText, template.lblTargetCoverage() + " : " + dto.x10Coverage()));
		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}

	PDPageContentStreamPageAccessible genes(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();
		var resource = template.resource();
		y-= 10;
		var styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(12).paragraph(false);
		float ty = y;
		Util.gradient(stream, s->s.rect(25, ty, 545, 20), resource.gradient(), 25, 545);
		stream.paragraph(35, y-10, 400, MIDDLE, new TextBlock(styleHeader, template.lblGeneList()));
		y-= 40;
		var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(false);
		String genes = Arrays.stream(template.testInfo().genes()).distinct().sorted().collect(Collectors.joining(", "));
		y = stream.paragraph(45, y, 520, new TextBlock(styleText, genes));
		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible limitation(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();
		var resource = template.resource();
		y-= 10;
		var styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(12).paragraph(false);
		float ty = y;
		Util.gradient(stream, s->s.rect(25, ty, 545, 20), resource.gradient(), 25, 545);
		stream.paragraph(35, y-10, 400, MIDDLE, new TextBlock(styleHeader, template.lblLimitation()));
		y-= 40;
		var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(false);
		y = stream.paragraph(45, y, 520, new TextBlock(styleText, template.lblLimitations()));
		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible reference(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
		stream.saveGraphicsState();
		float y = stream.cursorY();
		var resource = template.resource();
		y-= 10;
		var styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(12).paragraph(false);
		float ty = y;
		Util.gradient(stream, s->s.rect(25, ty, 545, 20), resource.gradient(), 25, 545);
		stream.paragraph(35, y-10, 400, MIDDLE, new TextBlock(styleHeader, template.lblReference()));
		y-= 40;
		var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(false);
		for(int i = 0; i < template.references().length; ++i) {
			stream.paragraph(45, y, 520, new TextBlock(styleText, (i+1) + ". " + template.references()[i]));
			y -= 15;
		}
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
