package com.gcgenome.lims.client.interpretation;

import com.gcgenome.lims.dto.interpretation.Hrd;
import com.google.gwt.core.client.Scheduler;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnText;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.jboss.elemento.Elements.div;

public class SnvHrdTableElement extends HTMLElementBuilder<HTMLDivElement, SnvHrdTableElement> {
    public static SnvHrdTableElement build() {
        return new SnvHrdTableElement(div());
    }
    private static ColumnText column(String id, String name){
        return ColumnBuilder.text(id, 10, 100).name(name).horizontal("center").vertical("middle");
    }
    private final SheetElement.SheetConfiguration config = SheetElement.builder()
            .autoColSize(true)
            .autoRowSize(false)
            .viewportColumnRenderingOffset(100.0)
            .rowHeaderWidth(30)
            .rowHeaders(false)
            .manualColumnMove(false)
            .manualColumnResize(true)
            .columns(column("gene", "Gene").build(),
                    column("dna", "DNA").build(),
                    column("protein", "Protein").build(),
                    column("vaf", "VAF(%)").build(),
                    column("depth", "Depth(X)").build(),
                    column("cosmic_id", "COSMIC ID").build())
            .data(new Data[] {})
            .colWidths(new double[]{80, 100, 100, 80, 80, 80})
            .stretchH("all");
    private final SheetElement sheet = config.build();
    private final HTMLDivElement empty = div().style("font-size: small;display: flex; align-items: center; justify-content: center;user-select: none; height: calc(100% - 27px);").add("No variant").element();
    private final HTMLContainerBuilder<HTMLDivElement> _this;
    private SnvHrdTableElement(HTMLContainerBuilder<HTMLDivElement> e) {
        super(e.css("variant-summary-table").style("height: auto; margin-right: 0px; margin-bottom: 3px;"));
        _this = e.add(div().style("overflow: hidden; height: auto;").add(sheet)).add(empty);
        // CTRL+V로 범위 이상의 데이터가 붙여넣기 되었을 때
        sheet.on(EventType.keydown, evt->{
            if(evt.ctrlKey && "keyV".equalsIgnoreCase(evt.code)) {
                Scheduler.get().scheduleDeferred(()->{
                    Object[] prev = sheet.values();
                    Data[] next = new Data[prev.length];
                    boolean hit = false;
                    for(int i = 0; i < prev.length; ++i) {
                        if(!(prev[i] instanceof Data) || !"-".equals(((Data)prev[i]).idx())) {
                            Data t = Data.create("-");
                            JsPropertyMap<Object> cast = Js.asPropertyMap(prev[i]);
                            cast.forEach(cb-> {if(cast.get(cb) instanceof String) try {t.initialize(cb, "").put(cb, (String)cast.get(cb)); } catch(Exception ex){}});
                            next[i] = t;
                            hit = true;
                        } else next[i] = (Data) prev[i];
                    }
                    if(hit) {
                        sheet.values(next);
                        int row = next.length;
                        Scheduler.get().scheduleFixedDelay(()->{
                            int height = 28 + 23*row;
                            _this.element().style.height = CSSProperties.HeightUnionType.of(Math.max(height, ((HTMLElement) sheet.element().querySelector(".ht_master .wtHider")).offsetHeight) + "px");
                            return false;
                        }, 10);
                    }
                });
            }
        });
    }
    public SnvHrdTableElement update(Hrd.Variant[] objs) {
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
            int height = 28 + 23*row;
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
            int height = 28 + 23*row;
            _this.element().style.height = CSSProperties.HeightUnionType.of(Math.max(height, ((HTMLElement) sheet.element().querySelector(".ht_master .wtHider")).offsetHeight) + "px");
            return false;
        }, 10);
    }
    private Data map(Hrd.Variant obj) {
        Data data = Data.create("-");
        data.put("gene", obj.gene())
            .put("dna", obj.dna())
            .put("protein", obj.protein())
            .put("vaf", obj.vaf()!=null?String.valueOf(obj.vaf()):null)
            .put("depth", obj.depth()!=null?String.valueOf(obj.depth()):null)
            .put("cosmic_id", obj.cosmicId());
        return data;
    }
    private void update(Data[] values) {
        sheet.values(values);
        Scheduler.get().scheduleFixedDelay(()->{
            int row = values.length;
            int height = 28 + 23*row;
            _this.element().style.height = CSSProperties.HeightUnionType.of(Math.max(height, ((HTMLElement) sheet.element().querySelector(".ht_master .wtHider")).offsetHeight) + "px");
            sheet.refresh();
            return false;
        }, 500);
    }
    public Hrd.Variant[] values() {
        return Arrays.stream(sheet.values()).map(this::map).toArray(Hrd.Variant[]::new);
    }
    public Hrd.Variant[] changes(String key) {
        return Arrays.stream(sheet.values()).filter(data->data.isChanged(key)).map(this::map).toArray(Hrd.Variant[]::new);
    }
    private Hrd.Variant map(Data data) {
        Double vaf = null;
        try { vaf = Double.parseDouble(data.get("vaf"));} catch(Exception ignore){}
        Integer depth = null;
        try { depth = Integer.parseInt(data.get("depth"));} catch(Exception ignore){}
        return new Hrd.Variant().gene(data.get("gene"))
                                .dna(data.get("dna"))
                                .protein(data.get("protein"))
                                .vaf(vaf)
                                .depth(depth)
                                .cosmicId(data.get("cosmic_id"));
    }
    @Override
    public SnvHrdTableElement that() {
        return this;
    }
}
