package com.greencross.lims.report;

import com.gcgenome.lims.report.Template;
import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.greencross.lims.report.builder.AbstractReportDto;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionBarcode<T extends Template<?>, D extends AbstractReportDto> implements Painter<T, D> {
	private final static int BARCODE_WIDTH = 100;
	private final static int BARCODE_HEIGHT = 20;
	private final static float BARCODE_POS_X = 440;
	private final static float BARCODE_POS_Y = 805;
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, T template, D dto) throws IOException {
		if(dto.barcode()!=null && !dto.barcode().trim().isEmpty()) {
			stream.saveGraphicsState();
			Code128Writer barcodeWriter = new Code128Writer();
			BitMatrix bitMatrix = barcodeWriter.encode(dto.barcode(), BarcodeFormat.CODE_128, BARCODE_WIDTH, BARCODE_HEIGHT);
			PDImageXObject barcodeImg = LosslessFactory.createFromImage(template.resource().doc(), MatrixToImageWriter.toBufferedImage(bitMatrix));
			TextStyle ts = new TextStyle().color(Color.decode("#808080")).fontSize(5).justify(true).paragraph(false);
			stream.drawImage(barcodeImg, BARCODE_POS_X, BARCODE_POS_Y+5, BARCODE_WIDTH, BARCODE_HEIGHT)
				  .paragraph(BARCODE_POS_X+2, BARCODE_POS_Y, BARCODE_WIDTH-4, JUSTIFY, MIDDLE, new TextBlock(ts, dto.barcode()));
			stream.restoreGraphicsState();
		}
		return stream;
	}
}
