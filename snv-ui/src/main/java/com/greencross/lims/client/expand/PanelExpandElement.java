package com.greencross.lims.client.expand;

import com.google.gwt.core.client.Scheduler;
import com.greencross.lims.api.SnvApi;
import com.greencross.lims.client.ExpandElement;
import com.greencross.lims.client.WindowState;
import com.greencross.lims.client.snv.*;
import com.greencross.lims.dto.Analysis2;
import com.greencross.lims.dto.Message;
import com.greencross.lims.dto.Panel;
import com.greencross.lims.dto.Query;
import com.greencross.lims.test.panel.TestInfo;
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
import java.util.stream.Stream;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.span;

public class PanelExpandElement extends HTMLElementBuilder<HTMLDivElement, PanelExpandElement> implements ExpandElement<HTMLDivElement> {
	public static PanelExpandElement build(String id, long sample, String service) {
		return new PanelExpandElement(div(), id, sample, service);
	}
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private final HtmlContentBuilder<HTMLDivElement> controller = div().css("controller");
	private final HtmlContentBuilder<HTMLDivElement> notAnalyzed = div().add("Not Analyzed Yet")
			.style("display:none;position: absolute;z-index: 999999;top: 50%;left: 0;right: 0; margin-left: auto; margin-right: auto;width: fit-content;text-align: center;" +
					"font-size: 3em;font-weight: 800;user-select: none; color: var(--mdc-theme-text-disabled-on-background);");
	private final ListElement<ListElement.SingleLineItem> panelList = ListElement.singleLineList();
	private final ListElement<ListElement.SingleLineItem> geneList = ListElement.singleLineList();
	private final ListElement<ListElement.SingleLineItem> analysisList = ListElement.singleLineList();
	private final DropDownElement iptPanels = DropDownElement.outlined(panelList).css("input").text("Panel").style("z-index:9999999; min-width: 150px;");
	private final DropDownElement iptGenes = DropDownElement.outlined(geneList).css("input").text("Gene").style("z-index:9999999; min-width: 200px;");
	private final DropDownElement iptAnalysis = DropDownElement.outlined(analysisList).css("input").text("Analysis").style("z-index:9999999; min-width: 400px;");
	private final TextFieldElement<String> iptKeyword = TextFieldElement.textBox().outlined().text("Search Keyword");
	private final ChipElementCheckable hasFiltered = ChipElement.check("Filtered Variant").value(true);
	private final ChipElementCheckable green = ChipElement.check("Green").value(false);
	private final ChipElementCheckable amber = ChipElement.check("Amber").value(false);
	private final ChipElementCheckable incidental = ChipElement.check("IF").value(false);
	private final ChipElementCheckable mitochondrial = ChipElement.check("MT").value(false);
	private final ChipElementCheckable panelOrGene = ChipElement.check("").style("display: none");
	private final ChipElementCheckable core = ChipElement.check("Tier1").value(true);
	private final ChipElementCheckable addendum = ChipElement.check("Tier2").value(true);

	private final ChipSetElement tagSet = ChipSetElement.filters(panelOrGene, hasFiltered, green, amber, core, addendum, incidental, mitochondrial);
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
	private final SelectGEPanelDialog dialog = SelectGEPanelDialog.instance();
	private SnvTableElement sheet;
	private final ButtonElement save = ButtonElement.outline().css("button").before(IconElement.icon("save")).text("Save");
	private final ButtonElement hide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	private final Query query = new Query();
	private final PageElement page = PageElement.instance();
	private final Set<String> cores;
	private final Set<String> addenda;
	private final Map<String, Panel> panels = new HashMap<>();
	private final Map<String, Analysis2> analysis = new HashMap<>();

	private final TestInfo test;
	private final Set<String> ifs;
	private Set<String> greens;
	private Set<String> ambers;
	private final String id;
	private final long sample;
	private final String service;

	private PanelExpandElement(HtmlContentBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e);
		this.id = id;
		this.sample = sample;
		this.service = service;
		_this = e.css("work").style("height: 100vh;");
		test = Arrays.stream(TestInfo.TESTS).filter(t->t.code().equals(service)).findAny().orElse(null);
		ifs = Arrays.stream(com.greencross.lims.test.wes.TestWithSingleInfo.GENE_INCIDENTAL_FINDINGS).collect(Collectors.toSet());
		cores = Arrays.stream(test.tier1()).collect(Collectors.toSet());
		addenda = Optional.ofNullable(test.tier2()).map(Arrays::stream).orElseGet(Stream::empty).collect(Collectors.toSet());
		this.page.show(this.predictRowCount()).idx(0L);
		this.page.onValueChange(evt ->update());
		resize();
		Scheduler.get().scheduleFixedDelay(()->{resize();return false;}, 1000);
		rebuildSheet();
		layout();
		hide.onClick(evt->fireStateChangeEvent());
		save.onClick(evt->save());
		hasFiltered.on(EventType.click, evt -> update());
		panelOrGene.on(EventType.click, evt -> update());
		mitochondrial.on(EventType.click, evt -> update());
		green.onValueChange(evt->{
			if(evt.value()) {
				green.value(false);
				dialog.callback(panel->{
					green.text(panel.panel() + ":Green");
					greens = Arrays.stream(panel.genes()).collect(Collectors.toSet());
					green.value(true);
					update();
				}).build("Green");
			} else {
				green.text("Green");
				greens = null;
				update();
			}
		});
		amber.onValueChange(evt->{
			if(evt.value()) {
				amber.value(false);
				dialog.callback(panel->{
					amber.text(panel.panel() + ":Amber");
					ambers = Arrays.stream(panel.genes()).collect(Collectors.toSet());
					amber.value(true);
					update();
				}).build("Amber");
			} else {
				amber.text("Amber");
				ambers = null;
				update();
			}
		});
		core.on(EventType.click, evt -> update());
		addendum.on(EventType.click, evt -> update());
		incidental.on(EventType.click, evt -> update());
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
							 .add(div().add(iptPanels).add(iptGenes).add(iptAnalysis)).add(iptKeyword.style("margin-left: auto; margin-top: 5px; width: 400px;")))
			 .add(div().style("display: flex; align-items: center; margin-left: 10px; ")
					   .add(span().addAll(columnSelectButtons))
					   .add(tagSet.style("margin-left: auto; align-items: center;")))
			 .add(sheetContainer.style("overflow: hidden;position: absolute;left: 0;right: 0;top: 115px;bottom: 60px; border: 1px solid #ddd;")).add(notAnalyzed)
			 .add(page)
			 .add(controller.add(save).add(hide))
			 .add(dialog);
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
								  .sorted(Comparator.comparing(Analysis2::batch)).peek(a -> analysis.put(a.serial(), a))
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
		if(panelOrGene.value()) {
			String value = panelOrGene.text();
			String key = value.substring(0, value.indexOf(":"));
			value = value.substring(value.indexOf(":")+1);
			filters.add(new Query.Filter().key(key).value(value));
		}
		if(core.value() || addendum.value()) {
			Query.Filter filter = new Query.Filter().key("genes");
			java.util.List<String> list = new LinkedList<>();
			if(core.value()) list.addAll(cores);
			if(addendum.value()) list.addAll(addenda);
			if(incidental.value()) list.addAll(ifs);
			if(greens!=null) list.addAll(greens);
			if(ambers!=null) list.addAll(ambers);
			filter.value(String.join(",", list));
			filters.add(filter);
		}
		if(incidental.value()) {
			Query.Filter filter = new Query.Filter().key("genes");
			java.util.List<String> list = new LinkedList<>();
			list.addAll(ifs);
			filter.value(String.join(",", list));
			filters.add(filter);
		}
		if(greens!=null) {
			Query.Filter filter = new Query.Filter().key("genes");
			java.util.List<String> list = new LinkedList<>();
			list.addAll(greens);
			filter.value(String.join(",", list));
			filters.add(filter);
		}
		if(ambers!=null) {
			Query.Filter filter = new Query.Filter().key("genes");
			java.util.List<String> list = new LinkedList<>();
			list.addAll(ambers);
			filter.value(String.join(",", list));
			filters.add(filter);
		}
		Arrays.stream(tagSet.value())
			  .filter(t->!t.text().equals(hasFiltered.text()))
			  // .filter(t->!t.text().equals(hasMulti.text()))
			  .filter(t->!t.text().equals(panelOrGene.text()))
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
					  String gene = (String) map.get("gene.refgene");
					  if(cores.contains(gene)) map.set("tier", "Tier1");
					  else if(addenda.contains(gene)) map.set("tier", "Tier2");
					  else map.set("tier", "Tier3");
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
	public PanelExpandElement that() {
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
