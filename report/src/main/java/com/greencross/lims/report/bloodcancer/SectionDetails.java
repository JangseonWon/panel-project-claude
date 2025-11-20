package com.greencross.lims.report.bloodcancer;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionDetails implements Painter<BloodCancerTemplate, BloodCancerDto> {
    private static final NumberFormat fmtNumber = NumberFormat.getInstance();
    private static final NumberFormat fmtPrecision = new DecimalFormat("0");
    private final Painter<BloodCancerTemplate, BloodCancerDto> newPage;
    public SectionDetails(Painter<BloodCancerTemplate, BloodCancerDto> newPage) {
        this.newPage = newPage;
        fmtPrecision.setMaximumFractionDigits(2);
    }
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, BloodCancerTemplate template, BloodCancerDto dto) throws IOException {
        BloodCancerResource resource = template.resource();
        Color colorPrimary = resource.colorPrimary();
        TextStyle styleHeader2 = new TextStyle().color(resource.colorTextWithPrimary()).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(13);
        stream.saveGraphicsState();
        float y = stream.cursorY();
        y -= 30;
        stream.setNonStrokingColor(colorPrimary).addRect(60, y-6,473, 20).fill();
        stream.paragraph(65, y, 150, new TextBlock(styleHeader2, template.lblDetails()));
        y += 3.2f;
        y -= 30;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        BloodCancerDto.Result t1 = Arrays.stream(dto.results()).filter(r->r.tier()==BloodCancerDto.Tier.Tier1).findFirst().orElse(new BloodCancerDto.Result());
        BloodCancerDto.Result t2 = Arrays.stream(dto.results()).filter(r->r.tier()==BloodCancerDto.Tier.Tier2).findFirst().orElse(new BloodCancerDto.Result());
        BloodCancerDto.Result t3 = Arrays.stream(dto.results()).filter(r->r.tier()==BloodCancerDto.Tier.Tier3).findFirst().orElse(new BloodCancerDto.Result());
        stream = details(t1).paint(stream, template, dto);
        stream = details(t2).paint(stream, template, dto);
        stream = details(t3).paint(stream, template, dto);
        return stream;
    }

    private Painter<BloodCancerTemplate, BloodCancerDto> details(BloodCancerDto.Result result) {
        return (stream, template, dto)->{
            BloodCancerResource resource = template.resource();
            Color colorPrimary = resource.colorPrimary();
            Color colorSecondary = resource.colorSecondary();
            TextStyle styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
            TextStyle styleHeader = styleHeaderTitle.clone().color(resource.colorTextWithPrimary()).fontSize(9).justify(false);
            TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).paragraph(false);
            TextStyle styleValueTable = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(false);
            TextStyle styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).justify(true).paragraph(true);
            float y = stream.cursorY();
            float height = 82 + ((result.variants()!=null && result.variants().length>0)?16*result.variants().length:22);
            if(y - height < 120) {
                stream = newPage.paint(stream, template, dto);
                y = stream.cursorY();
                y -= 30;
            }
            stream.saveGraphicsState();
            stream.setNonStrokingColor(colorPrimary).addRect(60, y,245, 20).fill()
                    .setLineWidth(0.25f).setStrokingColor(colorSecondary)
                    .line(60, y+20.1f, 533, y+20.1f).stroke();
            stream.paragraph(65, y+6, 237, new TextBlock(styleHeader, template.lblTier(result.tier()) + " : " + template.lblTierSignificance(result.tier())));
            stream.paragraph(320, y+6, 30, new TextBlock(styleValue, fmtNumber.format(result.variants()!=null?result.variants().length:0)));

            y -= 20;
            stream.addRect(60, y,473, 20).fill();
            y += 10;
            stream.paragraph(75, y, 30, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblVariantTable()[0]));
            stream.paragraph(125, y, 70, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblVariantTable()[1]));
            stream.paragraph(202.5f, y, 85, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblVariantTable()[2]));
            stream.paragraph(289, y, 88, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblVariantTable()[3]));
            stream.paragraph(363, y, 60, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblVariantTable()[4]));
            stream.paragraph(423, y, 60, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblVariantTable()[5]));
            stream.paragraph(493, y, 80, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblVariantTable()[6]));

            y -= 4;
            if(result.variants()!=null && result.variants().length > 0) for(int i =0 ; i < result.variants().length; ++i) {
                y -= 21;
                if(y < 120) {
                    stream = newPage.paint(stream, template, dto);
                    y = stream.cursorY();
                    y -= 30;
                }
                BloodCancerDto.Variant var = result.variants()[i];
                var sy = y;
                sy = Math.min(sy, stream.paragraph(75, y, 30, CENTER, new TextBlock(styleValueTable, fmtNumber.format(i+1))));
                sy = Math.min(sy, stream.paragraph(125, y, 70, CENTER, new TextBlock(styleValueTable, var.gene())));
                sy = Math.min(sy, stream.paragraph(202.5f, y, 85, CENTER, new TextBlock(styleValueTable, var.hgvsc())));
                sy = Math.min(sy, stream.paragraph(289, y, 88, CENTER, new TextBlock(styleValueTable, var.hgvsp())));
                sy = Math.min(sy, stream.paragraph(373, y, 60, RIGHT, new TextBlock(styleValueTable, fmtPrecision.format(var.vaf()))));
                sy = Math.min(sy, stream.paragraph(433, y, 60, RIGHT, new TextBlock(styleValueTable, fmtNumber.format(var.depth()))));
                sy = Math.min(sy, stream.paragraph(493, y, 80, CENTER,  new TextBlock(styleValueTable, var.cosmic())));
                y = sy;
            } else {
                y -= 21;
                stream.paragraph(295, y, 500, CENTER, new TextBlock(styleValueTable, template.lblNoVariant()));
            }
            y -= 8;
            stream.line(60, y, 533, y).stroke();
            y -= 15;
            // y = stream.paragraph(60, y, 473, new TextBlock(styleText, report.interpretation()));
            String[] paragraphs = result.interpretation().split("\n", -1);
            for(int i = 0; i < paragraphs.length; ++i) {
                String p = paragraphs[i];
                TextBlock block = new TextBlock(styleText, p + "\n\n");
                float height2 = stream.height(473, block);
                if(y - height2 < 100) {
                    stream.restoreGraphicsState();
                    stream = newPage.paint(stream, template, dto);
                    y = stream.cursorY() - 30;
                    stream.paragraph(60, y, 115, new TextBlock(styleHeader.clone().color(colorPrimary), template.lblInterpretation()));
                    y -= 20;
                    stream.saveGraphicsState();
                } else if(i == 0) {
                    stream.paragraph(60, y, 115, new TextBlock(styleHeader.clone().color(colorPrimary), template.lblInterpretation()));
                    y -= 15;
                }
                y = stream.paragraph(60, y, 473, block);
            }
            stream.setStrokingColor(colorSecondary).line(60, y, 533, y).stroke();
            y -= 35;
            stream.cursorY(y);
            stream.restoreGraphicsState();
            return stream;
        };
    }
}
