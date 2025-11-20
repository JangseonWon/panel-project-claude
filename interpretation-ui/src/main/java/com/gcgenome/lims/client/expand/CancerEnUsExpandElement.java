package com.gcgenome.lims.client.expand;

import com.gcgenome.lims.api.*;
import com.gcgenome.lims.client.ExpandElement;
import com.gcgenome.lims.client.Section;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.client.interpretation.Gene;
import com.gcgenome.lims.client.interpretation.PreviewElement;
import com.gcgenome.lims.client.interpretation.SelectDiseaseDialog;
import com.gcgenome.lims.client.interpretation.SnvPanelTestTableElement;
import com.gcgenome.lims.dto.Analysis2;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.dto.interpretation.PanelTest;
import com.gcgenome.lims.test.single.TestInfo;
import com.gcgenome.lims.ui.IconElement;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.NumberFormat;
import elemental2.core.JsArray;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.promise.Promise;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.*;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.span;

public class CancerEnUsExpandElement extends HTMLElementBuilder<HTMLDivElement, CancerEnUsExpandElement> implements ExpandElement<HTMLDivElement> {
	public static CancerEnUsExpandElement build(String id, long sample, String service) {
		return new CancerEnUsExpandElement(div(), id, sample, service);
	}
	private static final List<String> variantClassReportable = Arrays.asList("P", "LP", "PV", "LPV", "VUS");
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	protected final ReportElement elemReport = new ReportElement(div());
	protected final TextFieldElement.TextFieldOutlined<Double> iptDepth = TextFieldElement.numberBox().outlined().css("input").text("Mean depth of coverage").style("width: 300px;");
	protected final TextFieldElement.TextFieldOutlined<Double> iptCoverage = TextFieldElement.numberBox().outlined().css("input").text("% of Target bases > 10X").style("width: 300px;");
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller");
	private final ButtonElement btnAuto = ButtonElement.outline().css("button").before(IconElement.icon("smart_button")).text("Interpretation");
	private final ButtonElement btnNegative = ButtonElement.outline().css("button").before(IconElement.icon("search_off")).text("Negative");
	private final ButtonElement btnSave = ButtonElement.outline().css("button").before(IconElement.icon("save")).text("Save");
	private final ButtonElement btnPreview = ButtonElement.outline().css("button").before(IconElement.icon("preview")).text("Preview");
	private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	protected final SelectDiseaseDialog dialog = SelectDiseaseDialog.instance();
	protected final Map<String, JsPropertyMap<Object>> extra = new HashMap<>();
	private final String id;
	protected final long sample;
	protected final String service;
	private final Set<String> cores;
	protected PanelTest result;
	protected CancerEnUsExpandElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e);
		this.id = id;
		this.sample = sample;
		this.service = service;
		_this = e.css("work").style("height: 100vh;");
		var test = Arrays.stream(TestInfo.TESTS).filter(t->t.code().equals(service)).findAny().orElse(null);
		if(test != null) cores = Arrays.stream(test.genes()).collect(Collectors.toSet());
		else {
			var test2 = Arrays.stream(com.gcgenome.lims.test.panel.TestInfo.TESTS).filter(t->t.code().equals(service)).findAny().orElse(null);
			cores = Arrays.stream(test2.genes()).collect(Collectors.toSet());
		}
		btnHide.onClick(evt->fireStateChangeEvent());
		btnSave.onClick(evt->save());
		btnAuto.onClick(evt->auto());
		btnPreview.onClick(evt->preview());
		btnNegative.onClick(evt->negative());
		elemReport.exclusive(true);
		iptDepth.element().firstElementChild.setAttribute("step", "0.01");
		iptCoverage.element().firstElementChild.setAttribute("step", "0.01");
		layout();
	}
	private void layout() {
		_this.add(div().style("height: calc(100% - 55px); overflow: auto;").add(elemReport)
						.add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptDepth).add(iptCoverage)))
				.add(controller.add(span().add(btnAuto).add(btnNegative))
						.add(span().style("margin-left: 10px;").add(btnSave).add(btnPreview).add(btnHide)))
				.add(dialog);
	}
	private Promise<List<SnvProven<PanelTest.Variant>>> snvs(Object[] values) {
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
			boolean reported = false;
			if(map.get("reported")!=null) try {
				String t = (String) map.get("reported");
				JavaScriptObject obj = (JavaScriptObject) JSON.parse(t);
				Any[] split = Js.asArray(obj);
				Set<String> set = Arrays.stream(split).filter(Objects::nonNull).map(c->c.asString()).collect(Collectors.toSet());
				reported = set.stream().anyMatch(s->!s.contains(sample + ":" + service));
			} catch(Exception e) {
				DomGlobal.console.log(e.getMessage());
			}
			if("P".equals(clazz)) clazz = "PV";
			else if("LP".equals(clazz)) clazz = "LPV";
			if(variantClassReportable.contains(clazz)) extra.put(snv, map);
			return SnvProven.<PanelTest.Variant>builder().proven(reported)
					.variant(new PanelTest.Variant().snv(snv).analysis(analysis).gene(gene).originHgvsc(originHgvsc)
							.originHgvsp(originHgvsp).zygosity(zygosity).clazz(clazz)).build();
		}).filter(c->variantClassReportable.contains(c.variant().clazz())).collect(Collectors.toList()));
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
	private static PanelTest initialValue(List<PanelTest.Variant> coreInSnv, List<PanelTest.Variant> addendumInSnv) {
		PanelTest value = new PanelTest();
		if(coreInSnv.stream().anyMatch(v->"PV".equalsIgnoreCase(v.clazz()) || "LPV".equalsIgnoreCase(v.clazz()))) value.result("POSITIVE");
		else if(coreInSnv.stream().anyMatch(v->"VUS".equalsIgnoreCase(v.clazz()))) value.result("INCONCLUSIVE");
		else value.result("NEGATIVE").interpretation("").resultText("").reasonForReferral("")
					.abbreviation("").abbreviationDisease("").abbreviationReference("");
		value.variants(coreInSnv.stream().toArray(PanelTest.Variant[]::new));
		if(addendumInSnv!=null && !addendumInSnv.isEmpty()) value.addendum(new PanelTest().variants(addendumInSnv.stream().toArray(PanelTest.Variant[]::new))
				.resultText("").abbreviation("").abbreviationDisease("").abbreviationReference("").interpretation(""));
		return value;
	}
	@Override
	public void update() {
		Message msg = Message.builder().id(id).type(Message.MessageType.STRETCH).build();
		DomGlobal.window.parent.postMessage(JSON.stringify(msg), "*");
		SnvApi.reported(sample, service)
				.then(this::snvs)
				.then(snvs->{
					Map<String, Boolean> proven = snvs.stream().collect(Collectors.toMap(v->v.variant().snv(), v->v.proven()));
					List<PanelTest.Variant> coreInSnv = new LinkedList<>();
					List<PanelTest.Variant> addendumInSnv = new LinkedList<>();
					snvs.stream().map(v->v.variant()).filter(v->cores.contains(v.gene())).forEach(coreInSnv::add);
					snvs.stream().map(v->v.variant()).filter(v->!cores.contains(v.gene())).forEach(addendumInSnv::add);
					InterpretationApi.interpretation(sample, service)
							.then(obj->{
								PanelTest dto = (PanelTest)obj;
								if(dto == null) {
									dto = initialValue(coreInSnv, addendumInSnv);
									AnalysisApi.analysis(sample)
											.then(as->{
												if(as != null && as.length > 0)
													update(Arrays.stream(as).max(Comparator.comparing(Analysis2::batch)).get());
												return null;
											});
								} else {
									if (dto.addendum() == null) dto.addendum(new PanelTest().resultText("").abbreviation("").abbreviationDisease("").abbreviationReference("").interpretation(""));
									List<PanelTest.Variant> coreInInterpretation = new LinkedList<>();
									List<PanelTest.Variant> addendumInInterpretation = new LinkedList<>();
									if (dto.variants() != null) for(PanelTest.Variant v: dto.variants()) coreInInterpretation.add(v);
									if (dto.addendum().variants() != null) for(PanelTest.Variant v: dto.addendum().variants()) addendumInInterpretation.add(v);
									dto.variants(merge(coreInSnv, coreInInterpretation));
									dto.addendum().variants(merge(addendumInSnv, addendumInInterpretation));
								}
								update(dto, proven);
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
	private void update(Analysis2 analysis) {
		if(analysis.get("depth(x)")!=null) {
			Double v = Double.parseDouble(analysis.get("depth(x)"));
			String value = NumberFormat.getFormat("0.##").format(v);
			this.result.meanDepth(value);
			iptDepth.value(Double.valueOf(value));
		}
		if(analysis.get("10x(%)")!=null || analysis.get("10x>=(%)")!=null) {
			Double v = analysis.get("10x(%)")!=null ? Double.parseDouble(analysis.get("10x(%)")) : Double.parseDouble(analysis.get("10x>=(%)"));
			String value = NumberFormat.getFormat("0.##").format(v);
			this.result.coverage(value);
			iptCoverage.value(Double.valueOf(value));
		}
	}
	protected Map<String, Boolean> proven = null;
	protected void update(PanelTest dto, Map<String, Boolean> proven) {
		this.result = dto;
		this.proven = proven;
		elemReport.update(dto, proven).exclusive(true);
		if(dto.coverage()!=null) iptCoverage.value(Double.valueOf(dto.coverage()));
		else iptCoverage.value();
		if(dto.meanDepth()!=null) iptDepth.value(Double.valueOf(dto.meanDepth()));
		else iptDepth.value();
	}
	public void save() {
		if(!DomGlobal.confirm("저장합니다.")) return;
		result = elemReport.get().meanDepth(String.valueOf(iptDepth.value())).coverage(String.valueOf(iptCoverage.value()));
		result.addendum(null);
		InterpretationApi.save(sample, service, result)
				.then(callback->{
					update();
					DomGlobal.alert("저장되었습니다.");
					return null;
				});
	}
	public void auto() {
		result = elemReport.get().meanDepth(String.valueOf(iptDepth.value())).coverage(String.valueOf(iptCoverage.value()));
		result.addendum(null);
		extra.values().stream().filter(m -> m.has("mim.inheritance")).forEach(m -> m.set("mim.disease", m.get("mim.disease") + "%" + m.get("mim.inheritance")));
		Map<String, String> map = extra.values().stream().collect(Collectors.toMap(m->(String)m.get("gene.refgene"), m->(String)m.get("mim.disease"), (p1, p2)->p1));

		List<Gene> predefined = new LinkedList<>();
		Stream.concat(Arrays.stream(elemReport.variants.changes("disease")), Arrays.stream(elemReport.variants.changes("inheritance")))
				.distinct().flatMap(v->{
					String mim = map.get(v.gene());
					String[] split;
					if(mim.contains("%")){
						split = mim.split("%");
						String disease = split[0];
						String[] diseases;
						String[] inheritances = ((JsArray<String>)JSON.parse(split[1])).asArray(new String[]{});
						if(disease.contains("|")) diseases = mim.split("\\|");
						else diseases = new String[]{disease};
						List<String[]> temp = new ArrayList<>();
						for(int i = 0; i < diseases.length; i++){
							temp.add(new String[]{diseases[i], inheritances[i]});
						}
						return temp.stream().map(m->Gene.builder().symbol(v.gene())
								.disease(SelectDiseaseDialog.map(v.gene(), m[0], m[1]).get("disease"))
								.abbr(v.disease()).inheritance(v.inheritance()).build());
					}else{
						if(mim.contains("|")) split = mim.split("\\|");
						else split = new String[]{mim};
						return Arrays.stream(split).map(m->Gene.builder().symbol(v.gene())
								.disease(SelectDiseaseDialog.map(v.gene(), m).get("disease"))
								.abbr(v.disease()).inheritance(v.inheritance()).build());
					}
				}).distinct().map(gene->Arrays.stream(GENES).filter(g->g.symbol().equals(gene.symbol())).findFirst().orElse(null)).filter(Objects::nonNull).forEach(predefined::add);
		for(Gene g: GENES) predefined.add(g);
		dialog.callback(param->InterpretationApi.auto(sample, service, param.previous(result))
				.then(result->{
					update((PanelTest) result, proven);
					DomGlobal.alert("생성되었습니다. 저장하세요.");
					return null;
				})).build(result, map,  predefined.toArray(new Gene[0]));
	}
	public void negative() {
		if(!DomGlobal.confirm("음성 결과를 입력합니다.")) return;
		InterpretationApi.negative(sample, service)
				.then(obj->{
					PanelTest result = (PanelTest)obj;
					result.meanDepth(String.valueOf(iptDepth.value())).coverage(String.valueOf(iptCoverage.value()));
					update(result, proven);
					DomGlobal.alert("생성되었습니다. 저장하세요.");
					return null;
				});
	}
	public void preview() {
		PanelTest result = elemReport.get().meanDepth(String.valueOf(iptDepth.value())).coverage(String.valueOf(iptCoverage.value()));
		result.addendum(null);
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
	public CancerEnUsExpandElement that() {
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

	protected final class ReportElement extends HTMLElementBuilder<HTMLDivElement, ReportElement> {
		private final DropDownElement iptResult = DropDownElement.outlined(ListElement.singleLineList()
						.add(ListElement.singleLine().label("POSITIVE"))
						.add(ListElement.singleLine().label("INCONCLUSIVE"))
						.add(ListElement.singleLine().label("NEGATIVE")))
				.css("input").text("Result").style("width: 200px;");
		private final TextFieldElement.TextFieldOutlined<String> iptResultText = TextFieldElement.textBox().outlined().css("input").text("Result(Text)").style("width: calc(100% - 200px);");
		private final SnvPanelTestTableElement variants = SnvPanelTestTableElement.build().style("margin-right: 0px; margin-bottom: 5px; height: fit-content;");
		private final ButtonElement btnToUpward = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-up")).text("Up").enabled(false);
		private final ButtonElement btnToDownward = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-down")).text("Down").enabled(false);
		private final ButtonElement btnToAddendum = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-double-down")).text("Addendum").enabled(false);
		private final TextFieldElement.TextFieldOutlined<String> iptReferenceSeq = TextFieldElement.textBox().outlined().css("input").text("Reference Sequence").style("width: 300px");
		private final TextFieldElement.TextFieldOutlined<String> iptDisease = TextFieldElement.textBox().outlined().css("input").text("Disease Abbr").style("width: calc(100% - 300px);");
		private final TextAreaElement<String> iptAbbr = TextAreaElement.textBox().outlined().css("input").text("Abbreviation").style("width: 100%; min-height: 50px;");
		private final TextAreaElement<String> iptInterpretation = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; height: 250px; margin: 16px;").text("Interpretation");
		private final HTMLContainerBuilder<HTMLDivElement> _this;
		private final Section section = new Section(IconElement.icon(IconElement.Type.Light, "fa-file-prescription"), "CORE").style("display: none;");
		public ReportElement(HTMLContainerBuilder<HTMLDivElement> e) {
			super(e.style("transition: all 300ms ease 0s;"));
			_this = e;
			layout();
			variants.onSelectionChange(evt->{
				btnToUpward.enabled(evt.selection()!=null && evt.selection().length > 0);
				btnToDownward.enabled(evt.selection()!=null && evt.selection().length > 0);
				btnToAddendum.enabled(evt.selection()!=null && evt.selection().length > 0);
			});
			btnToAddendum.onClick(evt->{
				PanelTest.Variant[] target = variants.selected();
				variants.trimming(target);
				elemReport.exclusive(false);
			});
			btnToUpward.onClick(evt->{
				Set<String> selections = Arrays.stream(variants.selected()).map(v->v.snv()).collect(Collectors.toSet());
				PanelTest.Variant[] all = variants.values();
				for(int i = 1; i < all.length; ++i) {
					PanelTest.Variant v = all[i];
					String snv = v.snv();
					if(selections.contains(snv)) {
						PanelTest.Variant p = all[i-1];
						all[i] = all[i-1];
						all[i-1] = v;
					}
				}
				variants.update(all, proven);
			});
			btnToDownward.onClick(evt->{
				Set<String> selections = Arrays.stream(variants.selected()).map(v->v.snv()).collect(Collectors.toSet());
				PanelTest.Variant[] all = variants.values();
				for(int i = all.length-2; i >= 0; --i) {
					PanelTest.Variant v = all[i];
					String snv = v.snv();
					if(selections.contains(snv)) {
						PanelTest.Variant p = all[i+1];
						all[i] = all[i+1];
						all[i+1] = v;
					}
				}
				variants.update(all, proven);
			});
		}
		private void layout() {
			_this.add(section)
					.add(div().style("margin-left: 15px; margin-right: 15px; margin-bottom: 10px;").add(iptResult).add(iptResultText))
					.add(div().style("margin-left: 15px; padding-right: 15px;").add(variants)
							.add(div().style("margin-bottom: 16px; text-align: right;").add(btnToUpward).add(btnToDownward).add(btnToAddendum)))
					.add(div().style("margin-left: 15px; margin-right: 15px;").add(iptReferenceSeq).add(iptDisease))
					.add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptAbbr))
					.add(iptInterpretation);
		}
		public void exclusive(boolean exclusive) {
			if(exclusive) {
				section.style("display: none;");
				_this.style("margin-top: 10px;");
			} else {
				section.style("display: inherit;");
				_this.style("margin-top: none;");
			}
		}
		public ReportElement update(PanelTest result, Map<String, Boolean> proven) {
			if(result == null) {
				iptResultText.value("");
				variants.update(new PanelTest.Variant[0], proven);
				iptReferenceSeq.value("");
				iptDisease.value("");
				iptAbbr.value("");
				iptInterpretation.value("");
			} else {
				if("POSITIVE".equalsIgnoreCase(result.result())) iptResult.select(0);
				if("INCONCLUSIVE".equalsIgnoreCase(result.result())) iptResult.select(1);
				if("NEGATIVE".equalsIgnoreCase(result.result())) iptResult.select(2);
				if(result.resultText()!=null) iptResultText.value(result.resultText());
				else iptResultText.value("");
				if(result.variants()!=null) variants.update(result.variants(), proven);
				else variants.update(new PanelTest.Variant[0], proven);
				if(result.abbreviationReference()!=null) iptReferenceSeq.value(result.abbreviationReference());
				else iptReferenceSeq.value("");
				if(result.abbreviationDisease()!=null) iptDisease.value(result.abbreviationDisease());
				else iptDisease.value("");
				if(result.abbreviation()!=null) iptAbbr.value(result.abbreviation());
				else iptAbbr.value("");
				if(result.interpretation()!=null) iptInterpretation.value(result.interpretation());
				else iptInterpretation.value("");
			}
			return that();
		}
		public PanelTest get() {
			PanelTest result = new PanelTest();
			result.result(iptResult.value()).resultText(iptResultText.value());
			result.variants(variants.values());
			result.abbreviationReference(iptReferenceSeq.value()).abbreviationDisease(iptDisease.value()).abbreviation(iptAbbr.value())
					.interpretation(iptInterpretation.value());
			return result;
		}
		@Override
		public ReportElement that() {
			return this;
		}
	}

	private static final Gene[] GENES = new Gene[] {
			Gene.builder().symbol("APC").abbr("FAP").inheritance("AD").disease("Familial adenomatosis polyposis").build(),
			Gene.builder().symbol("ATM").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("BARD1").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("BRCA1").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("BRCA2").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("BRIP1").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("CDH1").abbr("BCs, HDGC").inheritance("AD").disease("Susceptibility to breast cancer, Hereditary diffuse gastic cancer").build(),
			Gene.builder().symbol("CHEK2").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("EPCAM").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("MEN1").abbr("MEN").inheritance("AD").disease("Multiple endocrine neoplasia").build(),
			Gene.builder().symbol("MLH1").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("MSH2").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("MSH6").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("MUTYH").abbr("MCA").inheritance("AR").disease("Multiple colorectal adenoma").build(),
			Gene.builder().symbol("NBN").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("PALB2").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("PMS2").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("PTEN").abbr("PHTS").inheritance("AD").disease("PTEN hamartoma tumor syndrome").build(),
			Gene.builder().symbol("RAD50").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("RAD51C").abbr("BOCs").inheritance("AD").disease("Susceptibility to breast-ovarian cancer").build(),
			Gene.builder().symbol("RET").abbr("MEN").inheritance("AD").disease("Multiple endocrine neoplasia").build(),
			Gene.builder().symbol("STK11").abbr("BCs, PJS").inheritance("AD").disease("Susceptibility to breast cancer, Peutz-Jeghers syndrome").build(),
			Gene.builder().symbol("TP53").abbr("LFS").inheritance("AD").disease("Li-Fraumeni syndrome").build(),
			Gene.builder().symbol("NF1").abbr("NF1").inheritance("AD").disease("Neurofibromatosis, type 1").build(),
			Gene.builder().symbol("RAD51D").abbr("BOCs").inheritance("AD").disease("Susceptibility to breast-ovarian cancer").build(),
			Gene.builder().symbol("POLD1").abbr("CRCs").inheritance("AD").disease("Susceptibility to colorectal cancer").build(),
			Gene.builder().symbol("POLE").abbr("CRCs").inheritance("AD").disease("Susceptibility to colorectal cancer").build(),
			Gene.builder().symbol("SMAD4").abbr("JPS").inheritance("AD").disease("Juvenile polyposis syndrome").build(),
			Gene.builder().symbol("AIP").abbr("PITA1").inheritance("AD").disease("Pituitary adenoma").build(),
			Gene.builder().symbol("ALK").abbr("NBLSTs").inheritance("AD").disease("Susceptibility to neuroblastoma").build(),
			Gene.builder().symbol("BAP1").abbr("TPDS").inheritance("AD").disease("Tumor predisposition syndrome").build(),
			Gene.builder().symbol("BLM").abbr("BCs, CRCs").inheritance("AD").disease("Susceptibility to breast cancer, Susceptibility to colorectal cancer").build(),
			Gene.builder().symbol("BMPR1A").abbr("JPS").inheritance("AD").disease("Juvenile polyposis syndrome").build(),
			Gene.builder().symbol("BUB1B").abbr("CRCs, MVA1").inheritance("AR").disease("Susceptibility to colorectal cancer, Mosaic variegated aneuploidy syndrome 1").build(),
			Gene.builder().symbol("CDC73").abbr("HRPT2").inheritance("AD").disease("Hyperparathyroidism-jaw tumor syndrome").build(),
			Gene.builder().symbol("CDK4").abbr("CMMs").inheritance("AD").disease("Susceptibility to cutaneous malignant melanoma").build(),
			Gene.builder().symbol("CDKN1C").abbr("BWS").inheritance("AD").disease("Beckwith-Wiedemann syndrome").build(),
			Gene.builder().symbol("CDKN2A").abbr("CMMs, HCs").inheritance("AD").disease("Susceptibility to cutaneous melanoma, Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("CEBPA").abbr("AMLs").inheritance("AD").disease("Susceptibility to acute myeloid leukemia").build(),
			Gene.builder().symbol("CEP57").abbr("MVA2").inheritance("AR").disease("Mosaic variegated aneuploidy syndrome 2").build(),
			Gene.builder().symbol("CYLD").abbr("SBS").inheritance("AD").disease("Brooke-Spiegler syndrome").build(),
			Gene.builder().symbol("DDB2").abbr("XP").inheritance("AR").disease("Xeroderma pigmentosum").build(),
			Gene.builder().symbol("DICER1").abbr("MNG").inheritance("AD").disease("Multinodular goiter").build(),
			Gene.builder().symbol("DIS3L2").abbr("PRLMNS").inheritance("AR").disease("Perlman syndrome").build(),
			Gene.builder().symbol("EGFR").abbr("NSCLCs").inheritance("AD").disease("Susceptibility to nonsmall cell lung cancer").build(),
			Gene.builder().symbol("ERCC2").abbr("XP").inheritance("AR").disease("Xeroderma pigmentosum").build(),
			Gene.builder().symbol("ERCC3").abbr("XP").inheritance("AR").disease("Xeroderma pigmentosum").build(),
			Gene.builder().symbol("ERCC4").abbr("XP").inheritance("AR").disease("Xeroderma pigmentosum").build(),
			Gene.builder().symbol("ERCC5").abbr("XP").inheritance("AR").disease("Xeroderma pigmentosum").build(),
			Gene.builder().symbol("EXT1").abbr("OSRCs").inheritance("AD").disease("Susceptibility to osteochondromas").build(),
			Gene.builder().symbol("EXT2").abbr("OSRCs").inheritance("AD").disease("Susceptibility to osteochondromas").build(),
			Gene.builder().symbol("EZH2").abbr("WVS, HCs").inheritance("AD").disease("Weaver syndrome, Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("FANCA").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCB").abbr("FANC").inheritance("XLR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCC").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCD2").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCE").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCF").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCG").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCI").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCL").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCM").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FH").abbr("HLRCC").inheritance("AD").disease("Hereditary leiomyomatosis and renal cell cancer").build(),
			Gene.builder().symbol("FLCN").abbr("BHD").inheritance("AD").disease("Birt-Hogg-Dube syndrome").build(),
			Gene.builder().symbol("GATA2").abbr("HCs").inheritance("AD").disease("Susceptibility to hematopoietic cancers").build(),
			Gene.builder().symbol("GPC3").abbr("SGBS1, WTs").inheritance("XLR").disease("Susceptibility to Wilms tumor, Simpson-Golabi-Behmel syndrome, type 1").build(),
			Gene.builder().symbol("HNF1A").abbr("HCCs, RCCs").inheritance("AD").disease("Susceptibility to hepatocellular carcinomas, Susceptibility to renal cell carcinoma").build(),
			Gene.builder().symbol("HOXB13").abbr("PCs").inheritance("AD").disease("Susceptibility to prostate cancer").build(),
			Gene.builder().symbol("HRAS").abbr("CSTLO, HCs").inheritance("AD").disease("Costello syndrome, Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("KIT").abbr("GIST, HCs").inheritance("AD").disease("Gastrointestinal stromal tumor, Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("MAX").abbr("PHEOs").inheritance("AD").disease("Susceptibility to pheochromocytoma").build(),
			Gene.builder().symbol("MET").abbr("HCs").inheritance("AD").disease("Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("NF2").abbr("NF2").inheritance("AD").disease("Neurofibromatosis, type 2").build(),
			Gene.builder().symbol("NSD1").abbr("SOTOS").inheritance("AD").disease("Sotos syndrome").build(),
			Gene.builder().symbol("PHOX2B").abbr("NBLSTs").inheritance("AD").disease("Susceptibility to neuroblastoma").build(),
			Gene.builder().symbol("PMS1").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("PPM1D").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("PRF1").abbr("FHL, HCs").inheritance("AR, AD").disease("Familial hemophagocytic lymphohistiocytosis, Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("PRKAR1A").abbr("CNC1").inheritance("AD").disease("Carney complex, type 1").build(),
			Gene.builder().symbol("PTCH1").abbr("BCNS").inheritance("AD").disease("Basal cell nevus syndrome").build(),
			Gene.builder().symbol("AXIN2").abbr("ODCRCS").inheritance("AD").disease("Oligodontia-colorectal cancer syndrome").build(),
			Gene.builder().symbol("CDK12").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("CDKN1B").abbr("MEN4").inheritance("AD").disease("Multiple endocrine neoplasia, type IV").build(),
			Gene.builder().symbol("CHEK1").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("CTNNA1").abbr("HDGC").inheritance("AD").disease("Hereditary diffuse gastric cancer").build(),
			Gene.builder().symbol("GREM1").abbr("HMPS").inheritance("AD").disease("Hereditary Mixed Polyposis syndrome").build(),
			Gene.builder().symbol("MRE11").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("MSH3").abbr("FAP").inheritance("AR").disease("Familial adenomatous polyposis").build(),
			Gene.builder().symbol("NTHL1").abbr("FAP").inheritance("AR").disease("Familial adenomatous polyposis").build(),
			Gene.builder().symbol("PPP2R2A").abbr("PCs").inheritance("AD").disease("Susceptibility to prostate cancer").build(),
			Gene.builder().symbol("RAD51B").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("RAD54L").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("RB1").abbr("RB").inheritance("AD").disease("RETINOBLASTOMA").build(),
			Gene.builder().symbol("SDHA").abbr("HPPS").inheritance("AD").disease("Hereditary paraganglioma-pheochromocytoma syndrome").build(),
			Gene.builder().symbol("SDHAF2").abbr("HPPS").inheritance("AD").disease("Hereditary paraganglioma-pheochromocytoma syndrome").build(),
			Gene.builder().symbol("SDHB").abbr("HPPS").inheritance("AD").disease("Hereditary paraganglioma-pheochromocytoma syndrome").build(),
			Gene.builder().symbol("SDHC").abbr("HPPS").inheritance("AD").disease("Hereditary paraganglioma-pheochromocytoma syndrome").build(),
			Gene.builder().symbol("SDHD").abbr("HPPS").inheritance("AD").disease("Hereditary paraganglioma-pheochromocytoma syndrome").build(),
			Gene.builder().symbol("SMARCA4").abbr("RTPS").inheritance("AD").disease("Rhabdoid tumor predisposition syndrome").build(),
			Gene.builder().symbol("SMARCB1").abbr("RTPS, SWNTS1").inheritance("AD").disease("Rhabdoid tumor predisposition syndrome, Susceptibility to Schwannomatosis").build(),
			Gene.builder().symbol("SUFU").abbr("BCNS, MDB").inheritance("AD").disease("Basal cell nevus syndrome, Medulloblastoma").build(),
			Gene.builder().symbol("TMEM127").abbr("PHEOs").inheritance("AD").disease("Susceptibility to Pheochromocytoma").build(),
			Gene.builder().symbol("TSC1").abbr("TSC").inheritance("AD").disease("Tuberous sclerosis complex").build(),
			Gene.builder().symbol("TSC2").abbr("TSC").inheritance("AD").disease("Tuberous sclerosis complex").build(),
			Gene.builder().symbol("VHL").abbr("VHL, PHEO").inheritance("AD").disease("von Hippel-Lindau syndrome, Pheochromocytoma").build(),
			Gene.builder().symbol("WT1").abbr("WT1").inheritance("AD").disease("Wilms tumor, type 1").build()
	};
}
