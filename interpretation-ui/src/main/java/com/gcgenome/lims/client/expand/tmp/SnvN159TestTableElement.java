package com.gcgenome.lims.client.expand.tmp;

import com.gcgenome.lims.dto.interpretation.tmp.N159Dto;
import com.google.gwt.core.client.Scheduler;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.ListElement;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnDropDown;
import net.sayaya.ui.chart.column.ColumnText;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.jboss.elemento.Elements.div;

public class SnvN159TestTableElement extends HTMLElementBuilder<HTMLDivElement, SnvN159TestTableElement> {
    public static SnvN159TestTableElement build() {
        return new SnvN159TestTableElement(div());
    }
    private static ColumnText column(String id, String name){
        return ColumnBuilder.text(id, 10, 100).name(name).horizontal("center").vertical("middle");
    }
    private static ColumnDropDown classColumn(String id, String name, String... classAllows) {
        ListElement.SingleLineItem[] classes = new ListElement.SingleLineItem[classAllows.length];
        for(int i = 0; i < classAllows.length; ++i) classes[i] = ListElement.singleLine().label(classAllows[i]);
        return ColumnBuilder.dropdown(id, classes)
                .name(name).horizontal("center");
    }
    private final SheetElement.SheetConfiguration config = SheetElement.builder()
            .autoColSize(true)
            .autoRowSize(false)
            .viewportColumnRenderingOffset(100.0)
            .rowHeaderWidth(30)
            .rowHeaders(false)
            .manualColumnMove(false)
            .manualColumnResize(true)
            .columns(column("gene","Gene").build(),
                    column("dna", "DNA").build(),
                    column("protein", "Protein").build(),
                    column("vaf", "VAF(%)").build(),
                    column("depth", "Depth(X)").build(),
                    column("cosmic_id", "COSMIC ID").build(),
                    classColumn("tier", "Tier", "Tier1/2", "Tier3", "Tier4")
                            .pattern("Tier1/2").than("#FFFFFF", "#AD1742")
                            .pattern("Tier3").than("#121212", "#D26263")
                            .pattern("Tier4").than("#121212", "#FF8200").build())
            .data(new Data[] {})
            .colWidths(new double[]{80, 100, 100, 80, 80, 80})
            .stretchH("all");
    private final SheetElement sheet = config.build();
    private final HTMLDivElement empty = div().style("font-size: small;display: flex; align-items: center; justify-content: center;user-select: none; height: calc(100% - 27px);").add("No variant").element();
    private final HTMLContainerBuilder<HTMLDivElement> _this;
    private SnvN159TestTableElement(HTMLContainerBuilder<HTMLDivElement> e) {
        super(e.css("variant-summary-table").style("height: auto; margin-right: 0px; margin-bottom: 3px;"));
        _this = e.add(div().style("overflow: hidden; height: auto;").add(sheet)).add(empty);
    }
    public SnvN159TestTableElement update(N159Dto.Variant[] objs) {
        if(objs!=null) update(Arrays.stream(objs).map(this::map).toArray(Data[]::new));
        else update(new Data[0]);
        return that();
    }
    public void append() {
        Data[] data = sheet.values();
        data = Stream.concat(Arrays.stream(data), Stream.of(Data.create(""))).toArray(Data[]::new);
        sheet.values(data);
        int row = data.length;
        Scheduler.get().scheduleFixedDelay(()->{
            int height = 28 + 24*row;
            _this.element().style.height = CSSProperties.HeightUnionType.of(Math.max(height, ((HTMLElement) sheet.element().querySelector(".ht_master .wtHider")).offsetHeight) + "px");
            return false;
        }, 10);
    }
    public void trimming() {
        Data[] data = sheet.values();
        data = Arrays.stream(data).limit(data.length-1).toArray(Data[]::new);
        sheet.values(data);
        int row = data.length;
        Scheduler.get().scheduleFixedDelay(()->{
            int height = 28 + 24*row;
            _this.element().style.height = CSSProperties.HeightUnionType.of(Math.max(height, ((HTMLElement) sheet.element().querySelector(".ht_master .wtHider")).offsetHeight) + "px");
            return false;
        }, 10);
    }
    private Data map(N159Dto.Variant obj) {
        Data data = Data.create("-");
        data.put("gene", obj.gene())
            .put("dna", obj.hgvsc())
            .put("protein", obj.hgvsp())
            .put("vaf", obj.vaf()!=null?String.valueOf(obj.vaf()):null)
            .put("depth", obj.depth()!=null?String.valueOf(obj.depth()):null)
            .put("cosmic_id", obj.cosmic())
            .put("tier", obj.tier()!=null?obj.tier().name():null);
        return data;
    }
    private void update(Data[] values) {
        sheet.values(values);
        Scheduler.get().scheduleFixedDelay(()->{
            int row = values.length;
            int height = 28 + 24*row;
            _this.element().style.height = CSSProperties.HeightUnionType.of(Math.max(height, ((HTMLElement) sheet.element().querySelector(".ht_master .wtHider")).offsetHeight) + "px");
            sheet.refresh();
            return false;
        }, 500);
    }
    public N159Dto.Variant[] values() {
        return Arrays.stream(sheet.values()).map(this::map).toArray(N159Dto.Variant[]::new);
    }
    private N159Dto.Variant map(Data data) {
        Double vaf = null;
        if(data.get("vaf")!=null && !data.get("vaf").trim().isEmpty()) vaf = Double.parseDouble(data.get("vaf"));
        Integer depth = null;
        if(data.get("depth")!=null && !data.get("depth").trim().isEmpty()) depth = Integer.parseInt(data.get("depth"));
        N159Dto.Tier tier = null;
        if(data.get("tier")!=null && !data.get("tier").trim().isEmpty()) tier = N159Dto.Tier.valueOf(data.get("tier").replace("/", ""));
        return new N159Dto.Variant().gene(data.get("gene"))
                                    .hgvsc(data.get("dna"))
                                    .hgvsp(data.get("protein"))
                                    .vaf(vaf)
                                    .depth(depth)
                                    .cosmic(data.get("cosmic_id"))
                                    .tier(tier);
    }
    @Override
    public SnvN159TestTableElement that() {
        return this;
    }
}
