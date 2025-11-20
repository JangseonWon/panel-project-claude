package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.Util;

import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionHeader implements Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> {
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
		stream.saveGraphicsState();
		float y = 775;
		var resource = template.resource();
		var styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(10).paragraph(false);
		stream.paragraph(560, y, 200, RIGHT, new TextBlock(styleTitle, template.lblTitle()));
		y-= 15;
		stream.setNonStrokingColor(resource.colorGray()).rect(25, y, 545, 75).fill();
		y-= 21;
		var styleHeader = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(false);
		stream.paragraph(60, y, 150, MIDDLE, new TextBlock(styleHeader, template.lblPatientName()));
		stream.paragraph(60, y-17, 150, MIDDLE, new TextBlock(styleHeader, template.lblDoBSex()));
		stream.paragraph(60, y-34, 150, MIDDLE, new TextBlock(styleHeader, template.lblMedicalRecordNumber()));

		stream.paragraph(330, y-17, 150, MIDDLE, new TextBlock(styleHeader, template.lblRequestNumber()));
		stream.paragraph(330, y-34, 150, MIDDLE, new TextBlock(styleHeader, template.lblMedicalInstitution()));

		styleHeader.fonts(resource.fontText(), resource.fontDefault()).color(resource.colorText());
		stream.paragraph(170, y, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(dto.patientName())));
		stream.paragraph(170, y-17, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(template.date(dto.birthDate())) + " / " + Util.dashIfEmpty(template.sex(dto.sex()))));
		stream.paragraph(170, y-34, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(dto.medicalRecordNumber())));

		stream.paragraph(440, y-17, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(dto.requestNumber())));
		stream.paragraph(440, y-34, 150, MIDDLE, new TextBlock(styleHeader, Util.dashIfEmpty(dto.medicalInstitution())));
		stream.restoreGraphicsState();
		return stream.cursorY(y-54);
	}
}
