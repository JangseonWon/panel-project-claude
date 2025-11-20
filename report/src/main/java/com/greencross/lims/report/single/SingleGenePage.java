package com.greencross.lims.report.single;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.single.TestInfo;
import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.builder.LogoType;

import java.awt.*;
import java.text.NumberFormat;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SingleGenePage extends Page<SingleGeneTemplate> {
	protected final Painter<SingleGeneTemplate, SingleGeneDto> barcode = new SectionBarcode<>();
	protected final Painter<SingleGeneTemplate, SingleGeneDto> ldt;
	protected final Painter<SingleGeneTemplate, SingleGeneDto> footer;
	protected final Painter<SingleGeneTemplate, SingleGeneDto> sign;
	protected final Painter<SingleGeneTemplate, SingleGeneDto> page;
	protected Color colorPrimary;
	protected Color colorSecondary;
	protected Color colorPrimaryLine;
	protected Color colorSecondaryLine;
	protected Color colorGray;
	protected TextStyle styleTitle;
	protected TextStyle styleHeaderTitle;
	protected TextStyle styleValueTitle;
	protected TextStyle styleHeader;			// Test Performed, Reason for referral, ...
	protected TextStyle styleHeaderTable;		// 변이 테이블의 헤더
	protected TextStyle styleHeaderInfo;		// Info 페이지의 항목 헤더
	//private TextStyle styleHeader4;
	protected TextStyle styleValue;
	protected TextStyle styleValueTable;
	protected TextStyle styleText;
	protected TextStyle styleTextSub;
	protected TextStyle styleTextSign;
	public SingleGenePage(SingleGeneTemplate template, Painter<SingleGeneTemplate, SingleGeneDto> footer, Painter<SingleGeneTemplate, SingleGeneDto> sign, Painter<SingleGeneTemplate, SingleGeneDto> page) {
		super(template);
		this.footer = footer;
		this.sign = sign;
		this.page = page;

		if(template.logoType() == LogoType.INDEPENDENT) ldt = new SectionLDT<>(template.logoType(), 90);
		else if(template.logoType() == LogoType.DEPENDENT) ldt = new SectionLDT<>(template.logoType(), 120);
		else ldt = new SectionLDT<>(template.logoType(), 90);
	}

	public Painter<SingleGeneTemplate, SingleGeneDto> page() {
		return initialize().and(barcode)
				.and(header())
				.and(sign())
				.and(variant())
				.and(addendum())
				.and(testInfo())
				.and(ldt)
				.and(page);
	}
	public Painter<SingleGeneTemplate, SingleGeneDto> sign() {
		return footer.and(sign);
	}
	protected Painter<SingleGeneTemplate, SingleGeneDto> initialize() {
		return (stream, template, dto) -> {
			SingleGeneResource resource = template.resource();
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
			styleTextSign = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).justify(false).paragraph(false);
			stream.font(template.resource().fontDefault());
			return stream;
		};
	}
	protected Painter<SingleGeneTemplate, SingleGeneDto> header() {
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
	protected Painter<SingleGeneTemplate, SingleGeneDto> variant() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = 651;
			stream.setNonStrokingColor(colorPrimary)
				  .setLineWidth(0.35f).setStrokingColor(colorPrimaryLine)
				  .addRect(60,y,473, 20).fill()
				  .line(60, y-20, 533, y-20).stroke();
			stream.paragraph(63, y+7, 115, new TextBlock(styleHeader, template.lblTestPerformed()));
			stream.paragraph(63, y-13, 500, new TextBlock(styleValue, template.testInfo().name()));

			if(dto.reasonFR()!=null && !dto.reasonFR().trim().isEmpty()) {
				y -= 46;
				stream.addRect(60, y,473, 20).fill()
					  .line(60, y-20, 533, y-20).stroke();
				stream.paragraph(63, y+7, 115, new TextBlock(styleHeader, template.lblReasonForRR()));
				stream.paragraph(63, y-13, 470, new TextBlock(styleValue, dto.reasonFR()));
			}
			y -= 46;
			stream.addRect(60,y,65, 20).fill()
				  .line(60, y+20, 533, y+20)
				  .line(60, y, 533, y)
				  .line(60, y-20, 533, y-20).stroke();
			stream.paragraph(63, y+7, 115, new TextBlock(styleHeader, template.lblResult()));
			stream.paragraph(135, y+7, 115,  new TextBlock(styleValue.clone().color(template.resultToColor(dto.result())), template.resultToString(dto.result())));
			stream.paragraph(63, y-13, 500, new TextBlock(styleValue, dto.report().resultText()));
			y -= 20;
			cursorY(y);
			stream.restoreGraphicsState();
			stream = variantTable(dto.report()).paint(stream, template, dto);
			stream = interpretation(dto.report()).paint(stream, template, dto);
			return stream;
		};
	}
	protected Painter<SingleGeneTemplate, SingleGeneDto> variantTable(SingleGeneDto.Report report) {
		return (stream, template, dto) -> {
			if(report.variants()==null || report.variants().length <= 0) return stream;
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
			y -= 2;
			for(SingleGeneDto.Variant variant: report.variants()) {
				y -= 16;
				stream.paragraph(80, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.gene()));
				stream.paragraph(140, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.dnaChange()));
				stream.paragraph(220, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.predictedAa()));
				stream.paragraph(295, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.zygosity()));
				stream.paragraph(375, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.omim()));
				stream.paragraph(455, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.inherit()));
				stream.paragraph(507, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.clazz()));
			}

			y -= 10;
			stream.setLineWidth(0.5f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			y -= 10;
			y = stream.paragraph(63, y, 500, new TextBlock(styleValueTable, template.lblReference()+": "+report.reference() + "\n"),
							 new TextBlock(styleValueTable, template.lblOMIM()+": "+report.omimDisease() + "\n"),
							 new TextBlock(styleValueTable, template.lblAbbreviation()+": "+report.abbreviation()));
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	protected Painter<SingleGeneTemplate, SingleGeneDto> interpretation(SingleGeneDto.Report report) {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			y -= 30;
			stream.setNonStrokingColor(colorPrimary).addRect(60,y-7,473, 20).fill();
			stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblInterpretation()));
			y -= 20;
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
			stream.setLineWidth(0.35f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			y -= 10;
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	protected Painter<SingleGeneTemplate, SingleGeneDto> addendum() {
		return (stream, template, dto) -> {
			if(dto.addendum() == null) return stream;
			stream = newPage(stream);
			header().and(sign()).paint(stream, template, dto);
			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorPrimary).addRect(60,651,473, 20).fill();
			stream.setLineWidth(0.35f)
				  .setStrokingColor(colorPrimaryLine).line(60, 631, 533, 631).stroke();
			stream.paragraph(63, 658, 115, new TextBlock(styleHeader, template.lblAddendumResult()));
			stream.paragraph(63, 638, 500, new TextBlock(styleValue, dto.addendum().resultText()));
			cursorY(631);
			stream.restoreGraphicsState();
			stream = variantTable(dto.addendum()).paint(stream, template, dto);
			stream = interpretation(dto.addendum()).paint(stream, template, dto);
			stream = newPage(stream);
			header().and(sign()).paint(stream, template, dto);
			return stream;
		};
	}
	protected Painter<SingleGeneTemplate, SingleGeneDto> testInfo() {
		return (stream, template, dto) -> method(template.testInfo())
					   .and(limitation(template.testInfo()))
					   .and(references(template.testInfo()))
					   .paint(stream, template, dto);
	}
	protected Painter<SingleGeneTemplate, SingleGeneDto> method(TestInfo info) {
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
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblMethods()));

			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblMethodList()[0]));
					 stream.paragraph(250, y+7, 300, new TextBlock(styleValue, info.specimen()));
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblMethodList()[1]));
					 stream.paragraph(250, y+7, 300, new TextBlock(styleValue, info.target()));
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblMethodList()[2]));
					 stream.paragraph(250, y+7, 300, new TextBlock(styleValue, info.method()));
			if(info.penetrance() != null && !info.penetrance().trim().isEmpty()) {
				float top = y;
				y -= 20; stream.paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblMethodList()[3]));
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
	protected Painter<SingleGeneTemplate, SingleGeneDto> limitation(TestInfo info) {
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
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblLimitation()));
			y -= 3;
			for(int i=0; i < info.limitations().length; ++i) {
				y -= 13;
				stream.paragraph(60, y, 7, JUSTIFY, new TextBlock(styleTextSub, NumberFormat.getInstance().format(i+1) + "."));
				y = stream.paragraph(69, y, 461, JUSTIFY, new TextBlock(styleTextSub, info.limitations()[i]));
			}
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	protected Painter<SingleGeneTemplate, SingleGeneDto> references(TestInfo info) {
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
	private static String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}
}
