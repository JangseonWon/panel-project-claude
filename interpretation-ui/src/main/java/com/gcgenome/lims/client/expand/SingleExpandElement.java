package com.gcgenome.lims.client.expand;

import com.gcgenome.lims.api.*;
import com.gcgenome.lims.client.ExpandElement;
import com.gcgenome.lims.client.Section;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.client.interpretation.Gene;
import com.gcgenome.lims.client.interpretation.PreviewElement;
import com.gcgenome.lims.client.interpretation.SelectDiseaseDialog;
import com.gcgenome.lims.client.interpretation.SnvPanelTestTableElement;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.dto.interpretation.PanelTest;
import com.gcgenome.lims.dto.interpretation.PanelTest.Variant;
import com.gcgenome.lims.test.single.TestInfo;
import com.gcgenome.lims.ui.IconElement;
import com.google.gwt.core.client.JavaScriptObject;
import elemental2.core.Global;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.span;

public class SingleExpandElement extends HTMLElementBuilder<HTMLDivElement, SingleExpandElement> implements ExpandElement<HTMLDivElement> {
	public static SingleExpandElement build(String id, long sample, String service) {
		var variantClassReportable = Arrays.asList("P", "LP", "PV", "LPV", "VUS");
		Supplier<SnvPanelTestTableElement> snvPanelTestTableElementSupplier = SnvPanelTestTableElement::build;
		return new SingleExpandElement(div(), id, sample, service, variantClassReportable, snvPanelTestTableElementSupplier);
	}
	public static SingleExpandElement buildWithBenign(String id, long sample, String service) {
		var variantClassReportable = Arrays.asList("P", "LP", "PV", "LPV", "VUS", "B", "LB", "BV", "LVB");
		Supplier<SnvPanelTestTableElement> snvPanelTestTableElementSupplier = SnvPanelTestTableElement::buildWithBenign;
		return new SingleExpandElement(div(), id, sample, service, variantClassReportable, snvPanelTestTableElementSupplier);
	}
	private final List<String> variantClassReportable;
	private final Supplier<SnvPanelTestTableElement> snvPanelTestTableElementSupplier;
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	protected final ReportElement elemReport;
	protected final AddendumElement elemAddendum = new AddendumElement(div());
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller");
	protected final ButtonElementToggle btnAddendum = ButtonElement.toggle().css("button").before(IconElement.icon("post_add")).text("Addendum");
	private final ButtonElement btnAuto = ButtonElement.outline().css("button").before(IconElement.icon("smart_button")).text("Interpretation");
	private final ButtonElement btnNegative = ButtonElement.outline().css("button").before(IconElement.icon("search_off")).text("Negative");
	private final ButtonElement btnSave = ButtonElement.outline().css("button").before(IconElement.icon("save")).text("Save");
	private final ButtonElement btnPreview = ButtonElement.outline().css("button").before(IconElement.icon("preview")).text("Preview");
	private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	protected final SelectDiseaseDialog dialog = SelectDiseaseDialog.instance();
	protected final Map<String, JsPropertyMap<Object>> extra = new HashMap<>();
	private final TestInfo test;
	private final String id;
	protected final long sample;
	protected final String service;
	private final Set<String> cores;
	protected PanelTest result;
	protected SingleExpandElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service, List<String> variantClassReportable, Supplier<SnvPanelTestTableElement> snvPanelTestTableElementSupplier) {
		super(e);
		this.id = id;
		this.sample = sample;
		this.service = service;
		this.variantClassReportable = variantClassReportable;
		this.snvPanelTestTableElementSupplier = snvPanelTestTableElementSupplier;
		if(Arrays.stream(TestInfo.TESTS_WITH_REASON_FOR_REFERRAL).anyMatch(t->t.code().equals(service))) elemReport = new ReportWithRefererralElement(div(), snvPanelTestTableElementSupplier.get());
		else elemReport = new ReportElement(div(), snvPanelTestTableElementSupplier.get());
		_this = e.css("work").style("height: 100vh;");
		test = Arrays.stream(TestInfo.TESTS).filter(t->t.code().equals(service)).findAny().orElse(null);
		cores = Arrays.stream(new String[] {test.gene()}).collect(Collectors.toSet());
		btnHide.onClick(evt->fireStateChangeEvent());
		btnSave.onClick(evt->save());
		btnAuto.onClick(evt->auto());
		btnPreview.onClick(evt->preview());
		btnNegative.onClick(evt->negative());
		btnAddendum.onValueChange(evt->{
			if(evt.value()) {
				elemReport.exclusive(false);
				elemAddendum.exclusive(false);
			} else {
				elemReport.exclusive(true);
				elemAddendum.exclusive(true);
			}
		});
		elemReport.exclusive(true);
		elemAddendum.exclusive(true);
		layout();
	}
	private void layout() {
		_this.add(div().style("height: calc(100% - 55px); overflow: auto;").add(elemReport)
					   .add(elemAddendum)
					   .add(dialog)
					   /*.add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptDepth).add(iptCoverage))*/)
			 .add(controller.add(span().add(btnAuto).add(btnNegative).add(btnAddendum))
							.add(span().style("margin-left: 10px;").add(btnSave).add(btnPreview).add(btnHide)));
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
							.variant(new PanelTest.Variant().snv(snv).analysis(analysis).gene(gene).originHgvsc(originHgvsc).originHgvsp(originHgvsp).zygosity(zygosity).clazz(clazz)).build();
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
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		SnvApi.reported(sample, service)
			  .then(this::snvs)
			  .then(snvs->{
				  Map<String, Boolean> proven_ = snvs.stream().collect(Collectors.toMap(v->v.variant().snv(), v->v.proven()));
				  List<PanelTest.Variant> coreInSnv = new LinkedList<>();
				  List<PanelTest.Variant> addendumInSnv = new LinkedList<>();
				  snvs.stream().map(v->v.variant()).filter(v->cores.contains(v.gene())).forEach(coreInSnv::add);
				  snvs.stream().map(v->v.variant()).filter(v->!cores.contains(v.gene())).forEach(addendumInSnv::add);
				  InterpretationApi.interpretation(sample, service)
								   .then(obj->{
									   PanelTest dto = (PanelTest)obj;
									   if(dto == null) dto = initialValue(coreInSnv, addendumInSnv).reasonForReferral("R/O " + test.referralDefault());
									   else {
										   if (dto.addendum() == null) dto.addendum(new PanelTest().resultText("").abbreviation("").abbreviationDisease("").abbreviationReference("").interpretation(""));
										   List<PanelTest.Variant> coreInInterpretation = new LinkedList<>();
										   List<PanelTest.Variant> addendumInInterpretation = new LinkedList<>();
										   if (dto.variants() != null) for(PanelTest.Variant v: dto.variants()) coreInInterpretation.add(v);
										   if (dto.addendum().variants() != null) for(PanelTest.Variant v: dto.addendum().variants()) addendumInInterpretation.add(v);
										   dto.variants(merge(coreInSnv, coreInInterpretation));
										   dto.addendum().variants(merge(addendumInSnv, addendumInInterpretation));
									   }
									   update(dto, proven_);
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
	protected Map<String, Boolean> proven = null;
	protected void update(PanelTest dto, Map<String, Boolean> proven) {
		this.result = dto;
		this.proven = proven;
		elemReport.update(dto, proven);
		if(dto.addendum()!=null && dto.addendum().variants()!=null && dto.addendum().variants().length > 0) {
			btnAddendum.value(true);
			elemReport.exclusive(false);
			elemAddendum.exclusive(false);
			elemAddendum.update(dto.addendum(), proven);
		} else {
			btnAddendum.value(false);
			elemReport.exclusive(true);
			elemAddendum.exclusive(true);
			elemAddendum.update(null, proven);
		}
	}
	public void save() {
		if(!DomGlobal.confirm("저장합니다.")) return;
		result = elemReport.get();
		if(btnAddendum.value()) result.addendum(elemAddendum.get());
		else result.addendum(null);
		InterpretationApi.save(sample, service, result)
						 .then(callback->{
							 update();
							 DomGlobal.alert("저장되었습니다.");
							 return null;
						 });
	}
	public void auto() {
		result = elemReport.get();
		if(btnAddendum.value()) result.addendum(elemAddendum.get());
		else result.addendum(null);
		extra.values().stream().filter(m -> m.has("mim.inheritance")).forEach(m -> m.set("mim.disease", m.get("mim.disease") + "%" + m.get("mim.inheritance")));
		Map<String, String> map = extra.values().stream().collect(Collectors.toMap(m->(String)m.get("gene.refgene"), m->(String)m.get("mim.disease"), (p1, p2)->p1));
		List<Gene> predefined = new LinkedList<>();
		Stream.concat(
				Stream.concat(Arrays.stream(elemReport.variants.changes("disease")), Arrays.stream(elemReport.variants.changes("inheritance"))),
				Stream.concat(Arrays.stream(elemAddendum.variants.changes("disease")), Arrays.stream(elemAddendum.variants.changes("inheritance")))
		).distinct().flatMap(v->{
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
		}).distinct().forEach(gene->predefined.add(gene));
		dialog.callback(param->InterpretationApi.auto(sample, service, param.previous(result))
												.then(result->{
													update((PanelTest) result, proven);
													DomGlobal.alert("생성되었습니다. 저장하세요.");
													return null;
												})).build(result, map, predefined.toArray(new Gene[0]));
	}
	public void negative() {
		if(!DomGlobal.confirm("음성 결과를 입력합니다.")) return;
		InterpretationApi.negative(sample, service)
						 .then(obj->{
							 PanelTest result = (PanelTest)obj;
							 update(result, proven);
							 DomGlobal.alert("생성되었습니다. 저장하세요.");
							 return null;
						 });
	}
	public void preview() {
		PanelTest result = elemReport.get();
		if(btnAddendum.value()) result.addendum(elemAddendum.get());
		else result.addendum(null);
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
						})
						.finally_(()->{
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
	public SingleExpandElement that() {
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

	protected class ReportElement extends HTMLElementBuilder<HTMLDivElement, ReportElement> {
		protected final DropDownElement iptResult = DropDownElement.outlined(ListElement.singleLineList()
																			   .add(ListElement.singleLine().label("POSITIVE"))
																			   .add(ListElement.singleLine().label("INCONCLUSIVE"))
																			   .add(ListElement.singleLine().label("NEGATIVE")))
												   .css("input").text("Result").style("width: 200px;");
		protected final TextFieldElement.TextFieldOutlined<String> iptResultText = TextFieldElement.textBox().outlined().css("input").text("Result(Text)").style("width: calc(100% - 200px);");
		protected final SnvPanelTestTableElement variants;
		protected final ButtonElement btnToUpward = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-up")).text("Up").enabled(false);
		protected final ButtonElement btnToDownward = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-down")).text("Down").enabled(false);
		protected final ButtonElement btnToAddendum = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-double-down")).text("Addendum").enabled(false);
		protected final TextFieldElement.TextFieldOutlined<String> iptReferenceSeq = TextFieldElement.textBox().outlined().css("input").text("Reference Sequence").style("width: 300px");
		protected final TextFieldElement.TextFieldOutlined<String> iptDisease = TextFieldElement.textBox().outlined().css("input").text("Disease Abbr").style("width: calc(100% - 300px);");
		protected final TextAreaElement<String> iptAbbr = TextAreaElement.textBox().outlined().css("input").text("Abbreviation").style("width: 100%; min-height: 50px;");
		protected final TextAreaElement<String> iptInterpretation = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; height: 250px; margin: 16px;").text("Interpretation");
		protected final HTMLContainerBuilder<HTMLDivElement> _this;
		protected final Section section = new Section(IconElement.icon(IconElement.Type.Light, "fa-file-prescription"), "CORE").style("display: none;");
		public ReportElement(HTMLContainerBuilder<HTMLDivElement> e, SnvPanelTestTableElement variants) {
			super(e.style("transition: all 300ms ease 0s;"));
			this.variants = variants.style("margin-right: 0px; margin-bottom: 5px; height: fit-content;");
			_this = e;
			layout();
			variants.onSelectionChange(evt->{
				btnToUpward.enabled(evt.selection()!=null && evt.selection().length > 0);
				btnToDownward.enabled(evt.selection()!=null && evt.selection().length > 0);
				btnToAddendum.enabled(evt.selection()!=null && evt.selection().length > 0);
			});
			btnToAddendum.onClick(evt->{
				btnAddendum.value(true);
				Variant[] target = variants.selected();
				elemAddendum.variants.append(target);
				variants.trimming(target);
				elemReport.exclusive(false);
				elemAddendum.exclusive(false);
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
		protected void layout() {
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
				if(result.variants()!=null) {
					variants.update(result.variants(), proven);
				}
				else {
					variants.update(new PanelTest.Variant[0], proven);
				}
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

	private final class ReportWithRefererralElement extends ReportElement {
		private final TextFieldElement.TextFieldOutlined<String> iptReferral = TextFieldElement.textBox().outlined().css("input").text("Reason for Referral").style("width: 300px;");
		public ReportWithRefererralElement(HTMLContainerBuilder<HTMLDivElement> e, SnvPanelTestTableElement variants) {
			super(e, variants);
			iptResultText.style("width: calc(100% - 500px);");
			layout();
		}
		@Override
		protected void layout() {
			_this.element().innerHTML = "";
			_this.add(section)
				 .add(div().style("margin-left: 15px; margin-right: 15px; margin-bottom: 10px;").add(iptResult).add(iptResultText).add(iptReferral))
				 .add(div().style("margin-left: 15px; padding-right: 15px;").add(variants)
						   .add(div().style("margin-bottom: 16px; text-align: right;").add(btnToUpward).add(btnToDownward).add(btnToAddendum)))
				 .add(div().style("margin-left: 15px; margin-right: 15px;").add(iptReferenceSeq).add(iptDisease))
				 .add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptAbbr))
				 .add(iptInterpretation);
		}
		@Override
		public ReportElement update(PanelTest result, Map<String, Boolean> proven) {
			super.update(result, proven);
			if(result == null) iptReferral.value("");
			else {
				if(result.reasonForReferral()!=null) iptReferral.value(result.reasonForReferral());
				else iptReferral.value("");
			}
			return that();
		}
		@Override
		public PanelTest get() {
			return super.get().reasonForReferral(iptReferral.value());
		}
	}

	protected final class AddendumElement extends HTMLElementBuilder<HTMLDivElement, AddendumElement> {
		private final TextFieldElement.TextFieldOutlined<String> iptResultText = TextFieldElement.textBox().outlined().css("input").text("Result(Text)").style("width: 100%;");
		private final SnvPanelTestTableElement variants = SnvPanelTestTableElement.build().style("margin-right: 0px; margin-bottom: 5px; height: fit-content;");
		private final ButtonElement btnToUpward = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-up")).text("Up").enabled(false);
		private final ButtonElement btnToDownward = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-down")).text("Down").enabled(false);
		private final ButtonElement btnToCore = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-double-up")).text("Shift to Core").enabled(false);
		private final TextFieldElement.TextFieldOutlined<String> iptReferenceSeq = TextFieldElement.textBox().outlined().css("input").text("Reference Sequence").style("width: 300px");
		private final TextFieldElement.TextFieldOutlined<String> iptDisease = TextFieldElement.textBox().outlined().css("input").text("Disease Abbr").style("width: calc(100% - 300px);");
		private final TextAreaElement<String> iptAbbr = TextAreaElement.textBox().outlined().css("input").text("Abbreviation").style("width: 100%; min-height: 50px;");
		private final TextAreaElement<String> iptInterpretation = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; height: 250px; margin: 16px;").text("Interpretation");
		private final HTMLContainerBuilder<HTMLDivElement> _this;
		private final Section section = new Section(IconElement.icon(IconElement.Type.Light, "fa-file-medical"), "ADDENDUM");
		public AddendumElement(HTMLContainerBuilder<HTMLDivElement> e) {
			super(e.style("transition: all 300ms ease 0s; height: 0px; overflow: hidden;"));
			_this = e;
			layout();
			variants.onSelectionChange(evt->{
				btnToUpward.enabled(evt.selection()!=null && evt.selection().length > 0);
				btnToDownward.enabled(evt.selection()!=null && evt.selection().length > 0);
				btnToCore.enabled(evt.selection()!=null && evt.selection().length > 0);
			});
			btnToCore.onClick(evt->{
				PanelTest.Variant[] target = variants.selected();
				elemReport.variants.append(target);
				variants.trimming(target);
				if(variants.values()==null || variants.values().length <= 0) {
					btnAddendum.value(false);
					elemReport.exclusive(true);
					elemAddendum.exclusive(true);
					iptInterpretation.value("");
				}
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
				 .add(div().style("margin-left: 15px; margin-right: 15px; margin-bottom: 10px;").add(iptResultText))
				 .add(div().style("margin-left: 15px; padding-right: 15px;").add(variants)
						   .add(div().style("margin-bottom: 16px; text-align: right;").add(btnToUpward).add(btnToDownward).add(btnToCore)))
				 .add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptReferenceSeq).add(iptDisease))
				 .add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptAbbr))
				 .add(iptInterpretation);
		}
		public void exclusive(boolean exclusive) {
			if(exclusive) _this.style("transition: all 300ms ease 0s; overflow: hidden; min-height: 0px; max-height: 0px;");
			else _this.style("transition: all 300ms ease 0s; margin-top: 20px; min-height: 600px; max-height: none;");
		}
		public AddendumElement update(PanelTest addendum, Map<String, Boolean> proven) {
			if(addendum == null) {
				iptResultText.value("");
				variants.update(new PanelTest.Variant[0], proven);
				iptReferenceSeq.value("");
				iptDisease.value("");
				iptAbbr.value("");
				iptInterpretation.value("");
			} else {
				if(addendum.resultText()!=null) iptResultText.value(addendum.resultText());
				else iptResultText.value("");
				if(addendum.variants()!=null) {
					variants.update(addendum.variants(), proven);
				}
				else {
					variants.update(new PanelTest.Variant[0], proven);
				}
				if(addendum.abbreviationReference()!=null) iptReferenceSeq.value(addendum.abbreviationReference());
				else iptReferenceSeq.value("");
				if(addendum.abbreviationDisease()!=null) iptDisease.value(addendum.abbreviationDisease());
				else iptDisease.value("");
				if(addendum.abbreviation()!=null) iptAbbr.value(addendum.abbreviation());
				else iptAbbr.value("");
				if(addendum.interpretation()!=null) iptInterpretation.value(addendum.interpretation());
				else iptInterpretation.value("");
			}
			return that();
		}
		public PanelTest get() {
			PanelTest addendum = new PanelTest();
			addendum.resultText(iptResultText.value());
			addendum.variants(variants.values());
			addendum.abbreviationReference(iptReferenceSeq.value()).abbreviationDisease(iptDisease.value()).abbreviation(iptAbbr.value())
					.interpretation(iptInterpretation.value());
			return addendum;
		}
		@Override
		public AddendumElement that() {
			return this;
		}
	}
}
