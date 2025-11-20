package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.AlignHorizontal;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.io.IOException;

public class SectionLimitation implements Painter<SolidTumorTemplate, SolidTumorDto> {
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        float y = stream.cursorY();
        stream.saveGraphicsState();
        var resource = template.resource();
        y -= 30;
        stream.paragraph(60, y, 120, new TextBlock(resource.styleHeader3(), template.lblTestLimitation()));
        y -= 5;
        stream.setLineWidth(1).setStrokingColor(resource.colorSecondary())
                .line(60, y, 533, y).stroke().stroke();
        TextStyle styleTextLimitation = resource.styleText().clone().justify(true).paragraph(true);
        for(var i = 0; i < dto.method().limitations().size(); i++) {
            var limitation = dto.method().limitations().get(i);
            y -= 12;
            stream.paragraph(63, y, 10, new TextBlock(styleTextLimitation, String.format("%d.", i+1)));
            y = stream.paragraph(75, y, 455, AlignHorizontal.JUSTIFY, new TextBlock(styleTextLimitation, limitation));
        }
        y -= 30;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
}
