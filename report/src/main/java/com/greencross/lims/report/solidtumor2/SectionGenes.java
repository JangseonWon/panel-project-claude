package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.io.IOException;
import java.util.List;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionGenes implements Painter<SolidTumorTemplate, SolidTumorDto> {
    private final Painter<SolidTumorTemplate, SolidTumorDto> newPage;
    public SectionGenes(Painter<SolidTumorTemplate, SolidTumorDto> newPage) {
        this.newPage = newPage;
    }
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        float y = stream.cursorY();
        stream.saveGraphicsState();
        var resource = template.resource();
        stream.paragraph(60, y, 120, new TextBlock(resource.styleHeader3(), template.lblTestGeneInfo()));
        y -= 20;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        for(var gs: dto.method().geneSets()) stream = genes(stream, template, dto, gs);
        return stream;
    }
    private PDPageContentStreamPageAccessible genes(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto, SolidTumorDto.GeneSet geneset) throws IOException {
        List<String> genes = geneset.genes();
        if(genes == null || genes.isEmpty()) return stream;
        float y = stream.cursorY();
        var resource = template.resource();
        stream.saveGraphicsState();
        y -= 5;
        stream.setNonStrokingColor(resource.colorGray()).addRect(60, y, 473, 20).fill();
        stream.paragraph(63, y+10, 470, MIDDLE, new TextBlock(resource.styleHeader3().clone().fontSize(9), geneset.label()));
        stream.setLineWidth(1).setStrokingColor(resource.colorSecondary())
                .line(60, y, 533, y)
                .line(60, y+20, 533, y+20).stroke();
        TextStyle styleHeader = resource.styleHeaderTitle().clone().fontSize(6).paragraph(false).justify(false);
        stream.setLineWidth(0.5f);
        y-= 10;
        float x = 0;
        for(int i = 0; i < genes.size(); ++i) {
            if(i%10 == 0) {
                x = 60;
                if(i > 0) {
                    y -= 20;
                    if(y < 120) {
                        stream.restoreGraphicsState();
                        stream = newPage.paint(stream, template, dto);
                        stream.saveGraphicsState();
                        y = stream.cursorY() - 5;
                        stream.setNonStrokingColor(resource.colorGray()).addRect(60, y, 473, 20).fill();
                        stream.paragraph(63, y+10, 470, MIDDLE, new TextBlock(resource.styleHeader3().clone().fontSize(9), geneset.label()));
                        stream.setLineWidth(1).setStrokingColor(resource.colorSecondary())
                                .line(60, y, 533, y)
                                .line(60, y+20, 533, y+20).stroke();
                        stream.setLineWidth(0.5f);
                        y -= 10;
                    } else stream.setStrokingColor(resource.colorGray()).line(60, y + 10, 533, y + 10).stroke();
                }
            }
            var gene = genes.get(i);
            stream.paragraph(x+47.3f/2, y, 47.3f, CENTER, MIDDLE, new TextBlock(styleHeader, gene));
            x += 47.3f;
        }
        y -= 20;
        if(geneset.label().startsWith("Fusion") && dto.method().fusionInfo() != null) {
            y = stream.paragraph(63, y, 470, new TextBlock(resource.styleValueTable(), dto.method().fusionInfo()));
            y -= 10;
        }
        stream.setLineWidth(1).setStrokingColor(resource.colorSecondary()).line(60, y, 533, y).stroke();
        y -= 10;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
}
