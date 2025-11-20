package com.greencross.lims.report.enus;

import com.gcgenome.lims.report.Template;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.builder.AbstractReportDto;
import com.greencross.lims.report.builder.Util;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SectionFooterGenome<T extends Template<? extends HasSign>, D extends AbstractReportDto> implements Painter<T, D> {
	private PDImageXObject img = null;
	private final File resource = new File("/data/lims/resources");
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, T template, D dto) throws IOException {
		if(img == null) {
			BufferedImage src = ImageIO.read(new File(resource, "/img/인쇄용지_하단부_eng.png"));
			BufferedImage dest = src.getSubimage(0, 0, src.getWidth(), src.getHeight());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(dest, "png", baos);
			img = PDImageXObject.createFromByteArray(template.resource().doc(), baos.toByteArray(), "footer.png");
		}
		stream.saveGraphicsState();
		Util.icon(stream, img, 0, 64, 595, 100);
		stream.restoreGraphicsState();
		return stream;
	}
}
