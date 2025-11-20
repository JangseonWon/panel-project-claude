package com.gcgenome.lims.client.interpretation;

import com.gcgenome.lims.dto.interpretation.PanelTest;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
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

public class SnvRiskscreenTableElement<V> extends HTMLElementBuilder<HTMLDivElement, SnvRiskscreenTableElement<V>> {
    public static <V> SnvRiskscreenTableElement<V> build(Class<V> clazz) {
        return new SnvRiskscreenTableElement<>(div());
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
            .columns(column("gene","유전자").build(),
                    column("hgvsc", "DNA 변이").build(),
                    column("hgvsp", "아미노산 변이").build(),
                    column("zygosity", "접합자").build(),
                    classColumn("PV", "LPV", "VUS")
                            .pattern("^PV$").than("#FFFFFF", "#AD1742")
                            .pattern("^LPV$").than("#121212", "#D26263")
                            .pattern("^VUS$").than("#121212", "#FF8200").build())
            .data(new Data[] {})
            .colWidths(new double[]{80, 100, 100, 80, 80, 80})
            .stretchH("all");
    private final SheetElement sheet = config.build();
    private final HTMLContainerBuilder<HTMLDivElement> _this;
    private SnvRiskscreenTableElement(HTMLContainerBuilder<HTMLDivElement> e) {
        super(e.css("variant-summary-table"));
        _this = e.add(sheet);
    }
    public SnvRiskscreenTableElement<V> update(V[] objs) {
        if(objs!=null) update(Arrays.stream(objs).map(this::map).toArray(Data[]::new));
        else update(new Data[0]);
        return that();
    }
    private Data map(V obj) {
        JsPropertyMap<Object> map = Js.asPropertyMap(obj);
        Data data = Data.create((String)map.get("snv"));
        data.put("gene", (String)map.get("gene"))
            .put("hgvsc", (String)map.get("hgvsc"))
            .put("hgvsp", (String)map.get("hgvsp"))
            .put("zygosity", (String)map.get("zygosity"))
            .put("disease", (String)map.get("disease"))
            .put("inheritance", (String)map.get("inheritance"))
            .put("class", (String)map.get("class"));
        return data;
    }
    private void update(Data[] values) {
        _this.element().style.height = CSSProperties.HeightUnionType.of((calculateTableHeight(values)+1) + "px");
        sheet.values(values);
    }
    public PanelTest.Variant[] values() {
        return Arrays.stream(sheet.values()).map(this::map).toArray(PanelTest.Variant[]::new);
    }
    public PanelTest.Variant[] changes(String key) {
        return Arrays.stream(sheet.values()).filter(data->data.isChanged(key)).map(this::map).toArray(PanelTest.Variant[]::new);
    }
    private PanelTest.Variant map(Data data) {
        return new PanelTest.Variant().gene(data.get("gene"))
                                      .hgvsc(data.get("hgvsc"))
                                      .hgvsp(data.get("hgvsp"))
                                      .zygosity(data.get("zygosity"))
                                      .disease(data.get("disease"))
                                      .inheritance(data.get("inheritance"))
                                      .clazz(data.get("class"));
    }
    public int height() {
        return calculateTableHeight(config.data());
    }
    private int calculateTableHeight(Data[] values) {
        if(values == null) return 0;
        return 26 + values.length*23;
    }
    @Override
    public SnvRiskscreenTableElement that() {
        return this;
    }
}
