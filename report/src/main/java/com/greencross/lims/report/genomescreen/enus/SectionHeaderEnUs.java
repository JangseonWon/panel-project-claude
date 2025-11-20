package com.greencross.lims.report.genomescreen.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.builder.Util;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.genomescreen.GenomeScreenDto;

import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionHeaderEnUs<R extends GenomeScreenResourceEnUs> implements Painter<GenomeScreenTemplateEnUs<R>, GenomeScreenDto> {
	private final static float HEADER_LOGO_WIDTH = 150;
	private final static float MARGIN = 5;
	private final static float BOX_WIDTH = 70;
	private final static float BOX1_START_X = 205, BOX1_END_X = BOX1_START_X+BOX_WIDTH;
	private final static float BOX2_START_X = 370, BOX2_END_X = BOX2_START_X+BOX_WIDTH;
	private final static float BOX_END_X = BOX2_START_X + (BOX2_START_X - BOX1_START_X);
	private final static float BOX1_CENTER = BOX1_START_X + BOX_WIDTH/2;
	private final static float BOX2_CENTER = BOX2_START_X + BOX_WIDTH/2;
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, GenomeScreenTemplateEnUs<R> template, GenomeScreenDto dto) throws IOException {
		stream.saveGraphicsState();
		var y = stream.cursorY();
		y -= 10;
		var img = template.resource().imgHeaderLogo();
		var height = img.getHeight() * HEADER_LOGO_WIDTH / img.getWidth();
		stream.setNonStrokingColor(template.resource().colorGray())
			  .roundRect(50, y-height, 495, height, 18)
			  .fill();
		stream.drawImage(img, 50, y - height, HEADER_LOGO_WIDTH, height);
		stream.setNonStrokingColor(template.resource().colorDarkGray())
			  .addRect(BOX1_START_X,y - MARGIN, BOX_WIDTH, -height + MARGIN*2)
			  .addRect(BOX2_START_X,y - MARGIN, BOX_WIDTH, -height + MARGIN*2)
			  .fill();
		var heightRow = (height - MARGIN*2)/4;
		stream.setStrokingColor(template.resource().colorGray()).setLineWidth(0.25f)
			  .line(BOX1_START_X, y - MARGIN - heightRow, BOX1_END_X, y - MARGIN - heightRow)
			  .line(BOX1_START_X, y - MARGIN - heightRow*2, BOX1_END_X, y - MARGIN - heightRow*2)
			  .line(BOX1_START_X, y - MARGIN - heightRow*3, BOX1_END_X, y - MARGIN - heightRow*3)
			  .line(BOX2_START_X, y - MARGIN - heightRow, BOX2_END_X, y - MARGIN - heightRow)
			  .line(BOX2_START_X, y - MARGIN - heightRow*2, BOX2_END_X, y - MARGIN - heightRow*2)
			  .line(BOX2_START_X, y - MARGIN - heightRow*3, BOX2_END_X, y - MARGIN - heightRow*3)
			  .stroke()
			  .setStrokingColor(template.resource().colorDarkGray())
			  .line(BOX1_END_X, y - MARGIN - heightRow, BOX2_START_X, y - MARGIN - heightRow)
			  .line(BOX1_END_X, y - MARGIN - heightRow*2, BOX2_START_X, y - MARGIN - heightRow*2)
			  .line(BOX1_END_X, y - MARGIN - heightRow*3, BOX2_START_X, y - MARGIN - heightRow*3)
			  .line(BOX1_END_X, y - MARGIN - heightRow*4, BOX2_START_X, y - MARGIN - heightRow*4)
			  .line(BOX2_END_X, y - MARGIN - heightRow, BOX_END_X, y - MARGIN - heightRow)
			  .line(BOX2_END_X, y - MARGIN - heightRow*2, BOX_END_X, y - MARGIN - heightRow*2)
			  .line(BOX2_END_X, y - MARGIN - heightRow*3, BOX_END_X, y - MARGIN - heightRow*3)
			  .line(BOX2_END_X, y - MARGIN - heightRow*4, BOX_END_X, y - MARGIN - heightRow*4)
			  .stroke();

		GenomeScreenResourceEnUs resource = template.resource();
		TextStyle styleTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(8);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontValue(), resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		stream.paragraph(BOX1_START_X + BOX_WIDTH/2, y - MARGIN - heightRow/2, BOX_WIDTH - 10, CENTER, MIDDLE, new TextBlock(styleTitle, template.lblMedicalInstitution()));
		stream.paragraph(BOX1_START_X + BOX_WIDTH/2, y - MARGIN - heightRow/2*3, BOX_WIDTH - 10, CENTER, MIDDLE, new TextBlock(styleTitle, template.lblPatientName()));
		stream.paragraph(BOX1_START_X + BOX_WIDTH/2, y - MARGIN - heightRow/2*5, BOX_WIDTH - 10, CENTER, MIDDLE, new TextBlock(styleTitle, template.lblAgeSex()));
		stream.paragraph(BOX1_START_X + BOX_WIDTH/2, y - MARGIN - heightRow/2*7, BOX_WIDTH - 10, CENTER, MIDDLE, new TextBlock(styleTitle, template.lblSpecimenType()));
		stream.paragraph(BOX2_START_X + BOX_WIDTH/2, y - MARGIN - heightRow/2, BOX_WIDTH - 10, CENTER, MIDDLE, new TextBlock(styleTitle, template.lblRequestNumber()));
		stream.paragraph(BOX2_START_X + BOX_WIDTH/2, y - MARGIN - heightRow/2*3, BOX_WIDTH - 10, CENTER, MIDDLE, new TextBlock(styleTitle, template.lblMedicalRecordNumber()));
		stream.paragraph(BOX2_START_X + BOX_WIDTH/2, y - MARGIN - heightRow/2*5, BOX_WIDTH - 10, CENTER, MIDDLE, new TextBlock(styleTitle, template.lblAcceptionDate()));
		stream.paragraph(BOX2_START_X + BOX_WIDTH/2, y - MARGIN - heightRow/2*7, BOX_WIDTH - 10, CENTER, MIDDLE, new TextBlock(styleTitle, template.lblReportDate()));

		float valueWidth = BOX2_START_X - BOX1_END_X;
		stream.paragraph(BOX1_END_X+2, y - MARGIN - heightRow/2, valueWidth, MIDDLE, new TextBlock(styleValue, Util.dashIfEmpty(dto.medicalInstitution())));
		stream.paragraph(BOX1_END_X+2, y - MARGIN - heightRow/2*3, valueWidth, MIDDLE, new TextBlock(styleValue, Util.dashIfEmpty(dto.patientName())));
		stream.paragraph(BOX1_END_X+2, y - MARGIN - heightRow/2*5, valueWidth, MIDDLE, new TextBlock(styleValue, Util.dashIfEmpty(template.age(dto.age(), dto.birthDate(), dto.collectionDate())) + " / " + Util.dashIfEmpty(template.sex(dto.sex()))));
		stream.paragraph(BOX1_END_X+2, y - MARGIN - heightRow/2*7, valueWidth, MIDDLE, new TextBlock(styleValue, "WB"));
		stream.paragraph(BOX2_END_X+2, y - MARGIN - heightRow/2, valueWidth, MIDDLE, new TextBlock(styleValue, Util.dashIfEmpty(dto.requestNumber())));
		stream.paragraph(BOX2_END_X+2, y - MARGIN - heightRow/2*3, valueWidth, MIDDLE, new TextBlock(styleValue, Util.dashIfEmpty(dto.medicalRecordNumber())));
		stream.paragraph(BOX2_END_X+2, y - MARGIN - heightRow/2*5, valueWidth, MIDDLE, new TextBlock(styleValue,  Util.dashIfEmpty(template.date(dto.receiptDate()))));
		stream.paragraph(BOX2_END_X+2, y - MARGIN - heightRow/2*7, valueWidth, MIDDLE, new TextBlock(styleValue, Util.dashIfEmpty(template.date(dto.reportDate()))));

		stream.restoreGraphicsState();
		return stream.cursorY(y - height);
	}
}
