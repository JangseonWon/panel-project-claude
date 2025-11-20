package com.greencross.lims.report.sanger.kokr;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.sanger.SangerDto;
import com.greencross.lims.report.sanger.SangerHeader;
import com.greencross.lims.report.sanger.SangerResource;
import com.greencross.lims.report.sanger.SangerTemplate;

import java.awt.*;

import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class HeaderKoKr extends SangerHeader {
	private Color colorGray;
	private TextStyle styleHeaderTitle;
	private TextStyle styleTitle;
	private TextStyle styleValueTitle;
	public HeaderKoKr(SangerTemplate template) {
		super(template);
	}
	@Override
	public Painter<SangerTemplate, SangerDto> initialize() {
		return (stream, template, dto) -> {
			SangerResource resource = template.resource();
			colorGray = resource.colorGray();
			styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(17).paragraph(false);
			styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
			styleValueTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).paragraph(false);
			return stream;
		};
	}

	@Override
	public Painter<SangerTemplate, SangerDto> header() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			stream.drawImage(template.resource().medal(), 47, 770,23,38);
			stream.paragraph(80, 773, 500, new TextBlock(styleTitle, template.testInfo().name()));
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
	private static String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}
}
