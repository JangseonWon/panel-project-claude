package com.greencross.lims.report.panels;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.builder.LogoType;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class PanelPage extends Page<PanelTemplate> {
	private final Painter<PanelTemplate, PanelDto> barcode = new SectionBarcode<>();
	private final Painter<PanelTemplate, PanelDto> ldt;
	private final Painter<PanelTemplate, PanelDto> footer;
	private final Painter<PanelTemplate, PanelDto> sign;
	private final Painter<PanelTemplate, PanelDto> page;
	private final PanelHeader header;
	private Color colorPrimary;
	private Color colorSecondary;
	private Color colorPrimaryLine;
	private Color colorSecondaryLine;
	private Color colorGray;
	private TextStyle styleHeaderTitle;
	private TextStyle styleHeader;			// Test Performed, Reason for referral, ...
	private TextStyle styleHeaderTable;		// 변이 테이블의 헤더
	private TextStyle styleHeaderInfo;		// Info 페이지의 항목 헤더
	private TextStyle styleValue;
	private TextStyle styleValueTable;
	private TextStyle styleText;
	private TextStyle styleTextSub;
	public PanelPage(PanelTemplate template, PanelHeader header, Painter<PanelTemplate, PanelDto> ldt, Painter<PanelTemplate, PanelDto> footer, Painter<PanelTemplate, PanelDto> sign, Painter<PanelTemplate, PanelDto> page) {
		super(template);
		this.header = header;
		this.ldt = ldt;
		this.footer = footer;
		this.sign = sign;
		this.page = page;
	}

	public Painter<PanelTemplate, PanelDto> page() {
		return initialize().and(barcode)
				.and(header.initialize())
				.and(header.header())
				.and(footer)
				.and(sign)
				.and(variant())
				.and(addendum())
				.and(testInfo())
				.and(ldt)
				.and(page);
	}
	private Painter<PanelTemplate, PanelDto> initialize() {
		return (stream, template, dto) -> {
			PanelResource resource = template.resource();
			colorPrimary = resource.colorPrimary();
			colorSecondary = resource.colorSecondary();
			colorPrimaryLine = resource.colorPrimaryLine();
			colorSecondaryLine = resource.colorSecondaryLine();
			colorGray = resource.colorGray();
			styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
			styleHeader = styleHeaderTitle.clone().color(resource.colorTextWithPrimary()).fontSize(9).justify(false);
			styleHeaderTable = styleHeaderTitle.clone().color(resource.colorTextWithSecondary()).fontSize(8).justify(false);
			styleHeaderInfo = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(9);
			styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).paragraph(false);
			styleValueTable = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(false);
			styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
			styleTextSub = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(false).paragraph(true);
			stream.font(template.resource().fontDefault());
			return stream;
		};
	}
	private Painter<PanelTemplate, PanelDto> variant() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorPrimary)
				  .addRect(60,651,473, 20)
				  .addRect(60,605,473, 20)
				  .addRect(60,558,65, 20)
				  .fill();
			stream.setLineWidth(0.35f)
				  .setStrokingColor(colorPrimaryLine)
				  .line(60, 631, 533, 631)
				  .line(60, 584, 533, 584)
				  .line(60, 578, 533, 578)
				  .line(60, 558, 533, 558)
				  .stroke();
			stream.paragraph(63, 658, 115, new TextBlock(styleHeader, template.lblTestPerformed()));
			stream.paragraph(63, 611.5f, 115, new TextBlock(styleHeader, template.lblReasonForRR()));
			stream.paragraph(63, 565, 115, new TextBlock(styleHeader, template.lblResult()));
			stream.paragraph(63, 638, 470, new TextBlock(styleValue, template.testInfo().panel()));
			stream.paragraph(63, 591, 470, new TextBlock(styleValue, dto.reasonFR()));
			stream.paragraph(135, 565, 115, new TextBlock(styleValue.clone().color(template.resultToColor(dto.result())), template.resultToString(dto.result())));
			float y = stream.paragraph(60, 546, 470, JUSTIFY, new TextBlock(styleValue.clone().justify(true).paragraph(true), dto.report().resultText()));
			y -= 6;
			stream.line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			if(dto.result() == PanelDto.Result.N) stream = variantTableNegative().paint(stream, template, dto);
			else stream = variantTable(dto.report()).paint(stream, template, dto);
			stream = interpretation(dto.report()).paint(stream, template, dto);
			return stream;
		};
	}
	private Painter<PanelTemplate, PanelDto> variantTableNegative() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			y -= 10;
			stream.setNonStrokingColor(colorSecondary)
				  .addRect(60, y-20,473, 20)
				  .fill();
			y -= 10;
			stream.paragraph(80, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[0]));
			stream.paragraph(140, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[1]));
			stream.paragraph(220, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[2]));
			stream.paragraph(295, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[3]));
			stream.paragraph(375, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[4]));
			stream.paragraph(455, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[5]));
			stream.paragraph(507, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[6]));
			y -= 25;
			stream.paragraph(295, y, 500, CENTER, new TextBlock(styleValueTable, template.lblNoDisease()));
			y -= 10;
			stream.setLineWidth(0.5f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			y -= 10;
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<PanelTemplate, PanelDto> variantTable(PanelDto.Report report) {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			if(report.variants()==null || report.variants().length <= 0) return stream;
			float y = cursorY();
			y -= 10;
			stream.setNonStrokingColor(colorSecondary)
				  .addRect(60, y-20,473, 20)
				  .fill();
			y -= 10;
			stream.paragraph(80, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[0]));
			stream.paragraph(140, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[1]));
			stream.paragraph(220, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[2]));
			stream.paragraph(295, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[3]));
			stream.paragraph(375, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[4]));
			stream.paragraph(455, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[5]));
			stream.paragraph(507, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[6]));
			y -= 5;
			for(PanelDto.Variant variant: report.variants()) {
				y -= 16;
				float ny = y;
				ny = Math.min(ny, stream.paragraph(80, y, 115, CENTER, new TextBlock(styleValueTable, variant.gene())));
				ny = Math.min(ny, stream.paragraph(140, y, 90, CENTER, new TextBlock(styleValueTable, variant.dnaChange())));
				ny = Math.min(ny, stream.paragraph(225, y, 90, CENTER, new TextBlock(styleValueTable, variant.predictedAa())));
				ny = Math.min(ny, stream.paragraph(295, y, 115, CENTER, new TextBlock(styleValueTable, variant.zygosity())));
				ny = Math.min(ny, stream.paragraph(375, y, 90, CENTER, new TextBlock(styleValueTable, variant.omim())));
				ny = Math.min(ny, stream.paragraph(455, y, 90, CENTER, new TextBlock(styleValueTable, variant.inherit())));
				ny = Math.min(ny, stream.paragraph(507, y, 115, CENTER, new TextBlock(styleValueTable, variant.clazz())));
				y = ny;
			}
			y -= 10;
			stream.setLineWidth(0.5f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			y -= 10;
			y = stream.paragraph(60, y, 470, new TextBlock(styleValueTable, StringUtils.hasText(report.reference())?(template.lblReference()+": "+report.reference() + "\n"):""),
							 new TextBlock(styleValueTable, StringUtils.hasText(report.omimDisease())?(template.lblOMIM()+": "+report.omimDisease() + "\n"):""),
							 new TextBlock(styleValueTable, StringUtils.hasText(report.abbreviation())?(template.lblAbbreviation()+": "+report.abbreviation()):""));
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<PanelTemplate, PanelDto> interpretation(PanelDto.Report report) {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			y -= 30;
			stream.setNonStrokingColor(colorPrimary).addRect(60,y-7,473, 20).fill();
			stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblInterpretation()));
			y -= 20;
			// y = stream.paragraph(60, y, 473, new TextBlock(styleText, report.interpretation()));
			String[] paragraphs = report.interpretation().split("\n", -1);
			for(String p: paragraphs) {
				TextBlock block = new TextBlock(styleText, p + "\n\n");
				float height = stream.height(473, block);
				if(y - height < 100) {
					stream.restoreGraphicsState();
					stream = newPage(stream);
					header.header().and(footer).and(sign).paint(stream, template, dto);
					y = header.cursorY() - 30;
					stream.setNonStrokingColor(colorPrimary).addRect(60,y-7,473, 20).fill();
					stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblInterpretation()));
					y -= 20;
					stream.saveGraphicsState();
				}
				y = stream.paragraph(60, y, 473, block);
			}
			y -= 10;
			stream.setLineWidth(0.5f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<PanelTemplate, PanelDto> addendum() {
		return (stream, template, dto) -> {
			if(dto.addendum() == null) return stream;
			stream = newPage(stream);
			header.header().and(footer).and(sign).paint(stream, template, dto);
			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorPrimary).addRect(60,651,473, 20).fill();
			stream.paragraph(63, 658, 115, new TextBlock(styleHeader, template.lblAddendumResult()));
			float y = stream.paragraph(60, 638, 470, JUSTIFY, new TextBlock(styleValue.clone().justify(true).paragraph(true), dto.addendum().resultText()));
			y -= 6;
			stream.setLineWidth(0.35f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			stream = variantTable(dto.addendum()).paint(stream, template, dto);
			stream = interpretation(dto.addendum()).paint(stream, template, dto);
			return stream;
		};
	}
	private Painter<PanelTemplate, PanelDto> testInfo() {
		return (stream, template, dto) -> {
			stream = newPage(stream);
			header.header().and(footer).and(sign).paint(stream, template, dto);
			return method(template.testInfo())
						   .and(coverage())
						   .and(limitation(template.testInfo()))
						   .and(genelist(template.testInfo()))
						   .paint(stream, template, dto);
		};
	}
	private Painter<PanelTemplate, PanelDto> method(TestInfo info) {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			String method = !info.panel().contains("NGS") ? info.panel().replace(" Panel", " NGS Panel") : info.panel();
			float y = 651;
			float height = (method.split("\n").length +
					info.region().split("\n").length +
					info.probe().split("\n").length +
					info.sequencing().split("\n").length +
					info.reference().split("\n").length +
					info.pipeline().split("\n").length) * 12 + 48;
			stream.setNonStrokingColor(colorGray).addRect(60, y-height,180, height).fill();	// 회색박스 먼저
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, 658, 500, new TextBlock(styleHeader, template.lblMethods()));
			stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine);
			y -= info(stream, y, template.lblMethodList()[0], method);
			y -= info(stream, y, template.lblMethodList()[1], info.region());
			y -= info(stream, y, template.lblMethodList()[2], info.probe());
			y -= info(stream, y, template.lblMethodList()[3], info.sequencing());
			y -= info(stream, y, template.lblMethodList()[4], info.reference());
			y -= info(stream, y, template.lblMethodList()[5], info.pipeline());
			stream.line(240, y, 240, y+120).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private float info(PDPageContentStreamPageAccessible stream, float y, String label, String value) throws IOException {
		float height = value.split("\n").length * 12 + 8;
		stream.line(60, y-height, 533, y-height).paragraph(63, y-height/2, 180, MIDDLE, new TextBlock(styleHeaderInfo, label));
		stream.paragraph(250, y-height/2, 300, MIDDLE, new TextBlock(styleValue, value));
		return height;
	}
	private Painter<PanelTemplate, PanelDto> coverage() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			y -= 30;
			stream.setNonStrokingColor(colorGray).addRect(60, y-40,180, 40).fill();
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblCoverage()));

			String meanDepth = dto.meanDepth();
			if(meanDepth!=null && !meanDepth.toUpperCase().contains("X") && !meanDepth.toUpperCase().contains("×")) meanDepth += "X";
			String coverage = dto.x10Coverage();
			if(coverage!=null && !coverage.contains("%")) coverage += "%";
			stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine);
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblMeanDepth()));
			stream.paragraph(250, y+7, 300, new TextBlock(styleValue, meanDepth));
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblX10Coverage()));
			stream.paragraph(250, y+7, 300, new TextBlock(styleValue, coverage));
			stream.line(240, y, 240, y+40).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<PanelTemplate, PanelDto> limitation(TestInfo info) {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			y -= 30;
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblLimitation()));
			y -= 13;
			y = stream.paragraph(60, y, 470, JUSTIFY, new TextBlock(styleTextSub, template.lblLimitations()));
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}

	private Painter<PanelTemplate, PanelDto> genelist(TestInfo info) {
		return (stream, template, dto) -> {
			float y = cursorY();
			float footerY = template.logoType().equals(LogoType.INDEPENDENT) ? 80 : 100;
			float minY = footerY + 30;

			String[] genes = info.tier1();
			TextBlock allGenesBlock = new TextBlock(styleTextSub.clone().fontSize(7), String.join(", ", genes));
			float allGenesHeight = stream.height(470, allGenesBlock);

			if (y - 45 - allGenesHeight < minY) {
				stream = newPage(stream);
				header.header().and(footer).and(sign).paint(stream, template, dto);
				y = header.cursorY();
			}

			int idx = 0;
			while (idx < genes.length) {
				StringBuilder sb = new StringBuilder();
				float testY = y - 30 - 15;
				float blockHeight = 0;
				int startIdx = idx;
				for (; idx < genes.length; idx++) {
					if (sb.length() > 0) sb.append(", ");
					sb.append(genes[idx]);
					TextBlock block = new TextBlock(styleTextSub.clone().fontSize(7), sb.toString());
					blockHeight = stream.height(470, block);
					if (testY - blockHeight < minY) {
						if (idx == startIdx) {
							idx++;
						}
						break;
					}
				}

				stream.saveGraphicsState();
				stream.setNonStrokingColor(colorPrimary).addRect(60, y - 30, 473, 20).fill();
				stream.paragraph(63, y - 23, 500, new TextBlock(styleHeader, template.lblGeneList()));
				y -= 45;
				TextBlock block = new TextBlock(styleTextSub.clone().fontSize(7), String.join(", ", java.util.Arrays.copyOfRange(genes, startIdx, idx)));
				y = stream.paragraph(60, y, 470, JUSTIFY, block);
				cursorY(y);
				stream.restoreGraphicsState();

				if (idx < genes.length) {
					stream = newPage(stream);
					header.header().and(footer).and(sign).paint(stream, template, dto);
					y = header.cursorY();
				}
			}
			return stream;
		};
	}
}
