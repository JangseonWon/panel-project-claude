package com.greencross.lims.client.analysis;

import com.greencross.lims.api.AnalysisApi;
import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.AnalysisTemplate;
import com.greencross.lims.sheet.Data;
import com.greencross.lims.sheet.Sheet;
import com.greencross.lims.sheet.SheetState;
import com.greencross.lims.sheet.SpreadSheet;
import com.greencross.lims.sheet.function.ColumnHeaderRenderer;
import elemental2.core.JsArray;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import lombok.Builder;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.event.HasStateChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jboss.elemento.Elements.div;

@Setter
@Accessors(fluent=true)
public class AnalysisGridElement extends HTMLElementBuilder<HTMLDivElement, AnalysisGridElement> implements HasSelectionChangeHandlers<Analysis[]>, HasStateChangeHandlers<SheetState> {
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private Sheet sheet;
	private AnalysisTemplate template;
	private Map<Integer, Analysis> values;
	private Set<String> columnIds;
	@Builder
	private AnalysisGridElement() {
		this(div());
	}
	private AnalysisGridElement(HtmlContentBuilder<HTMLDivElement> e) {
		super(e);
		_this = e;
		AnalysisApi.template(this::layout);
	}
	private void layout(AnalysisTemplate template) {
		this.template = template;
		columnIds = Arrays.stream(template.columns()).map(com.greencross.lims.dto.Sheet.ColumnDefinition::id).collect(Collectors.toSet());
		_this.textContent("");
		SpreadSheet.SheetBuilder builder = SpreadSheet.builder()
				.autoColSize(true)
				.autoRowSize(false)
				.rowHeaderWidth(30, 30)
				.manualColumnMove(true)
				.manualColumnResize(true)
				.data(new Data[] {})
				.stretchH("all");
		sheet = Sheet.build(builder, template.columns());
		builder.afterGetColumnHeaderRenderers(renderers->{
			ColumnHeaderRenderer defaultRenderer = renderers[0];
			ColumnHeaderRenderer proxy = (row, TH)  ->{
				if(row == -2) {
					TH.innerHTML = "<input class='select-all-header-checkbox' type='checkbox' style='vertical-align: middle;margin: 0px;'/>";
				} else defaultRenderer.apply(row, TH);
			};
			renderers[0] = proxy;
		}).afterGetRowHeaderRenderers(renderers->{
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
			} else if(target.classList.contains("select-all-header-checkbox")) {
				HTMLInputElement checkbox = (HTMLInputElement)target;
				Arrays.stream(builder.data()).forEach(d->d.select(checkbox.checked));
				sheet.element().getElementsByClassName("row-header-checkbox").asList().forEach(e->((HTMLInputElement)e).checked = checkbox.checked);
			}
		});
		HasSelectionChangeHandlers.SelectionChangeEventListener<Data[]> wrapper = evt->{
			Analysis[] selection = Arrays.stream(evt.selection()).map(Data::idx).map(values::get).toArray(Analysis[]::new);
			HasSelectionChangeHandlers.SelectionChangeEvent<Analysis[]> evt2 = HasSelectionChangeHandlers.SelectionChangeEvent.event(evt.event(), selection);
			for(HasSelectionChangeHandlers.SelectionChangeEventListener<Analysis[]> listener: selectionChangeEventListeners) listener.handle(evt2);
		};
		sheet.onSelectionChange(wrapper);
		sheet.onStateChange(evt->{
			for(HasStateChangeHandlers.StateChangeEventListener<SheetState> listener: stateChangeEventListeners) listener.handle(evt);
		});
		_this.add(sheet);
	}
	public void refresh() {
		sheet.refresh();
	}
	public AnalysisGridElement insert(Analysis analysis) {
		values.put(analysis.row(), analysis);
		Data convert = map(analysis);
		sheet.value(Stream.concat(Arrays.stream(sheet.value()), Stream.of(convert)).toArray(Data[]::new)).refresh();
		return that();
	}
	public AnalysisGridElement update(Analysis[] data) {
		try {
			this.values = Arrays.stream(data).collect(Collectors.toMap(Analysis::row, w->w));
			if (sheet != null) sheet.value(Arrays.stream(data).map(this::map).toArray(Data[]::new));
			return that();
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	public AnalysisGridElement update(Analysis analysis) {
		Data convert = map(analysis);
		Arrays.stream(sheet.value())
				.filter(d->d.idx().equals(analysis.row().toString()))
				.findFirst()
				.ifPresent(data->{
					for(String id: columnIds) data.initialize(id, convert.get(id));
					sheet.refresh();
				});
		return that();
	}
	private Data map(Analysis dto) {
		Data data = new Data(String.valueOf(dto.row()));
		if(dto.sample()!=null) data.put("id", String.valueOf(dto.sample()));
		Arrays.stream(template.columns())
			  .forEach(def->{
				  String name = def.id();
				  String value = dto.get(def.id());
				  data.initialize(name, value);
			  });
		return data.initialize("76db4f6e-c406-4e2f-a62d-e45f6e90d0e2", dto.sort())
				   .initialize("2d4833f7-389c-4211-ba07-40b36369d3ca", dto.serial());
	}
	public void put(int row, String key, String value) {
		String idx = String.valueOf(row);
		Arrays.stream(sheet.value())
			  .filter(d->d.idx().equals(idx))
			  .findFirst()
			  .ifPresent(data->data.initialize(key, value));
	}
	private Analysis map(Data data) {
		Analysis w = values.get(data.idx());
		for(com.greencross.lims.dto.Sheet.ColumnDefinition def: template.columns()) {
			String id = def.id();
			if(data.isChanged(id)) w.put(id, data.get(id));
		}
		return w;
	}
	public AnalysisReference[] changes() {
		return Arrays.stream(sheet.changed())
				.map(data->AnalysisReference.builder()
						.row(Integer.parseInt(data.idx()))
						.values(columnIds.stream().filter(data::isChanged).collect(Collectors.toMap(id->{
							if("76db4f6e-c406-4e2f-a62d-e45f6e90d0e2".equalsIgnoreCase(id)) return "sort";
							else if("2d4833f7-389c-4211-ba07-40b36369d3ca".equalsIgnoreCase(id)) return "serial";
							else return id;
						}, id->data.get(id))))
						.build())
				.filter(r->!r.values().isEmpty())
				.toArray(AnalysisReference[]::new);
	}
	public Analysis[] values() {
		return Arrays.stream(sheet.value()).map(this::map).toArray(Analysis[]::new);
	}
	@Override
	public AnalysisGridElement that() {
		return this;
	}
	@Override
	public Analysis[] selection() {
		return Arrays.stream(sheet.selection()).map(Data::idx).map(i->Integer.parseInt(i)).map(values::get).toArray(Analysis[]::new);
	}
	private final Set<SelectionChangeEventListener<Analysis[]>> selectionChangeEventListeners = new HashSet<>();
	@Override
	public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Analysis[]> selectionChangeEventListener) {
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
