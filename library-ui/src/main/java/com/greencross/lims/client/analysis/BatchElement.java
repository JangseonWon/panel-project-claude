package com.greencross.lims.client.analysis;

import com.greencross.lims.api.BatchApi;
import com.greencross.lims.api.ProgressApi;
import com.greencross.lims.api.RouteApi;
import com.greencross.lims.client.AbstractScenePageable;
import com.greencross.lims.client.Router;
import com.greencross.lims.dto.Batch;
import com.greencross.lims.dto.Query;
import com.greencross.lims.sheet.SheetState;
import com.greencross.lims.ui.IconElement;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.BreadcumbElement;
import net.sayaya.ui.ButtonElement;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.jboss.elemento.Elements.label;

public class BatchElement extends AbstractScenePageable<BatchElement> {
	private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Library");
	private final BreadcumbElement breadcumb = BreadcumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
		RouteApi.location("", true, false);
	}).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
	.add("패널검사", evt->{
		evt.preventDefault();
		evt.stopPropagation();
	}).add("Library", evt->{
		evt.preventDefault();
		evt.stopPropagation();
		Router.location("", true);
	});
	private final ButtonElement next = ButtonElement.outline().css("button").text("Sequencing").before(IconElement.icon(IconElement.Type.Regular, "fa-dna"));
	private final ButtonElement save = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Light, "fa-save"));
	// private final Button add = ButtonElement.outline().css("button").text("Add").before(IconElement.icon(IconElement.Type.Light, "fa-plus"));
	private final ButtonElement merge = ButtonElement.outline().css("button").text("Merge").before(IconElement.icon(IconElement.Type.Light, "fa-layer-plus"));
	private final ButtonElement delete = ButtonElement.outline().css("button").text("Delete").before(IconElement.icon(IconElement.Type.Light, "fa-times"));
	private final BatchGridElement grid = BatchGridElement.builder().build().style("position: absolute; top: 95px; bottom: 45px; left: 20px; right: 20px; overflow: hidden;" +
																				   "border-top: 1px solid #AAA; border-bottom: 1px solid #AAAA; transition: all 150ms;");
	public BatchElement(Query query) {
		super(query);
		// add.onClick(evt->add());
		save.onClick(evt->save());
		delete.onClick(evt->delete());
		merge.onClick(evt->merge());
		next.onClick(evt->next());

		grid.onSelectionChange(evt->{
			boolean selected = evt.selection().length > 0;
			next.enabled(selected);
			merge.enabled(selected);
			delete.enabled(selected);
		});
		grid.onStateChange(evt->{
			save.enabled(evt.state() == SheetState.CHANGED);
		});
		next.enabled(false);
		merge.enabled(false);
		delete.enabled(false);
		save.enabled(false);
	}
	@Override
	protected IconElement icon() {
		return IconElement.icon(IconElement.Type.Light, "fa-vials");
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
			new IsElement<?>[]{ next },
			new IsElement<?>[]{ save, /*add, */merge, delete }
		};
	}
	@Override
	protected IsElement<?> grid() {
		return grid;
	}
	@Override
	public void update() {
		BatchApi.batches(query().limit(show()).page((int) page()).sortBy("batch").asc(false), page->{
			grid.update(page.content());
			total(page.totalElement());
		});
	}
	private void add() {
		BatchApi.add(b->grid.insert(b));
	}
	private void save() {
		BatchChanges[] changes = grid.changes();
		if(changes.length <= 0) return;
		if(!DomGlobal.confirm("저장합니다.")) return;

		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for (BatchChanges change : changes) {
			BatchApi.save(change.batch(), change.values(), callback -> {
				int completed = complete.incrementAndGet();
				if (completed >= changes.length) ProgressApi.close();
				else ProgressApi.progress(completed / (double) changes.length);
				grid.update(callback);
			});
		}
	}
	private void delete() {
		Batch[] selected = grid.selection();
		if(selected.length <= 0) return;
		if(!DomGlobal.confirm("선택한 " + selected.length + "개의 배치를 삭제합니다.")) return;

		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for(Batch select: selected) {
			BatchApi.delete(select.id(), callback -> {
				int completed = complete.incrementAndGet();
				if (completed >= selected.length) {
					ProgressApi.close();
					update();
				} else ProgressApi.progress(completed / (double) selected.length);
			});
		}
	}
	private void merge() {
		Batch[] selected = grid.selection();
		if(selected.length <= 0) return;
		if(!DomGlobal.confirm("선택한 " + selected.length + "개의 배치를 병합합니다.")) return;
		BatchApi.merge(Arrays.stream(selected).mapToInt(Batch::id).toArray(), callback->update());
	}
	private void next() {
		Batch[] selected = grid.selection();
		if(selected.length <= 0) selected = grid.values();
		Batch[] selection = selected;
		if(!DomGlobal.confirm("선택한 " + selection.length + "개 배치를 Sequencing으로 복사합니다.")) return;
		com.greencross.lims.dto.BatchReference[] refs = Arrays.stream(selected).map(batch->new com.greencross.lims.dto.BatchReference().sheet(batch.sheet()).batch(batch.id())).toArray(com.greencross.lims.dto.BatchReference[]::new);
		BatchApi.checkout(refs, result->{
			if(DomGlobal.confirm("복사되었습니다. 시퀀싱 화면으로 이동합니다.")) {
				Router.location("패널검사/Sequencing", true);
			}
		});
	}
	@Override
	public BatchElement that() {
		return this;
	}
}
