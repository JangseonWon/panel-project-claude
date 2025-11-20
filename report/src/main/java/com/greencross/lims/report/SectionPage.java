package com.greencross.lims.report;

import com.gcgenome.lims.report.Template;
import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.AlignHorizontal;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.AbstractReportDto;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionPage<T extends Template<?>, D extends AbstractReportDto> implements Painter<T, D> {
    private final float x;
    private final float y;
    private final PDFont font;
    public SectionPage(float x, float y, PDFont font) {
        this.x = x;
        this.y = y+10;      // SIGNHEIGHT / 2
        this.font = font;
    }

    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, T template, D dto) throws IOException {
        stream.saveGraphicsState();
        TextStyle ts = new TextStyle().color(Color.decode("#808080")).fonts(font).fontSize(8).justify(false).paragraph(false);
        for(int i = 0; i < template.resource().doc().getNumberOfPages(); ++i) {
            PDPage page = template.resource().doc().getPage(i);
            var s = PDPageContentStreamPageAccessible.append(template.resource().doc(), page);
            String str = "[ " + (i + 1) + " / " + template.resource().doc().getNumberOfPages() + " ]";
            s.paragraph(x, y, 80, AlignHorizontal.RIGHT, MIDDLE, new TextBlock(ts, str));
            s.close();
        }
        stream.restoreGraphicsState();
		return stream;
    }
}