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
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionDrugs implements Painter<BloodCancerTemplate, BloodCancerDto> {
    private static final NumberFormat fmtNumber = NumberFormat.getInstance();
    private static final NumberFormat fmtPrecision = new DecimalFormat("0");
    private final static float[] COLUMN_WIDTH = new float[] {
            80, 150, 100, 143
    };private final Painter<BloodCancerTemplate, BloodCancerDto> newPage;
    public SectionDrugs(Painter<BloodCancerTemplate, BloodCancerDto> newPage) {
        this.newPage = newPage;
        fmtPrecision.setMaximumFractionDigits(2);
    }
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, BloodCancerTemplate template, BloodCancerDto dto) throws IOException {
        stream.cursorY(stream.cursorY()-30);
        BloodCancerResource resource = template.resource();
        TextStyle styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).justify(true).paragraph(true);
        BloodCancerDto.DrugPhenotype pheno1 = Arrays.stream(dto.drugPhenotypes()).filter(g->"NUDT15".equalsIgnoreCase(g.gene())).findFirst().orElse(null);
        BloodCancerDto.DrugPhenotype pheno2 = Arrays.stream(dto.drugPhenotypes()).filter(g->"TPMT".equalsIgnoreCase(g.gene())).findFirst().orElse(null);
        stream = details(pheno1).paint(stream, template, dto);
        stream = details(pheno2).paint(stream, template, dto);
        stream.saveGraphicsState();
        float y = stream.cursorY();
        stream.paragraph(60, y, 400, new TextBlock(styleText, "* Reference: CPIC (Clinical Pharmacogenetics Implementation Consortium) (https://cpicpgx.org/)"));
        y -= 35;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
    private Painter<BloodCancerTemplate, BloodCancerDto> details(BloodCancerDto.DrugPhenotype result) {
        return (stream, template, dto)->{
            BloodCancerResource resource = template.resource();
            Color colorPrimary = resource.colorPrimary();
            Color colorSecondary = resource.colorSecondary();
            TextStyle styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
            TextStyle styleHeader = styleHeaderTitle.clone().color(resource.colorTextWithPrimary()).fontSize(9).justify(false);
            TextStyle styleValueTable = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(false);
            TextStyle styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).justify(true).paragraph(true);
            float y = stream.cursorY();
            float height = 104;
            if(y - height < 120) {
                stream = newPage.paint(stream, template, dto);
                y = stream.cursorY();
                y -= 30;
            } else y -= 20;
            stream.saveGraphicsState();
            stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill()
                    .setLineWidth(0.25f).setStrokingColor(colorSecondary)
                    .line(60, y+20.1f, 533, y+20.1f).stroke();

            stream.paragraph(65, y+6, 237, new TextBlock(styleHeader, ((AllTemplate) template).lblPharmacogeneResult() + " : " + result.gene()));
            y -= 20;
            stream.addRect(60, y,473, 20).fill();
            y += 10;
            float x = 60;
            stream.paragraph(x + COLUMN_WIDTH[0]/2, y, COLUMN_WIDTH[0], CENTER, MIDDLE, new TextBlock(styleHeader, "Diplotype"));               x += COLUMN_WIDTH[0];
            stream.paragraph(x + COLUMN_WIDTH[1]/2, y, COLUMN_WIDTH[1], CENTER, MIDDLE, new TextBlock(styleHeader, "Allele function status"));  x += COLUMN_WIDTH[1];
            stream.paragraph(x + COLUMN_WIDTH[2]/2, y, COLUMN_WIDTH[2], CENTER, MIDDLE, new TextBlock(styleHeader, "Phenotype"));               x += COLUMN_WIDTH[2];
            stream.paragraph(x + COLUMN_WIDTH[3]/2, y, COLUMN_WIDTH[3], CENTER, MIDDLE, new TextBlock(styleHeader, "EHR Priority Result"));

            y -= 20;
            x = 60;
            stream.paragraph(x + COLUMN_WIDTH[0]/2, y, COLUMN_WIDTH[0], CENTER, MIDDLE, new TextBlock(styleValueTable, result.diplotype()));    x += COLUMN_WIDTH[0];
            stream.paragraph(x + COLUMN_WIDTH[1]/2, y, COLUMN_WIDTH[1], CENTER, MIDDLE, new TextBlock(styleValueTable, result.alleleStatus())); x += COLUMN_WIDTH[1];
            stream.paragraph(x + COLUMN_WIDTH[2]/2, y, COLUMN_WIDTH[2], CENTER, MIDDLE, new TextBlock(styleValueTable,result.phenotype()));     x += COLUMN_WIDTH[2];
            stream.paragraph(x + COLUMN_WIDTH[3]/2, y, COLUMN_WIDTH[3], CENTER, MIDDLE, new TextBlock(styleValueTable, result.result()));
            y -= 8;
            stream.line(60, y, 533, y).stroke();
            y -= 15;
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
            y -= 10;
            stream.cursorY(y);
            stream.restoreGraphicsState();
            return stream;
        };
    }
}