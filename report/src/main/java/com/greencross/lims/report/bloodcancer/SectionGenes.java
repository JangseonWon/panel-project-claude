package com.greencross.lims.report.bloodcancer;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.bloodcancer.TestInfo;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionGenes implements Painter<BloodCancerTemplate, BloodCancerDto> {
    private final Painter<BloodCancerTemplate, BloodCancerDto> newPage;
    public SectionGenes(Painter<BloodCancerTemplate, BloodCancerDto> newPage) {
        this.newPage = newPage;
    }
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, BloodCancerTemplate template, BloodCancerDto dto) throws IOException {
        stream.saveGraphicsState();
        BloodCancerResource resource = template.resource();
        Color colorPrimary = resource.colorPrimary();
        TextStyle styleHeader = new TextStyle().color(colorPrimary).fonts(resource.fontTitle()).fontSize(11);
        float y = stream.cursorY();
        stream.saveGraphicsState();
        y -= 30;
        stream.paragraph(60, y, 150, new TextBlock(styleHeader, template.lblTestGeneInfo()));
        y -= 20;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        stream = genes(template.lblGeneEssential(), template.testInfo().genesEssential())
                .and(genes(template.lblGeneSelective(), template.testInfo().genesSelective()))
                .paint(stream, template, dto);
        return stream;
    }

    private Painter<BloodCancerTemplate, BloodCancerDto> genes(String label, TestInfo.Gene[] genes) {
        return (stream, template, dto)->{
            if(genes == null || genes.length <= 0) return stream;
            BloodCancerResource resource = template.resource();
            Color colorPrimary = resource.colorPrimary();
            Color colorSecondary = resource.colorSecondary();
            Color colorGray = resource.colorGray();
            TextStyle styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
            TextStyle styleHeader3 = new TextStyle().color(colorPrimary).fonts(resource.fontTitle()).fontSize(11);

            float y = stream.cursorY();
            stream.saveGraphicsState();
            stream.setNonStrokingColor(colorGray).addRect(60, y+10, 473, -45).fill();
            stream.paragraph(65, y, 470, MIDDLE, new TextBlock(styleHeader3.clone().fontSize(9), label));
            y -= 10;
            stream.setLineWidth(1).setStrokingColor(colorSecondary).line(60, y, 533, y)
                    .line(60, y+20, 533, y+20).stroke();
            TextStyle styleHeader = styleHeaderTitle.clone().fontSize(6).paragraph(false).justify(false);
            stream.setLineWidth(0.5f);
            y-= 12.5f;
            stream.fill();
            for(int i = 0; i < 3; ++i) {
                float x = 60;
                if(i%3 == 1) x += 157.6667f;
                else if(i%3 == 2) x += 315.3333f;
                stream.paragraph(x+37.6667f/2, y, 37.6667f, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblGeneInfoHeaders()[0]));
                stream.paragraph(x+37.6667f+35, y, 70, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblGeneInfoHeaders()[1]));
                stream.paragraph(x+37.6667f+90, y, 60, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblGeneInfoHeaders()[2]));
            }
            y -= 25;
            for(int i = 0; i < genes.length; ++i) {
                float x = 60;
                if(i%3 == 0) {
                    if(y-25 < 100) {
                        stream.setLineWidth(1).setStrokingColor(colorSecondary).line(60, y+10, 533, y+10).stroke();
                        stream.cursorY(y);
                        stream.restoreGraphicsState();
                        stream = newPage.paint(stream, template, dto);
                        stream.cursorY(stream.cursorY()-30);
                        TestInfo.Gene[] remains = new TestInfo.Gene[genes.length-i];
                        System.arraycopy(genes, i, remains, 0, remains.length);
                        return genes(label, remains).paint(stream, template, dto);
                    }
                    stream.setStrokingColor(Color.WHITE)
                            .line(60, y+12.5f, 60+37.6667f, y+12.5f)
                            .line(60+157.6667f, y+12.5f, 60+157.6667f+37.6667f, y+12.5f)
                            .line(60+315.3333f, y+12.5f, 60+315.3333f+37.6667f, y+12.5f).stroke()
                            .setStrokingColor(colorGray)
                            .line(60+37.6667f, y+12.5f, 60+157.6667f, y+12.5f)
                            .line(60+157.6667f+37.6667f, y+12.5f, 60+315.3333f, y+12.5f)
                            .line(60+315.3333f+37.6667f, y+12.5f, 533, y+12.5f).stroke();
                } else if(i%3 == 1) x += 157.6667f;
                else x += 315.3333f;
                stream.setNonStrokingColor(colorGray).addRect(x, y+12, 37.666f, -25).fill();
                TestInfo.Gene gene = genes[i];
                stream.paragraph(x+37.6667f/2, y, 37.6667f, CENTER, MIDDLE, new TextBlock(styleHeader, gene.name()));
                stream.paragraph(x+37.6667f+35, y, 70, CENTER, MIDDLE, new TextBlock(styleHeader, gene.exon()));
                stream.paragraph(x+37.6667f+90, y, 60, CENTER, MIDDLE, new TextBlock(styleHeader, gene.reference()));
                if(i%3 == 2) y -= 25;
            }
            if(genes.length%3 > 0) y -= 25;
            stream.setLineWidth(1).setStrokingColor(colorSecondary).line(60, y+10, 533, y+10).stroke();
            stream.cursorY(y);
            stream.restoreGraphicsState();
            return stream;
        };
    }
}
