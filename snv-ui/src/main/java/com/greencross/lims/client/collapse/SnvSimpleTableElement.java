package com.greencross.lims.client.collapse;

import com.google.gwt.core.client.Scheduler;
import com.greencross.lims.client.snv.ColumnSet;
import com.greencross.lims.dto.Sheet;
import com.greencross.lims.sheet.Data;
import com.greencross.lims.sheet.SpreadSheet;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;
import java.util.List;

import static org.jboss.elemento.Elements.div;

public class SnvSimpleTableElement extends HTMLElementBuilder<HTMLDivElement, SnvSimpleTableElement> {
    private static final List<String> variantClassReportable = Arrays.asList("P", "LP", "PV", "LPV", "VUS", "B", "LB", "BV", "LBV");
    public static SnvSimpleTableElement build() {
        return new SnvSimpleTableElement(div());
    }
    private final Sheet.ColumnDefinition[] columns = new Sheet.ColumnDefinition[]{
            new Sheet.ColumnDefinition().readonly(true).id("class").name("D.Class").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER)),
            new Sheet.ColumnDefinition().readonly(true).id("gene").name("Gene").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER)),
            new Sheet.ColumnDefinition().readonly(true).id("hgvsc").name("HGVS.c").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER)),
            new Sheet.ColumnDefinition().readonly(true).id("hgvsp").name("HGVS.p").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER)),
            new Sheet.ColumnDefinition().readonly(true).id("genotype").name("Zygosity").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER)),
            new Sheet.ColumnDefinition().readonly(true).id("analysis").name("Analysis").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER))
    };
    private final SpreadSheet.SheetBuilder config = SpreadSheet.builder()
            .autoColSize(false)
            .autoRowSize(false)
            .rowHeaders(false)
            .manualColumnMove(true)
            .manualColumnResize(true)
            .data(new Data[] {})
            .colWidths(new Double[]{null, null, 80.0, 100.0, 100.0, null, null, null, 150.0})
            .stretchH("all");
    private final com.greencross.lims.sheet.Sheet sheet = com.greencross.lims.sheet.Sheet.build(config, columns);
    private final HtmlContentBuilder<HTMLDivElement> _this;
    private SnvSimpleTableElement(HtmlContentBuilder<HTMLDivElement> e) {
        super(e.css("variant-summary-table"));
        _this = e.add(sheet);
        sheet.on(EventType.click, evt->{
            evt.stopPropagation();
            evt.preventDefault();
        });
        sheet.on(EventType.mouseover, evt->{
            evt.stopPropagation();
            evt.preventDefault();
        });
    }
    public SnvSimpleTableElement update(Object[] objs) {
        if(objs!=null) update(Arrays.stream(objs).map(o->map(o)).filter(d->{
            String clazz = d.get("class");
            return variantClassReportable.contains(clazz);
        }).toArray(Data[]::new));
        else update(new Data[0]);
        return that();
    }
    private Data map(Object obj) {
        JsPropertyMap<Object> map = Js.asPropertyMap(obj);
        Data data = new Data(ColumnSet.unwrap(map.get("snv")));
        String analysis = ColumnSet.unwrap(map.get("analysis"));
        Object hc = map.get("hgvsc_in_mane");
        if(hc == null) hc = map.get("hgvsc");
        Object hp = map.get("hgvsp_in_mane");
        if(hp == null) hp = map.get("hgvsp");
        if(analysis!=null && analysis.contains(":")) analysis = analysis.substring(analysis.indexOf(":")+1);
        data.put("chr", ColumnSet.unwrap(map.get("chrom")))
            .put("pos", ColumnSet.unwrap(map.get("pos")))
            .put("ref", ColumnSet.unwrap(map.get("ref")))
            .put("alt", ColumnSet.unwrap(map.get("alt")))
            .put("gene", ColumnSet.unwrap(map.get("gene.refgene")))
            .put("hgvsc", ColumnSet.unwrap(hc))
            .put("hgvsp", ColumnSet.unwrap(hp))
            .put("exon", ColumnSet.unwrap(map.get("exon_in_mane")))
            .put("so", ColumnSet.unwrap(map.get("so_term")))
            .put("effect", ColumnSet.unwrap(map.get("effect_level")))
            .put("genotype", ColumnSet.unwrap(map.get("genotype")))
            .put("depth", ColumnSet.unwrap(map.get("depth")))
            .put("vaf", ColumnSet.unwrap(map.get("vaf")))
            .put("filter", ColumnSet.unwrap(map.get("filter")))
            .put("sanger", ColumnSet.unwrap(map.get("insilico_sanger")))
            .put("analysis", analysis)
            .put("class", ColumnSet.unwrap(map.get("class")));
        return data;
    }
    private void update(Data[] values) {
        sheet.value(values);
        Scheduler.get().scheduleDeferred(()->_this.element().style.height = CSSProperties.HeightUnionType.of((((HTMLElement) sheet.element().querySelector(".ht_master .wtHider")).offsetHeight)+ "px"));
    }
    public int height() {
        return calculateTableHeight(config.data());
    }
    private int calculateTableHeight(Data[] values) {
        if(values == null) return 0;
        return 26 + values.length*21;
    }
    @Override
    public SnvSimpleTableElement that() {
        return this;
    }
}
