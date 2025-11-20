package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.report.func.WhiteSpace;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionVariants implements Painter<SolidTumorTemplate, SolidTumorDto> {
    private static final NumberFormat fmtNumber = NumberFormat.getInstance();
    private static final NumberFormat fmtPrecision = new DecimalFormat("0.0");
    private final Painter<SolidTumorTemplate, SolidTumorDto> newPage;
    public SectionVariants(Painter<SolidTumorTemplate, SolidTumorDto> newPage) {
        this.newPage = newPage;
    }
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        stream = header(stream, template, dto);
        stream = details(stream, template, dto);
        return stream;
    }
    private PDPageContentStreamPageAccessible header(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        float y = stream.cursorY();
        if(y < 140) {
            stream = newPage.paint(stream, template, dto);
            y = stream.cursorY() - 5;
        }

        stream.saveGraphicsState();
        var resource = template.resource();
        stream.font(template.resource().fontDefault());
        y -= 5;
        stream.setNonStrokingColor(resource.colorPrimary()).addRect(60,y,473, 20).fill();
        stream.paragraph(63, y+7, 115, new TextBlock(resource.styleHeader2(), template.lblDetailsVariant()));
        y -= 20;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
    private float headerDetails(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template) throws IOException {
        float y = stream.cursorY();
        var resource = template.resource();
        stream.setNonStrokingColor(resource.colorPrimary()).addRect(60, y,473, 20).fill();
        y += 10;
        stream.paragraph(75, y, 30, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[0]));
        stream.paragraph(125, y, 70, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[1]));
        stream.paragraph(202.5f, y, 85, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[2]));
        stream.paragraph(289, y, 88, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[3]));
        stream.paragraph(383, y, 100, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[4]));
        stream.paragraph(483, y, 100, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[5]));
        return y;
    }
    private PDPageContentStreamPageAccessible details(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        float y = stream.cursorY();
        var resource = template.resource();
        if(y + 20 < 140) {
            stream = newPage.paint(stream, template, dto);
            y = stream.cursorY() - 5;
        }
        stream.saveGraphicsState();
        y = headerDetails(stream.cursorY(y), template);
        var variants = Arrays.stream(dto.variants()).sorted(Comparator.comparing(SolidTumorDto.Variant::tier)).toArray(SolidTumorDto.Variant[]::new);
        if(variants.length > 0) for(int i = 0; i < variants.length; ++i) {
            var tier = variants[i].tier();
            if(y < 140) {
                stream.restoreGraphicsState();
                stream = newPage.paint(stream, template, dto);
                y = stream.cursorY() - 5;
                y -= 23;
                y = headerDetails(stream.cursorY(y), template);
                stream.saveGraphicsState();
            }
            y -= 21;
            var variant = variants[i];
            var sy = y;
            sy = Math.min(sy, stream.paragraph(75, y, 30, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), fmtNumber.format(tier.ordinal()+1))));
            sy = Math.min(sy, stream.paragraph(125, y, 70, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), variant.gene())));
            sy = Math.min(sy, stream.paragraph(202.5f, y, 85, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), variant.hgvsc())));
            sy = Math.min(sy, stream.paragraph(289, y, 100, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), variant.hgvsp())));
            Object vaf = variant.vaf();
            if(vaf instanceof Double) sy = Math.min(sy, stream.paragraph(393, y, 100, RIGHT, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), fmtPrecision.format(vaf))));
            else if(vaf!=null) sy = Math.min(sy, stream.paragraph(383, y, 100, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), String.valueOf(vaf))));
            else sy = Math.min(sy, stream.paragraph(383, y, 100, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), "-")));

            var depth = variant.depth();
            if(vaf!=null) sy = Math.min(sy, stream.paragraph(493, y, 140, RIGHT, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), fmtPrecision.format(depth))));
            else sy = Math.min(sy, stream.paragraph(483, y, 140, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), "-")));
            y = sy;
        } else {
            y -= 21;
            stream.paragraph(295, y, 500, CENTER, new TextBlock(resource.styleValueTable(), template.lblNoVariant()));
        }
        y -= 8;
        stream.setStrokingColor(resource.colorSecondary()).setLineWidth(0.35f).line(60, y, 533, y).stroke();
        y -= 30;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }

}
