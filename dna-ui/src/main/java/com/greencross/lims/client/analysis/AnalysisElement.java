package com.greencross.lims.client.analysis;

import com.greencross.lims.api.*;
import com.greencross.lims.client.AbstractScene;
import com.greencross.lims.client.Router;
import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.Query;
import com.greencross.lims.sheet.SheetState;
import com.greencross.lims.ui.IconElement;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.BreadcumbElement;
import net.sayaya.ui.ButtonElement;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.jboss.elemento.Elements.label;

public class AnalysisElement extends AbstractScene<AnalysisElement> {
	private final HtmlContentBuilder<HTMLLabelElement> title = label().add("DNA");
	private final BreadcumbElement breadcumb = BreadcumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
		RouteApi.location("", true, false);
	}).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
	.add("패널검사", evt->{
		evt.preventDefault();
		evt.stopPropagation();
	}).add("DNA", evt->{
		evt.preventDefault();
		evt.stopPropagation();
		Router.location("", true);
	});
	private final ButtonElement add = ButtonElement.outline().css("button").text("Add").before(IconElement.icon(IconElement.Type.Light, "fa-plus"));
	private final ButtonElement delete = ButtonElement.outline().css("button").text("Delete").before(IconElement.icon(IconElement.Type.Light, "fa-comment-times"));
	private final ButtonElement redo = ButtonElement.outline().css("button").text("Rerun").before(IconElement.icon(IconElement.Type.Regular, "fa-redo"));
	private final ButtonElement print = ButtonElement.outline().css("button").text("Print").before(IconElement.icon(IconElement.Type.Light, "fa-print"));
	private final ButtonElement split = ButtonElement.outline().css("button").text("Split").before(IconElement.icon(IconElement.Type.Light, "fa-page-break"));
	private final ButtonElement save = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Light, "fa-save"));
	// private final Button next = ButtonElement.outline().css("button").text("Library QC").before(IconElement.icon(IconElement.Type.Regular, "fa-vials"));
	/*private final HtmlContentBuilder<HTMLTableElement> header = table().add(tbody()
			.add(tr().add(td().add(label().add("Title")))));*/
	private final AnalysisGridElement grid = AnalysisGridElement.builder().build().style("position: absolute; top: 95px; bottom: 20px; left: 20px; right: 20px; overflow: hidden;" +
																						 "border-top: 1px solid #AAA; border-bottom: 1px solid #AAAA; transition: all 200ms;");
	private Integer batch;
	public AnalysisElement(Query query) {
		super(query);
		add.onClick(evt->add());
		save.onClick(evt->save());
		delete.onClick(evt->delete());
		redo.onClick(evt->redo());
		grid.onSelectionChange(evt->{
			boolean selected = evt.selection().length > 0;
			split.enabled(selected);
			redo.enabled(selected);
			delete.enabled(selected);
		});
		grid.onStateChange(evt->{
			save.enabled(evt.state() == SheetState.CHANGED);
		});
		split.enabled(false);
		redo.enabled(false);
		delete.enabled(false);
		save.enabled(false);
	}

	@Override
	protected IconElement icon() {
		return IconElement.icon(IconElement.Type.Light, "fa-vial");
	}
	@Override
	protected HtmlContentBuilder<HTMLLabelElement> title() {
		return title;
	}
	@Override
	protected BreadcumbElement breadcumb() {
		return breadcumb;
	}
	@Override
	protected IsElement<?>[][] controls() {
		return new IsElement<?>[][]{
				// new IsElement<?>[]{ next },
				new IsElement<?>[]{ save, redo, add, delete },
				new IsElement<?>[]{ print, split }
		};
	}
	@Override
	protected IsElement<?>[] contents() {
		return new IsElement<?>[] {
			grid
		};
	}
	public AnalysisElement parent(int batch) {
		this.batch = batch;
		while(breadcumb.element().childElementCount > 5) ((HTMLElement)breadcumb.element().childNodes.getAt(5)).remove();
		breadcumb.add(String.valueOf(batch), evt->{
			evt.preventDefault();
			evt.stopPropagation();
			Router.location(String.valueOf(batch), false);
		});
		BatchApi.batch(batch, parent->{
			title.element().innerHTML = parent.title();
		});
		return that();
	}
	@Override
	public void update() {
		if(batch !=null) AnalysisApi.analysis(batch, query().sortBy("row").asc(true), grid::update);
	}
	@Override
	public AnalysisElement that() {
		return this;
	}
	private void add() {
		AnalysisApi.add(batch, grid::insert);
	}
	public void save() {
		AnalysisReference[] changes = grid.changes();
		if(changes.length <= 0) return;
		if(!DomGlobal.confirm("저장합니다.")) return;

		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		boolean hasReorder = Arrays.stream(changes).anyMatch(c->c.values().containsKey("sort"));
		for (AnalysisReference change : changes) {
			AnalysisApi.save(batch, change.row(), change.values(), callback -> {
				int completed = complete.incrementAndGet();
				if (completed >= changes.length) {
					ProgressApi.close();
					if (hasReorder) BatchApi.rebuild(batch, callback2 -> update());
				} else ProgressApi.progress(completed / (double) changes.length);
				if (callback != null) grid.update(callback);
			});
		}
	}
	public void delete() {
		Analysis[] selected = grid.selection();
		if(selected.length <= 0) return;
		if(!DomGlobal.confirm("선택한 " + selected.length + "개의 데이터를 삭제합니다.")) return;

		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for(Analysis select: selected) {
			AnalysisApi.delete(batch, select.row(), callback -> {
				int completed = complete.incrementAndGet();
				if (completed >= selected.length) {
					ProgressApi.close();
					update();
				} else ProgressApi.progress(completed / (double) selected.length);
			});
		}
	}
	private void redo() {
		Analysis[] selected = Arrays.stream(grid.selection()).filter(a->a.requests()!=null && a.requests().length>0).toArray(Analysis[]::new);
		long noRequests = Arrays.stream(grid.selection()).filter(a->a.requests()==null || a.requests().length<=0).count();
		if(selected.length <= 0 && noRequests > 0) {
			DomGlobal.alert("선택한 " + noRequests + "개의 데이터는 검체와 연결되어 있지 않아 재분석이 불가능합니다.");
			return;
		}
		if(!DomGlobal.confirm("선택한 " + selected.length + "개 검체를 재분석 대기열에 추가합니다." + (noRequests>0?(" (" + noRequests + "개 데이터는 검체와 연결되어 있지 않아 제외됩니다.)"):""))) return;
		com.greencross.lims.dto.RequestReference[] refs = Arrays.stream(selected)
																.map(Analysis::requests)
																.flatMap(Arrays::stream)
																.map(req->new com.greencross.lims.dto.RequestReference()
																				  .sample(req.sample())
																				  .service(req.serviceCode()))
																.toArray(com.greencross.lims.dto.RequestReference[]::new);
		QueueApi.queue(refs, result->{
			AtomicInteger complete = new AtomicInteger(selected.length);
			Map<String, String> failed = new HashMap<>();
			failed.put("9abadf27-70dd-402c-afd6-5ba8e6af51d1", "FAIL");
			Arrays.stream(selected)
				  .forEach(a->AnalysisApi.save(a.batch(), a.row(), failed, cb->{
				  	grid.put(a.row(), "9abadf27-70dd-402c-afd6-5ba8e6af51d1", "FAIL");
					if(complete.decrementAndGet() <= 0) grid.refresh();
			}));
		});
	}
}
