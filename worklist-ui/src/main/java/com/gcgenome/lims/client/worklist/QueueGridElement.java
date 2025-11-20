package com.gcgenome.lims.client.worklist;

import com.gcgenome.lims.dto.Sheet;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.gcgenome.lims.api.WorklistApi;
import com.gcgenome.lims.dto.Work;
import com.gcgenome.lims.dto.WorklistTemplate;
import com.gcgenome.lims.util.DataTransformUtil;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.SheetElementSelectableMulti;
import net.sayaya.ui.chart.SheetState;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.event.HasStateChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.div;

public class QueueGridElement extends HTMLElementBuilder<HTMLDivElement, QueueGridElement> implements HasSelectionChangeHandlers<Work[]>, HasStateChangeHandlers<SheetState> {
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final SheetElement.SheetConfiguration config;
	private final SheetElement elemSheet;
	private final SheetElementSelectableMulti selected;
	private WorklistTemplate template;
	private Map<String, Work> values;
	public static QueueGridElement build() {
		return new QueueGridElement(div());
	}
	private QueueGridElement(HTMLContainerBuilder<HTMLDivElement> e) {
		super(e);
		_this = e;
		this.config = SheetElement.builder()
				.rowHeaders(false)
				.autoColSize(true)
				.autoRowSize(false)
				.manualColumnMove(true)
				.manualColumnResize(true)
				.stretchH("all")
				.data(new Data[0]);
		this.elemSheet = config.build();
		this.selected = SheetElementSelectableMulti.wrap(elemSheet);
		WorklistApi.template(this::layout);
	}
	private void layout(WorklistTemplate template) {
		this.template = template;
		template.values(Arrays.stream(template.values()).filter(c->!c.name().equals("#")).toArray(Sheet.ColumnDefinition[]::new));
		_this.textContent("");
		_this.add(elemSheet);
	}
	public void refresh() {
		elemSheet.refresh();
	}
	public QueueGridElement update(Work[] data) {
		try {
			this.values = Arrays.stream(data).collect(Collectors.toMap(Work::id, w->w));
			elemSheet.values(Arrays.stream(data).map(this::map).toArray(Data[]::new));
			return that();
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	private Data map(Work dto) {
		Data data = Data.create(dto.id());
		Arrays.stream(template.columns())
			  .forEach(def->{
				  String name = def.id();
				  String value = map(def, dto);
				  data.initialize(name, value);
			  });
		return data;
	}
	private String map(Sheet.ColumnDefinition def, Work dto) {
		if("Request".equals(def.name())) return DataTransformUtil.formatSampleId(dto.sample());
		else if("ID".equals(def.name())) return dto.id();
		else if("검사명".equals(def.name())) return dto.serviceName();
		else if("수진자명".equals(def.name())) return dto.patientName();
		else if("접수일".equals(def.name())) return dto.dateRequest()!=null?String.valueOf(dto.dateRequest()):null;
		else if("검체타입(1)".equals(def.name())) return dto.sampleType();
		else if("완료예상일".equals(def.name())) return dto.dateDue()!=null?String.valueOf(dto.dateDue()):null;
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
	@Override
	public QueueGridElement that() {
		return this;
	}

	@Override
	public Work[] selection() {
		return Arrays.stream(selected.selection()).map(Data::idx).map(values::get).toArray(Work[]::new);
	}
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
		// return elemSheet.state();
		return null;
	}
}
