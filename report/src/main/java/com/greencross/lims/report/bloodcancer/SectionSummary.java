package com.greencross.lims.report.bloodcancer;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionSummary implements Painter<BloodCancerTemplate, BloodCancerDto> {
    private static final NumberFormat fmtNumber = NumberFormat.getInstance();
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, BloodCancerTemplate template, BloodCancerDto dto) throws IOException {
        BloodCancerResource resource = template.resource();
        Color colorPrimary = resource.colorPrimary();
        Color colorSecondary = resource.colorSecondary();
        Color colorGray = resource.colorGray();
        TextStyle styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
        TextStyle styleHeader = styleHeaderTitle.clone().color(resource.colorTextWithPrimary()).fontSize(9).justify(false);
        TextStyle styleHeader2 = new TextStyle().color(resource.colorTextWithPrimary()).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(13);
        TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).paragraph(false);
        TextStyle styleValueTable = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(false);
        stream.saveGraphicsState();
        float y = stream.cursorY();
        y -= 30;
        stream.setNonStrokingColor(colorGray)
                .addRect(60, y,65, 20).fill()
                .setLineWidth(0.25f).setStrokingColor(colorSecondary)
                .line(60, y+0.1f, 533, y+0.1f)
                .line(60, y+20, 533, y+20)
                .stroke();
        stream.paragraph(62, y+8, 60, new TextBlock(styleHeaderTitle, template.lblCancerType()));
        y = stream.paragraph(130, y+8, 300, new TextBlock(styleValue, dto.cancerType()));
        y -= 30;

        stream.setNonStrokingColor(colorPrimary).addRect(60, y-6,473, 20).fill();
        stream.paragraph(65, y, 120, new TextBlock(styleHeader2, template.lblSummary()));
        y -= 25;

        stream.addRect(60, y,473, 20).fill()
                .setLineWidth(0.25f).setStrokingColor(colorSecondary)
                .line(60, y+18.1f, 533, y+18.1f).stroke()
                .setNonStrokingColor(colorPrimary);
        stream.paragraph(200.5f, y+10, 200, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblTier(BloodCancerDto.Tier.Tier1)));
        stream.paragraph(333.5f, y+10, 200, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblTier(BloodCancerDto.Tier.Tier2)));
        y = stream.paragraph(466.5f, y+10, 200, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblTier(BloodCancerDto.Tier.Tier3)));

        BloodCancerDto.Result t1 = Arrays.stream(dto.results()).filter(r->r.tier()==BloodCancerDto.Tier.Tier1).findFirst().orElse(new BloodCancerDto.Result());
        BloodCancerDto.Result t2 = Arrays.stream(dto.results()).filter(r->r.tier()==BloodCancerDto.Tier.Tier2).findFirst().orElse(new BloodCancerDto.Result());
        BloodCancerDto.Result t3 = Arrays.stream(dto.results()).filter(r->r.tier()==BloodCancerDto.Tier.Tier3).findFirst().orElse(new BloodCancerDto.Result());
        y -= 16;
        stream.paragraph(90, y, 60, CENTER, MIDDLE, new TextBlock(styleValueTable, template.lblVariant()));
        stream.paragraph(200.5f, y, 133, CENTER, MIDDLE, new TextBlock(styleValueTable, fmtNumber.format(t1.variants()!=null?t1.variants().length:0)));
        stream.paragraph(333.5f, y, 133, CENTER, MIDDLE, new TextBlock(styleValueTable, fmtNumber.format(t2.variants()!=null?t2.variants().length:0)));
        stream.paragraph(466.5f, y, 133, CENTER, MIDDLE, new TextBlock(styleValueTable, fmtNumber.format(t3.variants()!=null?t3.variants().length:0)));

        y -= 20;
        String g1 = t1.variants()!=null?Arrays.stream(t1.variants()).map(BloodCancerDto.Variant::gene).distinct().collect(Collectors.joining(", ")):"-";
        String g2 = t2.variants()!=null?Arrays.stream(t2.variants()).map(BloodCancerDto.Variant::gene).distinct().collect(Collectors.joining(", ")):"-";
        String g3 = t3.variants()!=null?Arrays.stream(t3.variants()).map(BloodCancerDto.Variant::gene).distinct().collect(Collectors.joining(", ")):"-";
        stream.paragraph(90, y, 60, CENTER, MIDDLE, new TextBlock(styleValueTable, template.lblGene()));
        float y1 = stream.paragraph(200.5f, y, 133, CENTER, MIDDLE, new TextBlock(styleValueTable, g1.trim().isEmpty()?"-":g1));
        float y2 = stream.paragraph(333.5f, y, 133, CENTER, MIDDLE, new TextBlock(styleValueTable, g2.trim().isEmpty()?"-":g2));
        float y3 = stream.paragraph(466.5f, y, 133, CENTER, MIDDLE, new TextBlock(styleValueTable, g3.trim().isEmpty()?"-":g3));
        y = Math.min(Math.min(y1, y2), y3);
        y -= 8;
        stream.setLineWidth(1).line(60, y, 533, y).stroke();
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
}
