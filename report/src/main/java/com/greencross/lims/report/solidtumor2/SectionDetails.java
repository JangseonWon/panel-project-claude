package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.report.func.WhiteSpace;

import java.io.IOException;
import java.util.Arrays;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.TOP;

public class SectionDetails implements Painter<SolidTumorTemplate, SolidTumorDto> {
    private final Painter<SolidTumorTemplate, SolidTumorDto> newPage;
    public SectionDetails(Painter<SolidTumorTemplate, SolidTumorDto> newPage) {
        this.newPage = newPage;
    }
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        if(Arrays.stream(dto.variants()).noneMatch(k -> k.tier() == SolidTumorDto.Tier.Tier1 || k.tier() == SolidTumorDto.Tier.Tier2)) return stream;
        stream = header(stream, template, dto);
        var variants = Arrays.stream(dto.variants()).filter(k->k.tier() == SolidTumorDto.Tier.Tier1 || k.tier() == SolidTumorDto.Tier.Tier2).toArray(SolidTumorDto.Variant[]::new);
        for(var variant: variants) stream = details(stream, template, dto, variant.tier(), variant);
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
        stream.paragraph(63, y+7, 115, new TextBlock(resource.styleHeader2(), template.lblDetailsResult()));
        y -= 10;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
    private PDPageContentStreamPageAccessible details(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto, SolidTumorDto.Tier tier, SolidTumorDto.Variant variant) throws IOException {
        float y = stream.cursorY();
        var resource = template.resource();
        var interpretation = new TextBlock(resource.styleText(), variant.interpretation());
        if(y - 50 - stream.height(473, interpretation) < 140) {
            stream = newPage.paint(stream, template, dto);
            y = stream.cursorY() - 5;
        }
        stream.saveGraphicsState();
        y -= 4;
        var ymax = y;
        ymax = Math.min(ymax, stream.paragraph(75, y, 30, CENTER, TOP, new TextBlock(resource.styleHeader4(), tier.name())));
        ymax = Math.min(ymax, stream.paragraph(125, y, 70, CENTER, TOP, new TextBlock(resource.styleHeader4(), variant.gene())));
        ymax = Math.min(ymax, stream.paragraph(202.5f, y, 85, CENTER, TOP, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleHeader4(), variant.hgvsc())));
        ymax = Math.min(ymax, stream.paragraph(319, y, 148, CENTER, TOP, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleHeader4(), variant.hgvsp())));
        ymax = Math.min(ymax, stream.paragraph(453, y, 160, CENTER, TOP, WhiteSpace.BREAK_SPACES, new TextBlock(resource.styleHeader4().clone().color(resource.colorRed()), variant.significance())));
        y = ymax;
        y -= 20;
        y = stream.paragraph(60, y, 473, interpretation);
        y -= 10;
        stream.setStrokingColor(resource.colorSecondary()).setLineWidth(0.35f).line(60, y, 533, y).stroke();
        y -= 20;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }

}
