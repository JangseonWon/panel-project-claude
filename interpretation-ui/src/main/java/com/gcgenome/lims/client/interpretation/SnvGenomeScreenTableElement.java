package com.gcgenome.lims.client.interpretation;

import com.gcgenome.lims.client.util.Format;
import com.gcgenome.lims.dto.interpretation.PanelTest;
import com.google.gwt.core.client.Scheduler;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.ListElement;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnDropDown;
import net.sayaya.ui.chart.column.ColumnText;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;

import static org.jboss.elemento.Elements.div;

public class SnvGenomeScreenTableElement extends HTMLElementBuilder<HTMLDivElement, SnvGenomeScreenTableElement> {
    public static SnvGenomeScreenTableElement build() {
        return new SnvGenomeScreenTableElement(div());
    }
    private static ColumnText column(String name){
        return column(name, name);
    }
    private static ColumnText column(String id, String name){
        return ColumnBuilder.text(id, 10, 100).name(name).horizontal("center").vertical("middle");
    }
    private static ColumnDropDown classColumn(String... classAllows) {
        ListElement.SingleLineItem[] classes = new ListElement.SingleLineItem[classAllows.length];
        for(int i = 0; i < classAllows.length; ++i) classes[i] = ListElement.singleLine().label(classAllows[i]);
        return ColumnBuilder.dropdown("class", classes)
                .name("Class").horizontal("center")
                .pattern("^PV$").than("#FFFFFF", "#AD1742")
                .pattern("^LPV$").than("#121212", "#D26263")
                .pattern("^VUS$").than("#121212", "#FF8200");
    }
    private final SheetElement.SheetConfiguration config = SheetElement.builder()
            .autoColSize(true)
            .autoRowSize(false)
            .viewportColumnRenderingOffset(100.0)
            .rowHeaderWidth(30)
            .rowHeaders(false)
            .manualColumnMove(false)
            .manualColumnResize(true)
            .columns(classColumn("PV", "LPV", "VUS").build(),
                    column("Gene").build(),
                    column("hgvsc", "HGVS.c").build(),
                    column("hgvsp", "HGVS.p").build(),
                    column("Zygosity").build())
            .data(new Data[] {})
            .colWidths(new double[]{40.0, 50.0, 80.0, 100.0, 40.0})
            .stretchH("all");
    private final SheetElement sheet = config.build();
    private final HTMLContainerBuilder<HTMLDivElement> _this;
    private SnvGenomeScreenTableElement(HTMLContainerBuilder<HTMLDivElement> e) {
        super(e.css("variant-summary-table"));
        _this = e.add(sheet);
    }
    public SnvGenomeScreenTableElement update(PanelTest.Variant[] objs) {
        if(objs!=null && objs.length > 0) update(Arrays.stream(objs).map(this::map).toArray(Data[]::new));
        else update(new Data[0]);
        return that();
    }
    private Data map(PanelTest.Variant obj) {
        JsPropertyMap<Object> map = Js.asPropertyMap(obj);
        Data data = Data.create(Format.unwrap(map.get("snv")));
        data.put("analysis", Format.unwrap(map.get("analysis")))
            .put("gene", Format.unwrap(map.get("gene")))
            .put("hgvsc", Format.unwrap(map.get("hgvsc")))
            .put("hgvsp", Format.unwrap(map.get("hgvsp")))
            .put("origin_hgvsc", Format.unwrap(map.get("origin_hgvsc")))
            .put("origin_hgvsp", Format.unwrap(map.get("origin_hgvsp")))
            .put("zygosity", Format.unwrap(map.get("zygosity")))
            .put("class", Format.unwrap(map.get("class")));
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
    public PanelTest.Variant[] values() {
        return Arrays.stream(sheet.values()).map(this::map).toArray(PanelTest.Variant[]::new);
    }
    public PanelTest.Variant[] changes(String key) {
        return Arrays.stream(sheet.values()).filter(data->data.isChanged(key)).map(this::map).toArray(PanelTest.Variant[]::new);
    }
    private PanelTest.Variant map(Data data) {
        return new PanelTest.Variant().snv(data.idx()).analysis(data.get("analysis"))
                                      .gene(data.get("gene"))
                                      .hgvsc(data.get("hgvsc"))
                                      .hgvsp(data.get("hgvsp"))
                                      .originHgvsc(data.get("origin_hgvsc"))
                                      .originHgvsp(data.get("origin_hgvsp"))
                                      .zygosity(data.get("zygosity"))
                                      .clazz(data.get("class"));
    }
    @Override
    public SnvGenomeScreenTableElement that() {
        return this;
    }
}
