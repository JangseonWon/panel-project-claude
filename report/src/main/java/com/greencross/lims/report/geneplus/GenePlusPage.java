package com.greencross.lims.report.geneplus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.geneplus.TestInfo;
import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.builder.LogoType;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.text.NumberFormat;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class GenePlusPage extends Page<GenePlusTemplate> {
	private final Painter<GenePlusTemplate, GenePlusDto> barcode = new SectionBarcode<>();
	private final Painter<GenePlusTemplate, GenePlusDto> ldt;
	private final Painter<GenePlusTemplate, GenePlusDto> footer;
	private final Painter<GenePlusTemplate, GenePlusDto> sign;
	private final Painter<GenePlusTemplate, GenePlusDto> page;
	private Color colorPrimary;
	private Color colorSecondary;
	private Color colorPrimaryLine;
	private Color colorSecondaryLine;
	private Color colorGray;
	private TextStyle styleTitle;
	private TextStyle styleHeaderTitle;
	private TextStyle styleValueTitle;
	private TextStyle styleHeader;			// Test Performed, Reason for referral, ...
	private TextStyle styleHeaderTable;		// 변이 테이블의 헤더
	private TextStyle styleHeaderInfo;		// Info 페이지의 항목 헤더
	//private TextStyle styleHeader4;
	private TextStyle styleValue;
	private TextStyle styleValueTable;
	private TextStyle styleText;
	private TextStyle styleTextSub;
	public GenePlusPage(GenePlusTemplate template, Painter<GenePlusTemplate, GenePlusDto> footer, Painter<GenePlusTemplate, GenePlusDto> sign, Painter<GenePlusTemplate, GenePlusDto> page) {
		super(template);
		this.footer = footer;
		this.sign = sign;
		this.page = page;

		if(template.logoType() == LogoType.INDEPENDENT) ldt = new SectionLDT<>(template.logoType(), 90);
		else if(template.logoType() == LogoType.DEPENDENT) ldt = new SectionLDT<>(template.logoType(), 120);
		else ldt = new SectionLDT<>(template.logoType(), 90);
	}

	public Painter<GenePlusTemplate, GenePlusDto> page() {
		return initialize().and(barcode)
				.and(header())
				.and(sign())
				.and(variant())
				.and(addendum())
				.and(testInfo())
				.and(ldt)
				.and(page);
	}
	private Painter<GenePlusTemplate, GenePlusDto> initialize() {
		return (stream, template, dto) -> {
			GenePlusResource resource = template.resource();
			colorPrimary = resource.colorPrimary();
			colorSecondary = resource.colorSecondary();
			colorPrimaryLine = resource.colorPrimaryLine();
			colorSecondaryLine = resource.colorSecondaryLine();
			colorGray = resource.colorGray();
			styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(17).paragraph(false);
			styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
			styleValueTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).paragraph(false);
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
	private Painter<GenePlusTemplate, GenePlusDto> header() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			stream.drawImage(template.resource().medal(), 47, 770,23,38);
			stream.paragraph(80, 773, 500, new TextBlock(styleTitle, template.testInfo().title()));
			stream.setNonStrokingColor(colorGray)
				  .setLineWidth(0.25f).setStrokingColor(colorGray)
				  .addRect(60,677.5f,65, 63)
				  .addRect(211,677.5f,75, 63)
				  .addRect(374,677.5f,75, 63)
				  .fill();

			float y = 731;
			stream.line(60, y-5, 533, y-5).stroke();
			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblMedicalInstitution()));
			stream.paragraph(214, y, 69, new TextBlock(styleHeaderTitle, template.lblMedicalRecordNumber()));
			stream.paragraph(377, y, 69, new TextBlock(styleHeaderTitle, template.lblRequestNumber()));
			stream.paragraph(130, y+2.5f, 83, MIDDLE, new TextBlock(styleValueTitle, dashIfEmpty(dto.medicalInstitution())));
			stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, dashIfEmpty(dto.medicalRecordNumber())));
			y = stream.paragraph(452, y, 100, new TextBlock(styleValueTitle, dashIfEmpty(dto.requestNumber())));
			y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblPatientName()));
			stream.paragraph(214, y, 69, new TextBlock(styleHeaderTitle, template.lblPatientCode()));
			stream.paragraph(377, y, 69, new TextBlock(styleHeaderTitle, template.lblAgeSex()));
			stream.paragraph(130, y, 86, new TextBlock(styleValueTitle, dashIfEmpty(dto.patientName())));
			stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, dashIfEmpty(dto.patientCode())));
			y = stream.paragraph(452, y, 100,
								 new TextBlock(styleValueTitle, dashIfEmpty(template.age(dto.age(), dto.birthDate(), dto.collectionDate()))),
								 new TextBlock(styleValueTitle, " / "),
								 new TextBlock(styleValueTitle, dashIfEmpty(template.sex(dto.sex()))));
			stream.line(60, y-5, 533, y-5);
			y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblSpecimenType()));
			stream.paragraph(214, y, 69, new TextBlock(styleHeaderTitle, template.lblWardDepartment()));
			stream.paragraph(377, y, 69, new TextBlock(styleHeaderTitle, template.lblCollectionDate()));
			stream.paragraph(130, y, 86, new TextBlock(styleValueTitle, dashIfEmpty(dto.specimenType())));
			stream.paragraph(290, y, 88,
							 new TextBlock(styleValueTitle, dashIfEmpty(dto.ward())),
							 new TextBlock(styleValueTitle, " / "),
							 new TextBlock(styleValueTitle, dashIfEmpty(dto.department())));
			y = stream.paragraph(452, y, 100, new TextBlock(styleValueTitle, dashIfEmpty(template.date(dto.collectionDate()))));
			stream.line(60, y-5, 533, y-5);
			y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblPatientInfo()));
			stream.paragraph(214, y, 69, new TextBlock(styleHeaderTitle, template.lblPhysician()));
			stream.paragraph(377, y, 69, new TextBlock(styleHeaderTitle, template.lblReceiptReportDate()));
			stream.paragraph(130, y, 86, new TextBlock(styleValueTitle, dashIfEmpty(dto.patientInfo())));
			stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, dashIfEmpty(dto.physician())));
			y = stream.paragraph(452, y, 100,
								 new TextBlock(styleValueTitle, dashIfEmpty(template.date(dto.receiptDate()))),
								 new TextBlock(styleValueTitle, " / "),
								 new TextBlock(styleValueTitle, dashIfEmpty(template.date(dto.reportDate()))));
			stream.line(60, y-5, 533, y-5).stroke();
			stream.restoreGraphicsState();
			cursorY(y);
			return stream;
		};
	}
	private Painter<GenePlusTemplate, GenePlusDto> variant() {
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
			stream.paragraph(63, 638, 470, new TextBlock(styleValue, template.testInfo().name()));
			stream.paragraph(63, 591, 470, new TextBlock(styleValue, dto.reasonFR()));
			stream.paragraph(135, 565, 115, new TextBlock(styleValue.clone().color(template.resultToColor(dto.result())), template.resultToString(dto.result())));
			float y = stream.paragraph(60, 546, 470, JUSTIFY, new TextBlock(styleValue.clone().justify(true).paragraph(true), dto.report().resultText()));
			y -= 6;
			stream.line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			if(dto.result() == GenePlusDto.Result.N) stream = variantTableNegative().paint(stream, template, dto);
			else stream = variantTable(dto.report()).paint(stream, template, dto);
			stream = interpretation(dto.report()).paint(stream, template, dto);
			return stream;
		};
	}
	private Painter<GenePlusTemplate, GenePlusDto> variantTableNegative() {
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
	private Painter<GenePlusTemplate, GenePlusDto> variantTable(GenePlusDto.Report report) {
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
			for(GenePlusDto.Variant variant: report.variants()) {
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
	private Painter<GenePlusTemplate, GenePlusDto> interpretation(GenePlusDto.Report report) {
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
					header().and(sign()).paint(stream, template, dto);
					y = cursorY() - 30;
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
	private Painter<GenePlusTemplate, GenePlusDto> addendum() {
		return (stream, template, dto) -> {
			if(dto.addendum() == null) return stream;
			stream = newPage(stream);
			header().and(sign()).paint(stream, template, dto);
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
	private Painter<GenePlusTemplate, GenePlusDto> testInfo() {
		return (stream, template, dto) -> testInformation(template.testInfo())
					   .and(limitation(template.testInfo()))
					   .and(references(template.testInfo()))
					   .paint(stream, template, dto);
	}
	private Painter<GenePlusTemplate, GenePlusDto> testInformation(TestInfo info) {
		return (stream, template, dto) -> {
			float y = cursorY();
			float height = 70;
			if(y - height < 130) {
				stream = newPage(stream);
				header().and(sign()).paint(stream, template, dto);
				y = cursorY();
			}
			y -= 40;
			stream.saveGraphicsState();
			float heightPenetrance = 0;
			if(info.penetrance() != null && !info.penetrance().trim().isEmpty()) heightPenetrance = 8 + stream.height(283,  new TextBlock(styleValue, info.penetrance()));
			stream.setNonStrokingColor(colorGray).addRect(60, y-60-heightPenetrance,180, 60+heightPenetrance).fill();	// 회색박스 먼저
			stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine)
				  .line(240, y-60, 240, y).stroke();

			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblTestInformation()));

			stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine);
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblTestInformationList()[0]));
					 stream.paragraph(250, y+7, 300, new TextBlock(styleValue, info.specimen()));
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblTestInformationList()[1]));
					 stream.paragraph(250, y+7, 300, new TextBlock(styleValue, info.target()));
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblTestInformationList()[2]));
					 stream.paragraph(250, y+7, 300, new TextBlock(styleValue, info.method()));
			if(info.penetrance() != null && !info.penetrance().trim().isEmpty()) {
				float top = y;
				y -= 20; stream.paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblTestInformationList()[3]));
				y = stream.paragraph(250, y+7, 283, new TextBlock(styleValue, info.penetrance()));
				y -= 7;
				stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine)
					  .line(60, y, 533, y)
					  .line(240, y, 240, top).stroke();
			}
			stream.stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<GenePlusTemplate, GenePlusDto> limitation(TestInfo info) {
		return (stream, template, dto) -> {
			float y = cursorY();
			float height = 40;
			for(int i=0; i < info.limitations().length; ++i) height += stream.height(461, new TextBlock(styleTextSub, info.limitations()[i]));
			if(y - height < 130) {
				stream = newPage(stream);
				header().and(sign()).paint(stream, template, dto);
				y = cursorY();
			}
			y -= 30;
			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblRemark()));
			for(int i = 0; i < info.limitations().length; ++i) {
				y -= 13;
				stream.paragraph(60, y, 7, JUSTIFY, new TextBlock(styleTextSub, NumberFormat.getInstance().format(i+1) + "."));
				y = stream.paragraph(69, y, 461, JUSTIFY, new TextBlock(styleTextSub, info.limitations()[i]));
			}
			y -= 10;
			stream.setLineWidth(0.5f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<GenePlusTemplate, GenePlusDto> references(TestInfo info) {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			float height = 70;
			if(y - height < 130) {
				stream = newPage(stream);
				header().and(sign()).paint(stream, template, dto);
				y = cursorY();
			}
			y -= 30;
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblReferences()));
			y -= 3;
			for(int i=0; i < info.references().length; ++i) {
				y -= 13;
				stream.paragraph(60, y, 7, JUSTIFY, new TextBlock(styleTextSub, NumberFormat.getInstance().format(i+1) + "."));
				y = stream.paragraph(69, y, 461, JUSTIFY, new TextBlock(styleTextSub, info.references()[i]));
			}
			y -= 8;
			stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine).line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<GenePlusTemplate, GenePlusDto> sign() {
		return footer.and(sign);
	}
	private static String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}
}
