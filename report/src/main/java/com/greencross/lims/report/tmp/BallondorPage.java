package com.greencross.lims.report.tmp;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.SectionBarcode;

import java.awt.*;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class BallondorPage extends Page<BallondorTemplate> {
	private final Painter<BallondorTemplate, BallondorDto> barcode = new SectionBarcode<>();
	private final Painter<BallondorTemplate, BallondorDto> ldt;
	private final Painter<BallondorTemplate, BallondorDto> footer;
	private final Painter<BallondorTemplate, BallondorDto> sign;
	private final Painter<BallondorTemplate, BallondorDto> pn;
	private Color colorGray;
	private TextStyle styleTitle;
	private TextStyle styleHeader;			// Test Performed, Reason for referral, ...
	private TextStyle styleHeaderTitle;
	private TextStyle styleValueTitle;
	private TextStyle styleText;
	public BallondorPage(BallondorTemplate template, Painter<BallondorTemplate, BallondorDto> ldt, Painter<BallondorTemplate, BallondorDto> footer, Painter<BallondorTemplate, BallondorDto> sign, Painter<BallondorTemplate, BallondorDto> pn) {
		super(template);
		this.ldt = ldt;
		this.footer = footer;
		this.sign = sign;
		this.pn = pn;
	}
	public Painter<BallondorTemplate, BallondorDto> page() {
		return initialize().and(barcode)
				.and(header())
				.and(footer).and(sign)
				.and(content())
				.and(ldt)
				.and(pn);
	}
	private Painter<BallondorTemplate, BallondorDto> initialize() {
		return (stream, template, dto) -> {
			BallondorResource resource = template.resource();
			colorGray = resource.colorGray();
			styleTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(20).paragraph(false);
			styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
			styleHeader = styleHeaderTitle.clone().color(resource.colorText()).fontSize(15).justify(false);
			styleValueTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).paragraph(false);
			styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).justify(true).paragraph(true);
			stream.font(template.resource().fontDefault());
			return stream;
		};
	}
	private Painter<BallondorTemplate, BallondorDto> header() {
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
	private Painter<BallondorTemplate, BallondorDto> content() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = 658;
			stream.paragraph(60, y, 473, new TextBlock(styleHeader, template.testInfo().name()));
			y -= 20;
			stream.paragraph(60, y, 473, new TextBlock(styleText, "[Conclusion]\n" + dto.summary()));
			y -= 50;
			StringBuilder sb = new StringBuilder("[검사결과]\n")
					.append("===================================================================\n")
					.append("  Gene           DNA change           AA change                  Results                VAF(%)\n")
					.append("----------------------------------------------------------------------------------------------------------------------\n");
			if("Detected".equalsIgnoreCase(dto.result())) {
				sb.append(" {:GENE}           {:HGVSC}            {:HGVSP}            {:RESULT}             {:VAF}\n"
						.replace("{:GENE}", template.testInfo().gene())
						.replace("{:HGVSC}", dto.hgvsc())
						.replace("{:HGVSP}", template.testInfo().hgvsp())
						.replace("{:RESULT}", dto.result().contains("Not")?dto.result():("   " + dto.result() + "  "))
						.replace("{:VAF}", dto.vaf()));
			} else for(String hgvsc: template.testInfo().hgvsc()) {
				sb.append(" {:GENE}           {:HGVSC}            {:HGVSP}            {:RESULT}             {:VAF}\n"
						.replace("{:GENE}", template.testInfo().gene())
						.replace("{:HGVSC}", hgvsc)
						.replace("{:HGVSP}", template.testInfo().hgvsp())
						.replace("{:RESULT}", dto.result())
						.replace("{:VAF}", dto.vaf()));
			}
			y = stream.paragraph(60, y, 800, new TextBlock(styleText, sb.append("===================================================================").toString()));
			y -= 30;
			y = stream.paragraph(60, y, 473, new TextBlock(styleText, "[비고]\n" + template.info()));

			y -= 30;
			stream.paragraph(60, y, 473, new TextBlock(styleText, "[검사정보]\n" +
					"대상질환 : " + template.testInfo().disease() + "\n" +
					"검체정보 : " + dto.sample() + "\n" +
					"검사방법 : " + template.testInfo().method() + "\n" +
					"검사대상 : " + template.testInfo().target() + "\n" +
					"검출한계 : " + template.testInfo().resolution()));

			y -= 110;
			stream.paragraph(60, y, 473, new TextBlock(styleText, "[검체정보]\n" + dto.info()));
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private static String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}
}
