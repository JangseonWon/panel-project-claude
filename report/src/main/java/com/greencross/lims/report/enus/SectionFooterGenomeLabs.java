package com.greencross.lims.report.enus;

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

public class SectionFooterGenomeLabs<T extends Template<? extends HasSign>, D extends AbstractReportDto> implements Painter<T, D> {
	private PDImageXObject img = null;
	private final File resource = new File("/data/lims/resources");
	private final Color color1 = new Color(0, 54, 105);
	private final Color color2 = new Color(230, 0, 33);
	private final Color color3 = new Color(146, 196, 29);
	private final Color color4 = new Color(0, 143, 73);
	private final String address1 = "www.gcgenome.com ";
	private final String address2 = "107 Ihyeon-ro, 30 beon-gil, Giheung-gu, Yongin-si, Gyeonggi-do 16924 South Korea";
	private final String address3 = "Tel ";
	private final String address4 = "031-280-9900";
	private final String address5 = "Q&A ";
	private final String address6 = "https://gccs.channel.io/";
	private final String address7 = "www.gclabs.co.kr";
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, T template, D dto) throws IOException {
		if(img == null) {
			BufferedImage src = ImageIO.read(new File(resource, "/img/footerLabsGenomeEnUs.png"));
			BufferedImage dest = src.getSubimage(0, 0, src.getWidth(), src.getHeight() - 100);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(dest, "png", baos);
			img = PDImageXObject.createFromByteArray(template.resource().doc(), baos.toByteArray(), "footer.png");
		}
		stream.saveGraphicsState();
		stream.setLineWidth(0.5f).setStrokingColor(Color.decode("#32A66D")).line(26, 60, 562, 60).stroke();
		Util.icon(stream, img, 0, 64, 595, 100);
		stream.setLineWidth(0.01f)
				.setNonStrokingColor(color4).setStrokingColor(color4).moveTo(0, 0).lineTo(595, 0).lineTo(595, 20.5f).lineTo(0, 20.5f).fill()
				.setNonStrokingColor(color3).setStrokingColor(color3).moveTo(0, 0).lineTo(481, 0).lineTo(496, 20.5f).lineTo(0, 20.5f).fill()
				.setNonStrokingColor(color2).setStrokingColor(color2).moveTo(0, 0).lineTo(465, 0).lineTo(480, 20.5f).lineTo(0, 20.5f).fill()
				.setNonStrokingColor(color1).setStrokingColor(color1).moveTo(0, 0).lineTo(449, 0).lineTo(464, 20.5f).lineTo(0, 20.5f).fill();
		var fontBold	= template.resource().font(new File(resource, "/font/CALIBRIB.TTF"));
		var fontNormal	= template.resource().font(new File(resource, "/font/CALIBRI.TTF"));
		TextStyle bold = new TextStyle().fonts(fontBold).color(Color.WHITE).fontSize(7).paragraph(false);
		TextStyle plain = new TextStyle().fonts(fontNormal).color(Color.WHITE).fontSize(7).paragraph(false);
		stream.paragraph(14, 10, 400, new TextBlock(bold, address1), new TextBlock(plain, address2));
		stream.paragraph(316, 10, 400, new TextBlock(bold, address3), new TextBlock(plain, address4));
		stream.paragraph(369, 10, 400, new TextBlock(bold, address5), new TextBlock(plain, address6));
		stream.paragraph(510, 10, 400, new TextBlock(new TextStyle().fonts(fontBold).color(Color.WHITE).fontSize(8).paragraph(false), address7));

		stream.restoreGraphicsState();
		return stream;
	}
}
