package com.greencross.lims.report.bloodcancer;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;

public class SectionQc implements Painter<BloodCancerTemplate, BloodCancerDto> {
    private final Painter<BloodCancerTemplate, BloodCancerDto> newPage;
    public SectionQc(Painter<BloodCancerTemplate, BloodCancerDto> newPage) {
        this.newPage = newPage;
    }
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, BloodCancerTemplate template, BloodCancerDto dto) throws IOException {
        BloodCancerResource resource = template.resource();
        Color colorPrimary = resource.colorPrimary();
        Color colorSecondary = resource.colorSecondary();
        Color colorGray = resource.colorGray();
        TextStyle styleHeader3 = new TextStyle().color(colorPrimary).fonts(resource.fontTitle()).fontSize(11);
        TextStyle styleHeader4 = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(9);
        TextStyle styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).justify(true).paragraph(true);

        float y = stream.cursorY();
        float height = 90;
        if(y - height < 120) {
            stream = newPage.paint(stream, template, dto);
            y = stream.cursorY();
        }
        stream.saveGraphicsState();
        y -= 30;
        stream.paragraph(60, y, 120, new TextBlock(styleHeader3, template.lblTestQc()));
        y -= 5;
        String meanDepth = dto.meanDepth();
        if(meanDepth!=null && !meanDepth.toUpperCase().contains("X") && !meanDepth.toUpperCase().contains("×")) meanDepth += "X";
        String coverage = dto.coverage();
        if(coverage!=null && !coverage.contains("%")) coverage += "%";
        stream.setLineWidth(1).setStrokingColor(colorSecondary)
                .line(60, y, 533, y).stroke()
                .setNonStrokingColor(template.resource().colorGray())
                .addRect(60, y-1, 180, -60)
                .addRect(300, y-1, 180, -60).fill();
        y-= 15;	stream.setStrokingColor(template.resource().colorGray()).setLineWidth(0.5f).setStrokingColor(Color.WHITE)
                .line(60, y-8, 240, y-8)
                .line(300, y-8, 480, y-8).stroke()
                .setStrokingColor(colorGray)
                .line(240, y-8, 300, y-8)
                .line(480, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblQcSample()));
        stream.paragraph(260, y, 50, new TextBlock(styleText, dto.qcDna()));
        stream.paragraph(310, y, 200, new TextBlock(styleHeader4, template.lblQcMeanDepth()));
        stream.paragraph(500, y, 50, new TextBlock(styleText, meanDepth));
        y-= 20;	stream.setStrokingColor(Color.WHITE)
                .line(60, y-8, 240, y-8)
                .line(300, y-8, 480, y-8).stroke()
                .setStrokingColor(colorGray)
                .line(240, y-8, 300, y-8)
                .line(480, y-8, 533, y-8).stroke()
                .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblQcLibrary()));
        stream.paragraph(260, y, 50, new TextBlock(styleText, dto.qcLibrary()));
        stream.paragraph(310, y, 200, new TextBlock(styleHeader4, template.lblQcCoverage()));
        stream.paragraph(500, y, 50, new TextBlock(styleText, coverage));
        y-= 20;	stream.setLineWidth(1).setStrokingColor(colorSecondary)
                .line(60, y-7, 533, y-7).stroke()
                .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblQcSequencing()));
        stream.paragraph(260, y, 50, new TextBlock(styleText, dto.qcSequencing()));
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
}
