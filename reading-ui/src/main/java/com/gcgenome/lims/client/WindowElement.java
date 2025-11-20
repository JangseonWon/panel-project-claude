package com.gcgenome.lims.client;

import com.gcgenome.lims.api.AnalysisApi;
import com.gcgenome.lims.api.Callback;
import com.gcgenome.lims.dto.Analysis;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.util.DataTransformUtil;
import elemental2.dom.*;
import lombok.Builder;
import net.sayaya.ui.*;
import net.sayaya.ui.chart.Column;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnText;
import net.sayaya.ui.event.HasStateChangeHandlers;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;

import static org.jboss.elemento.Elements.div;

public class WindowElement extends HTMLElementBuilder<HTMLDivElement, WindowElement> implements HasStateChangeHandlers<WindowElement.WindowState> {
	private final HTMLContainerBuilder<HTMLLabelElement> label = org.jboss.elemento.Elements.label().css("window-label");
	private final Query query;
	private final PageElement page = PageElement.instance().show(16).idx(0L).sortable("ID", "Panel", "Batch", "#", "생성시각").sort("생성시각", false);
	private final String id;
	private boolean notCompleteOnly = true;
	private final String prefix;
	private final SheetElement.SheetConfiguration config;
	private final SheetElement elemSheet;
	@Builder private WindowElement(String id, Query query, String prefix) {
		this(div(), id, query, prefix!=null?prefix:"");
	}
	protected WindowElement(HTMLContainerBuilder<HTMLDivElement> e, String id, Query query, String prefix) {
		super(e.css("window"));
		this.id = id;
		this.query = query;
		this.prefix = prefix;
		this.config = SheetElement.builder()
				.rowHeaders(false)
				.autoColSize(true)
				.autoRowSize(false)
				.manualColumnMove(true)
				.manualColumnResize(true)
				.stretchH("all")
				.columns(buildWithStrike(column("panel", "").horizontal("right").bold(true)),
						buildWithStrike(column("Batch", "Batch").font("Nanum Gothic Coding")),
						buildWithStrike(column("#", "#").horizontal("right")),
						buildWithStrike(ColumnBuilder.link("ID", data-> this.prefix + "sample.html#"+data.get("id"))
								.name("ID").onClick(this::link).font("Nanum Gothic Coding").horizontal("center").readOnly(true)),
						buildWithStrike(column("검사명", "검사명").horizontal("left")),
						buildWithStrike(column("수검자명", "수검자명")),
						buildWithStrike(column("진행상태", "진행상태")),
						buildWithStrike(column("검사결과", "검사결과")))
				.colWidths(new Double[]{10.0, 22.0, 10.0, 50.0, 50.0, 30.0, 22.0, 22.0})
				.data(new Data[0]);
		this.elemSheet = config.build();
		page.element().style.top = "5px";
		page.element().style.paddingLeft = CSSProperties.PaddingLeftUnionType.of("16px");
		page.element().style.paddingRight = CSSProperties.PaddingRightUnionType.of("16px");
		page.element().style.position = "relative";
		page.element().style.borderTop = "1px solid #ddd";
		query.limit(page.show()).page((int) page.page());
		page.onValueChange(evt->{
			query.limit(page.show()).page((int)page.page());
			update();
		});
		label.add(id);
		e.add(label).add(div().css("window-tablebox").add(div().css("container").add(elemSheet))).add(page);
	}
	public WindowElement complteOpt(boolean opt) {
		if(notCompleteOnly!=opt) page.idx(0L);
		notCompleteOnly = opt;
		return that();
	}
	public void page1() {
		page.idx(0);
	}
	public void update() {
		update(c->{});
	}
	public void update(Callback<Void> callback) {
		java.util.List<Query.Filter> filters = new LinkedList<>();
		if(query.filters!=null) filters.addAll(Arrays.asList(query.filters));
		filters.add(new Query.Filter().key("except_control").value("true"));
		if(notCompleteOnly) filters.add(new Query.Filter().key("not_complete").value("true"));
		Query clone = new Query();
		clone.filters(filters.stream().toArray(Query.Filter[]::new));
		String sortKey = "file";
		boolean asc = false;
		if(page.sortBy()!=null && !page.sortBy().isEmpty()) {
			switch(page.sortBy()) {
				case "생성시각": sortKey = "sort"; break;
				case "Panel": sortKey = "panel"; break;
				case "#": sortKey = "row"; break;
				case "Batch": sortKey = "batch"; break;
				case "ID": sortKey = "sample"; break;
			}
			asc = page.isAsc();
		}
		AnalysisApi.list(id, clone.sortBy(sortKey).asc(asc).limit(page.show()).page((int)page.page()), list->{
			try {
				page.total(list.totalElement());
				update(list.content());
			} catch(Exception ignore){}
			if(callback!=null) callback.onSuccess(null);
		});
	}
	public WindowElement update(Analysis[] data) {
		try {
			if (elemSheet != null) elemSheet.values(Arrays.stream(data).map(this::map).toArray(Data[]::new));
			return that();
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	private static ColumnText column(String id, String name){
		return ColumnBuilder.text(id, 10, 100).name(name).horizontal("center").vertical("middle");
	}
	private static Column buildWithStrike(ColumnBuilder builder) {
		var origin = builder.build();
		var renderer = origin.renderer();
		return origin.renderer((sheet, td, row, col, prop, value, ci)->{
			td = (HTMLTableCellElement) renderer.render(sheet, td, row, col, prop, value, ci);
			Data data = sheet.spreadsheet.values()[row];
			if("true".equalsIgnoreCase(data.get("cancel")) || "true".equalsIgnoreCase(data.get("delete"))) {
				td.style.textDecoration = "line-through";
				td.style.color = "#CCC";
			}
			return td;
		});
	}
	public WindowElement sort(String header) {
		if("&nbsp;".equalsIgnoreCase(header)) header = "Panel";
		String sort = page.sortBy();
		if(header.equalsIgnoreCase(sort)) page.sort(header, !page.isAsc());
		else page.sort(header, page.isAsc());
		return this;
	}
	protected Data map(Analysis dto) {
		Data data = Data.create(dto.panel());
		data.initialize("sample", String.valueOf(dto.sample()));
		data.initialize("id", String.valueOf(dto.sample()));
		data.initialize("panel", dto.panel());
		data.initialize("Batch", dto.batch());
		data.initialize("#", String.valueOf(dto.row()));
		data.initialize("ID", DataTransformUtil.formatSampleId(dto.sample()));
		data.initialize("검사명", dto.serviceName());
		data.initialize("수검자명", dto.patientName());
		data.initialize("진행상태", dto.state());
		data.initialize("검사결과", dto.result());
		if("검사취소".equals(dto.state())) data.initialize("cancel", "true");
		if("검사삭제".equals(dto.state())) data.initialize("delete", "true");
		return data;
	}
	private void link(Data data){
		String link = prefix + "sample.html#"+data.get("id");
		DomGlobal.window.open(link, "sample", null);
	}
	private final Set<StateChangeEventListener<WindowState>> stateChangeEventListeners = new HashSet<>();
	@Override
	public Collection<StateChangeEventListener<WindowState>> listeners() {
		return stateChangeEventListeners;
	}
	@Override
	public WindowState state() {
		return state;
	}
	@Override
	public WindowElement that() {
		return this;
	}
	public enum WindowState {
		SUMMARY, FULL
	}
	private WindowState state;
}
