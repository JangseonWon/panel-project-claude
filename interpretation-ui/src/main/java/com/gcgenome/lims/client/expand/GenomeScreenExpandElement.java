package com.gcgenome.lims.client.expand;

import com.gcgenome.lims.api.*;
import com.gcgenome.lims.client.ExpandElement;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.client.interpretation.PreviewElement;
import com.gcgenome.lims.client.interpretation.SelectDiseaseDialog;
import com.gcgenome.lims.client.interpretation.SnvGenomeScreenTableElement;
import com.gcgenome.lims.client.util.Order;
import com.gcgenome.lims.dto.Analysis2;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.dto.interpretation.GenomeScreen;
import com.gcgenome.lims.dto.interpretation.InterpretationParam;
import com.gcgenome.lims.dto.interpretation.PanelTest;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import com.gcgenome.lims.test.genomescreen.TestWithRiskScreen;
import com.google.gwt.i18n.client.NumberFormat;
import elemental2.core.Global;
import elemental2.dom.CSSProperties;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.promise.Promise;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.*;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.*;

public class GenomeScreenExpandElement extends HTMLElementBuilder<HTMLDivElement, GenomeScreenExpandElement> implements ExpandElement<HTMLDivElement> {
	public static GenomeScreenExpandElement build(String id, long sample, String service) {
		return new GenomeScreenExpandElement(div(), id, sample, service);
	}

	public static GenomeScreenExpandElement build(String id, long sample, String service, Set<String> reportable) {
		return new GenomeScreenExpandElement(div(), id, sample, service, reportable);
	}
	private final Set<String> cancerGeneScreens = new HashSet<>(Arrays.asList("N101", "N111")); // 캔서 진 스크린의 경우 interpretation 항목 필수로 변경.
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private Set<String> variantClassReportable = new HashSet<>();
	private final TextAreaElement<String> iptSummary = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; height: 150px;").text("Summary").required(true);
	private final SnvGenomeScreenTableElement variants = SnvGenomeScreenTableElement.build().style("margin-right: 0px;");
	private final TextAreaElement<String> iptInterpretation = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; height: 250px;").text("Interpretation");
	private final CheckBoxElement chkDepthCoverage = CheckBoxElement.checkBox(false);
	private final TextFieldElement.TextFieldOutlined<Double> iptDepth = TextFieldElement.numberBox().outlined().css("input").text("Mean depth of coverage").style("width: 300px;");
	private final TextFieldElement.TextFieldOutlined<Double> iptCoverage = TextFieldElement.numberBox().outlined().css("input").text("% of Target bases > 10X").style("width: 300px;");
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller");
	private final ButtonElement btnAuto = ButtonElement.outline().css("button").before(IconElement.icon("smart_button")).text("Interpretation");
	private final ButtonElement btnNegative = ButtonElement.outline().css("button").before(IconElement.icon("search_off")).text("Negative");
	private final ButtonElement btnSave = ButtonElement.outline().css("button").before(IconElement.icon("save")).text("Save");
	private final ButtonElement btnPreview = ButtonElement.outline().css("button").before(IconElement.icon("preview")).text("Preview");
	private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	protected final SelectDiseaseDialog dialog = SelectDiseaseDialog.instance();
	private final Map<TestInfo.Disease, Map<String, DropDownElement>> iptGenes = new HashMap<>();
	private final Map<TestInfo.Genotype, DropDownElement> iptGenotypes = new HashMap<>();
	private final String id;
	private final long sample;
	private final String service;
	private final TestInfo test;
	private GenomeScreen result;
	private final Set<String> cores;
	protected final Map<String, JsPropertyMap<Object>> extra = new HashMap<>();
	private GenomeScreenExpandElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e);
		_this = e.css("work").style("height: 100vh;");
		test = Arrays.stream(TestInfo.TESTS).filter(t->t.code().equals(service)).findAny().orElse(null);
		cores = Arrays.stream(test.genes()).collect(Collectors.toSet());
		this.id = id;
		this.sample = sample;
		this.service = service;
		layout();
		btnHide.onClick(evt->fireStateChangeEvent());
		btnSave.onClick(evt->save());
		btnAuto.onClick(evt->auto());
		btnPreview.onClick(evt->preview());
		btnNegative.onClick(evt->negative());
		if(cancerGeneScreens.contains(service.toUpperCase())) iptInterpretation.required(true);
		iptDepth.element().firstElementChild.setAttribute("step", "0.01");
		iptCoverage.element().firstElementChild.setAttribute("step", "0.01");
		chkDepthCoverage.onValueChange(evt->setDepthCoverage(evt.value()));
		setDepthCoverage(!chkDepthCoverage.value());
	}

	private GenomeScreenExpandElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service, Set<String> reportable) {
		super(e);
		_this = e.css("work").style("height: 100vh;");
		test = Arrays.stream(TestInfo.TESTS).filter(t->t.code().equals(service)).findAny().orElse(null);
		cores = Arrays.stream(test.genes()).collect(Collectors.toSet());
		this.id = id;
		this.sample = sample;
		this.service = service;
		layout();
		btnHide.onClick(evt->fireStateChangeEvent());
		btnSave.onClick(evt->save());
		btnAuto.onClick(evt->auto());
		btnPreview.onClick(evt->preview());
		btnNegative.onClick(evt->negative());
		variantClassReportable = reportable;
		if(cancerGeneScreens.contains(service.toUpperCase())) iptInterpretation.required(true);
		iptDepth.element().firstElementChild.setAttribute("step", "0.01");
		iptCoverage.element().firstElementChild.setAttribute("step", "0.01");
		chkDepthCoverage.onValueChange(evt->setDepthCoverage(evt.value()));
		setDepthCoverage(!chkDepthCoverage.value());
	}

	private void layout() {
		HTMLContainerBuilder<HTMLDivElement> diseases = div().style("margin-left: 15px; padding-right: 15px; margin-bottom: 16px; margin-bottom: 16px;");
		_this.add(div().style("height: calc(100% - 55px); overflow: auto;")
						.add(div().style("margin-left: 15px; padding-right: 15px; margin-top: 10px; margin-bottom: 16px; margin-bottom: 16px;").add(iptSummary))
						.add(div().style("margin-left: 15px; padding-right: 15px; margin-top: 10px;").add(variants))
						.add(div().style("margin-left: 15px; padding-right: 15px; margin-bottom: 16px; margin-bottom: 16px;").add(iptInterpretation))
						.add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px; display: flex; vertical-align: middle;").add(iptDepth).add(iptCoverage).add(chkDepthCoverage))
						.add(diseases))
				.add(controller.add(span().add(btnAuto).add(btnNegative))
						.add(span().style("margin-left: 10px;").add(btnSave).add(btnPreview).add(btnHide)))
						.add(dialog);
		HTMLElement summaryArea = (HTMLElement) iptSummary.element().firstElementChild.firstElementChild;
		summaryArea.style.textAlign = "center";
		summaryArea.style.fontSize = CSSProperties.FontSizeUnionType.of("1.3em");
		Arrays.stream(test.diseases()).forEach(disease-> {
			diseases.add(div().add(label(disease.name())));
			Map<String, DropDownElement> iptGenes = new HashMap<>();
			this.iptGenes.put(disease, iptGenes);
			Arrays.stream(disease.genes()).map(g -> g.gene()).forEach(gene -> {
				ListElement<ListElement.SingleLineItem> values = ListElement.singleLineList();
				values.add(ListElement.singleLine().label("미발견"));
				values.add(ListElement.singleLine().label("발견"));
				DropDownElement ipt = DropDownElement.outlined(values).css("input").style("width: 232px;transition: all 250ms ease 0s;").text(gene);
				ipt.onSelectionChange(evt -> {
					if (result == null) return;
					result.put(gene, ipt.value());
					HTMLElement text = (HTMLElement) ipt.element().getElementsByClassName("mdc-select__selected-text").item(0);
					HTMLElement label = (HTMLElement) ipt.element().getElementsByClassName("mdc-floating-label mdc-floating-label--float-above").item(0);
					if (evt.selection() > 0) {
						ipt.element().style.backgroundColor = "rgba(135,51,61,0.7)";
						text.style.color = "#FFFFFF";
						label.style.backgroundColor = "#FFFFFF";
					} else {
						ipt.element().style.backgroundColor = "transparent";
						text.style.color = "#888888";
						label.style.backgroundColor = null;
					}
				});
				diseases.add(ipt);
				iptGenes.put(gene, ipt);
			});
		});
		if(test instanceof TestWithRiskScreen) {
			TestWithRiskScreen cast = (TestWithRiskScreen)test;
			HTMLContainerBuilder<HTMLDivElement> riskscreen = div().add(label("Risk Screen"));
			diseases.add(riskscreen);
			Arrays.stream(cast.genotypes())
				  .forEach(gt->{
					  ListElement<ListElement.SingleLineItem> values = ListElement.singleLineList();
					  for(String value: gt.types()) values.add(ListElement.singleLine().label(value));
					  DropDownElement ipt = DropDownElement.outlined(values).css("input").style("width: 232px;transition: all 250ms ease 0s;").text(gt.gene() + " " + gt.pos());
					  ipt.onSelectionChange(evt->{
						  if(result == null) return;
						  result.put(gt.gene(), ipt.value());
						  HTMLElement text = (HTMLElement) ipt.element().getElementsByClassName("mdc-select__selected-text").item(0);
						  HTMLElement label = (HTMLElement) ipt.element().getElementsByClassName("mdc-floating-label mdc-floating-label--float-above").item(0);
						  if(evt.selection() > 0) {
							  ipt.element().style.backgroundColor = "rgba(135,51,61,0.7)";
							  text.style.color = "#FFFFFF";
							  label.style.backgroundColor = "#FFFFFF";
						  } else {
							  ipt.element().style.backgroundColor = "transparent";
							  text.style.color = "#888888";
							  label.style.backgroundColor = null;
						  }
					  });
					  iptGenotypes.put(gt, ipt);
					  diseases.add(ipt);
				  });
		}
	}
	private Promise<List<PanelTest.Variant>> snvs(Object[] values) {
		extra.clear();
		return Promise.resolve(Arrays.stream(values).map(dto->{
			JsPropertyMap<Object> map = Js.asPropertyMap(dto);
			String hgvsc = (String) (map.get("hgvsc_in_mane") != null ? map.get("hgvsc_in_mane") : map.get("hgvsc"));
			String hgvsp = (String) (map.get("hgvsp_in_mane") != null ? map.get("hgvsp_in_mane") : map.get("hgvsp"));
			String snv = (String)map.get("snv");
			String analysis = (String)map.get("analysis");
			String gene = (String)map.get("gene.refgene");
			String originHgvsc = hgvsc;
			String originHgvsp = hgvsp;
			String zygosity = (String)map.get("genotype");
			String clazz = (String)map.get("class");
			if("P".equals(clazz)) clazz = "PV";
			else if("LP".equals(clazz)) clazz = "LPV";
			else if("LB".equals(clazz)) clazz = "LBV";
			else if("B".equals(clazz)) clazz = "BV";
			if(variantClassReportable.contains(clazz)) extra.put(snv, map);
			return new PanelTest.Variant().snv(snv).analysis(analysis).gene(gene).originHgvsc(originHgvsc).originHgvsp(originHgvsp).zygosity(zygosity).clazz(clazz);
		}).filter(var-> variantClassReportable.contains(var.clazz())).collect(Collectors.toList()));
	}
	private PanelTest.Variant[] merge(List<PanelTest.Variant> snv, List<PanelTest.Variant> interpretation) {
		List<PanelTest.Variant> tmp2 = new LinkedList<>();
		// SNV, Interpretation 양쪽에 있으면 Interpretation을 채택
		List<PanelTest.Variant> tmp1 = interpretation.stream()
				.map(v -> snv.stream().filter(s -> s.snv().equals(v.snv())).findFirst().map(matchedSnv -> {
					if (v.originHgvsc() == null) v.originHgvsc(matchedSnv.originHgvsc());
					if (v.originHgvsp() == null) v.originHgvsp(matchedSnv.originHgvsp());
					return v;
				}).orElse(null))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		// SNV에만 있고 Interpretation에 없으면 SNV를 추가
		for(PanelTest.Variant v: snv) if(interpretation.stream().noneMatch(c->c.snv().equals(v.snv()))) tmp2.add(v);
		for(PanelTest.Variant v: tmp2) if(tmp1.stream().noneMatch(c->c.snv().equals(v.snv()))) tmp1.add(v);
		return tmp1.stream().toArray(PanelTest.Variant[]::new);
	}
	private static GenomeScreen initialValue(List<PanelTest.Variant> coreInSnv) {
		GenomeScreen value = new GenomeScreen();
		value.variants(coreInSnv.stream().toArray(PanelTest.Variant[]::new));
		return value;
	}
	@Override
	public void update() {
		if ("ON087".equals(service) || "ON088".equals(service)) {
			DomGlobal.alert("영문 결과지를 지원하지 않는 검사입니다.");
		}
		Message msg = Message.builder().id(id).type(Message.MessageType.STRETCH).build();
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		SnvApi.reported(sample, service)
			  .then(this::snvs)
			  .then(snvs->{
				  List<PanelTest.Variant> coreInSnv = new LinkedList<>();
				  snvs.stream().filter(v->cores.contains(v.gene())).sorted(Order::order).forEach(coreInSnv::add);
				  InterpretationApi.interpretation(sample, service)
								   .then(obj->{
									   GenomeScreen dto = (GenomeScreen)obj;
									   if(dto == null) dto = initialValue(coreInSnv);
									   else {
										   List<PanelTest.Variant> coreInInterpretation = new LinkedList<>();
										   if (dto.variants() != null) Arrays.asList(dto.variants()).stream().sorted(Order::order).forEach(coreInInterpretation::add);
										   dto.variants(merge(coreInSnv, coreInInterpretation));
									   }
									   update(dto);
									   return null;
								   });
				  return null;
			  });
		ReportApi.state(sample, service).then(state->{
			boolean isFinal = "F".equalsIgnoreCase(state);
			btnSave.enabled(!isFinal);
			btnPreview.enabled(!isFinal);
			btnSave.text(isFinal?"검사 완료":"SAVE");
			return null;
		});
	}
	private void setDepthCoverage(boolean enabled) {
		iptDepth.input().disabled(!enabled);
		iptCoverage.input().disabled(!enabled);
		if(!enabled) {
			iptDepth.value(null);
			iptCoverage.value(null);
		} else {
			if(result==null || result.coverage()==null || result.meanDepth()==null) AnalysisApi.analysis(sample).then(as -> {
				if (as != null && as.length > 0) update(Arrays.stream(as).max(Comparator.comparing(Analysis2::batch)).get());
				return null;
			}); else {
				iptDepth.value(Double.parseDouble(result.meanDepth()));
				iptCoverage.value(Double.parseDouble(result.coverage()));
			}
		}
	}
	private void update(Analysis2 analysis) {
		if(analysis.get("mean depth tier1(x)")!=null) {
			Double v = Double.parseDouble(analysis.get("mean depth tier1(x)"));
			String value = NumberFormat.getFormat("0.##").format(v);
			this.result.meanDepth(value);
			iptDepth.value(Double.valueOf(value));
		}
		if(analysis.get("% bases above 10 tier1")!=null) {
			Double v = Double.parseDouble(analysis.get("% bases above 10 tier1")) ;
			String value = NumberFormat.getFormat("0.##").format(v);
			this.result.coverage(value);
			iptCoverage.value(Double.valueOf(value));
		}
	}
	private void update(GenomeScreen dto) {
		if(dto == null) dto = new GenomeScreen();
		this.result = dto;
		iptSummary.value(dto.summary());
		iptInterpretation.value(dto.interpretation());
		chkDepthCoverage.value(dto.meanDepth()!=null && dto.coverage()!=null);
		setDepthCoverage(dto.meanDepth()!=null && dto.coverage()!=null);
		if(dto.diseases() != null) for(TestInfo.Disease disease: iptGenes.keySet()) {
			for(String gene: iptGenes.get(disease).keySet()) {
				Optional<GenomeScreen.Gene> found = Arrays.stream(dto.diseases()).filter(g -> g.name().equals(disease.name()))
						.map(d -> d.values()).flatMap(Arrays::stream).filter(g -> g.name().equals(gene)).findAny();
				if(found.isPresent() && found.get().value()!=null && found.get().value()) iptGenes.get(disease).get(gene).select(1);
				else iptGenes.get(disease).get(gene).select(0);
			}
		}
		if(dto.genotypes()!=null) for(TestInfo.Genotype gt: iptGenotypes.keySet()) {
			Optional<GenomeScreen.Genotype> found = Arrays.stream(dto.genotypes())
														  .filter(g->g.gene().equals(gt.gene()))
														  .filter(g->g.pos().equals(gt.pos())).findAny();
			if(found.isPresent()) iptGenotypes.get(gt).select(found.get().genotype());
			else iptGenotypes.get(gt).select(0);
		}
		if(result.variants()!=null) variants.update(result.variants());
		else variants.update(new PanelTest.Variant[0]);
	}
	public void save() {
		if(!DomGlobal.confirm("저장합니다.")) return;
		if(cancerGeneScreens.contains(service.toUpperCase()) && iptInterpretation.value().trim().isEmpty()) return;
		if(iptSummary.value().trim().isEmpty()) return;
		result.summary(iptSummary.value()).variants(variants.values()).interpretation(iptInterpretation.value());
		if(chkDepthCoverage.value()) {
			String value = NumberFormat.getFormat("0.##").format(iptDepth.value());
			result.meanDepth(value);
			value = NumberFormat.getFormat("0.##").format(iptCoverage.value());
			result.coverage(value);
		} else result.meanDepth(null).coverage(null);
		GenomeScreen.Disease[] diseases = iptGenes.keySet().stream().map(disease->{
			GenomeScreen.Gene[] values = iptGenes.get(disease).entrySet().stream().map(e->new GenomeScreen.Gene().name(e.getKey()).value("발견".equalsIgnoreCase(e.getValue().value()))).toArray(GenomeScreen.Gene[]::new);
			return new GenomeScreen.Disease().name(disease.name()).values(values);
		}).toArray(GenomeScreen.Disease[]::new);
		result.diseases(diseases);

		if(test instanceof TestWithRiskScreen) {
			GenomeScreen.Genotype[] genotypes = iptGenotypes.keySet().stream().map(gt->{
				String value = iptGenotypes.get(gt).value();
				return new GenomeScreen.Genotype().gene(gt.gene()).pos(gt.pos()).genotype(value);
			}).toArray(GenomeScreen.Genotype[]::new);
			result.genotypes(genotypes);
		}
		InterpretationApi.save(sample, service, result)
						 .then(callback->{
							 update();
							 DomGlobal.alert("저장되었습니다.");
							 return null;
						 });
	}
	public void auto() {
		result.summary(iptSummary.value()).variants(variants.values()).interpretation(iptInterpretation.value());
		if(chkDepthCoverage.value()) {
			String value = NumberFormat.getFormat("0.##").format(iptDepth.value());
			result.meanDepth(value);
			value = NumberFormat.getFormat("0.##").format(iptCoverage.value());
			result.coverage(value);
		} else result.meanDepth(null).coverage(null);
		dialog.callback(param->InterpretationApi.auto(sample, service, param.previous(result))
				.then(r->{
					update((GenomeScreen) r);
					DomGlobal.alert("생성되었습니다. 저장하세요.");
					return null;
				})).build(result, null, null);
	}
	public void negative() {
		if(!DomGlobal.confirm("음성 결과를 입력합니다.")) return;
		InterpretationParam param = new InterpretationParam();
		result.summary(null).variants(new PanelTest.Variant[0]).interpretation(null);
		if(chkDepthCoverage.value()) {
			String value = NumberFormat.getFormat("0.##").format(iptDepth.value());
			result.meanDepth(value);
			value = NumberFormat.getFormat("0.##").format(iptCoverage.value());
			result.coverage(value);
		} else result.meanDepth(null).coverage(null);
		InterpretationApi.auto(sample, service, param.previous(result))
						 .then(obj->{
							 GenomeScreen result = (GenomeScreen)obj;
							 update(result);
							 DomGlobal.alert("생성되었습니다. 저장하세요.");
							 return null;
						 });
	}
	public void preview() {
		GenomeScreen result = new GenomeScreen().summary(iptSummary.value())
				.variants(variants.values())
				.interpretation(iptInterpretation.value());
		if(chkDepthCoverage.value()) {
			String value = NumberFormat.getFormat("0.##").format(iptDepth.value());
			result.meanDepth(value);
			value = NumberFormat.getFormat("0.##").format(iptCoverage.value());
			result.coverage(value);
		} else result.meanDepth(null).coverage(null);
		GenomeScreen.Disease[] diseases = iptGenes.keySet().stream().map(disease->{
			GenomeScreen.Gene[] values = iptGenes.get(disease).entrySet().stream().map(e->new GenomeScreen.Gene().name(e.getKey()).value("발견".equalsIgnoreCase(e.getValue().value()))).toArray(GenomeScreen.Gene[]::new);
			return new GenomeScreen.Disease().name(disease.name()).values(values);
		}).toArray(GenomeScreen.Disease[]::new);
		result.diseases(diseases);

		if(test instanceof TestWithRiskScreen) {
			GenomeScreen.Genotype[] genotypes = iptGenotypes.keySet().stream().map(gt->{
				String value = iptGenotypes.get(gt).value();
				return new GenomeScreen.Genotype().gene(gt.gene()).pos(gt.pos()).genotype(value);
			}).toArray(GenomeScreen.Genotype[]::new);
			result.genotypes(genotypes);
		}
		ReportApi.preview(sample, service, result).then(blob->{
			PreviewElement preview = PreviewElement.build(blob);
			preview.onConfirm(confirm->{
				if(!DomGlobal.confirm("이대로 결과지를 생성하고 완료 합니다.")) return;
				ProgressApi.open(true);
				ProgressApi.progress(0.3);
				VersionCheckApi.isNew(sample, service)
				.then(isNew->isNew?Promise.resolve(""):DescriptionDialog.dialog())
				.then(description->InterpretationApi.save(sample, service, result).finally_(()->ProgressApi.progress(0.6))
				.then(saved->ReportApi.print(sample, service, description))).then(report->{
					ProgressApi.progress(0.9);
					return ReportApi.publish(sample, service, report.createAt());
				}).then(e->{
					DomGlobal.alert("검사가 완료되었습니다.");
					return null;
				}).finally_(()->{
					update();
					ProgressApi.close();
					preview.element().remove();
				});
			});
			_this.add(preview);
			return null;
		});
	}
	@Override
	public GenomeScreenExpandElement that() {
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
