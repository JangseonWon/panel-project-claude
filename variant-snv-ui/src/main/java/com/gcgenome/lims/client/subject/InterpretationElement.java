package com.gcgenome.lims.client.subject;

import com.gcgenome.lims.api.InterpretationReservedApi;
import com.gcgenome.lims.client.SectionElement;
import com.gcgenome.lims.client.SubjectElement;
import com.gcgenome.lims.dto.InterpretationReserved;
import com.gcgenome.lims.ui.IconElement;
import elemental2.dom.HTMLDivElement;
import elemental2.promise.Promise;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.chart.Column;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.SheetElementSelectableMulti;
import net.sayaya.ui.chart.column.ColumnBuilder;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.jboss.elemento.Elements.div;

public class InterpretationElement extends SubjectElement<InterpretationElement> {
	public static InterpretationElement build(String id, JsPropertyMap snv) {
		return new InterpretationElement(id, div());
	}
	private final Column[] columns = new Column[]{
			ColumnBuilder.string("Test").width(200).name("Test").build(),
			ColumnBuilder.text("Interpretation", 20, 180).width(800).name("Interpretation").build()
	};
	private final SheetElement.SheetConfiguration config = SheetElement.builder()
															   .autoColSize(false)
															   .autoRowSize(false)
															   .rowHeaders(false)
															   .manualColumnMove(true)
															   .manualColumnResize(true)
															   .columns(columns)
															   .data(new Data[] {}).height(200)
			.width(1200)
															   .stretchH("last");
	private final SheetElement sheet = config.build().style("border-bottom: 1px solid #AAA;");
	private final SectionElement section = SectionElement.build(IconElement.icon(IconElement.Type.Light, "fa-comment-medical"), "Interpretation reserved");
	private final ButtonElement save = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Light, "fa-save"));
	private final ButtonElement add = ButtonElement.outline().css("button").text("Add").before(IconElement.icon(IconElement.Type.Light, "fa-plus-circle"));
	private final ButtonElement delete = ButtonElement.outline().css("button").text("Delete").before(IconElement.icon(IconElement.Type.Light, "fa-times-circle")).enabled(false);
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().style("display: flex;justify-content: flex-end;align-items: center;margin-right: 20px;")
																	   .add(add).add(save).add(delete);
	private final SheetElementSelectableMulti selection = SheetElementSelectableMulti.wrap(sheet);
	protected InterpretationElement(String id, HTMLContainerBuilder<HTMLDivElement> e) {
		super(id, e);
		e.add(section)
		 .add(div().style("overflow: hidden; padding-bottom: 15px; min-height: 7px; border-top: 1px solid #AAA; margin-right: 20px; margin-left: 20px;")
				 .add(sheet))
		 .add(controller);
		add.onClick(this::append);
		save.onClick(evt->save());
		delete.onClick(evt->delete());
		selection.onSelectionChange(evt->{
			delete.enabled(evt.selection()!=null && evt.selection().length>0);
		});
	}

	@Override
	public void initialize() {
		InterpretationReservedApi.findLast(id)
							 .then(rs->{
								 if(rs!=null) {
								 	Data[] data = Arrays.stream(rs).map(this::map).toArray(Data[]::new);
								 	sheet.values(data);
								 } else sheet.values(new Data[]{});
								 return null;
							 });
	}

	private void append(Object evt) {
		Data[] data = sheet.values();
		data = Stream.concat(Arrays.stream(data), Stream.of(Data.create(""))).toArray(Data[]::new);
		sheet.values(data);
	}

	private void save() {
		Data[] data = sheet.values();
		InterpretationReserved[] reserveds = Arrays.stream(data)
												   .filter(d->d.isChanged("Test") || d.isChanged("Interpretation"))
												   .map(this::map).toArray(InterpretationReserved[]::new);
		Promise<?>[] promises = Arrays.stream(reserveds).map(s->InterpretationReservedApi.save(id, s)).toArray(Promise[]::new);
		Promise.all(promises).then(s->{
			initialize();
			return null;
		});
	}
	private void delete() {
		var selections = selection.selection();
		if(selections == null) return;
		Promise<?>[] promises = Arrays.stream(selections).map(s->InterpretationReservedApi.delete(id, map(s))).toArray(Promise[]::new);
		Promise.all(promises).then(s->{
			initialize();
			return null;
		});

	}
	private Data map(InterpretationReserved dto) {
		if(dto == null) return null;
		return Data.create(String.valueOf(dto.createAt())).initialize("Test", dto.service()).initialize("Interpretation", dto.interpretation());
	}
	private InterpretationReserved map(Data data) {
		String service = data.get("Test");
		if(service == null || service.trim().isEmpty()) service = "*";
		return new InterpretationReserved().service(service).interpretation(data.get("Interpretation"));
	}
	@Override
	public InterpretationElement that() {
		return this;
	}
}
