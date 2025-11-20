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
import java.util.stream.Stream;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class HereditaryON040 extends HereditaryEnUsPageBuilder<HereditaryTemplateEnUs> {
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> barcode = new SectionBarcode<>();
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> ldt;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> sign;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> footer;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> page;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> header = new SectionHeader();
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> cover = new SectionCover(405);
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> empty = (s, t, d) -> newPage(s);

    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> summary = new SectionSummary();
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> result = new SectionResult();
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> interpretation;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> details = new SectionDetailsN040();
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> disease;
    private final Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> test = new SectionTestInfo() {
        PDPageContentStreamPageAccessible genes(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
            stream = super.genes(stream, template, dto);
            stream = disease(stream, template);
            return empty.and(template()).and((s, t, d)->{
                s.saveGraphicsState();
                float y = s.cursorY();
                y-= 25;
                var resource = t.resource();
                var styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(13).paragraph(false);
                s.paragraph(25, y, 400, new TextBlock(styleTitle, t.lblTest()));
                s.restoreGraphicsState();
                return s.cursorY(y);
            }).paint(stream, template, dto);
        }
        private PDPageContentStreamPageAccessible disease(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template) throws IOException {
            stream.saveGraphicsState();
            float y = stream.cursorY();
            var resource = template.resource();
            y-= 10;
            var styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(12).paragraph(false);
            float ty = y;
            Util.gradient(stream, s->s.rect(25, ty, 545, 20), resource.gradient(), 25, 545);
            stream.paragraph(35, y-10, 400, MIDDLE, new TextBlock(styleHeader, "Cancer/Tumor List"));
            y-= 40;
            var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(false);
            int rowMax = (int) Math.ceil(template.cancerTypes().length / 3.0)-1;
            float x = 45;
            int row = 0;
            var styleBullet = styleText.clone().color(resource.colorPrimary()).fontSize(4);
            for(var type: template.cancerTypes()) {
                stream.paragraph(x, y+2 - row * 13, 520, new TextBlock(styleBullet, bullet));
                stream.paragraph(x+8, y - row * 13, 182, new TextBlock(styleText, type.title()));
                if(row++ >= rowMax) {
                    row = 0;
                    x += 181.67f;
                }
            }
            y -= 10+rowMax*13;
            float ty2 = y - 5;
            Util.gradient(stream, s->s.rect(25, ty2, 545, 0.2f), resource.gradient(), 25, 545);
            stream.restoreGraphicsState();
            return stream.cursorY(y);
        }
    };
    public HereditaryON040(HereditaryTemplateEnUsON040 template, HereditaryEnUsDto dto,
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

    private final class SectionDetailsN040 implements Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> {
        private final int[] COLUMN_WIDTHS = new int[] {120, 305, 120};
        @Override
        public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto) throws IOException {
            stream.saveGraphicsState();
            var resource = template.resource();
            stream = header(stream, template);
            float y = stream.cursorY();
            var styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(false);
            var types = Arrays.stream(template.cancerTypes()).distinct().collect(Collectors.toList());
            for(HereditaryTemplateEnUs.CancerType type: types) {

                Set<HereditaryTemplateEnUs.Gene> detected =  new HashSet<>();
                if(dto.report()!=null && dto.report().variants()!=null) for(HereditaryEnUsDto.Variant variant: dto.report().variants())
                    for(HereditaryTemplateEnUs.Gene gene: type.genes()) if(gene.symbol().equals(variant.gene()) && !"VUS".equalsIgnoreCase(variant.clazz()))
                        detected.add(gene);
                Set<HereditaryTemplateEnUs.Gene> vus =  new HashSet<>();
                if(dto.report()!=null && dto.report().variants()!=null) for(HereditaryEnUsDto.Variant variant: dto.report().variants())
                    for(HereditaryTemplateEnUs.Gene gene: type.genes()) if(gene.symbol().equals(variant.gene()) && !detected.contains(gene))
                        vus.add(gene);
                stream = gene(stream.cursorY(y), template, dto, styleText, type, detected, vus);
                float ty2 = stream.cursorY();
                Util.gradient(stream, s->s.rect(25, ty2, 545, 0.2f), resource.gradient(), 25, 545);
                y = ty2;
            }
            stream.restoreGraphicsState();
            return stream.cursorY(y);
        }
        private PDPageContentStreamPageAccessible header(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template) throws IOException {
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
            stream.setStrokingColor(Color.decode("0x898989")).setLineWidth(0.5f);
            int sx0 = sx; Util.gradient(stream, s->s.rect(sx0, ty, COLUMN_WIDTHS[0], 20), resource.gradient(), sx0, COLUMN_WIDTHS[0]);
            stream.paragraph(sx + COLUMN_WIDTHS[0]/2f, y-10, COLUMN_WIDTHS[0], CENTER, MIDDLE, new TextBlock(styleTitleSmall, "Cancer"));	sx += COLUMN_WIDTHS[0];
            stream.setStrokingColor(resource.colorGray()).line(sx, y, sx, y-20).stroke();
            int sx1 = sx; Util.gradient(stream, s->s.rect(sx1, ty,  COLUMN_WIDTHS[1], 20), resource.gradient(), sx1, COLUMN_WIDTHS[1]);
            stream.paragraph(sx + COLUMN_WIDTHS[1]/2f, y-10, COLUMN_WIDTHS[1], CENTER, MIDDLE, new TextBlock(styleTitleSmall, "Gene")); sx += COLUMN_WIDTHS[1];
            stream.setStrokingColor(resource.colorGray()).line(sx, y, sx, y-20).stroke();
            int sx2 = sx; Util.gradient(stream, s->s.rect(sx2, ty,  COLUMN_WIDTHS[2], 20), resource.gradient(), sx2, COLUMN_WIDTHS[2]);
            stream.paragraph(sx + COLUMN_WIDTHS[2]/2f, y-10, COLUMN_WIDTHS[2], CENTER, MIDDLE, new TextBlock(styleTitleSmall, "Results"));
            y-= 20;
            stream.restoreGraphicsState();
            return stream.cursorY(y);
        }
        private PDPageContentStreamPageAccessible gene(PDPageContentStreamPageAccessible stream, HereditaryTemplateEnUs template, HereditaryEnUsDto dto, TextStyle _styleText, HereditaryTemplateEnUs.CancerType type, Set<HereditaryTemplateEnUs.Gene> detected, Set<HereditaryTemplateEnUs.Gene> vus) throws IOException {
            float y = stream.cursorY();
            y -= 11;
            Color warn = Color.decode("#AD1742");
            Color warn2 = Color.decode("#0D3663");
            TextStyle styleText = _styleText.clone().fontSize(9);
            TextStyle styleWarn = styleText.clone().color(warn);
            TextStyle styleWarn2 = styleText.clone().color(warn2);
            TextBlock[] genes = Arrays.stream(type.genes())
                    .flatMap(gene -> {
                        TextStyle style = styleText;
                        if(detected.contains(gene)) style = styleWarn;
                        else if(vus.contains(gene)) style = styleWarn2;
                        return Stream.of(
                            new TextBlock(styleText, gene==type.genes()[0]?"":", "),
                            new TextBlock(style, gene.symbol().toUpperCase()));
                    }).toArray(TextBlock[]::new);
            float height = Math.max(stream.height(COLUMN_WIDTHS[0]-40, new TextBlock(_styleText, type.title())), stream.height(COLUMN_WIDTHS[1]-60, genes));
            if(y - height - 13 < 100) {
                stream = empty.and(template()).and((s, t, d)->header(s, t)).paint(stream, template, dto);
                y = stream.cursorY();
                y -= 11;
            }
            stream.saveGraphicsState();
            float sx = 25;
            stream.paragraph(sx+10, y - height/2, COLUMN_WIDTHS[0]-40, MIDDLE, new TextBlock(_styleText, type.title()));	sx += COLUMN_WIDTHS[0];
            stream.paragraph(sx+10, y - height/2, COLUMN_WIDTHS[1]-60, MIDDLE, genes);	                                sx += COLUMN_WIDTHS[1];
            String color = "#898989";
            String value = "Not Detected";
            if(!detected.isEmpty()) {
                color = "#AD1742";
                value = "Detected";
            } else if(!vus.isEmpty()) {
                color = "#0D3663";
                value = "VUS";
            }
            stream.paragraph(sx + COLUMN_WIDTHS[2]/2f, y - height/2, COLUMN_WIDTHS[2], CENTER, MIDDLE, new TextBlock(styleText.clone().fontSize(12).color(Color.decode(color)), value));
            y -= height+11;
            stream.restoreGraphicsState();
            return stream.cursorY(y);
        }
    }
}
