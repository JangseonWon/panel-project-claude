package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;

public class SectionCancerTypes implements Painter<SolidTumorTemplate, SolidTumorDto> {
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        stream.saveGraphicsState();
        float y = stream.cursorY();
        y -= "KOKR".equalsIgnoreCase(template.testInfo().i18n()) ? 30 : 15;
        var resource = template.resource();
        stream.setNonStrokingColor(resource.colorGray())
                .addRect(60, y, 65, 20).fill()
                .addRect(290, y, 65, 20).fill()
                .setLineWidth(0.25f).setStrokingColor(resource.colorSecondary())
                .line(60, y + 0.1f, 533, y + 0.1f)
                .line(60, y + 20, 533, y + 20)
                .stroke();
        stream.paragraph(90, y + 7, 60, CENTER, new TextBlock(resource.styleHeaderTitle(), template.lblCancerCategory()));
        stream.paragraph(320, y + 7, 60, CENTER, new TextBlock(resource.styleHeaderTitle(), template.lblCancerType()));
        stream.paragraph(130, y + 8, 300, new TextBlock(resource.styleValue(), dto.cancerCategory()));
        y = stream.paragraph(360, y + 8, 300, new TextBlock(resource.styleValue(), dto.cancerType()));
        y -= 30;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
}
