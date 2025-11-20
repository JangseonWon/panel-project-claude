package com.greencross.lims.client.expand;

import com.google.gwt.core.client.Scheduler;
import com.greencross.lims.api.SnvApi;
import com.greencross.lims.client.ExpandElement;
import com.greencross.lims.client.WindowState;
import com.greencross.lims.client.snv.*;
import com.greencross.lims.dto.Analysis2;
import com.greencross.lims.dto.Message;
import com.greencross.lims.dto.Query;
import com.greencross.lims.test.wes.TestWithSingleInfo;
import com.greencross.lims.ui.IconElement;
import elemental2.core.Global;
import elemental2.dom.CSSProperties;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.*;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.span;

public class DgsExpandElement extends HTMLElementBuilder<HTMLDivElement, DgsExpandElement> implements ExpandElement<HTMLDivElement> {
	public static DgsExpandElement build(String id, long sample, String service) {
		return new DgsExpandElement(div(), id, sample, service);
	}
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private final HtmlContentBuilder<HTMLDivElement> controller = div().css("controller");
	private final HtmlContentBuilder<HTMLDivElement> notAnalyzed = div().add("Not Analyzed Yet")
			.style("display:none;position: absolute;z-index: 999999;top: 50%;left: 0;right: 0; margin-left: auto; margin-right: auto;width: fit-content;text-align: center;" +
					"font-size: 3em;font-weight: 800;user-select: none; color: var(--mdc-theme-text-disabled-on-background);");
	private final ListElement<ListElement.SingleLineItem> analysisList = ListElement.singleLineList();
	private final DropDownElement iptAnalysis = DropDownElement.outlined(analysisList).css("input").text("Analysis").style("z-index:9999999; min-width: 400px;");
	private final TextFieldElement<String> iptKeyword = TextFieldElement.textBox().outlined().text("Search Keyword");
	private final ChipElementCheckable hasFiltered = ChipElement.check("Filtered Variant").value(true);
	private final ChipElementCheckable incidental = ChipElement.check("IF").value(false);
	private final ChipElementCheckable het = ChipElement.check("CompoundHet").value(false);
	private final ChipElementCheckable ncrna = ChipElement.check("ncRNA").value(false);
	private final ChipElementCheckable mitochondrial = ChipElement.check("MT").value(false);
	private final ChipSetElement tagSet = ChipSetElement.filters(hasFiltered, incidental, het, ncrna, mitochondrial);
	private final ColumnSet[] columnSets = new ColumnSet[] {
		new ColumnSetBasic(), new ColumnSetClass(), new ColumnSetFreq(), new ColumnSetPrediction(), new ColumnSetDisease(), new ColumnSetEtc()
	};
	private final ButtonElement[] columnSelectButtons = Arrays.stream(columnSets).map(c->{
		ButtonElementToggle btn = ButtonElement.toggle().text(c.name()).value(c.select()).css("button");
		btn.onValueChange(evt->{
			c.select(evt.value());
			rebuildSheet();
		});
		return btn;
	}).toArray(ButtonElement[]::new);
	private final HtmlContentBuilder<HTMLDivElement> controller2 = div();
	private final HtmlContentBuilder<HTMLDivElement> sheetContainer = div();
	private SnvTableElement sheet;
	private final ButtonElement save = ButtonElement.outline().css("button").before(IconElement.icon("save")).text("Save");
	private final ButtonElement hide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	private final Query query = new Query();
	private final PageElement page = PageElement.instance();
	private final Set<String> ifs;
	private final Map<String, Analysis2> analysis = new HashMap<>();

	private final String id;
	private final long sample;
	private final String service;

	private DgsExpandElement(HtmlContentBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e);
		this.id = id;
		this.sample = sample;
		this.service = service;
		_this = e.css("work").style("height: 100vh;");
		ifs = Arrays.stream(TestWithSingleInfo.GENE_INCIDENTAL_FINDINGS).collect(Collectors.toSet());
		this.page.show(this.predictRowCount()).idx(0L);
		this.page.onValueChange(evt ->update());
		resize();
		Scheduler.get().scheduleFixedDelay(()->{resize();return false;}, 1000);
		rebuildSheet();
		layout();
		hide.onClick(evt->fireStateChangeEvent());
		save.onClick(evt->save());
		hasFiltered.on(EventType.click, evt -> update());
		incidental.on(EventType.click, evt -> update());
		mitochondrial.on(EventType.click, evt -> update());
		het.on(EventType.click, evt -> update());
		ncrna.on(EventType.click, evt -> update());
		iptAnalysis.onValueChange(evt->{
			Analysis2 target = analysis.get(evt.value());
			updateVcfTable(target);
		});
		iptKeyword.onValueChange(evt->{
			if(evt.value() == null || evt.value().isEmpty()) return;
			iptKeyword.value("");
			ChipElement tag = ChipElement.chip(evt.value()).removable();
			tagSet.add(tag);
			tag.onDetach(evt2->update());
			update();
		});
	}
	private void layout() {
		this.page.element().style.position = "absolute";
		this.page.element().style.zIndex = CSSProperties.ZIndexUnionType.of("999");
		this.page.element().style.left = "20px";
		this.page.element().style.right = "240px";
		this.page.element().style.bottom = "5px";

		_this.add(controller2.style("display: flex;align-items: center; margin-top: 5px;margin-left: 10px; margin-right: 10px;")
							 .add(div().add(iptAnalysis)).add(iptKeyword.style("margin-left: auto; margin-top: 5px; width: 400px;")))
			 .add(div().style("display: flex; align-items: center; margin-left: 10px; ")
					   .add(span().addAll(columnSelectButtons))
					   .add(tagSet.style("margin-left: auto; align-items: center;")))
			 .add(sheetContainer.style("overflow: hidden;position: absolute;left: 0;right: 0;top: 115px;bottom: 60px; border: 1px solid #ddd;")).add(notAnalyzed)
			 .add(page)
			 .add(controller.add(save).add(hide));
	}
	private void rebuildSheet() {
		SnvTableElement sheet = SnvTableElement.build(columnSets);
		if(this.sheet!=null) {
			sheet.copy(this.sheet);
			this.sheet.element().remove();
		}
		this.sheet = sheet;
		sheetContainer.add(sheet);
		Scheduler.get().scheduleFixedDelay(()->{
			sheet.refresh();
			return false;
		}, 300);
	}
	private int predictRowCount() {
		int windowHeight = DomGlobal.window.outerHeight;
		int tableMargin = 110;
		int headerHeightExpect = 28;
		int rowHeightExpect = 23;
		return 30;
		// return Math.max(1, (windowHeight - tableMargin - headerHeightExpect) / rowHeightExpect - 1);
	}
	private void resize() {
		int rowCountCurrnt = this.page.show();
		int rowCountExpect = this.predictRowCount();
		if (rowCountCurrnt != rowCountExpect) {
			this.page.show(rowCountExpect).idx(0L);
			query.limit(this.page.show()).page((int)this.page.page());
			this.update();
		}
	}
	public void update() {
		Message msg = new Message().id(id).type(Message.MessageType.FULLSCREEN);
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		SnvApi.reported(sample, service).then(values->{
			update(values);
			SnvApi.analysis(sample)
				  .then(as->{
					  analysis.clear();
					  analysisList.clear();
					  if(as==null || as.length<=0) notAnalyzed.element().style.display = null;
					  else {
						  notAnalyzed.element().style.display = "none";
						  as = Arrays.stream(as).filter(c->c.service().equals(service)).toArray(Analysis2[]::new);
						  Arrays.stream(as).filter(c->c.service().equals(service))
								  .sorted(Comparator.comparing(c -> c.batch())).peek(a -> analysis.put(a.serial(), a))
								  .forEach(a -> analysisList.add(ListElement.singleLine().label(a.serial())));
						  iptAnalysis.select(as.length - 1);
						  Analysis2 target = analysis.get(iptAnalysis.value());
						  updateVcfTable(target);
					  }
					  return null;
				  });
			return null;
		});
	}
	private void update(Object[] values) {
		SnvTableElement.SnvReport[] reported = Arrays.stream(values).map(dto->{
			JsPropertyMap<Object> map = Js.asPropertyMap(dto);
			String analysis = (String) map.get("analysis");
			String variant = (String) map.get("snv");
			String classification = (String) map.get("class");
			return new SnvTableElement.SnvReport().variant(analysis + ":" + variant).classification(classification);
		}).toArray(SnvTableElement.SnvReport[]::new);
		if(sheet!=null) sheet.reported(reported);
	}
	private void updateVcfTable(Analysis2 dto) {
		Set<Query.Filter> filters = new HashSet<>();
		if(hasFiltered.value()) filters.add(new Query.Filter().key("tags").value("candidate"));
		if(mitochondrial.value()) filters.add(new Query.Filter().key("tags").value("mt"));
		//if(hasMulti.value()) filters.add(new Query.Filter().key("tags").value("multi"));
		if(incidental.value()) {
			Query.Filter filter = new Query.Filter().key("genes");
			java.util.List<String> list = new LinkedList<>();
			list.addAll(ifs);
			filter.value(String.join(",", list));
			filters.add(filter);
		}
		if(het.value()) filters.add(new Query.Filter().key("tags").value("het"));
		if(ncrna.value()) filters.add(new Query.Filter().key("tags").value("ncrna"));
		Arrays.stream(tagSet.value())
			  .filter(t->!t.text().equals(hasFiltered.text()))
			  .forEach(t->{
				  if(t.text().contains(":")) {
					  String[] split = t.text().split(":");
					  filters.add(new Query.Filter().key(split[0]).value(split[1]));
				  } else filters.add(new Query.Filter().key("").value(t.text()));
			  });
		SnvApi.snvs(sample, service, dto.batch(), dto.row(), query.page((int) page.page()).limit(page.show()).sortBy("snv").asc(true).filters(filters.stream().toArray(Query.Filter[]::new)))
			  .then(objs->{
				  if(objs == null) return null;
				  page.total(objs.totalElement());
				  for(Object obj: objs.content()) {
					  JsPropertyMap<Object> map = Js.asPropertyMap(obj);
					  map.set("tier", "-");
				  }
				  if(sheet!=null) Scheduler.get().scheduleFixedDelay(() -> {
					  sheet.update(objs.content());
					  return false;
				  }, 200);
				  return null;
			  });
	}
	public void save() {
		SnvTableElement.SnvReport[] changes = sheet.selection();
		if(changes.length <= 0) return;
		if(!DomGlobal.confirm("저장합니다.")) return;

		//ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for (SnvTableElement.SnvReport change : changes) {
			SnvApi.update(sample, service, change.variant(), change.classification())
				  .then(response->{
					  int completed = complete.incrementAndGet();
					  if (completed >= changes.length) {
						  //		ProgressApi.close();
						  update();
						  DomGlobal.alert("저장되었습니다.");
					  } //else ProgressApi.progress(completed / (double) changes.length);
					  return null;
				  });
		}
	}

	@Override
	public DgsExpandElement that() {
		return this;
	}

	private final Set<StateChangeEventListener<WindowState>> listeners = new HashSet<>();
	@Override
	public Collection<StateChangeEventListener<WindowState>> listeners() {
		return listeners;
	}
	@Override
	public WindowState state() {
		return WindowState.COLLAPSE;
	}
}
