package com.greencross.lims.report.dgs;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.dgs.TestInfo;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.SectionRevision;
import org.springframework.util.StringUtils;

import java.awt.*;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class DgsPage extends Page<DgsTemplate> {
	private final Painter<DgsTemplate, DgsDto> barcode = new SectionBarcode<>();
	private final Painter<DgsTemplate, DgsDto> revision = new SectionRevision<>();
	private final Painter<DgsTemplate, DgsDto> ldt;
	private final Painter<DgsTemplate, DgsDto> footer;
	private final Painter<DgsTemplate, DgsDto> sign;
	private final Painter<DgsTemplate, DgsDto> pn;
	private final DgsHeader header;
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
	public DgsPage(DgsTemplate template, DgsHeader header, Painter<DgsTemplate, DgsDto> ldt, Painter<DgsTemplate, DgsDto> footer, Painter<DgsTemplate, DgsDto> sign, Painter<DgsTemplate, DgsDto> pn) {
		super(template);
		this.header = header;
		this.ldt = ldt;
		this.footer = footer;
		this.sign = sign;
		this.pn = pn;
	}

	public Painter<DgsTemplate, DgsDto> page() {
		return initialize().and(barcode).and(revision)
				.and(header.initialize())
				.and(header.header())
				.and(footer)
				.and(sign)
				.and(variant())
				.and(recommendation())
				.and(incidentalFindings())
				.and(testInfo())
				.and(ldt)
				.and(pn);
	}
	private Painter<DgsTemplate, DgsDto> initialize() {
		return (stream, template, dto) -> {
			DgsResource resource = template.resource();
			colorPrimary = resource.colorPrimary();
			colorSecondary = resource.colorSecondary();
			colorPrimaryLine = resource.colorPrimaryLine();
			colorSecondaryLine = resource.colorSecondaryLine();
			colorGray = resource.colorGray();
			styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText2(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
			styleHeader = styleHeaderTitle.clone().color(resource.colorTextWithPrimary()).fontSize(9).justify(false);
			styleHeaderTable = styleHeaderTitle.clone().color(resource.colorTextWithSecondary()).fontSize(8).justify(false);
			styleHeaderInfo = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(9);
			styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText1(), resource.fontText2(), resource.fontDefault()).fontSize(9).paragraph(false);
			styleValueTable = new TextStyle().color(resource.colorText()).fonts(resource.fontText1(), resource.fontText2(), resource.fontDefault()).fontSize(8).paragraph(false);
			styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText1(), resource.fontText2(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
			styleTextSub = new TextStyle().color(resource.colorText()).fonts(resource.fontText1(), resource.fontText2(), resource.fontDefault()).fontSize(8).justify(false).paragraph(true);
			stream.font(template.resource().fontDefault());
			return stream;
		};
	}
	private Painter<DgsTemplate, DgsDto> variant() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorPrimary)
				  .addRect(60,651,473, 20)
				  .addRect(60,605,473, 20)
				  .fill();
			stream.setLineWidth(0.35f)
				  .setStrokingColor(colorPrimaryLine)
				  .line(60, 631, 533, 631).stroke();
			stream.paragraph(63, 658, 115, new TextBlock(styleHeader, template.lblTestPerformed()));
			stream.paragraph(63, 638, 500, new TextBlock(styleValue, template.testInfo().panel()));
			stream.paragraph(63, 611.5f, 115, new TextBlock(styleHeader, template.lblReasonForRR()));
			float y = stream.paragraph(63, 591, 470, new TextBlock(styleValue, dto.reasonFR()));
			y -= 7;

			stream.addRect(60,y-26,65, 20).fill();
			stream.line(60, y, 533, y)
				  .line(60, y-6, 533, y-6)
				  .line(60, y-26, 533, y-26)
				  .stroke();
			y -= 19;
			stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblResult()));
			stream.paragraph(135, y, 115, new TextBlock(styleValue.clone().color(template.resultToColor(dto.result())), template.resultToString(dto.result())));
			y -= 19;
			y = stream.paragraph(60, y, 470, JUSTIFY, new TextBlock(styleValue.clone().justify(true).paragraph(true), dto.report().resultText()));
			y -= 6;
			stream.line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			if(dto.result() == DgsDto.Result.N) stream = variantTableNegative().paint(stream, template, dto);
			else stream = variantTable(dto.report()).paint(stream, template, dto);
			stream = interpretation(dto.report()).paint(stream, template, dto);
			return stream;
		};
	}
	protected Painter<DgsTemplate, DgsDto> variantTableNegative() {
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
	private Painter<DgsTemplate, DgsDto> variantTable(DgsDto.Report report) {
		return (stream, template, dto) -> {
			if(report.variants()==null || report.variants().length <= 0)  return stream;
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
			for(DgsDto.Variant variant: report.variants()) {
				y -= 22;
				stream.paragraph(80, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.gene()));
				stream.paragraph(140, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.dnaChange()));
				stream.paragraph(220, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.predictedAa()));
				stream.paragraph(295, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.zygosity()));
				stream.paragraph(375, y, 120, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.omim()));
				stream.paragraph(455, y, 90, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.inherit()));
				stream.paragraph(507, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.clazz()));
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
	private Painter<DgsTemplate, DgsDto> interpretation(DgsDto.Report report) {
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
			y -= 6;
			stream.setLineWidth(0.35f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<DgsTemplate, DgsDto> recommendation() {
		return (stream, template, dto) -> {
			if(dto.result() == DgsDto.Result.N) return stream;
			float y = cursorY();
			TextBlock recommendation = new TextBlock(styleValue.clone().justify(true).paragraph(true), dto.recommendation());
			float height = 100 + stream.height(470, recommendation);
			if(y - height < 100)  {
				stream = newPage(stream);
				header.header().and(footer).and(sign).paint(stream, template, dto);
				y = header.cursorY();
			}
			y -= 30;
			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorPrimary).addRect(60,y-7,473, 20).fill();
			stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblRecommendation()));
			y = stream.paragraph(60, y-20, 470, JUSTIFY, recommendation);
			y -= 6;
			stream.setLineWidth(0.35f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}

	private Painter<DgsTemplate, DgsDto> incidentalFindings() {
		return (stream, template, dto) -> {
			if(!dto.consentIncidentalFindings()) return stream;
			float y = cursorY();
			TextBlock interpretation = new TextBlock(styleValue.clone().justify(true).paragraph(true), (dto.incidentalFindings()!=null?
																									   dto.incidentalFindings().interpretation():
																									   template.templateEmptyIncidentalFinding().replace("%d", String.valueOf(TestWithSingleInfo.GENE_INCIDENTAL_FINDINGS.length))) +
																									"\r\n" + template.lblIncidentalFindingInfo());
			float height = 100 + stream.height(470, interpretation);
			if(y - height < 100)  {
				stream = newPage(stream);
				header.header().and(footer).and(sign).paint(stream, template, dto);
				y = header.cursorY();
			}
			y -= 30;
			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorPrimary).addRect(60,y-7,473, 20).fill();
			stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblIncidentalFindingResult()));
			// stream.paragraph(530, y, 355, RIGHT, new TextBlock(styleHeader.clone().fontSize(7).color(new Color(127, 127, 127)), template.lblIncidentalFindingInfo()));
			if(dto.incidentalFindings()!=null && dto.incidentalFindings().variants()!=null && dto.incidentalFindings().variants().length>0) {
				cursorY(y+1);
				stream = variantTable(dto.incidentalFindings()).paint(stream, template, dto);
				y = cursorY();
			}
			if(dto.incidentalFindings().interpretation()!=null && !dto.incidentalFindings().interpretation().trim().isEmpty()) {
				y = stream.paragraph(60, y-20, 470, JUSTIFY, interpretation);
				y -= 6;
				stream.setLineWidth(0.35f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			}
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<DgsTemplate, DgsDto> testInfo() {
		return (stream, template, dto) -> method(template.testInfo())
					   .and(analysisStatistic())
					   .and(limitation(template.testInfo()))
					   .paint(stream, template, dto);
	}
	private Painter<DgsTemplate, DgsDto> method(TestInfo info) {
		return (stream, template, dto) -> {
			float y = cursorY();
			float contentsHeight = 0;
			for(String method: info.methods()) contentsHeight += stream.height(470, new TextBlock(styleTextSub, method));
			float height = 50 + contentsHeight;
			if(y - height < 100)  {
				stream = newPage(stream);
				header.header().and(footer).and(sign).paint(stream, template, dto);
				y = header.cursorY();
			}
			y -= 30;

			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblMethods()));
			for(String method: info.methods()) y = stream.paragraph(60, y-13, 470, JUSTIFY, new TextBlock(styleTextSub, method));
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<DgsTemplate, DgsDto> analysisStatistic() {
		return (stream, template, dto) -> {
			float y = cursorY();
			float height = 80;
			if(y - height < 100)  {
				stream = newPage(stream);
				header.header().and(footer).and(sign).paint(stream, template, dto);
				y = header.cursorY();
			}
			y -= 30;
			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorGray).addRect(60, y-40,180, 40).fill();
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblAnalysisStatistic()));

			String meanDepth = dto.meanDepth();
			if(meanDepth!=null && !meanDepth.toUpperCase().contains("X") && !meanDepth.toUpperCase().contains("×")) meanDepth += "X";
			String coverage = dto.x10Coverage();
			if(coverage!=null && !coverage.contains("%")) coverage += "%";
			stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine);
			stream.line(60, y, 533, y);
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
	private Painter<DgsTemplate, DgsDto> limitation(TestInfo info) {
		return (stream, template, dto) -> {
			float y = cursorY();
			TextBlock content = new TextBlock(styleTextSub, info.limitation());
			float height = 50 + stream.height(470, content);
			if(y - height < 100)  {
				stream = newPage(stream);
				header.header().and(footer).and(sign).paint(stream, template, dto);
				y = header.cursorY();
			}
			y -= 30;
			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblLimitation()));
			y -= 13;
			y = stream.paragraph(60, y, 470, JUSTIFY, content);
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
}
