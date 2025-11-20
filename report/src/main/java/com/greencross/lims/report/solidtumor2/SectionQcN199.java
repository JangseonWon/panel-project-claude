package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.report.func.WhiteSpace;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionQcN199 implements SectionQc {
    protected static final NumberFormat fmtNumber = NumberFormat.getInstance();
    private static final NumberFormat fmtPrecision = new DecimalFormat("0.0");
    private static final NumberFormat fmtPrecision2 = new DecimalFormat("0.00#");
    private final Painter<SolidTumorTemplate, SolidTumorDto> newPage;
    public SectionQcN199(Painter<SolidTumorTemplate, SolidTumorDto> newPage) {
        this.newPage = newPage;
    }
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        float y = stream.cursorY();
        if(y < 180) stream = newPage.paint(stream, template, dto);
        stream.saveGraphicsState();
        y = stream.cursorY() - 5;

        var resource = template.resource();
        stream.font(template.resource().fontDefault());
        stream.setNonStrokingColor(resource.colorPrimary())
                .addRect(60,y,473, 20)
                .fill();
        stream.paragraph(63, y+7, 115, new TextBlock(resource.styleHeader2(), template.lblQcSummary()));
        y -= 20;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        stream = summary(stream, template, dto);
        stream.cursorY(stream.cursorY()-20);
        return stream;
    }
    private PDPageContentStreamPageAccessible summary(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        if(dto.qc().snvQc()==null || dto.qc().snvQc().isEmpty()) throw new RuntimeException("QC.snvqc is empty");
        if(dto.qc().cnvQc()==null || dto.qc().cnvQc().isEmpty()) throw new RuntimeException("QC.cnvqc is empty");
        if(dto.qc().msiQc()==null || dto.qc().msiQc().isEmpty()) throw new RuntimeException("QC.msiqc is empty");
        if(dto.qc().rnaQc()==null || dto.qc().rnaQc().isEmpty()) throw new RuntimeException("QC.rnaqc is empty");
        stream.saveGraphicsState();
        var resource = template.resource();
        float y = stream.cursorY();
        stream.setNonStrokingColor(resource.colorPrimary()).addRect(60, y,473, 20).fill()
                .setStrokingColor(resource.colorSecondary()).setLineWidth(0.35f).line(60, y+20.1f, 533, y+20.1f).stroke();
        y += 10;
        stream.paragraph(115, y, 110, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), "SNV & TMB"));
        stream.paragraph(225, y, 110, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), "CNV"));
        stream.paragraph(322, y, 84, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), "MSI"));
        stream.paragraph(406, y, 84, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), "RNA"));
        stream.paragraph(490.5f, y, 85, CENTER, MIDDLE, new TextBlock(resource.styleHeader(), "Tumor purity"));
        y -= 21;
        var result = dto.qc();
        y = Math.min(y, stream.paragraph(115, y, 110, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), dto.qc().snvQc())));
        y = Math.min(y, stream.paragraph(225, y,  110, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), dto.qc().cnvQc())));
        y = Math.min(y, stream.paragraph(322, y, 84, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), dto.qc().msiQc())));
        y = Math.min(y, stream.paragraph(406, y, 84, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), dto.qc().rnaQc())));
        y = Math.min(y, stream.paragraph(490.5f, y, 85, CENTER, WhiteSpace.BREAK_ALL, new TextBlock(resource.styleValueTable(), fmtNumber.format(dto.qc().purity()) + "%")));

        y -= 8;
        stream.setStrokingColor(resource.colorSecondary()).line(60, y, 533, y).stroke();
        if(dto.method().qcInfo()!=null && !dto.method().qcInfo().isEmpty()) {
            y -= 10;
            y = stream.paragraph(63, y, 470, new TextBlock(resource.styleValueTable(), dto.method().qcInfo()));
        }
        if(result.interpretation()!=null && !result.interpretation().isEmpty()) {
            y -= 15;
            String[] paragraphs = result.interpretation().split("\n", -1);
            for (String p : paragraphs) {
                TextBlock block = new TextBlock(resource.styleText(), p + "\n\n");
                float height2 = stream.height(473, block);
                if (y - height2 < 140) {
                    stream.restoreGraphicsState();
                    stream = newPage.paint(stream, template, dto);
                    y = stream.cursorY() - 5;
                    stream.setNonStrokingColor(resource.colorPrimary()).addRect(60, y, 473, 20).fill();
                    stream.paragraph(63, y + 7, 115, new TextBlock(resource.styleHeader(), template.lblQcSummary()));
                    y -= 20;
                    stream.saveGraphicsState();
                }
                y = stream.paragraph(60, y, 473, block);
            }
            stream.setStrokingColor(resource.colorSecondary()).line(60, y, 533, y).stroke();
        }
        y -= 5;
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
    @Override
    public PDPageContentStreamPageAccessible qc(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        stream.saveGraphicsState();
        var resource = template.resource();
        float y = stream.cursorY();

        stream.saveGraphicsState();
        y -= 30;
        stream.paragraph(60, y, 120, new TextBlock(resource.styleHeader3(), template.lblTestQc()));
        y -= 5;
        TextStyle styleHeader = resource.styleHeaderTitle().clone().paragraph(false).justify(false);
        stream.setNonStrokingColor(resource.colorGray()).addRect(60, y, 473, -15).fill()
                .setLineWidth(1).setStrokingColor(resource.colorSecondary())
                .line(60, y, 533, y).stroke();
        y -= 15; stream.paragraph(70, y+4, 200, new TextBlock(styleHeader, "SNV & TMB"));
        stream.paragraph(310, y+4, 200, new TextBlock(styleHeader, "MSI"));
        stream.setNonStrokingColor(resource.colorGray())
                .addRect(60, y, 120, -60)
                .addRect(240, y, 60, -60)
                .addRect(300, y, 120, -60)
                .addRect(480, y, 53, -60)
                .addRect(60, y-15, 473, -15).fill()
                .line(60, y, 533, y).stroke();
        y-= 15;
        stream.paragraph(70, y+4, 100, new TextBlock(resource.styleText(), "Percent (PCT) Exon 100X"));
        stream.paragraph(230, y+4, 40, RIGHT, new TextBlock(resource.styleText(), fmtNumber.format(dto.qc().pctExonOver100X())));
        stream.paragraph(250, y+4, 40, new TextBlock(resource.styleText(), ">=90"));
        stream.paragraph(310, y+4, 100, new TextBlock(resource.styleText(), "Usable MSI Sites"));
        stream.paragraph(470, y+4, 40, RIGHT, new TextBlock(resource.styleText(), fmtNumber.format(dto.qc().usableMsi())));
        stream.paragraph(486, y+4, 40, new TextBlock(resource.styleText(), ">=40"));

        y-= 15; stream.setLineWidth(1).setStrokingColor(resource.colorSecondary())
                .line(60, y+15, 533, y+15)
                .line(60, y, 533, y).stroke();
        stream.paragraph(70, y+4, 200, new TextBlock(styleHeader, "CNV"));
        stream.paragraph(310, y+4, 200, new TextBlock(styleHeader, "RNA"));
        y-= 15; stream.setLineWidth(0.5f).setStrokingColor(Color.WHITE)
                .line(60, y, 180, y)
                .line(240, y, 300, y)
                .line(300, y, 420, y)
                .line(480, y, 533, y).stroke()
                .setStrokingColor(resource.colorGray())
                .line(180, y, 240, y)
                .line(420, y, 480, y).stroke();
        stream.paragraph(70, y+4, 100, new TextBlock(resource.styleText(), "MAD"));
        stream.paragraph(230, y+4, 40, RIGHT, new TextBlock(resource.styleText(), fmtPrecision2.format(dto.qc().mad())));
        stream.paragraph(250, y+4, 40, new TextBlock(resource.styleText(), "<=0.134"));
        stream.paragraph(310, y+4, 100, new TextBlock(resource.styleText(), "Total on target reads"));
        stream.paragraph(470, y+4, 40, RIGHT, new TextBlock(resource.styleText(), fmtNumber.format(dto.qc().onTargetReads())));
        stream.paragraph(486, y+4, 40, new TextBlock(resource.styleText(), ">=9,000,000"));
        y-= 15;
        stream.paragraph(70, y+4, 100, new TextBlock(resource.styleText(), "Median bin count"));
        stream.paragraph(230, y+4, 40, RIGHT, new TextBlock(resource.styleText(), fmtPrecision.format(dto.qc().mbc())));
        stream.paragraph(250, y+4, 40, new TextBlock(resource.styleText(), ">=1.0"));
        stream.paragraph(310, y+4, 120, new TextBlock(resource.styleText(), "Median CV for genes with > 500x"));
        stream.paragraph(470, y+4, 40, RIGHT, new TextBlock(resource.styleText(), fmtPrecision2.format(dto.qc().medianCvOver500X())));
        stream.paragraph(486, y+4, 40, new TextBlock(resource.styleText(), "<=0.93"));
        stream.setLineWidth(1).setStrokingColor(resource.colorSecondary())
                .line(60, y, 533, y)
                .line(300, y, 300, y+75).stroke();
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
}
