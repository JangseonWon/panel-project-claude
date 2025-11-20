package com.gcgenome.lims.client.interpretation;

import com.gcgenome.lims.client.util.Format;
import com.gcgenome.lims.dto.interpretation.PanelTest;
import elemental2.dom.CustomEvent;
import elemental2.dom.HTMLDivElement;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.Delegate;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.ListElement;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.SheetElementSelectableMulti;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnDropDown;
import net.sayaya.ui.chart.column.ColumnText;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jboss.elemento.Elements.div;

public class SnvPanelTestTableElement extends HTMLElementBuilder<HTMLDivElement, SnvPanelTestTableElement> implements HasSelectionChangeHandlers<Data[]> {
    public static SnvPanelTestTableElement build() {
        return new SnvPanelTestTableElement(div(), "PV", "LPV", "VUS");
    }
    public static SnvPanelTestTableElement buildWithBenign() {
        return new SnvPanelTestTableElement(div(), "PV", "LPV", "VUS", "LBV", "BV");
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
                .pattern("^VUS$").than("#121212", "#FF8200")
                .pattern("^LBV$").than("#FFFFFF", "#00AB84")
                .pattern("^BV$").than("#FFFFFF", "#007B5F");
    }
    private final SheetElement.SheetConfiguration config = SheetElement.builder()
            .autoColSize(true)
            .autoRowSize(false)
            .viewportColumnRenderingOffset(100.0)
            .rowHeaderWidth(30)
            .rowHeaders(false)
            .manualColumnMove(false)
            .manualColumnResize(true)
            .data(new Data[] {})
            .colWidths(new double[]{40, 50, 80, 100, 40, 80, 40, 40})
            .stretchH("all");
    private final SheetElement sheet = config.build();
    @Delegate
    private final SheetElementSelectableMulti selection = SheetElementSelectableMulti.wrap(sheet);
    private final Set<String> classAllows;
    private SnvPanelTestTableElement(HTMLContainerBuilder<HTMLDivElement> e, String... classAllows) {
        super(e.css("variant-summary-table"));
        this.classAllows = Arrays.stream(classAllows).collect(Collectors.toSet());
        config.columns(classColumn(classAllows).build(),
                column("gene", "Gene").build(),
                column("hgvsc", "HGVS.c").readOnly(false).build(), column("hgvsp", "HGVS.p").build(),
                column("zygosity","Zygosity").build(),
                column("disease","Disease").build(),
                column("inheritance", "Inheritance").build(),
                ColumnBuilder.link("Reported", data-> "../info.html#/panel-service/varsnv.html#" + data.get("snv")).horizontal("center").vertical("middle").build());
        e.add(sheet);
    }
    public void append(PanelTest.Variant[] variants) {
        Data[] data = sheet.values();
        data = Stream.concat(Arrays.stream(data), Stream.of(variants).map(this::map)).toArray(Data[]::new);
        update(data);
    }
    public void trimming(PanelTest.Variant[] variants) {
        Data[] data = sheet.values();
        Set<String> candidates = Stream.of(variants).map(v->v.snv()).collect(Collectors.toSet());
        data = Arrays.stream(data).filter(v->!candidates.contains(v.idx())).toArray(Data[]::new);
        update(data);
    }
    public SnvPanelTestTableElement update(PanelTest.Variant[] objs, Map<String, Boolean> proven) {
        if(objs!=null) update(Arrays.stream(objs).map(this::map).map(v->{
            String href = "varsnv.html#" + v.idx();
            String label = "-";
            if(proven.containsKey(v.idx()) && proven.get(v.idx())) label = "Reported";
            // v.initialize("variant", Global.JSON.stringify(new Sheet.Link().label(label).href(href).target("_blank")));
            return v;
        }).toArray(Data[]::new));
        else update(new Data[0]);
        return that();
    }
    private Data map(PanelTest.Variant obj) {
        JsPropertyMap<Object> map = Js.asPropertyMap(obj);
        Data data = Data.create(Format.unwrap(map.get("snv")));
        data.put("snv", Format.unwrap(map.get("snv")))
            .put("analysis", Format.unwrap(map.get("analysis")))
            .put("gene", Format.unwrap(map.get("gene")))
            .put("hgvsc", Format.unwrap(map.get("hgvsc")))
            .put("hgvsp", Format.unwrap(map.get("hgvsp")))
            .put("origin_hgvsc", Format.unwrap(map.get("origin_hgvsc")))
            .put("origin_hgvsp", Format.unwrap(map.get("origin_hgvsp")))
            .put("zygosity", Format.unwrap(map.get("zygosity")))
            .put("disease", Format.unwrap(map.get("disease")))
            .put("inheritance", Format.unwrap(map.get("inheritance")))
            .put("class", Format.unwrap(map.get("class")))
            .put("variant", Format.unwrap(map.get("snv")));
        return data;
    }
    private void update(Data[] values) {
        sheet.values(values);
        element().dispatchEvent(new CustomEvent<>("change"));
    }
    public PanelTest.Variant[] values() {
        return Arrays.stream(sheet.values()).map(this::map).toArray(PanelTest.Variant[]::new);
    }
    public PanelTest.Variant[] selected() {
        return Arrays.stream(selection()).map(this::map).toArray(PanelTest.Variant[]::new);
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
                                      .disease(data.get("disease"))
                                      .inheritance(data.get("inheritance"))
                                      .clazz(data.get("class"));
    }
    @Override
    public SnvPanelTestTableElement that() {
        return this;
    }
}
