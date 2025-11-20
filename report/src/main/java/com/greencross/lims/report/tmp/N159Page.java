package com.greencross.lims.report.tmp;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.AlignHorizontal;
import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.SectionBarcode;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class N159Page extends Page<N159Template> {
	private final Painter<N159Template, N159Dto> barcode = new SectionBarcode<>();
	private final Painter<N159Template, N159Dto> ldt;
	private final Painter<N159Template, N159Dto> footer;
	private final Painter<N159Template, N159Dto> sign;
	private final Painter<N159Template, N159Dto> pn;
	private final NumberFormat fmtNumber = NumberFormat.getInstance();
	private final NumberFormat fmtPrecision = new DecimalFormat("0.0");
	private Color colorPrimary;
	private Color colorSecondary;
	private Color colorPrimaryLine;
	private Color colorGray;
	private TextStyle styleTitle;
	private TextStyle styleHeaderTitle;
	private TextStyle styleValueTitle;
	private TextStyle styleHeader;			// Test Performed, Reason for referral, ...
	private TextStyle styleHeaderTable;		// 변이 테이블의 헤더
	private TextStyle styleValue;
	private TextStyle styleValueTable;
	private TextStyle styleText;
	private TextStyle styleTextSub;
	public N159Page(N159Template template, Painter<N159Template, N159Dto> ldt, Painter<N159Template, N159Dto> footer, Painter<N159Template, N159Dto> sign, Painter<N159Template, N159Dto> pn) {
		super(template);
		fmtPrecision.setMaximumFractionDigits(1);
		this.ldt = ldt;
		this.footer = footer;
		this.sign = sign;
		this.pn = pn;
	}

	public Painter<N159Template, N159Dto> page() {
		return initialize().and(barcode)
				.and(header())
				.and(footer).and(sign)
				.and(variant())
				.and(limitation())
				.and(ldt)
				.and(pn);
	}
	private Painter<N159Template, N159Dto> initialize() {
		return (stream, template, dto) -> {
			N159Resource resource = template.resource();
			colorPrimary = resource.colorPrimary();
			colorSecondary = resource.colorSecondary();
			colorPrimaryLine = resource.colorPrimaryLine();
			colorGray = resource.colorGray();
			styleTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(20).paragraph(false);
			styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
			styleValueTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).paragraph(false);
			styleHeader = styleHeaderTitle.clone().color(resource.colorTextWithPrimary()).fontSize(9).justify(false);
			styleHeaderTable = styleHeaderTitle.clone().color(resource.colorTextWithSecondary()).fontSize(8).justify(false);
			styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).paragraph(false);
			styleValueTable = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(false);
			styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
			styleTextSub = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(false).paragraph(true);
			stream.font(template.resource().fontDefault());
			return stream;
		};
	}
	private Painter<N159Template, N159Dto> header() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			stream.paragraph(297.5f, 773, 500, CENTER, new TextBlock(styleTitle, template.testInfo().title()));
			stream.setNonStrokingColor(colorGray)
				  .setLineWidth(0.25f).setStrokingColor(colorGray)
				  .addRect(60,677.5f,65, 63)
				  .addRect(221,677.5f,65, 63)
				  .addRect(374,677.5f,65, 63)
				  .fill();

			float y = 731;
			stream.line(60, y-5, 533, y-5).stroke();
			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblMedicalInstitution()));
			stream.paragraph(224, y, 60, new TextBlock(styleHeaderTitle, template.lblMedicalRecordNumber()));
			stream.paragraph(377, y, 60, new TextBlock(styleHeaderTitle, template.lblRequestNumber()));
			stream.paragraph(130, y+2.5f, 83, MIDDLE, new TextBlock(styleValueTitle, dashIfEmpty(dto.medicalInstitution())));
			stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, dashIfEmpty(dto.medicalRecordNumber())));
			y = stream.paragraph(442, y, 100, new TextBlock(styleValueTitle, dashIfEmpty(dto.requestNumber())));
			y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblPatientName()));
			stream.paragraph(224, y, 60, new TextBlock(styleHeaderTitle, template.lblPatientCode()));
			stream.paragraph(377, y, 60, new TextBlock(styleHeaderTitle, template.lblAgeSex()));
			stream.paragraph(130, y, 86, new TextBlock(styleValueTitle, dashIfEmpty(dto.patientName())));
			stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, dashIfEmpty(dto.patientCode())));
			y = stream.paragraph(442, y, 100,
								 new TextBlock(styleValueTitle, dashIfEmpty(template.age(dto.age(), dto.birthDate(), dto.collectionDate()))),
								 new TextBlock(styleValueTitle, " / "),
								 new TextBlock(styleValueTitle, dashIfEmpty(template.sex(dto.sex()))));
			stream.line(60, y-5, 533, y-5);
			y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblSpecimenType()));
			stream.paragraph(224, y, 60, new TextBlock(styleHeaderTitle, template.lblWardDepartment()));
			stream.paragraph(377, y, 60, new TextBlock(styleHeaderTitle, template.lblCollectionDate()));
			stream.paragraph(130, y, 86, new TextBlock(styleValueTitle, dashIfEmpty(dto.specimenType())));
			stream.paragraph(290, y, 88,
							 new TextBlock(styleValueTitle, dashIfEmpty(dto.ward())),
							 new TextBlock(styleValueTitle, " / "),
							 new TextBlock(styleValueTitle, dashIfEmpty(dto.department())));
			y = stream.paragraph(442, y, 100, new TextBlock(styleValueTitle, dashIfEmpty(template.date(dto.collectionDate()))));
			stream.line(60, y-5, 533, y-5);
			y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblPatientInfo()));
			stream.paragraph(224, y, 60, new TextBlock(styleHeaderTitle, template.lblPhysician()));
			stream.paragraph(377, y, 60, new TextBlock(styleHeaderTitle, template.lblReceiptReportDate()));
			stream.paragraph(130, y, 86, new TextBlock(styleValueTitle, dashIfEmpty(dto.patientInfo())));
			stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, dashIfEmpty(dto.physician())));
			y = stream.paragraph(442, y, 100,
								 new TextBlock(styleValueTitle, dashIfEmpty(template.date(dto.receiptDate()))),
								 new TextBlock(styleValueTitle, " / "),
								 new TextBlock(styleValueTitle, dashIfEmpty(template.date(dto.reportDate()))));
			stream.line(60, y-5, 533, y-5).stroke();
			stream.restoreGraphicsState();
			cursorY(y);
			return stream;
		};
	}
	private Painter<N159Template, N159Dto> variant() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorPrimary)
				  .addRect(60,651,473, 20)
				  .addRect(60,605,65, 20)
				  .fill();
			stream.setLineWidth(0.35f)
				  .setStrokingColor(colorPrimaryLine)
				  .line(60, 631, 533, 631)
				  .line(60, 625, 533, 625)
				  .line(60, 605, 533, 605)
				  .stroke();
			stream.paragraph(63, 658, 115, new TextBlock(styleHeader, template.lblTestPerformed()));
			stream.paragraph(63, 611.5f, 115, new TextBlock(styleHeader, template.lblResult()));
			stream.paragraph(63, 638, 470, new TextBlock(styleValue, template.testInfo().panel()));
			// stream.paragraph(63, 591, 470, new TextBlock(styleValue, dto.reasonFR()));
			stream.paragraph(135, 611.5f, 115, new TextBlock(styleValue.clone().color(template.resultToColor(dto.result())), template.resultToString(dto.result())));
			float y = stream.paragraph(60, 591, 470, JUSTIFY, new TextBlock(styleValue.clone().justify(true).paragraph(true), dto.report().resultText()));
			y -= 6;
			stream.line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			if(dto.result() == N159Dto.Result.N) stream = variantTableNegative().paint(stream, template, dto);
			else stream = variantTable(dto.report()).paint(stream, template, dto);
			stream = interpretation(dto.report()).paint(stream, template, dto);
			return stream;
		};
	}
	private Painter<N159Template, N159Dto> variantTableNegative() {
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
	private Painter<N159Template, N159Dto> variantTable(N159Dto.Report report) {
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
			y -= 2;
			for(N159Dto.Variant variant: report.variants()) {
				y -= 16;
				stream.paragraph(80, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.gene()));
				stream.paragraph(140, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.dnaChange()));
				stream.paragraph(220, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.predictedAa()));
				stream.paragraph(295, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.vaf()!=null?fmtPrecision.format(variant.vaf()):"-"));
				stream.paragraph(375, y, 90, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.depth()!=null?fmtNumber.format(variant.depth()):"-"));
				stream.paragraph(455, y, 90, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.cosmic()));
				stream.paragraph(507, y, 115, CENTER, MIDDLE, new TextBlock(styleValueTable, variant.tier()));
			}
			y -= 10;
			stream.setLineWidth(0.5f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			y -= 10;
			y = stream.paragraph(60, y, 470, new TextBlock(styleValueTable, StringUtils.hasText(report.reference())?(template.lblReference()+": "+report.reference() + "\n"):""));
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<N159Template, N159Dto> interpretation(N159Dto.Report report) {
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
					header().and(footer).and(sign).paint(stream, template, dto);
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

	private Painter<N159Template, N159Dto> limitation() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			y -= 30;
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblLimitation()));
			for(String limitation: template.limitations()) {
				y -= 12;
				stream.paragraph(63, y+1, 10, new TextBlock(styleTextSub.clone().fontSize(4), "●"));
				y = stream.paragraph(70, y, 460, AlignHorizontal.JUSTIFY, new TextBlock(styleTextSub, limitation));
			}
			y -= 10;
			stream.setLineWidth(0.5f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
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
