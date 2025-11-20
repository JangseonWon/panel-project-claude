package com.gcgenome.lims.client.worklist;

import com.gcgenome.lims.dto.Request;
import com.gcgenome.lims.util.DataTransformUtil;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnText;

import java.util.Arrays;
import java.util.function.BiFunction;

public class ReceptGridElement extends HTMLElementBuilder<HTMLDivElement, ReceptGridElement> {
	private final SheetElement sheet;
	private Request[] data;
	public static ReceptGridElement build() {
		var  config = SheetElement.builder()
				.autoColSize(true)
				.autoRowSize(false)
				.viewportColumnRenderingOffset(100.0)
				.rowHeaderWidth(30)
				.rowHeaders(false)
				.manualColumnMove(false)
				.manualColumnResize(true)
				.columns(
						text("의뢰일").build(),
						text("검체번호").build(),
						text("검사명").build(),
						text("의뢰기관").build(),
						text("MRN").build(),
						text("수진자명").build(),
						text("수진자코드").build(),
						text("성별").build(),
						text("검체타입").build(),
						text("지놈예정일").build(),
						text("TAT").build(),
						text("Remark").build()
				).data(new Data[] {})
				.stretchH("all");
		return new ReceptGridElement(config.build());
	}
	private static ColumnText text(String name){
		return text(name, name);
	}
	private static ColumnText text(String id, String name){
		return ColumnBuilder.text(id, 10, 100).name(name).horizontal("center").vertical("middle").readOnly(true);
	}
	private ReceptGridElement(SheetElement e) {
		super(e);
		this.sheet = e;
	}
	public void refresh() {
		sheet.refresh();
	}
	public <T> boolean select(T value, BiFunction<T, Request, Boolean> comparison) {
		if(data == null || data.length <= 0) return false;
		int min = -1;
		int max = -1;
		for(int i = 0; i < data.length; ++i) {
			if(comparison.apply(value, data[i])) max = i;
			else if(min > 0) break;
			if(min < 0 && max > 0) min = max;
		}
		if(min > 0) {
			sheet.selectRows(min, max);
			return true;
		} else return false;
	}
	public ReceptGridElement update(Request[] data) {
		try {
			this.data = data;
			if (sheet != null) sheet.values(Arrays.stream(data).map(this::map).toArray(Data[]::new));
			return that();
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	private Data map(Request dto) {
		Data data = Data.create(String.valueOf(dto.sample()));
		data.initialize("의뢰일", DataTransformUtil.formatDate(dto.dateRequest()));
		data.initialize("검체번호", DataTransformUtil.formatSampleId(dto.sample()));
		data.initialize("검사명", dto.serviceName());
		data.initialize("의뢰기관", dto.customerName());
		data.initialize("MRN", dto.mrn());
		data.initialize("수진자명", dto.patientName());
		data.initialize("수진자코드", dto.patientCode());
		data.initialize("성별", DataTransformUtil.toSex(dto.patientCode()));
		data.initialize("검체타입", dto.sampleType());
		data.initialize("지놈예정일", DataTransformUtil.formatDate(dto.dateDuePublish()));
		data.initialize("TAT", dto.tat()!=null?String.valueOf(dto.tat()):null);
		data.initialize("Remark", dto.remark());
		return data;
	}
	@Override
	public ReceptGridElement that() {
		return this;
	}
}
