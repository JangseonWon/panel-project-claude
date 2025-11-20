package com.gcgenome.lims.client.worklist;

import com.gcgenome.lims.api.*;
import com.gcgenome.lims.client.AbstractScene;
import com.gcgenome.lims.client.Router;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.dto.RequestReference;
import com.gcgenome.lims.ui.IconElement;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.regexp.shared.RegExp;
import com.gcgenome.lims.client.Effect;
import com.gcgenome.lims.dto.Work;
import com.gcgenome.lims.util.DataTransformUtil;
import elemental2.core.JsDate;
import elemental2.dom.CSSProperties;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.BreadcrumbElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.TextFieldElement;
import net.sayaya.ui.chart.SheetState;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HTMLContainerBuilder;
import org.jboss.elemento.IsElement;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.jboss.elemento.Elements.label;

public class WorkElement extends AbstractScene<WorkElement> {
	private final HTMLContainerBuilder<HTMLLabelElement> title = label().add("Worklist");
	private final BreadcrumbElement breadcumb = BreadcrumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
		RouteApi.location("", true, false);
	}).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
	.add("패널검사", evt->{
		evt.preventDefault();
		evt.stopPropagation();
	}).add("Worklist", evt->{
		evt.preventDefault();
		evt.stopPropagation();
		Router.location("", true);
	});
	private final ButtonElement toAppendMode = ButtonElement.outline().css("button").text("Add").before(IconElement.icon(IconElement.Type.Light, "fa-qrcode"));
	private final ButtonElement toQueueMode = ButtonElement.outline().css("button").text("Queue").before(IconElement.icon(IconElement.Type.Brands, "fa-stack-overflow"));
	private final ButtonElement toListMode = ButtonElement.outline().css("button").text("Complete").before(IconElement.icon(IconElement.Type.Light, "fa-check")).style("display: inline-block; align-self: flex-end;");
	private final ButtonElement toListMode2 = ButtonElement.outline().css("button").text("Complete").before(IconElement.icon(IconElement.Type.Light, "fa-check")).style("display: inline-block; align-self: flex-end;");
	private final ButtonElement delete = ButtonElement.outline().css("button").text("Delete").before(IconElement.icon(IconElement.Type.Light, "fa-comment-alt-times"));
	private final ButtonElement print = ButtonElement.outline().css("button").text("Print").before(IconElement.icon(IconElement.Type.Light, "fa-print"));
	private final ButtonElement split = ButtonElement.outline().css("button").text("Split").before(IconElement.icon(IconElement.Type.Light, "fa-page-break"));
	private final ButtonElement save = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Light, "fa-save"));
	private final ButtonElement next = ButtonElement.outline().css("button").text("DNA QC").before(IconElement.icon(IconElement.Type.Regular, "fa-vial"));
	private final ButtonElement search = ButtonElement.outline().css("button").text("Search Request").before(IconElement.icon(IconElement.Type.Light, "fa-search")).style("display: inline-block; align-self: flex-end;");
	private final ButtonElement pop = ButtonElement.outline().css("button").text("Add").before(IconElement.icon(IconElement.Type.Regular, "fa-cart-plus"));
	private final ButtonElement deleteQueueItem = ButtonElement.outline().css("button").text("Delete").before(IconElement.icon(IconElement.Type.Light, "fa-trash-alt"));
	private final TextFieldElement.TextFieldOutlined<JsDate> iptDateFrom = TextFieldElement.dateBox().outlined().text("Date from").value(yesterday());
	private final TextFieldElement.TextFieldOutlined<JsDate> iptDateTo = TextFieldElement.dateBox().outlined().text("Date to").value(new JsDate());
	private final TextFieldElement.TextFieldOutlined<String> iptSample = TextFieldElement.textBox().outlined().text("검체바코드").style("margin-left: 10px;");
	private final WorkGridElement grid = WorkGridElement.builder().build().style("position: absolute; top: 95px; bottom: 20px; left: 20px; right: 20px; overflow: hidden;" +
																				 "border-top: 1px solid #AAA; border-bottom: 1px solid #AAA; transition: all 200ms;");
	private final ReceptGridElement previous = ReceptGridElement.build().style("position: absolute; top: 95px; height: 0px; left: 20px; right: 20px; overflow: hidden;" +
																					   "border-top: 1px solid #AAA; border-bottom: 1px solid #AAA; transition: all 200ms;");
	private final QueueGridElement queue = QueueGridElement.build().style("position: absolute; top: 95px; height: 0px; left: 20px; right: 20px; overflow: hidden;" +
																					   "border-top: 1px solid #AAA; border-bottom: 1px solid #AAA; transition: all 200ms;");
	private final IsElement<? extends HTMLElement>[] appendControls;
	private final IsElement<? extends HTMLElement>[] listControls;
	private final IsElement<? extends HTMLElement>[] queueControls;
	private Integer worklist;
	public WorkElement(Query query) {
		super(query);
		initialize();
		listControls = new IsElement[] {
			controlPanelElements()[0], controlPanelElements()[1], controlPanelElements()[2]
		};
		appendControls = new IsElement[] {
			controlPanelElements()[3], controlPanelElements()[4], controlPanelElements()[5]
		};
		queueControls = new IsElement[] {
			controlPanelElements()[6]
		};
		hideControls(appendControls);
		hideControls(queueControls);
		save.onClick(evt->save());
		next.onClick(evt->checkout());
		pop.onClick(evt->pop());
		deleteQueueItem.onClick(evt->deleteQueueItem());
		grid.onSelectionChange(evt->{
			boolean selected = evt.selection().length > 0;
			delete.enabled(selected);
		});
		queue.onSelectionChange(evt->{
			boolean selected = evt.selection().length > 0;
			pop.enabled(selected);
			deleteQueueItem.enabled(selected);
		});
		grid.onStateChange(evt->save.enabled(evt.state() == SheetState.CHANGED));
		delete.enabled(false);
		save.enabled(false);

		//delete.onClick(evt->grid.delete());
		toAppendMode.onClick(evt->appendMode());
		toQueueMode.onClick(evt->queueMode());
		toListMode.onClick(evt->listMode());
		toListMode2.onClick(evt->listMode());
		search.onClick(evt->updatePrevious());
		RegExp iptSampleAllowKey = RegExp.compile("[0-9-]{1}");
		iptSample.on(EventType.keypress, evt->{
			if(iptSample.value() == null) return;
			if(!"Enter".equals(evt.key) && !iptSampleAllowKey.test(evt.key)) {
				evt.stopImmediatePropagation();
				evt.preventDefault();
				return;
			}
			Scheduler.get().scheduleDeferred(()->{
				String replace = iptSample.value().replace("-", "");
				if (replace.length() == 11) {
					Long value = Long.parseLong(replace);
					if (previous.select(value, (v, r) -> v.equals(r.barcode()))) Effect.SUCCESS.play();
					else Effect.FAILURE.play();
					String cast = String.valueOf(value);
					iptSample.value(cast.substring(0, 4) + "-" + cast.substring(4, 7) + "-" + cast.substring(7));
				} else if (replace.length() == 15) {
					Long value = Long.parseLong(replace);
					if (previous.select(value, (v, r) -> v.equals(r.sample()))) Effect.SUCCESS.play();
					else Effect.FAILURE.play();
					iptSample.value(DataTransformUtil.formatSampleId(value));
				}
			});
		});
	}
	private static JsDate yesterday() {
		JsDate today = new JsDate();
		JsDate yesterday = new JsDate(today);
		yesterday.setDate(yesterday.getDate()-1);
		yesterday.setHours(0, 0, 0, 0);
		return yesterday;
	}
	private void appendMode() {
		grid.element().style.top = "calc(50% + 40px)";
		previous.element().style.height = CSSProperties.HeightUnionType.of("calc(50% - 70px)");
		showControls(appendControls);
		hideControls(listControls);
		hideControls(queueControls);
		iptSample.focus();
		updatePrevious();
		Scheduler.get().scheduleFixedDelay(()->{
			grid.refresh();
			previous.refresh();
			return false;
		}, 210);
	}
	private void queueMode() {
		grid.element().style.top = "calc(50% + 40px)";
		queue.element().style.height = CSSProperties.HeightUnionType.of("calc(50% - 70px)");
		showControls(queueControls);
		hideControls(appendControls);
		hideControls(listControls);
		pop.enabled(false);
		deleteQueueItem.enabled(false);
		updateQueue();
		Scheduler.get().scheduleFixedDelay(()->{
			grid.refresh();
			queue.refresh();
			return false;
		}, 210);
	}
	private void listMode() {
		grid.element().style.top = "95px";
		previous.element().style.height = CSSProperties.HeightUnionType.of("0px");
		queue.element().style.height = CSSProperties.HeightUnionType.of("0px");
		showControls(listControls);
		hideControls(appendControls);
		hideControls(queueControls);
		Scheduler.get().scheduleFixedDelay(()->{
			grid.refresh();
			return false;
		}, 210);
	}
	private void showControls(IsElement<? extends HTMLElement>[] controlPanels) {
		for(var controlPanel: controlPanels) controlPanel.element().style.display = "flex";
	}
	private void hideControls(IsElement<? extends HTMLElement>[] controlPanels) {
		for(var controlPanel: controlPanels) controlPanel.element().style.display = "none";
	}
	@Override
	protected IconElement icon() {
		return IconElement.icon(IconElement.Type.Light, "fa-clipboard-list");
	}
	@Override
	protected HTMLContainerBuilder<HTMLLabelElement> title() {
		return title;
	}
	@Override
	protected BreadcrumbElement breadcrumb() {
		return breadcumb;
	}
	@Override
	protected IsElement<?>[][] controls() {
		return new IsElement<?>[][]{
			new IsElement<?>[]{ next },
			new IsElement<?>[]{ save, toAppendMode, toQueueMode, delete },
			new IsElement<?>[]{ print, split },
			new IsElement<?>[]{ iptSample},
			new IsElement<?>[]{ iptDateFrom, label("~").style("line-height: 56px; margin-left: 2px; margin-right: 2px;"), iptDateTo},
			new IsElement<?>[]{ search, toListMode },
			new IsElement<?>[]{ pop, deleteQueueItem, toListMode2 }
		};
	}
	@Override
	protected IsElement<?>[] contents() {
		return new IsElement<?>[] {
			previous, queue, grid
		};
	}
	public WorkElement parent(int idx) {
		this.worklist = idx;
		while(breadcumb.element().childElementCount > 5) ((HTMLElement)breadcumb.element().childNodes.getAt(5)).remove();
		breadcumb.add(String.valueOf(idx), evt->{
			evt.preventDefault();
			evt.stopPropagation();
			Router.location(String.valueOf(idx), false);
		});
		return that();
	}
	@Override
	public void update() {
		if(worklist !=null) WorklistApi.works(worklist, query().sortBy("serial").asc(true), grid::update);
	}
	private void updatePrevious() {
		Query query = new Query().page(0).limit(1000).sortBy("sample").asc(true)
								 .filters(new Query.Filter[]{
								 		new Query.Filter().key("from").value(String.valueOf(iptDateFrom.value().getTime())),
										new Query.Filter().key("to").value(String.valueOf(iptDateTo.value().getTime()))
								 });

		ReceptApi.requests(query, cb->{
			previous.update(cb.content());
		});
	}
	private void updateQueue() {
		Query query = new Query().page(0).limit(1000).sortBy("serial").asc(true);

		QueueApi.queue(query, cb->{
			queue.update(cb.content());
		});
	}
	private void save() {
		WorkChanges[] changes = grid.changed();
		if(changes.length <= 0) return;
		if(!DomGlobal.confirm("저장합니다.")) return;
		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);

		for (WorkChanges change : changes) {
			WorklistApi.save(worklist, change.sample(), change.service(), change.values(), callback -> {
				int completed = complete.incrementAndGet();
				if (completed >= changes.length) {
					ProgressApi.close();
				} else ProgressApi.progress(completed / (double) changes.length);
				if (callback != null) grid.update(callback);
			});
		}
	}
	private void checkout() {
		Work[] selection = grid.selection();
		if(selection.length <= 0) selection = grid.values();
		if(!DomGlobal.confirm("선택한 " + selection.length + "개의 검사를 시작합니다.")) return;
		AtomicInteger row = new AtomicInteger(0);
		DnaApi.checkout(worklist, Arrays.stream(selection)
										.map(w->new RequestReference().row(row.incrementAndGet()).serial(w.id())
																	  .sample(w.sample()).service(w.serviceCode()))
										.toArray(RequestReference[]::new), result->{
			DomGlobal.alert("생성 완료");
		});
	}
	private void pop() {
		Work[] selection = queue.selection();
		if(selection.length <= 0) return;
		if(!DomGlobal.confirm("선택한 " + selection.length + "개를 워크리스트로 이동합니다.")) return;
		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for (Work work: selection) {
			QueueApi.add(worklist, work.sample(), work.serviceCode(), work, callback->{
				int completed = complete.incrementAndGet();
				if (completed >= selection.length) {
					ProgressApi.close();
					update();
					updateQueue();
				}
				else ProgressApi.progress(completed / (double) selection.length);
			});
		}
	}
	private void deleteQueueItem() {
		Work[] selection = queue.selection();
		if(selection.length <= 0) return;
		if(!DomGlobal.confirm("선택한 " + selection.length + "개를 대기열에서 삭제합니다.")) return;
		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for (Work work: selection) {
			QueueApi.delete(work.id(), callback->{
				int completed = complete.incrementAndGet();
				if (completed >= selection.length) {
					ProgressApi.close();
					updateQueue();
				}
				else ProgressApi.progress(completed / (double) selection.length);
			});
		}
	}
	@Override
	public WorkElement that() {
		return this;
	}
}
