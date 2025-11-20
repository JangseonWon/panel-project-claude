package com.greencross.lims.client.sequencing;

import com.greencross.lims.api.BatchApi;
import com.greencross.lims.dto.Batch;
import com.greencross.lims.dto.BatchTemplate;
import com.greencross.lims.sheet.Data;
import com.greencross.lims.sheet.Sheet;
import com.greencross.lims.sheet.SheetState;
import com.greencross.lims.sheet.SpreadSheet;
import elemental2.core.JsArray;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import lombok.Builder;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.Dialog;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.event.HasStateChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.div;

@Setter
@Accessors(fluent=true)
public class BatchGridElement extends HTMLElementBuilder<HTMLDivElement, BatchGridElement> implements HasSelectionChangeHandlers<Batch[]>, HasStateChangeHandlers<SheetState> {
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private Sheet sheet;
	private BatchTemplate template;
	private Map<Integer, Batch> values;
	private Set<String> columnIds;
	@Builder
	private BatchGridElement() {
		this(div());
	}
	private BatchGridElement(HtmlContentBuilder<HTMLDivElement> e) {
		super(e);
		_this = e;
		BatchApi.template(this::layout);
	}
	private void layout(BatchTemplate template) {
		this.template = template;
		template.columns(Arrays.stream(template.columns()).filter(c->!c.name().equals("#")).toArray(com.greencross.lims.dto.Sheet.ColumnDefinition[]::new));
		columnIds = Arrays.stream(template.columns()).map(com.greencross.lims.dto.Sheet.ColumnDefinition::id).collect(Collectors.toSet());
		_this.textContent("");
		SpreadSheet.SheetBuilder builder = SpreadSheet.builder()
				.autoColSize(true)
				.autoRowSize(false)
				.rowHeaderWidth(30, 50)
				.manualColumnMove(true)
				.manualColumnResize(true)
				.data(new Data[] {})
				.stretchH("all");
		sheet = Sheet.build(builder, template.columns());
		builder.afterGetRowHeaderRenderers(renderers->{
			Data[] data = builder.data();
			JsArray.asJsArray(renderers).push((row, TH)->{
				boolean checked = data[row].state() == Data.DataState.SELECTED;
				TH.innerHTML = "<input class='row-header-checkbox' " +
						"idx='" + data[row].idx() + "' " + (checked?"checked ":"") +
					   "type='checkbox' style='vertical-align: middle;margin: 0px;'/>";
			}, (row, TH)->{
				TH.classList.add("worklist-header");
				TH.innerHTML = data[row].idx();
			});
		}).rowHeaders(false);
		Dialog config = Dialog.simple("A").add(ButtonElement.flat().text("A"));
		sheet.element().appendChild(config.element());
		sheet.element().addEventListener("click", evt->{
			HTMLElement target = (HTMLElement) evt.target;
			if(target.classList.contains("config")) {
				DomGlobal.console.log("Config");
				//config.
				//config.open();
			} else if(target.classList.contains("row-header-checkbox")) {
				String idx = target.getAttribute("idx");
				HTMLInputElement checkbox = (HTMLInputElement)target;
				Arrays.stream(builder.data()).filter(d->idx.equals(d.idx())).findAny().get().select(checkbox.checked);
			}
		});
		SelectionChangeEventListener<Data[]> wrapper = evt->{
			Batch[] selection = Arrays.stream(evt.selection()).map(Data::idx).map(Integer::valueOf).map(values::get).toArray(Batch[]::new);
			SelectionChangeEvent<Batch[]> evt2 = SelectionChangeEvent.event(evt.event(), selection);
			for(SelectionChangeEventListener<Batch[]> listener: selectionChangeEventListeners) listener.handle(evt2);
		};
		sheet.onSelectionChange(wrapper);
		sheet.onStateChange(evt->{
			for(StateChangeEventListener<SheetState> listener: stateChangeEventListeners) listener.handle(evt);
		});
		_this.add(sheet);
	}
	public void refresh() {
		sheet.refresh();
	}
	public BatchGridElement update(Batch[] data) {
		try {
			this.values = Arrays.stream(data).collect(Collectors.toMap(Batch::id, w->w));
			if (sheet != null) sheet.value(Arrays.stream(data).map(this::map).toArray(Data[]::new));
			return that();
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	public BatchGridElement insert(Batch batch) {
		values.put(batch.id(), batch);
		Data convert = map(batch);
		sheet.value(Stream.concat(Stream.of(convert), Arrays.stream(sheet.value())).toArray(Data[]::new)).refresh();
		return that();
	}
	public BatchGridElement update(Batch batch) {
		Data convert = map(batch);
		Arrays.stream(sheet.value())
				.filter(d->d.idx().equals(batch.id().toString()))
				.findFirst()
				.ifPresent(data->{
					for(String id: columnIds) data.initialize(id, convert.get(id));
					sheet.refresh();
				});
		return that();
	}
	private Data map(Batch dto) {
		Data data = new Data(String.valueOf(dto.id()));
		Arrays.stream(template.columns())
			  .forEach(def->{
				  String name = def.id();
				  String value = map(def, dto);;
				  data.initialize(name, value);
			  });
		return data;
	}
	public Batch[] changed() {
		Set<String> ids = Arrays.stream(template.columns()).map(com.greencross.lims.dto.Sheet.ColumnDefinition::id).collect(Collectors.toSet());
		return Arrays.stream(sheet.value()).filter(d->ids.stream().anyMatch(d::isChanged)).map(this::map).toArray(Batch[]::new);
	}
	public Batch[] values() {
		return Arrays.stream(sheet.value()).map(this::map).toArray(Batch[]::new);
	}
	private String map(com.greencross.lims.dto.Sheet.ColumnDefinition def, Batch dto) {
		if("Create at".equals(def.name())) return String.valueOf(dto.createTime());
		if("Title".equals(def.name())) return dto.title();
		if("Sample".equals(def.name())) {
			String href = "#" + dto.id();
			return JSON.stringify(new com.greencross.lims.dto.Sheet.Link().href(href).label(dto.sampleCnt() + " Samples").target("_self()"));
		}
	//	if("State".equals(def.name())) return dto.state();
		if("Create by".equals(def.name())) return dto.user();
		return dto.get(def.id());
	}
	private Batch map(Data data) {
		Batch w = values.get(Integer.parseInt(data.idx()));
		for(com.greencross.lims.dto.Sheet.ColumnDefinition def: template.columns()) {
			String id = def.id();
			if(data.isChanged(id)) w.put(id, data.get(id));
		}
		return w;
	}
	@Override
	public BatchGridElement that() {
		return this;
	}
	public BatchReference[] changes() {
		return Arrays.stream(sheet.changed())
				.map(data->BatchReference.builder()
						.batch(Integer.parseInt(data.idx()))
						.values(columnIds.stream().filter(data::isChanged).collect(Collectors.toMap(id->{
							if("953b8dc4-d2e4-48a7-a367-55bce333286d".equalsIgnoreCase(id)) return "title";
							else return id;
						}, id->data.get(id))))
						.build())
				.filter(r->!r.values().isEmpty())
				.toArray(BatchReference[]::new);
	}
	@Override
	public Batch[] selection() {
		return Arrays.stream(sheet.selection()).map(Data::idx).map(Integer::parseInt).map(values::get).toArray(Batch[]::new);
	}
	private final Set<SelectionChangeEventListener<Batch[]>> selectionChangeEventListeners = new HashSet<>();
	@Override
	public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Batch[]> selectionChangeEventListener) {
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
}
