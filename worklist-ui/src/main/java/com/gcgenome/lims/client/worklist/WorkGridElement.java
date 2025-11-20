package com.gcgenome.lims.client.worklist;

import com.gcgenome.lims.dto.Sheet;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.gcgenome.lims.api.WorklistApi;
import com.gcgenome.lims.dto.Work;
import com.gcgenome.lims.dto.WorklistTemplate;
import com.gcgenome.lims.util.DataTransformUtil;
import com.gcgenome.lims.util.SheetColumnConverter;
import elemental2.dom.*;
import lombok.Builder;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.*;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.event.HasStateChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.div;

@Setter
@Accessors(fluent=true)
public class WorkGridElement extends HTMLElementBuilder<HTMLDivElement, WorkGridElement> implements HasSelectionChangeHandlers<Work[]>, HasStateChangeHandlers<SheetState> {
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private SheetElement sheet;
	private SheetElementSelectableMulti selection;
	private WorklistTemplate template;
	private List<String> columnIds;
	private Map<String, Work> values;
	@Builder private WorkGridElement() {
		this(div());
	}

	public WorkGridElement(HTMLContainerBuilder<HTMLDivElement> e) {
		super(e);
		_this = e;
		WorklistApi.template(this::layout);
	}
	private void layout(WorklistTemplate template) {
		this.template = template;
		columnIds = Arrays.stream(template.columns()).map(Sheet.ColumnDefinition::id).collect(Collectors.toList());
		Column[] columns = Arrays.stream(template.columns()).map(SheetColumnConverter::convert).toArray(Column[]::new);
		_this.textContent("");
		var config = SheetElement.builder()
				.autoColSize(true)
				.autoRowSize(false)
				.viewportColumnRenderingOffset(100.0)
				.rowHeaderWidth(30)
				.rowHeaders(false)
				.manualColumnMove(false)
				.manualColumnResize(true)
				.columns(columns)
				.data(new Data[] {})
				.colWidths(new Double[]{100.0, 80.0, 180.0, 150.0, 80.0, 80.0, 400.0, 230.0, 250.0, 80.0, 150.0, 80.0, 150.0, 150.0})
				.stretchH("all");
		sheet = config.build();
		selection = SheetElementSelectableMulti.wrap(sheet);
		sheet.element().addEventListener("click", evt->{
			HTMLElement target = (HTMLElement) evt.target;
			if(target.classList.contains("htCore")) {
				HTMLInputElement check = (HTMLInputElement) target.querySelector(".select-all-header-checkbox");
				check.checked = !check.checked;
				Arrays.stream(config.data()).forEach(d -> d.select(check.checked));
				sheet.element().getElementsByClassName("row-header-checkbox").asList().forEach(e ->
						((HTMLInputElement)e.firstElementChild).checked = check.checked
				);
				sheet.element().dispatchEvent(new CustomEvent("selection-change"));
			}
		});
		SelectionChangeEventListener<Data[]> wrapper = evt->{
			Work[] selection = Arrays.stream(evt.selection()).map(Data::idx).map(values::get).toArray(Work[]::new);
			SelectionChangeEvent<Work[]> evt2 = SelectionChangeEvent.event(evt.event(), selection);
			for(SelectionChangeEventListener<Work[]> listener: selectionChangeEventListeners) listener.handle(evt2);
		};
		selection.onSelectionChange(wrapper);
		_this.add(sheet);
	}
	public void refresh() {
		sheet.refresh();
	}
	public WorkGridElement update(Work data) {
		Data convert = map(data);
		Arrays.stream(sheet.values())
			  .filter(d->d.idx().equals(convert.idx()))
			  .findFirst()
			  .ifPresent(data2->{
				  data2.initialize("serial", convert.get("serial"));
				  for(String id: columnIds) data2.initialize(id, convert.get(id));
				  sheet.refresh();
			  });
		return that();
	}
	public WorkGridElement update(Work[] data) {
		try {
			this.values = Arrays.stream(data).collect(Collectors.toMap(Work::id, w->w));
			if (sheet != null) sheet.values(Arrays.stream(data).map(this::map).toArray(Data[]::new));
			return that();
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	private Data map(Work dto) {
		Data data = Data.create(dto.sample() + "/" + dto.serviceCode());
		data.initialize("serial", dto.id());
		data.initialize("sample", String.valueOf(dto.sample()));
		data.initialize("service", dto.serviceCode());
		Arrays.stream(template.columns())
			  .forEach(def->{
				  String name = def.id();
				  String value = map(def, dto);
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
	public WorkChanges[] changed() {
		return Arrays.stream(sheet.values()).filter(Data::isChanged)
				.map(data-> WorkChanges.builder().sample(Long.parseLong(data.get("sample"))).service(data.get("service"))
						.values(columnIds.stream().filter(data::isChanged).collect(Collectors.toMap(id->{
							if("9baf6c92-bb90-4309-9391-212ef8702b03".equalsIgnoreCase(id)) return "serial";
							else return id;
						}, data::get))).build())
				.filter(r->!r.values().isEmpty())
				.toArray(WorkChanges[]::new);
	}
	public Work[] values() {
		return Arrays.stream(sheet.values()).map(this::map).toArray(Work[]::new);
	}
	/*
	Worklist Column 예약어
	Create at, Title, Sample, Create by, State
	 */
	private String map(Sheet.ColumnDefinition def, Work dto) {
		if("Request".equals(def.name())) return DataTransformUtil.formatSampleId(dto.sample());
		else if("ID".equals(def.name())) return dto.id();
		else if("검사명".equals(def.name())) return dto.serviceName();
		else if("검사코드".equals(def.name())) return dto.serviceCode();
		else if("수진자명".equals(def.name())) return dto.patientName();
		else if("접수일".equals(def.name())) return dto.dateRequest()!=null?String.valueOf(dto.dateRequest()):null;
		else if("검체타입(1)".equals(def.name())) return dto.sampleType();
		else if("완료예상일".equals(def.name())) return dto.dateDue()!=null?String.valueOf(dto.dateDue()):null;
		else if("지놈예정일".equals(def.name())) return dto.dateDuePublish()!=null?String.valueOf(dto.dateDuePublish()):".";
		else if("의뢰기관".equals(def.name())) return dto.customerName();
		else if("의뢰기관 등록번호".equals(def.name())) return dto.mrn();
		else if("나이".equals(def.name()) && dto.patientCode()!=null) {
			if(dto.get(def.id()) != null && dto.get(def.id()).trim().length() > 0) return dto.get(def.id());
			Date birth = DataTransformUtil.toBirth(dto.patientCode().trim());
			Date reqDate = new Date(dto.dateRequest());
			int age = (int)(CalendarUtil.getDaysBetween(birth, reqDate) / 365.2425);
			return String.valueOf(age);
		} else if("성별".equals(def.name()) && dto.patientCode()!=null) {
			if(dto.get(def.id()) != null && dto.get(def.id()).trim().length() > 0) return dto.get(def.id());
			return DataTransformUtil.toSex(dto.patientCode().trim());
		} else if("생년월일".equals(def.name()) && dto.patientCode()!=null) {
			if(dto.get(def.id()) != null && dto.get(def.id()).trim().length() > 0) return dto.get(def.id());
			DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
			return dtf.format(DataTransformUtil.toBirth(dto.patientCode().trim()));
		} else if("Remark".equals(def.name())) return dto.remark();
		else return dto.get(def.id());
	}
	private Work map(Data data) {
		Work w = values.get(data.idx());
		for(Sheet.ColumnDefinition def: template.columns()) {
			String id = def.id();
			if(data.isChanged(id)) w.put(id, data.get(id));
		}
		return w;
	}

	@Override
	public WorkGridElement that() {
		return this;
	}
	@Override
	public Work[] selection() {
		return Arrays.stream(selection.selection()).map(d->d.get("serial")).map(values::get).toArray(Work[]::new);
	}
	private SheetState state = null;
	private final Set<SelectionChangeEventListener<Work[]>> selectionChangeEventListeners = new HashSet<>();
	@Override
	public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Work[]> selectionChangeEventListener) {
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
		return state;
	}

}
