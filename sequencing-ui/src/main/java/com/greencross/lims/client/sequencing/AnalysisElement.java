package com.greencross.lims.client.sequencing;

import com.greencross.lims.api.*;
import com.greencross.lims.client.AbstractScene;
import com.greencross.lims.client.Router;
import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.Query;
import com.greencross.lims.dto.Sheet;
import com.greencross.lims.sheet.SheetState;
import com.greencross.lims.ui.IconElement;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.BreadcumbElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.DropDownElement;
import net.sayaya.ui.ListElement;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class AnalysisElement extends AbstractScene<AnalysisElement> {
	private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Sequencing");
	private final BreadcumbElement breadcumb = BreadcumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
		RouteApi.location("", true, false);
	}).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
	.add("패널검사", evt->{
		evt.preventDefault();
		evt.stopPropagation();
	}).add("Sequencing", evt->{
		evt.preventDefault();
		evt.stopPropagation();
		Router.location("", true);
	});
	private final ButtonElement add = ButtonElement.outline().css("button").text("Add").before(IconElement.icon(IconElement.Type.Light, "fa-plus"));
	private final ButtonElement delete = ButtonElement.outline().css("button").text("Delete").before(IconElement.icon(IconElement.Type.Light, "fa-comment-times"));
	private final ButtonElement print = ButtonElement.outline().css("button").text("Print").before(IconElement.icon(IconElement.Type.Light, "fa-print"));
	private final ButtonElement split = ButtonElement.outline().css("button").text("Split").before(IconElement.icon(IconElement.Type.Light, "fa-page-break"));
	private final ButtonElement save = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Light, "fa-save"));
	private final ButtonElement samplesheet = ButtonElement.outline().css("button").text("Create Sample Sheet").before(IconElement.icon(IconElement.Type.Regular, "fa-file-csv"));
	private final ButtonElement demultiplex = ButtonElement.outline().css("button").text("Demultiplex").before(IconElement.icon(IconElement.Type.Regular, "fa-project-diagram"));
	private final ListElement<ListElement.SingleLineItem> devices = ListElement.singleLineList().add(ListElement.singleLine().label(""));
	private final DropDownElement iptDevice = DropDownElement.outlined(devices).text("Device").css("input").style("width: 200px;");
	private final TextField<Sheet.Link> iptSampleSheet = TextField.fileBox().outlined().text("Sample Sheet").css("input");
	private final TextField<String> iptRunfolder = TextField.textBox().outlined().text("Run ID").css("input").style("width: 400px");
	private final TextField<Double> iptRead1 = TextField.numberBox().outlined().text("Read1").css("input").style("width: 100px");
	private final TextField<Double> iptRead2 = TextField.numberBox().outlined().text("Read2").css("input").style("width: 100px");
	private final TextField<Double> iptIdx1 = TextField.numberBox().outlined().text("Index1").css("input").style("width: 100px");
	private final TextField<Double> iptIdx2 = TextField.numberBox().outlined().text("Index2").css("input").style("width: 100px");
	private final DropDownElement iptMismatch = DropDownElement.outlined(ListElement.singleLineList()
															 .add(ListElement.singleLine().label(""))
															 .add(ListElement.singleLine().label("1 Base"))
															 .add(ListElement.singleLine().label("Perfect")))
											   .text("Mismatch").css("input").style("width: 60px;");
	private final HtmlContentBuilder<HTMLDivElement> parent = div().style("margin-top: 10px;")
																   .add(iptDevice).add(iptSampleSheet).add(iptRunfolder)
																   .add(iptRead1).add(iptRead2).add(iptIdx1).add(iptIdx2).add(iptMismatch);
	private final AnalysisGridElement grid = AnalysisGridElement.builder().build().style("position: absolute; top: 155px; bottom: 20px; left: 20px; right: 20px; overflow: hidden;" +
																						 "border-top: 1px solid #AAA; border-bottom: 1px solid #AAAA; transition: all 200ms;");
	private Integer batch;
	public AnalysisElement(Query query) {
		super(query);
		add.onClick(evt->add());
		save.onClick(evt->save());
		delete.onClick(evt->delete());
		grid.onSelectionChange(evt->{
			boolean selected = evt.selection().length > 0;
			split.enabled(selected);
			delete.enabled(selected);
		});
		grid.onStateChange(evt->{
			save.enabled(evt.state() == SheetState.CHANGED);
		});
		split.enabled(false);
		delete.enabled(false);
		save.enabled(false);
		BatchApi.template(template->{
			Arrays.stream(template.columns()).filter(c->"27fe2fed-f688-4400-a1f7-81c3a56af5b1".equals(c.id())).findFirst().ifPresent(c->{
				Arrays.stream(c.styleColor().conditions()).map(Sheet.StyleColor.StyleConditional::param).sorted().forEach(n->devices.add(ListElement.singleLine().label(n)));
			});
		});
	}

	@Override
	protected IconElement icon() {
		return IconElement.icon(IconElement.Type.Light, "fa-dna");
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
				new IsElement<?>[]{ samplesheet, demultiplex },
				new IsElement<?>[]{ save, add, delete },
				new IsElement<?>[]{ print, split }
		};
	}
	@Override
	protected IsElement<?>[] contents() {
		return new IsElement<?>[] {
			parent, grid
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
			if(parent.values()!=null) {
				iptRunfolder.value(parent.get("64740ccd-659a-4a62-8992-358cb07792da"));
				try {iptRead1.value(Double.parseDouble(parent.get("0c8eaf97-62a7-4de8-84e3-caa902ca4e76")));} catch(Exception ignore){}
				try {iptRead2.value(Double.parseDouble(parent.get("572f4c04-487c-4582-af46-fee1b9b20d5f")));} catch(Exception ignore){}
				try {iptIdx1.value(Double.parseDouble(parent.get("3fffff88-05ee-4792-a931-779c523f7526")));} catch(Exception ignore){}
				try {iptIdx2.value(Double.parseDouble(parent.get("fc312d65-e14a-475b-a04a-dcbdb66e60e0")));} catch(Exception ignore){}
				if("1".equalsIgnoreCase(parent.get("54a46cec-08f8-46eb-9412-69728e59e166"))) iptMismatch.select(1);
				else if("Perfect Match".equalsIgnoreCase(parent.get("54a46cec-08f8-46eb-9412-69728e59e166"))) iptMismatch.select(2);
			}
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
		if(!DomGlobal.confirm("선택한 " + selected.length + "개 검체를 재분석 대기열에 추가합니다.")) return;
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
