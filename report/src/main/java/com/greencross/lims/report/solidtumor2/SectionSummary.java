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

@SuppressWarnings("ReassignedVariable")
public class SectionSummary implements Painter<SolidTumorTemplate, SolidTumorDto> {
    private static final NumberFormat fmtNumber = NumberFormat.getInstance();
    private static final NumberFormat fmtPrecision = new DecimalFormat("0.0");
    private final Painter<SolidTumorTemplate, SolidTumorDto> newPage;
    public SectionSummary(Painter<SolidTumorTemplate, SolidTumorDto> newPage) {
        this.newPage = newPage;
    }

    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        stream.saveGraphicsState();
        var resource = template.resource();
        stream.font(template.resource().fontDefault());
        float y = stream.cursorY();
        y -= 5;
        stream.setNonStrokingColor(resource.colorPrimary())
                .addRect(60,y,473, 20)
                .fill();
        stream.paragraph(63, y+7, 115, new TextBlock(resource.styleHeader2(), template.lblSummary()));
        y -= 20;
        stream.cursorY(y);
        stream.restoreGraphicsState();

        stream = genomicAlterations(stream, template, dto);
        stream = immunotherapyMarkers(stream, template, dto);
        stream.cursorY(stream.cursorY()-20);
        return stream;
    }
    private float headerGenomicAlterations(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template) throws IOException {
        var resource = template.resource();
        float y = stream.cursorY() - 5;
        stream.setLineWidth(0.35f)
                .setStrokingColor(resource.colorSecondary())
                .line(60, y, 533, y).stroke();
        stream.paragraph(63, y+3, 115, new TextBlock(resource.styleValue().clone().fontSize(9), "Genomic Alterations"));
        y -= 23;
        stream.setNonStrokingColor(resource.colorPrimary()).addRect(60, y,473, 20).fill();
        y += 10;
        stream.paragraph(75, y, 30, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[0]));
        stream.paragraph(125, y, 70, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[1]));
        stream.paragraph(202.5f, y, 85, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[2]));
        stream.paragraph(289, y, 88, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[3]));
        stream.paragraph(363, y, 60, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[4]));
        stream.paragraph(463, y, 140, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), template.lblVariantTable()[6]));
        return y;
    }
    private PDPageContentStreamPageAccessible genomicAlterations(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        stream.saveGraphicsState();
        var resource = template.resource();
        float y = headerGenomicAlterations(stream, template);
        var variants = Arrays.stream(dto.variants()).filter(c->c.tier().ordinal() < SolidTumorDto.Tier.Tier3.ordinal()).sorted(Comparator.comparing(SolidTumorDto.Variant::tier)).toArray(SolidTumorDto.Variant[]::new);
        if(variants.length > 0) for (SolidTumorDto.Variant variant : variants) {
            var tier = variant.tier();
            if (y < 140) {
                stream.restoreGraphicsState();
                stream = newPage.paint(stream, template, dto);
                y = headerGenomicAlterations(stream, template);
                stream.saveGraphicsState();
            }
            y -= 21;
            var sy = y;
            sy = Math.min(sy, stream.paragraph(75, y, 30, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), fmtNumber.format(tier.ordinal() + 1))));
            sy = Math.min(sy, stream.paragraph(125, y, 70, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), variant.gene())));
            sy = Math.min(sy, stream.paragraph(202.5f, y, 85, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), variant.hgvsc())));
            sy = Math.min(sy, stream.paragraph(289, y, 100, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), variant.hgvsp())));
            Object vaf = variant.vaf();
            if (vaf instanceof Double) sy = Math.min(sy, stream.paragraph(373, y, 60, RIGHT, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), fmtPrecision.format(vaf))));
            else if (vaf != null) sy = Math.min(sy, stream.paragraph(363, y, 60, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), String.valueOf(vaf))));
            else sy = Math.min(sy, stream.paragraph(363, y, 60, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), "-")));
            sy = Math.min(sy, stream.paragraph(463, y, 140, CENTER, WhiteSpace.BREAK_SPACES, new TextBlock(resource.styleValueTable().clone().color(resource.colorRed()), variant.significance())));
            y = sy;
        } else {
            y -= 21;
            stream.paragraph(295, y, 500, CENTER, new TextBlock(resource.styleValueTable(), template.lblNoVariant()));
        }
        y -= 8;
        stream.line(60, y, 533, y).stroke();
        y -= 10;
        y = stream.paragraph(63, y, 470, new TextBlock(resource.styleValueTable(), template.lblReferT3NextPage()));
        y -= 20;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
    private PDPageContentStreamPageAccessible immunotherapyMarkers(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        float y = stream.cursorY();
        if(y - 100 < 140) {
            stream = newPage.paint(stream, template, dto);
            y = stream.cursorY() - 5;
        }

        stream.saveGraphicsState();
        var resource = template.resource();
        y -= 10;
        stream.setLineWidth(0.35f).setStrokingColor(resource.colorSecondary()).line(60, y, 533, y).stroke();
        stream.paragraph(63, y+3, 115, new TextBlock(resource.styleValue().clone().fontSize(9), "Immunotherapy Markers"));
        y -= 23;
        stream.setNonStrokingColor(resource.colorPrimary()).addRect(60, y,473, 20).fill();
        y += 10;
        stream.paragraph(178.25f, y, 236.5f, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), "Tumor Mutation Burden (TMB)"));
        stream.paragraph(414.75f, y, 236.5f, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), "Microsatellite Instability Status (MSI)"));
        y -= 21;
        y = Math.min(y, stream.paragraph(178.25f, y, 236.5f, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), dto.hypermutability().tmb())));
        y = Math.min(y, stream.paragraph(414.75f, y, 236.5f, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), dto.hypermutability().msi() + "(" + dto.hypermutability().msiScore() + ")")));

        y -= 8;
        stream.line(60, y, 533, y).stroke();
        if(dto.method().immunotherapyInfo()!=null && !dto.method().immunotherapyInfo().isEmpty()) {
            y -= 10;
            y = stream.paragraph(63, y, 470, new TextBlock(resource.styleValueTable(), dto.method().immunotherapyInfo()));
        }
        y -= 20;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }

}
