package com.greencross.lims.client.expand;

import com.google.gwt.core.client.Scheduler;
import com.greencross.lims.api.WindowApi;
import com.greencross.lims.client.snv.ColumnSet;
import com.greencross.lims.dto.Sheet;
import com.greencross.lims.dto.Sheet.StyleColor.StyleConditional;
import com.greencross.lims.sheet.Data;
import com.greencross.lims.sheet.SheetState;
import com.greencross.lims.sheet.SpreadSheet;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.Window;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.Accessors;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.event.HasStateChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SnvTableElement extends HTMLElementBuilder<HTMLDivElement, SnvTableElement> implements HasSelectionChangeHandlers<SnvTableElement.SnvReport[]>, HasStateChangeHandlers<SheetState> {
    public static SnvTableElement build(ColumnSet[] columnSets) {
        List<Sheet.ColumnDefinition> columns = new LinkedList<>();
        columns.add(new Sheet.ColumnDefinition().id("report").name("D.Class").type(Sheet.ColumnDefinition.ColumnType.DROPDOWN)
                .styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER))
                .styleColor(new Sheet.StyleColor().conditions(new Sheet.StyleColor.StyleConditional[]{
                        new StyleConditional().type(StyleConditional.Condition.EQ).param("P").colorBg("#AD1742").colorFg("#FFFFFF"),
                        new StyleConditional().type(StyleConditional.Condition.EQ).param("LP").colorBg("#D26263").colorFg("#FFFFFF"),
                        new StyleConditional().type(StyleConditional.Condition.EQ).param("VUS").colorBg("#FF8200").colorFg("#121212"),
                        new StyleConditional().type(StyleConditional.Condition.EQ).param("LB").colorBg("#00AB84").colorFg("#FFFFFF"),
                        new StyleConditional().type(StyleConditional.Condition.EQ).param("B").colorBg("#007B5F").colorFg("#FFFFFF"),
                        new StyleConditional().type(StyleConditional.Condition.EQ).param("FP").colorBg("#DDDDDD").colorFg("#FFFFFF"),
                        new StyleConditional().type(StyleConditional.Condition.EQ).param("-"),
                        new StyleConditional().type(StyleConditional.Condition.EQ).param("*").colorBg("#AD1742"),
                        new StyleConditional().type(StyleConditional.Condition.EQ).param("+").colorBg("#D26263"),
                        new StyleConditional().type(StyleConditional.Condition.EQ).param("?").colorBg("#FF8200"),
                        new StyleConditional().type(StyleConditional.Condition.EQ).param(".").colorBg("#00AB84"),
                        new StyleConditional().type(StyleConditional.Condition.EQ).param("'").colorBg("#007B5F")
                })).width(80).readonly(false));
        columns.add(new Sheet.ColumnDefinition().width(50).readonly(true).id("tier").name("Tier").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER)));
        columns.add(new Sheet.ColumnDefinition().width(50).readonly(true).id("class").name("Class").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER)));
        columns.add(new Sheet.ColumnDefinition().id("variant").name("Id").type(Sheet.ColumnDefinition.ColumnType.LINK).href("#").target("variant")
                .styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT).font("JetBrains Mono")).width(180).readonly(true)
                                                .callback(link-> WindowApi.open("varsnv.html#" + link.label(), "_blank", null, false)));
        columns.add(new Sheet.ColumnDefinition().width(80).readonly(true).id("gene").name("Gene").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER)));
        // columns.add(new Sheet.ColumnDefinition().width(90).readonly(true).id("pos_").name("Pos").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER)));
        columns.add(new Sheet.ColumnDefinition().width(90).readonly(true).id("pos_").name("Pos").type(Sheet.ColumnDefinition.ColumnType.LINK).href("http://localhost:60151\\goto?locus=[\"pos_\"]").styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER))
                .callback(link->{
                    Window popup = DomGlobal.window.open(link.href(), "_blank", "left=10000,top=100000,width=2,height=2,toolbar=0,resizable=0");
                    Scheduler.get().scheduleFixedDelay(()->{
                        popup.close();
                        return false;
                    }, 1000);
                }));
        columns.add(new Sheet.ColumnDefinition().width(180).readonly(true).id("hgvsc").name("HGVS.c").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)));
        columns.add(new Sheet.ColumnDefinition().width(120).readonly(true).id("hgvsp").name("HGVS.p").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)));

        Arrays.stream(columnSets)
                .filter(ColumnSet::select)
                .map(c->c.columns())
                .flatMap(Arrays::stream).forEach(columns::add);
        return new SnvTableElement(com.greencross.lims.sheet.Sheet.build(SpreadSheet.builder()
                .autoColSize(false)
                .autoRowSize(false)
                .rowHeaders(false)
                .renderAllRows(true)
                .manualColumnMove(true)
                .manualColumnResize(true)
                .fixedColumnsLeft(8)
                .data(new Data[] {})
                .stretchH("all"), columns.stream().toArray(Sheet.ColumnDefinition[]::new)), columnSets);
    }
   // private final SpreadSheet.SheetBuilder config;
    private final com.greencross.lims.sheet.Sheet sheet;
    private Map<String, SnvReport> reported = new HashMap<>();
    private ColumnSet[] columnSets;
    private SnvTableElement(com.greencross.lims.sheet.Sheet e, ColumnSet[] columnSets) {
        super(e);
      //  config = sheet.
        this.columnSets = columnSets;
        sheet = e;
    }
    public SnvTableElement reported(SnvReport[] s) {
        reported.clear();
        for(SnvReport r: s) reported.put(r.variant, r);
        return this;
    }
    public SnvTableElement update(Object[] objs) {
        AtomicInteger row = new AtomicInteger(0);
        sheet.value(Arrays.stream(objs).map(obj->map(row.incrementAndGet(), obj)).peek(v->{
            String id = v.get("analysis") + ":" + v.get("variant");
            if(reported.containsKey(id)) v.initialize("report", reported.get(id).classification());
        }).toArray(Data[]::new));
        return this;
    }
    public Data[] changes() {
        return Arrays.stream(sheet.value()).filter(d->d.isChanged("report") || d.isChanged("class")).toArray(Data[]::new);
    }
    public SnvTableElement copy(SnvTableElement other) {
        sheet.value(other.sheet.value());
        return that();
    }
    public Data map(int row, Object obj) {
        JsPropertyMap<Object> map = Js.asPropertyMap(obj);
        Data data = new Data(row+"");
        String reported = (String) map.get("reported");
        if(reported!=null && reported.trim().length() > 5) {
            if(reported.contains("=P")) data.put("report", "*");
            else if(reported.contains("=LP")) data.put("report", "+");
            else if(reported.contains("=VUS")) data.put("report", "?");
            else if(reported.contains("=B")) data.put("report", "'");
            else if(reported.contains("=LB")) data.put("report", ".");
        }
        else data.put("report", "-");
        data.put("analysis", (String)map.get("analysis"));
        data.put("variant", map.get("snv")!=null?(String)map.get("snv"):"");
        for(ColumnSet c: columnSets) c.map(map, data);
        return data;
    }
    public SnvTableElement refresh() {
        sheet.refresh();
        return this;
    }
    @Override
    public SnvTableElement that() {
        return this;
    }
    @Override
    public SnvReport[] selection() {
        return Arrays.stream(sheet.changed()).map(d->{
            String analysis = d.get("analysis");
            String variant = d.get("variant");
            String report = d.get("report");
            if("-".equals(report) || "".equals(report)) report = null;
            return new SnvReport().variant(analysis + ":" + variant).classification(report);
        }).toArray(SnvReport[]::new);
    }
    private final Set<SelectionChangeEventListener<SnvReport[]>> selectionChangeEventListeners = new HashSet<>();
    @Override
    public HandlerRegistration onSelectionChange(SelectionChangeEventListener<SnvReport[]> selectionChangeEventListener) {
        selectionChangeEventListeners.add(selectionChangeEventListener);
        return ()->selectionChangeEventListeners.remove(selectionChangeEventListener);
    }
    private final Set<StateChangeEventListener<SheetState>> stateChangeEventListeners = new HashSet<>();
    @Override
    public Collection<StateChangeEventListener<SheetState>> listeners() {
        return stateChangeEventListeners;
    }
    @Override
    public SheetState state() {
        return sheet.state();
    }

    @lombok.Data
    @Accessors(fluent = true)
    public static final class SnvReport {
        private String variant;
        private String classification;
    }
}
