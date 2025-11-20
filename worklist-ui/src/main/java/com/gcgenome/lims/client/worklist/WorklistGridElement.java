package com.gcgenome.lims.client.worklist;

import com.gcgenome.lims.api.WorklistApi;
import com.gcgenome.lims.dto.Sheet;
import com.gcgenome.lims.dto.Worklist;
import com.gcgenome.lims.dto.WorklistTemplate;
import com.gcgenome.lims.util.SheetColumnConverter;
import elemental2.dom.*;
import lombok.Builder;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.Dialog;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.*;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.event.HasStateChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jboss.elemento.Elements.div;

@Setter
@Accessors(fluent=true)
public class WorklistGridElement extends HTMLElementBuilder<HTMLDivElement, WorklistGridElement> implements HasSelectionChangeHandlers<Worklist[]>, HasStateChangeHandlers<SheetState> {
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private SheetElement sheet;
	private SheetElementSelectableMulti selection;
	private WorklistTemplate template;
	private Map<Integer, Worklist> values;
	private Set<String> columnIds;
	public WorklistGridElement() {
		this(div());
	}
	public WorklistGridElement(HTMLContainerBuilder<HTMLDivElement> e) {
		super(e);
		_this = e;
		WorklistApi.template(this::layout);
	}
	private void layout(WorklistTemplate template) {
		this.template = template;
		columnIds = Arrays.stream(template.values()).map(Sheet.ColumnDefinition::id).collect(Collectors.toSet());
		_this.textContent("");
		template.values(Arrays.stream(template.values()).peek(c->{
			if(c.name().equals("Title")) c.href("worklist.html#[\"worklist\"]").target("_self()");
		}).toArray(Sheet.ColumnDefinition[]::new));

		Column[] columns = Arrays.stream(template.values()).map(SheetColumnConverter::convert).toArray(Column[]::new);
		var builder = SheetElement.builder()
				.autoColSize(true)
				.autoRowSize(false)
				.viewportColumnRenderingOffset(100.0)
				.rowHeaderWidth(30)
				.rowHeaders(false)
				.manualColumnMove(false)
				.manualColumnResize(true)
				.data(new Data[] {})
				.columns(columns)
				.colWidths(new Double[]{20.0, 80.0, 200.0, 80.0, 50.0, 50.0, 100.0})
				.stretchH("all");
		sheet = builder.build();
		selection = SheetElementSelectableMulti.wrap(sheet);
		Dialog config = Dialog.simple("A").add(ButtonElement.flat().text("A"));
		sheet.element().appendChild(config.element());
		sheet.element().addEventListener("click", evt->{
			HTMLElement target = (HTMLElement) evt.target;
			if(target.classList.contains("htCore")) {
				HTMLInputElement check = (HTMLInputElement) target.querySelector(".select-all-header-checkbox");
				check.checked = !check.checked;
				Arrays.stream(builder.data()).forEach((d) -> d.select(check.checked));
				sheet.element().getElementsByClassName("row-header-checkbox").asList().forEach((e2) ->
						((HTMLInputElement)e2.firstElementChild).checked = check.checked
				);
				sheet.element().dispatchEvent(new CustomEvent("selection-change"));
			}
		});
		SelectionChangeEventListener<Data[]> wrapper = evt->{
			Worklist[] selection = Arrays.stream(evt.selection()).map(Data::idx).map(Integer::valueOf).map(values::get).toArray(Worklist[]::new);
			SelectionChangeEvent<Worklist[]> evt2 = SelectionChangeEvent.event(evt.event(), selection);
			for(SelectionChangeEventListener<Worklist[]> listener: selectionChangeEventListeners) listener.handle(evt2);
		};
		selection.onSelectionChange(wrapper);
		_this.add(sheet);
	}
	public WorklistGridElement update(Worklist[] data) {
		if(data==null) return that();
		try {
			this.values = Arrays.stream(data).peek(d->DomGlobal.console.log(d)).collect(Collectors.toMap(Worklist::worklist, w->w));
			if (sheet != null) sheet.values(Arrays.stream(data).map(this::map).toArray(Data[]::new));
			return that();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	public WorklistGridElement insert(Worklist work) {
		values.put(work.worklist(), work);
		Data convert = map(work);
		sheet.values(Stream.concat(Stream.of(convert), Arrays.stream(sheet.values())).toArray(Data[]::new)).refresh();
		return that();
	}
	public WorklistGridElement update(Worklist work) {
		Data convert = map(work);
		Arrays.stream(sheet.values())
			  .filter(d->d.idx().equals(work.worklist().toString()))
			  .findFirst()
			  .ifPresent(data->{
				for(String id: columnIds) data.initialize(id, convert.get(id));
				sheet.refresh();
			  });
		return that();
	}
	private Data map(Worklist dto) {
		Data data = Data.create(String.valueOf(dto.worklist()));
		data.put("worklist", String.valueOf(dto.worklist()));
		Arrays.stream(template.values())
			  .forEach(def->{
				  String name = def.id();
				  String value = map(def, dto);;
				  data.initialize(name, value);
			  });
		data.onValueChange(evt->{
			var isChanged = Arrays.stream(sheet.values()).anyMatch(Data::isChanged);
			if(isChanged && state!=SheetState.CHANGED) {
				state = SheetState.CHANGED;
				fireStateChangeEvent();
			} else if(!isChanged && state!=SheetState.INITIALIZED) {
				state = SheetState.INITIALIZED;
				fireStateChangeEvent();
			}
		});
		return data;
	}
	public WorklistReference[] changes() {
		return Arrays.stream(sheet.values()).filter(Data::isChanged)
					 .map(data-> WorklistReference.builder()
												  .worklist(Integer.parseInt(data.idx()))
												  .values(columnIds.stream().filter(data::isChanged).collect(Collectors.toMap(id->{
								 	if("953b8dc4-d2e4-48a7-a367-55bce333286d".equalsIgnoreCase(id)) return "title";
								 	else return id;
								 }, id->data.get(id))))
												  .build())
					 .filter(r->!r.values().isEmpty())
					 .toArray(WorklistReference[]::new);
	}
	private SheetState state = null;
	private final Set<SelectionChangeEventListener<Worklist[]>> selectionChangeEventListeners = new HashSet<>();
	@Override
	public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Worklist[]> selectionChangeEventListener) {
		selectionChangeEventListeners.add(selectionChangeEventListener);
		return ()->selectionChangeEventListeners.remove(selectionChangeEventListener);
	}
	@Override
	public SheetState state() {
		return state;
	}
	@Override
	public Worklist[] selection() {
		return Arrays.stream(selection.selection()).map(Data::idx).map(Integer::valueOf).map(values::get).toArray(Worklist[]::new);
	}
	/*
	Worklist Column 예약어
	Create at, Title, Sample, Create by, State
	 */
	private String map(Sheet.ColumnDefinition def, Worklist dto) {
		if("#".equals(def.name())) return String.valueOf(dto.worklist());
		if("Create at".equals(def.name())) return String.valueOf(dto.createTime());
		if("Sample".equals(def.name())) return String.valueOf(dto.sampleCnt());
		if("Title".equals(def.name())) return dto.title();
		if("State".equals(def.name())) return dto.state();
		if("Create by".equals(def.name())) return dto.user();
		return dto.get(def.id());
	}
	@Override
	public WorklistGridElement that() {
		return this;
	}

	private final Set<StateChangeEventListener<SheetState>> stateChangeEventListeners = new HashSet<>();
	@Override
	public Collection<StateChangeEventListener<SheetState>> listeners() {
		return stateChangeEventListeners;
	}
}
