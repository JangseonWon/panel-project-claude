package com.greencross.lims.report.bloodcancer;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;

public class SectionMethod implements Painter<BloodCancerTemplate, BloodCancerDto> {
    private final Painter<BloodCancerTemplate, BloodCancerDto> newPage;
    public SectionMethod(Painter<BloodCancerTemplate, BloodCancerDto> newPage) {
        this.newPage = newPage;
    }
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, BloodCancerTemplate template, BloodCancerDto dto) throws IOException {
        BloodCancerResource resource = template.resource();
        Color colorPrimary = resource.colorPrimary();
        Color colorSecondary = resource.colorSecondary();
        Color colorGray = resource.colorGray();
        TextStyle styleHeader2 = new TextStyle().color(resource.colorTextWithPrimary()).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(13);
        TextStyle styleHeader3 = new TextStyle().color(colorPrimary).fonts(resource.fontTitle()).fontSize(11);
        TextStyle styleHeader4 = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(9);
        TextStyle styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).justify(true).paragraph(true);

        float y = stream.cursorY();
        float height = 170;
        if(y - height < 120) {
            stream = newPage.paint(stream, template, dto);
            y = stream.cursorY();
            y -= 30;
        }
        stream.saveGraphicsState();
        stream.setNonStrokingColor(colorPrimary).addRect(60, y-6,473, 20).fill();
        stream.paragraph(65, y, 150, new TextBlock(styleHeader2, template.lblTestInfo()));
        y -= 20;
        stream.paragraph(60, y, 120, new TextBlock(styleHeader3, template.lblTestMethod()));
        y -= 5;
        stream.setLineWidth(1).setStrokingColor(colorSecondary)
                .line(60, y, 533, y).stroke()
                .setNonStrokingColor(template.resource().colorGray())
                .addRect(60, y-1, 180, -120).fill();
        y-= 15;	stream.setStrokingColor(template.resource().colorGray()).setLineWidth(0.5f)
                .setStrokingColor(Color.WHITE).line(60, y-8, 240, y-8).stroke()
                .setStrokingColor(colorGray).line(240, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblTestRegion()));
        stream.paragraph(260, y, 230, new TextBlock(styleText, template.testInfo().region()));
        y-= 20;	stream.setStrokingColor(Color.WHITE).line(60, y-8, 240, y-8).stroke()
                .setStrokingColor(colorGray).line(240, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblTestPanel()));
        stream.paragraph(260, y, 230, new TextBlock(styleText, template.testInfo().panel()));
        y-= 20;	stream.setStrokingColor(Color.WHITE).line(60, y-8, 240, y-8).stroke()
                .setStrokingColor(colorGray).line(240, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblTestMethodEnrichment()));
        stream.paragraph(260, y, 230, new TextBlock(styleText, template.testInfo().method()));
        y-= 20;	stream.setStrokingColor(Color.WHITE).line(60, y-8, 240, y-8).stroke()
                .setStrokingColor(colorGray).line(240, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblTestSequencing()));
        stream.paragraph(260, y, 230, new TextBlock(styleText, template.testInfo().sequencing()));
        y-= 20;	stream.setStrokingColor(Color.WHITE).line(60, y-8, 240, y-8).stroke()
                .setStrokingColor(colorGray).line(240, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblTestPipeline()));
        stream.paragraph(260, y, 230, new TextBlock(styleText, template.testInfo().pipeline()));
        y-= 20;	stream.setLineWidth(1).setStrokingColor(colorSecondary)
                .line(60, y-7, 533, y-7).stroke()
                .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblTestReference()));
        stream.paragraph(260, y, 230, new TextBlock(styleText, template.testInfo().reference()));
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
}
