package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionMethod implements Painter<SolidTumorTemplate, SolidTumorDto> {
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        stream.saveGraphicsState();
        var resource = template.resource();

        float y = stream.cursorY();
        stream.setNonStrokingColor(resource.colorPrimary()).addRect(60, y-6,473, 20).fill();
        stream.paragraph(65, y, 120, new TextBlock(resource.styleHeader2(), template.lblTestInfo()));
        y -= 20;
        stream.paragraph(60, y, 120, new TextBlock(resource.styleHeader3(), template.lblTestMethod()));
        y -= 5;
        stream.setLineWidth(1).setStrokingColor(resource.colorSecondary())
                .line(60, y, 533, y).stroke()
                .setNonStrokingColor(template.resource().colorGray())
                .addRect(60, y-1, 180, -120).fill();
        y-= 15;	stream.setStrokingColor(template.resource().colorGray()).setLineWidth(0.5f)
                .setStrokingColor(Color.WHITE).line(60, y-8, 240, y-8).stroke()
                .setStrokingColor(resource.colorGray()).line(240, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(resource.styleHeader4(), template.lblTestRegion()));
        stream.paragraph(250, y+2, 280, MIDDLE, new TextBlock(resource.styleText(), dto.method().region()));
        y-= 20;	stream.setStrokingColor(Color.WHITE).line(60, y-8, 240, y-8).stroke()
                .setStrokingColor(resource.colorGray()).line(240, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(resource.styleHeader4(), template.lblTestPanel()));
        stream.paragraph(250, y+2, 280, MIDDLE, new TextBlock(resource.styleText(), dto.method().panel()));
        y-= 20;	stream.setStrokingColor(Color.WHITE).line(60, y-8, 240, y-8).stroke()
                .setStrokingColor(resource.colorGray()).line(240, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(resource.styleHeader4(), template.lblTestMethodEnrichment()));
        stream.paragraph(250, y+2, 280, MIDDLE, new TextBlock(resource.styleText(), dto.method().method()));
        y-= 20;	stream.setStrokingColor(Color.WHITE).line(60, y-8, 240, y-8).stroke()
                .setStrokingColor(resource.colorGray()).line(240, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(resource.styleHeader4(), template.lblTestSequencing()));
        stream.paragraph(250, y+2, 280, MIDDLE, new TextBlock(resource.styleText(), dto.method().sequencing()));
        y-= 20;	stream.setStrokingColor(Color.WHITE).line(60, y-8, 240, y-8).stroke()
                .setStrokingColor(resource.colorGray()).line(240, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(resource.styleHeader4(), template.lblTestPipeline()));
        stream.paragraph(250, y+2, 280, MIDDLE, new TextBlock(resource.styleText(), dto.method().pipeline()));
        y-= 20;	stream.setLineWidth(1).setStrokingColor(resource.colorSecondary())
                .line(60, y-7, 533, y-7).stroke()
                .paragraph(70, y, 200, new TextBlock(resource.styleHeader4(), template.lblTestReference()));
        stream.paragraph(250, y+2, 280, MIDDLE, new TextBlock(resource.styleText(), dto.method().reference()));
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
}
