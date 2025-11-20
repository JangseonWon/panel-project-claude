package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.builder.Util;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class HereditaryON001 extends HereditaryEnUsPageBuilder<HereditaryTemplateEnUs> {
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> barcode = new SectionBarcode<>();
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> ldt;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> sign;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> footer;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> page;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> header = new SectionHeader();
//    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> logo = template().;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> cover = new SectionCover(375);
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> empty = (s, t, d) -> newPage(s);

    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> summary = new SectionSummary();
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> result = new SectionResult();
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> interpretation;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> details = new SectionDetailsN001();
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> disease;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> test = new SectionTestInfo();
    public HereditaryON001(HereditaryTemplateEnUsON001 template, HereditaryEnUsDto dto,
                           Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> sign,
                           Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> footer,
                           Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> page) {
        super(template, dto);
        this.sign = sign;
        this.footer = footer;
        this.page = page;
        interpretation = new SectionInterpretation(empty.and(template()));
        disease = new SectionDiseaseInfo(empty.and(template()));
        ldt = new SectionLDT<>(template.logoType(), 100);
    }

    @Override
    public Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> template() {
        return header.and(footer).and(sign);
    }

    @Override
    public Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> pages() {
        return cover.and((stream, template, dto) -> {
            stream.font(template.resource().fontDefault());
            return stream;
        }).and(barcode).and(footer).and(sign)
        .and(empty).and(template()).and(summary).and(result).and(interpretation)
        .and(empty).and(template()).and(details).and(disease)
        .and(empty).and(template()).and(test).and(ldt).and(page);
    }

    private final static class SectionDetailsN001 implements Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> {
        private final static int[] COLUMN_WIDTHS = new int[] {120, 305, 120};
        @Override
        public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
            stream.saveGraphicsState();
            float y = stream.cursorY();

            y-= 25;
            var resource = template.resource();
            var styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(13).paragraph(false);
            stream.paragraph(25, y, 500, new TextBlock(styleTitle, template.lblDisease()));
            y-= 10;
            var styleTitleSmall = new TextStyle().color(Color.WHITE).fonts(resource.fontHeaderSmall2(), resource.fontDefault()).fontSize(11).paragraph(false);
            float ty = y;
            int sx = 25;
            var genes = Arrays.stream(template.cancerTypes()).flatMap(type-> Arrays.stream(type.genes())).distinct().collect(Collectors.toList());
            stream.setStrokingColor(Color.decode("0x898989")).setLineWidth(0.5f);
            int sx0 = sx; Util.gradient(stream, s->s.rect(sx0, ty, COLUMN_WIDTHS[0], 20), resource.gradient(), sx0, COLUMN_WIDTHS[0]);
            stream.paragraph(sx + COLUMN_WIDTHS[0]/2f, y-10, COLUMN_WIDTHS[0], CENTER, MIDDLE, new TextBlock(styleTitleSmall, "Gene"));	sx += COLUMN_WIDTHS[0];
            stream.setStrokingColor(resource.colorGray()).line(sx, y, sx, y-20-genes.size()*34).stroke();
            int sx1 = sx; Util.gradient(stream, s->s.rect(sx1, ty,  COLUMN_WIDTHS[1], 20), resource.gradient(), sx1, COLUMN_WIDTHS[1]);
            stream.paragraph(sx + COLUMN_WIDTHS[1]/2f, y-10, COLUMN_WIDTHS[1], CENTER, MIDDLE, new TextBlock(styleTitleSmall, "Key Areas Where Hereditary Cancer occurs")); sx += COLUMN_WIDTHS[1];
            stream.setStrokingColor(resource.colorGray()).line(sx, y, sx, y-20-genes.size()*34).stroke();
            int sx2 = sx; Util.gradient(stream, s->s.rect(sx2, ty,  COLUMN_WIDTHS[2], 20), resource.gradient(), sx2, COLUMN_WIDTHS[2]);
            stream.paragraph(sx + COLUMN_WIDTHS[2]/2f, y-10, COLUMN_WIDTHS[2], CENTER, MIDDLE, new TextBlock(styleTitleSmall, "Results"));
            y-= 20;
            var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(false);
            Set<String> detected =  new HashSet<>();
            if(dto.report()!=null && dto.report().variants()!=null) for(HereditaryEnUsDto.Variant variant: dto.report().variants())
                if(!"VUS".equalsIgnoreCase(variant.clazz())) detected.add(variant.gene());
            Set<String> vus =  new HashSet<>();
            if(dto.report()!=null && dto.report().variants()!=null) for(HereditaryEnUsDto.Variant variant: dto.report().variants())
                if(!detected.contains(variant.gene())) vus.add(variant.gene());
            for(var gene: genes) {
                y -= 21;
                stream = gene(stream.cursorY(y), styleText, (HereditaryTemplateEnUsON001.GeneON001) gene, detected.contains(gene.symbol()), vus.contains(gene.symbol()));
                float ty2 = stream.cursorY();
                Util.gradient(stream, s->s.rect(25, ty2, 545, 0.2f), resource.gradient(), 25, 545);
                y = ty2;
            }
            stream.restoreGraphicsState();
            return stream.cursorY(y);
        }
        private PDPageContentStreamPageAccessible gene(PDPageContentStreamPageAccessible stream, TextStyle styleText, HereditaryTemplateEnUsON001.GeneON001 gene, boolean isPositive, boolean isVus) throws IOException {
            stream.saveGraphicsState();
            float y = stream.cursorY();
            float sx = 25;
            float sy = stream.paragraph(sx+10, y, COLUMN_WIDTHS[0], new TextBlock(styleText, gene.symbol().toUpperCase())); 			sx += COLUMN_WIDTHS[0];
            sy = Math.min(sy, stream.paragraph(sx+10, y, COLUMN_WIDTHS[1], new TextBlock(styleText, gene.disease().toUpperCase())));	sx += COLUMN_WIDTHS[1];
            String color = "#898989";
            String value = "Not Detected";
            if(isPositive) {
                color = "#AD1742";
                value = "Detected";
            } else if(isVus) {
                color = "#0D3663";
                value = "VUS";
            }
            sy = Math.min(sy, stream.paragraph(sx + COLUMN_WIDTHS[2]/2f, y, COLUMN_WIDTHS[2], CENTER, new TextBlock(styleText.clone().color(Color.decode(color)), value)));
            y = sy-13;
            stream.restoreGraphicsState();
            return stream.cursorY(y);
        }
    }
}
