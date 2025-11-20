package com.greencross.lims.report.solidtumor2.kokr;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.solidtumor2.SolidTumorDto;
import com.greencross.lims.report.solidtumor2.SolidTumorTemplate;

import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SolidTumorHeaderKoKr implements Painter<SolidTumorTemplate, SolidTumorDto> {

	private static String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}

	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var colorSecondary = resource.colorSecondary();
		var colorGray = resource.colorGray();
		var styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(17).paragraph(false);
		var styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
		var styleValueTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).paragraph(false);

		stream.drawImage(template.resource().medal(), 47, 770,23,38);
		stream.paragraph(80, 773, 500, new TextBlock(styleTitle, template.testInfo().name()));
		stream.setNonStrokingColor(colorGray)
				.setLineWidth(0.25f).setStrokingColor(colorSecondary)
				.addRect(60,677.5f,65, 63)
				.addRect(211,677.5f,75, 63)
				.addRect(374,677.5f,75, 63)
				.fill();

		float y = 731;
		stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblMedicalInstitution()));
		stream.paragraph(214, y, 69, new TextBlock(styleHeaderTitle, template.lblMedicalRecordNumber()));
		stream.paragraph(377, y, 69, new TextBlock(styleHeaderTitle, template.lblRequestNumber()));
		stream.paragraph(130, y+2.5f, 83, MIDDLE, new TextBlock(styleValueTitle, dashIfEmpty(dto.medicalInstitution())));
		stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, dashIfEmpty(dto.medicalRecordNumber())));
		y = stream.paragraph(452, y, 100, new TextBlock(styleValueTitle, dashIfEmpty(dto.requestNumber())));
		stream.line(60, y-5, 533, y-5);
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
		stream.cursorY(y);
		return stream;
	}
}
