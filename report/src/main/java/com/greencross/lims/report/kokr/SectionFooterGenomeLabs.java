package com.greencross.lims.report.kokr;

import com.gcgenome.lims.report.Template;
import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.builder.AbstractReportDto;
import com.greencross.lims.report.builder.Util;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;

public class SectionFooterGenomeLabs<T extends Template<? extends HasSign>, D extends AbstractReportDto> implements Painter<T, D> {
	private PDImageXObject img = null;
	private PDImageXObject img2 = null;
	private final File resource = new File("/data/lims/resources");
	private final Color color1 = new Color(0, 54, 105);
	private final Color color2 = new Color(230, 0, 33);
	private final Color color3 = new Color(146, 196, 29);
	private final Color color4 = new Color(0, 143, 73);
	private final String address = "www.gcgenome.com 경기도 용인시 기흥구 이현로 30번길 107 대표전화 031-280-9900 상담톡 https://gccs.channel.io/";
	private final String address2 = "www.gclabs.co.kr";
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, T template, D dto) throws IOException {
		if(img == null) {
			BufferedImage src = ImageIO.read(new File(resource, "/img/인쇄용지 하단부_지놈_Labs-25.04.png"));
			BufferedImage dest = src.getSubimage(0, 0, src.getWidth(), src.getHeight() - 80);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(dest, "png", baos);
			img = PDImageXObject.createFromByteArray(template.resource().doc(), baos.toByteArray(), "footer.png");
		}

		stream.saveGraphicsState();
		stream.setLineWidth(0.5f).setStrokingColor(Color.decode("#32A66D")).line(26, 60, 562, 60).stroke();
		Util.icon(stream, img, 0, 64, 595, 100);
		//Util.icon(stream, img2, 23.5f, 56, 125, 27);
		stream.setLineWidth(0.01f)
			  .setNonStrokingColor(color4).setStrokingColor(color4).moveTo(0, 0).lineTo(595, 0).lineTo(595, 21.5f).lineTo(0, 21.5f).fill()
			  .setNonStrokingColor(color3).setStrokingColor(color3).moveTo(0, 0).lineTo(481, 0).lineTo(496, 21.5f).lineTo(0, 21.5f).fill()
			  .setNonStrokingColor(color2).setStrokingColor(color2).moveTo(0, 0).lineTo(465, 0).lineTo(480, 21.5f).lineTo(0, 21.5f).fill()
			  .setNonStrokingColor(color1).setStrokingColor(color1).moveTo(0, 0).lineTo(449, 0).lineTo(464, 21.5f).lineTo(0, 21.5f).fill();
		TextStyle font = new TextStyle().color(Color.WHITE).fontSize(8).paragraph(false);
		stream.paragraph(30, 12, 500, new TextBlock(font, address));
		stream.paragraph(565, 12, 500, RIGHT, new TextBlock(font, address2));
		stream.restoreGraphicsState();
		return stream;
	}
}
