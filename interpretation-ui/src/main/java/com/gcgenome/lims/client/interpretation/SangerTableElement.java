package com.gcgenome.lims.client.interpretation;

import com.gcgenome.lims.client.util.Format;
import com.gcgenome.lims.dto.interpretation.Sanger;
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
import net.sayaya.ui.event.HasValueChangeHandlers;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;

import static org.jboss.elemento.Elements.div;

public class SangerTableElement extends HTMLElementBuilder<HTMLDivElement, SangerTableElement> {
    public static SangerTableElement build() {
        return new SangerTableElement(div());
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
            .columns(classColumn("class", "Class", "-", "Pathogenic", "Likely Pathogenic", "VUS", "Likely Benign", "Benign")
                            .pattern("Pathogenic").than("#FFFFFF", "#AD1742")
                            .pattern("Likely Pathogenic").than("#FFFFFF", "#D26263")
                            .pattern("VUS").than("#121212", "#FF8200")
                            .pattern("Likely Benign").than("#FFFFFF", "#00AB84")
                            .pattern("Benign").than("#FFFFFF", "#007B5F").build(),
                    column("gene", "Gene").build(),
                    column("hgvsc", "DNA change").build(),
                    column("hgvsp", "Predicted AA change").build(),
                    classColumn("zygosity", "Zygosity", "Wild", "Het", "Hom", "Hem", "Hom or Hem", "-").build(),
                    classColumn("result", "Result", "Not Detected", "Detected", "-")
                            .pattern("^Not Detected$").than("#888888", "transparent")
                            .pattern("^Detected$").than("#FFFFFF", "#AD1742")
                            .build()
            ).data(new Data[] {})
            .colWidths(new double[]{40, 50, 80, 100, 40, 80, 40})
            .stretchH("all");
    private final SheetElement sheet = config.build();
    private final HTMLContainerBuilder<HTMLDivElement> _this;
    private SangerTableElement(HTMLContainerBuilder<HTMLDivElement> e) {
        super(e.css("variant-summary-table"));
        _this = e.add(sheet);
    }
    public SangerTableElement update(Sanger.Variant obj) {
        if(obj!=null) update(new Data[] {map(obj)});
        else update(new Data[0]);
        return that();
    }
    private Data map(Sanger.Variant obj) {
        JsPropertyMap<Object> map = Js.asPropertyMap(obj);
        Data data = Data.create(Format.unwrap(map.get("snv")));
        data.put("analysis", Format.unwrap(map.get("analysis")))
                .put("gene", Format.unwrap(map.get("gene")))
                .put("hgvsc", Format.unwrap(map.get("hgvsc")))
                .put("hgvsp", Format.unwrap(map.get("hgvsp")))
                .put("zygosity", Format.unwrap(map.get("zygosity")))
                .put("class", Format.unwrap(map.get("class")))
                .put("result", Format.unwrap(map.get("result")));
        data.onValueChange(this::zygosityChangeHandler);
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
    public Sanger.Variant value() {
        return Arrays.stream(sheet.values()).map(this::map).toArray(Sanger.Variant[]::new)[0];
    }
    public Sanger.Variant[] changes(String key) {
        return Arrays.stream(sheet.values()).filter(data->data.isChanged(key)).map(this::map).toArray(Sanger.Variant[]::new);
    }
    private Sanger.Variant map(Data data) {
        return new Sanger.Variant().gene(data.get("gene"))
                .hgvsc(data.get("hgvsc"))
                .hgvsp(data.get("hgvsp"))
                .zygosity(data.get("zygosity"))
                .result(data.get("result"))
                .clazz(data.get("class"));
    }
    @Override
    public SangerTableElement that() {
        return this;
    }
    private void zygosityChangeHandler(HasValueChangeHandlers.ValueChangeEvent<Data> event){
        Arrays.stream(sheet.values()).forEach(datum -> {
            String zygosity = datum.get("zygosity");
            switch (zygosity.toLowerCase()) {
                case "wild":
                    datum.put("result", "Not Detected");
                    break;
                case "het":
                case "hom":
                case "hem":
                    datum.put("result", "Detected");
                    break;
                case "hom or hem":
                    datum.put("result", "Detected");
                    break;
                case "-":
                    datum.put("result", "-");
                    break;
                default:
            }
        });
    }
}
