package com.gcgenome.lims.client.worklist;

import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.RouteApi;
import com.gcgenome.lims.client.AbstractScenePageable;
import com.gcgenome.lims.client.Router;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.ui.IconElement;
import com.gcgenome.lims.api.WorklistApi;
import com.gcgenome.lims.dto.Worklist;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.BreadcrumbElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.chart.SheetState;
import org.jboss.elemento.HTMLContainerBuilder;
import org.jboss.elemento.IsElement;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.jboss.elemento.Elements.label;

public class WorklistElement extends AbstractScenePageable<WorklistElement> {
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
	// private final Button next = ButtonElement.outline().css("button").text("DNA QC").before(IconElement.icon(IconElement.Type.Regular, "fa-vial"));
	private final ButtonElement save = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Light, "fa-save"));
	private final ButtonElement add = ButtonElement.outline().css("button").text("Add").before(IconElement.icon(IconElement.Type.Light, "fa-plus"));
	private final ButtonElement merge = ButtonElement.outline().css("button").text("Merge").before(IconElement.icon(IconElement.Type.Light, "fa-layer-plus"));
	private final ButtonElement close = ButtonElement.outline().css("button").text("Close").before(IconElement.icon(IconElement.Type.Light, "fa-door-closed"));
	private final ButtonElement delete = ButtonElement.outline().css("button").text("Delete").before(IconElement.icon(IconElement.Type.Light, "fa-times"));
	private final WorklistGridElement grid = new WorklistGridElement().style("position: absolute; top: 95px; bottom: 45px; left: 20px; right: 20px; overflow: hidden;" +
																						 "border-top: 1px solid #AAA; border-bottom: 1px solid #AAAA; transition: all 150ms;");
	public WorklistElement(Query query) {
		super(query);
		add.onClick(evt->add());
		save.onClick(evt->save());
		delete.onClick(evt->delete());
		close.onClick(evt->close());
		merge.onClick(evt->merge());
		grid.onSelectionChange(evt->{
			boolean selected = evt.selection().length > 0;
			merge.enabled(selected);
			delete.enabled(selected);
		});
		grid.onStateChange(evt->{
			save.enabled(evt.state() == SheetState.CHANGED);
		});
		merge.enabled(false);
		delete.enabled(false);
		save.enabled(false);
	}
	@Override
	protected IsElement<?> grid() {
		return grid;
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
			new IsElement<?>[]{ save, add, merge, close, delete }
		};
	}
	@Override
	public void update() {
		WorklistApi.worklists(query().limit(show()).page((int) page()).sortBy("worklist").asc(false), page->{
			DomGlobal.console.log(page);
			grid.update(page.content());
			total(page.totalElement());
		});
	}
	private void add() {
		WorklistApi.add(grid::insert);
	}
	private void save() {
		WorklistReference[] changes = grid.changes();
		if(changes.length <= 0) return;
		if(!DomGlobal.confirm("저장합니다.")) return;

		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for (WorklistReference change : changes) {
			WorklistApi.save(change.worklist(), change.values(), callback -> {
				int completed = complete.incrementAndGet();
				if (completed >= changes.length) ProgressApi.close();
				else ProgressApi.progress(completed / (double) changes.length);
				grid.update(callback);
			});
		}
	}
	private void close() {
		Worklist[] selected = grid.selection();
		if(selected.length <= 0) return;
		if(!DomGlobal.confirm("선택한 " + selected.length + "개의 워크리스트에 검체가 더 이상 추가되지 않도록 닫습니다.")) return;
		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		Map<String, String> value = new HashMap<>();
		value.put("state", "CLOSE");
		for (Worklist select: selected) {
			WorklistApi.save(select.worklist(), value, callback -> {
				int completed = complete.incrementAndGet();
				if (completed >= selected.length) ProgressApi.close();
				else ProgressApi.progress(completed / (double) selected.length);
				grid.update(select.state("CLOSE"));
			});
		}
	}
	private void delete() {
		Worklist[] selected = grid.selection();
		if(selected.length <= 0) return;
		if(!DomGlobal.confirm("선택한 " + selected.length + "개의 워크리스트를 삭제합니다.")) return;

		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for (Worklist select: selected) {
			WorklistApi.delete(select.worklist(), callback -> {
				int completed = complete.incrementAndGet();
				if (completed >= selected.length) {
					ProgressApi.close();
					update();
				} else ProgressApi.progress(completed / (double) selected.length);
			});
		}
	}
	private void merge() {

	}
	@Override
	public WorklistElement that() {
		return this;
	}
}
