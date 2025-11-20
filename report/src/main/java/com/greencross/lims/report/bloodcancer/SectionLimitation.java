package com.greencross.lims.report.bloodcancer;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.AlignHorizontal;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;

public class SectionLimitation implements Painter<BloodCancerTemplate, BloodCancerDto> {
    private final Painter<BloodCancerTemplate, BloodCancerDto> newPage;
    public SectionLimitation(Painter<BloodCancerTemplate, BloodCancerDto> newPage) {
        this.newPage = newPage;
    }
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, BloodCancerTemplate template, BloodCancerDto dto) throws IOException {
        BloodCancerResource resource = template.resource();
        Color colorPrimary = resource.colorPrimary();
        Color colorSecondary = resource.colorSecondary();
        TextStyle styleHeader3 = new TextStyle().color(colorPrimary).fonts(resource.fontTitle()).fontSize(11);
        TextStyle styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).justify(true).paragraph(true);

        float y = stream.cursorY();
        TextStyle styleTextLimitation = styleText.clone().justify(true).paragraph(true);
        float height = 35;
        for(String limitation: template.lblTestLimitations()) {
            height += 12 + stream.height(460, new TextBlock(styleTextLimitation, limitation));
        }
        if(y - height < 120) {
            stream = newPage.paint(stream, template, dto);
            y = stream.cursorY();
        }
        stream.saveGraphicsState();
        y -= 30;
        stream.paragraph(60, y, 120, new TextBlock(styleHeader3, template.lblTestLimitation()));
        y -= 5;
        stream.setLineWidth(1).setStrokingColor(colorSecondary)
                .line(60, y, 533, y).stroke().stroke();
        for(String limitation: template.lblTestLimitations()) {
            y -= 12;
            stream.paragraph(63, y+1, 10, new TextBlock(styleTextLimitation.clone().fontSize(4), "●"));
            y = stream.paragraph(70, y, 460, AlignHorizontal.JUSTIFY, new TextBlock(styleTextLimitation, limitation));
        }
        y -= 30;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
}
