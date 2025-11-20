package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.Util;

import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionCover implements Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> {
	private final float iconX;
	public SectionCover(float iconX) {
		this.iconX = iconX;
	}
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var styleTitleSmall = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitleSmall(), resource.fontDefault()).fontSize(19).paragraph(false);
		var styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(44).paragraph(false);
		float y = 703;
		stream.paragraph(55, y, 500,  new TextBlock(styleTitleSmall, template.lblCategory()));
		Util.icon(stream, resource.icon(), iconX, y-3, 595, 30);
		y-= 53;
		stream.paragraph(55, y, 400,  new TextBlock(styleTitle, template.lblTitle()));
		y-= 97;
		stream.setStrokingColor(resource.colorPrimary()).setLineWidth(1)
				.line(55, y, 205, y)
				.line(55, y-47, 205, y-47).stroke();
		var styleHeader = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(25).paragraph(false);
		stream.paragraph(130, y-23, 150, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblTestReport()));
		y-= 25;
		Util.icon(stream, resource.cancer(), 220, y, 300, 300);
		y-= 320;
		stream.setNonStrokingColor(resource.colorGray()).rect(25, y, 545, 115).fill()
				.setLineWidth(0.5f).line(297.5f, y-32, 297.5f, y-102).stroke();
		y-= 25;
		styleHeader.fontSize(10);
		stream.paragraph(60, y, 150, MIDDLE, new TextBlock(styleHeader, template.lblPatientName()));
		stream.paragraph(60, y-17, 150, MIDDLE, new TextBlock(styleHeader, template.lblDoBSex()));
		stream.paragraph(60, y-34, 150, MIDDLE, new TextBlock(styleHeader, template.lblMedicalRecordNumber()));
		stream.paragraph(60, y-51, 150, MIDDLE, new TextBlock(styleHeader, template.lblMedicalInstitution()));
		stream.paragraph(60, y-68, 150, MIDDLE, new TextBlock(styleHeader, template.lblPhysician()));

		stream.paragraph(330, y-17, 150, MIDDLE, new TextBlock(styleHeader, template.lblRequestNumber()));
		stream.paragraph(330, y-34, 150, MIDDLE, new TextBlock(styleHeader, template.lblCollectionDate()));
		stream.paragraph(330, y-51, 150, MIDDLE, new TextBlock(styleHeader, template.lblReceiptDate()));
		stream.paragraph(330, y-68, 150, MIDDLE, new TextBlock(styleHeader, template.lblReportDate()));

		styleHeader.fonts(resource.fontText(), resource.fontDefault()).color(resource.colorText());
		stream.paragraph(170, y, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(dto.patientName())));
		stream.paragraph(170, y-17, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(template.date(dto.birthDate())) + " / " + Util.dashIfEmpty(template.sex(dto.sex()))));
		stream.paragraph(170, y-34, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(dto.medicalRecordNumber())));
		stream.paragraph(170, y-51, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(dto.medicalInstitution())));
		stream.paragraph(170, y-68, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(dto.physician())));

		stream.paragraph(440, y-17, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(dto.requestNumber())));
		stream.paragraph(440, y-34, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(template.date(dto.collectionDate()))));
		stream.paragraph(440, y-51, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(template.date(dto.receiptDate()))));
		stream.paragraph(440, y-68, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(template.date(dto.reportDate()))));
		stream.restoreGraphicsState();
		return stream.cursorY(0f);
	}
}
