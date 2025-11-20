package com.greencross.lims.report.hrd.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.hrd.HrdDto;
import com.greencross.lims.report.hrd.HrdHeader;
import com.greencross.lims.report.hrd.HrdResource;
import com.greencross.lims.report.hrd.HrdTemplate;

import java.awt.*;

public class HrdHeaderEnUs extends HrdHeader {
	protected Color colorSecondary;
	protected Color colorGray;
	protected TextStyle styleTitle;
	protected TextStyle styleHeaderTitle;
	protected TextStyle styleValueTitle;
	protected TextStyle styleTextSign;
	public HrdHeaderEnUs(HrdTemplate template) {
		super(template);
	}
	@Override
	public Painter<HrdTemplate, HrdDto> initialize() {
		return (stream, template, dto) -> stream;
	}

	@Override
	public Painter<HrdTemplate, HrdDto> header() {
		return (stream, template, dto)->{
			stream.saveGraphicsState();
			HrdResource resource = template.resource();
			colorSecondary = resource.colorSecondary();
			colorGray = resource.colorGray();
			styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9);
			styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(17).paragraph(false);
			styleValueTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).paragraph(false);
			styleTextSign = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).justify(false).paragraph(false);

			stream.drawImage(template.resource().medal(), 47, 770,23,38);
			stream.paragraph(80, 773, 500, new TextBlock(styleTitle, template.testInfo().name()));
			stream.setNonStrokingColor(colorGray)
				  .setLineWidth(0.25f).setStrokingColor(colorGray)
				  .addRect(60,677.5f,2, 63)
				  .addRect(211,677.5f,2, 63)
				  .addRect(374,677.5f,2, 63)
				  .fill();

			float y = 731;
			float y1 = 0, y2 = 0, y3 = 0;
			y1 = stream.paragraph(72, y, 150, new TextBlock(styleHeaderTitle, "Personal Information"));
			y2 = stream.paragraph(224, y, 150, new TextBlock(styleHeaderTitle, "Specimen Information"));
			y3 = stream.paragraph(387, y, 150, new TextBlock(styleHeaderTitle, "Test Information"));
			y = Math.max(Math.max(y1, y2), y3) - 20;

			stream.paragraph(72, y, 150, new TextBlock(styleHeaderTitle, "Name: "));
			stream.paragraph(224, y, 150, new TextBlock(styleHeaderTitle, "Sample ID: "));
			stream.paragraph(387, y, 150, new TextBlock(styleHeaderTitle, "Test reported: "));
			y1 = stream.paragraph(103.8f, y, 118.2f, new TextBlock(styleValueTitle, dto.patientName()));
			y2 = stream.paragraph(273.9f, y, 100.1f, new TextBlock(styleValueTitle, dashIfEmpty(dto.requestNumber())));
			y3 = stream.paragraph(452.8f, y, 84.2f, new TextBlock(styleValueTitle, dashIfEmpty(template.date(dto.reportDate()))));
			y = Math.max(Math.max(y1, y2), y3) - 16;

			stream.paragraph(72, y, 150, new TextBlock(styleHeaderTitle, "Relation: "));
			stream.paragraph(224, y, 150, new TextBlock(styleHeaderTitle, "Medical record No: "));
			stream.paragraph(387, y, 150, new TextBlock(styleHeaderTitle, "Ordering physician: "));
			y1 = stream.paragraph(114.5f, y, 107.5f, new TextBlock(styleValueTitle,  dashIfEmpty(dto.relation())));
			y2 = stream.paragraph(311, y, 63.1f, new TextBlock(styleValueTitle, dashIfEmpty(dto.medicalRecordNumber())));
			y3 = stream.paragraph(477, y, 60, new TextBlock(styleValueTitle, dashIfEmpty(dto.physician())));
			y = Math.max(Math.max(y1, y2), y3) - 16;

			stream.paragraph(72, y, 150, new TextBlock(styleHeaderTitle, "Sex/Birth: "));
			stream.paragraph(224, y, 150, new TextBlock(styleHeaderTitle, "Date received: "));
			stream.paragraph(387, y, 150, new TextBlock(styleHeaderTitle, "Institution: "));
			y1 = stream.paragraph(118.8f, y, 103.2f, new TextBlock(styleValueTitle, dashIfEmpty(dto.sex()!=null?dto.sex().name():null) + " / " + dashIfEmpty(template.date(dto.birthDate()))));
			y2 = stream.paragraph(290.8f, y, 83.2f, new TextBlock(styleValueTitle, dashIfEmpty(template.date(dto.receiptDate()))));
			y3 = stream.paragraph(439.9f, y, 97.1f, new TextBlock(styleValueTitle, dashIfEmpty(dto.medicalInstitution())));
			y = Math.max(Math.max(y1, y2), y3) - 16;
			stream.restoreGraphicsState();
			stream.cursorY(y);
			return stream;
		};
	}
}
