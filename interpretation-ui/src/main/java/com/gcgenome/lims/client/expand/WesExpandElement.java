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
import com.gcgenome.lims.dto.interpretation.Des;
import com.gcgenome.lims.dto.interpretation.PanelTest.Variant;
import com.gcgenome.lims.test.I18N;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import com.gcgenome.lims.ui.IconElement;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.NumberFormat;
import elemental2.core.Global;
import elemental2.core.JsArray;
import elemental2.dom.CSSProperties;
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

public class WesExpandElement extends HTMLElementBuilder<HTMLDivElement, WesExpandElement> implements ExpandElement<HTMLDivElement> {
	public static WesExpandElement build(String id, long sample, JsPropertyMap<?> service) {
		return new WesExpandElement(div(), id, sample, service);
	}
	private static final List<String> variantClassReportable = Arrays.asList("P", "LP", "PV", "LPV", "VUS");
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final ReportElement elemReport;
	private final IncidentalFindingElement elemIncidental = new IncidentalFindingElement(div());
	private final TextFieldElement.TextFieldOutlined<Double> iptDepth = TextFieldElement.numberBox().outlined().css("input").text("Mean depth of coverage").style("width: 300px;");
	private final TextFieldElement.TextFieldOutlined<Double> iptCoverage = TextFieldElement.numberBox().outlined().css("input").text("% of Target bases > 10X").style("width: 300px;");
	private final DropDownElement iptReporter = DropDownElement.outlined(ListElement.singleLineList()
																			 .add(ListElement.singleLine().label(""))
																			 .add(ListElement.singleLine().label("설창안"))
																			 .add(ListElement.singleLine().label("이새미"))
																			 .add(ListElement.singleLine().label("기창석"))
																			 .add(ListElement.singleLine().label("이청화")))
												 .css("input").text("보고자").style("width: 300px;") .select(0);
	private final TextFieldElement.TextFieldOutlined<String> iptReporterComment = TextFieldElement.textBox().outlined().css("input").text("Comment").style("width: calc(100% - 300px);");
	private final DropDownElement iptReviewer = DropDownElement.outlined(ListElement.singleLineList()
																			 .add(ListElement.singleLine().label(""))
																			 .add(ListElement.singleLine().label("설창안"))
																			 .add(ListElement.singleLine().label("이새미"))
																			 .add(ListElement.singleLine().label("이청화"))
																			 .add(ListElement.singleLine().label("기창석")))
												 .css("input").text("검토자").style("width: 300px;").select(0);
	private final TextFieldElement.TextFieldOutlined<String> iptReviewerComment = TextFieldElement.textBox().outlined().css("input").text("Comment").style("width: calc(100% - 300px);");
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller").style("display: flex; margin-left: 14px; margin-right: 32px;");
	private final ButtonElement btnAuto = ButtonElement.outline().css("button").before(IconElement.icon("smart_button")).text("Interpretation");
	private final ButtonElement btnNegative = ButtonElement.outline().css("button").before(IconElement.icon("search_off")).text("Negative");
	private final ButtonElement btnSave = ButtonElement.outline().css("button").before(IconElement.icon("save")).text("Save");
	private final ButtonElement btnPreview = ButtonElement.outline().css("button").before(IconElement.icon("preview")).text("Preview");
	private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	private final ButtonElementToggle btnRevision = ButtonElement.toggle().css("button").before(IconElement.icon("label")).text("Revision");
	private final SelectDiseaseDialog dialog = SelectDiseaseDialog.instance();
	private final Map<String, JsPropertyMap<Object>> extra = new HashMap<>();
	//private final TestInfo test;
	private final String id;
	private final long sample;
	private final String category;
	private final String code;
	private Des result;
	private final Set<String> if59;
	private WesExpandElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, JsPropertyMap<?> service) {
		super(e);
		this.id = id;
		this.sample = sample;
		this.category = (String)service.get("interpretation_category");
		this.code = (String)service.get("code");
		_this = e.css("work").style("height: 100vh;");
		elemReport = new ReportElement(div());
		I18N test = Stream.concat(
						Arrays.stream(com.gcgenome.lims.test.wes.TestInfo.TESTS),
						Arrays.stream(com.gcgenome.lims.test.dgs.TestInfo.TESTS)
				).filter(t -> t.code().equals(code)).findAny().orElse(null);
		if("KOKR".equalsIgnoreCase(test.i18n())) no = no1;
		else no = no2;
		if59 = Arrays.stream(TestWithSingleInfo.GENE_INCIDENTAL_FINDINGS).collect(Collectors.toSet());
		btnHide.onClick(evt->fireStateChangeEvent());
		btnSave.onClick(evt->save());
		btnAuto.onClick(evt->auto());
		btnPreview.onClick(evt->preview());
		btnNegative.onClick(evt->negative());
		iptDepth.element().firstElementChild.setAttribute("step", "0.01");
		iptCoverage.element().firstElementChild.setAttribute("step", "0.01");
		layout();
	}
	private void layout() {
		_this.add(div().style("height: calc(100% - 55px); overflow: auto;").add(elemReport)
					   .add(elemIncidental)
					   .add(dialog)
					   .add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptDepth).add(iptCoverage))
					   .add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptReporter).add(iptReporterComment))
					   .add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptReviewer).add(iptReviewerComment)))
			 .add(controller.add(span().style("margin-right: auto;").add(btnRevision))
					 		.add(span().add(btnAuto).add(btnNegative))
							.add(span().style("margin-left: 10px;").add(btnSave).add(btnPreview).add(btnHide)));
	}
	private Promise<List<SnvProven<Variant>>> snvs(Object[] values) {
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
				reported = set.stream().anyMatch(s->!s.contains(sample + ":" + code));
			} catch(Exception e) {
				DomGlobal.console.log(e.getMessage());
			}
			if("P".equals(clazz)) clazz = "PV";
			else if("LP".equals(clazz)) clazz = "LPV";
			if(variantClassReportable.contains(clazz)) extra.put(snv, map);
			return SnvProven.<Variant>builder().proven(reported)
							.variant(new Variant().snv(snv).analysis(analysis).gene(gene).originHgvsc(originHgvsc)
													  .originHgvsp(originHgvsp).zygosity(zygosity).clazz(clazz)).build();
		}).filter(c->{
			return variantClassReportable.contains(c.variant().clazz());
		}).collect(Collectors.toList()));
	}
	private static Des initialValue(List<Variant> coreInSnv, List<Variant> ifInSnv) {
		Des value = new Des();
		if(coreInSnv.stream().anyMatch(v->"PV".equalsIgnoreCase(v.clazz()) || "LPV".equalsIgnoreCase(v.clazz()))) value.result("POSITIVE");
		else if(coreInSnv.stream().anyMatch(v->"VUS".equalsIgnoreCase(v.clazz()))) value.result("INCONCLUSIVE");
		else value.result("NEGATIVE").interpretation("").resultText("").reasonForReferral("")
				  .abbreviation("").abbreviationDisease("").abbreviationReference("");
		value.variants(coreInSnv.stream().toArray(Variant[]::new));
		if(ifInSnv!=null && !ifInSnv.isEmpty()) value.incidentalFindings(new Des().variants(ifInSnv.stream().toArray(Variant[]::new))
																						  .resultText("").abbreviation("").abbreviationDisease("").abbreviationReference("").interpretation(""));
		return value;
	}
	@Override
	public void update() {
		Message msg = Message.builder().id(id).type(Message.MessageType.STRETCH).build();
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		InterpretationApi.interpretation(sample, code)
						 .then(obj->{
							 SnvApi.reported(sample, code)
									 .then(this::snvs)
									 .then(snvs->{
										 Map<String, Boolean> proven = snvs.stream().collect(Collectors.toMap(v->v.variant().snv(), v->v.proven()));
										 List<Variant> coreInSnv = new LinkedList<>();
										 List<Variant> ifInSnv = new LinkedList<>();
										 Des dto = (Des)obj;
										 if(dto == null) {
											 snvs.stream().map(v->v.variant()).filter(v->!if59.contains(v.gene())).forEach(coreInSnv::add);
											 snvs.stream().map(v->v.variant()).filter(v->if59.contains(v.gene())).forEach(ifInSnv::add);
											 dto = initialValue(coreInSnv, ifInSnv);
											 AnalysisApi.analysis(sample)
													 .then(as->{
														 if(as != null && as.length > 0)
															 update(Arrays.stream(as).max(Comparator.comparing(Analysis2::batch)).get());
														 return null;
													 });
										 } else {
											 if (dto.incidentalFindings() == null) dto.incidentalFindings(new Des().resultText("").abbreviation("").abbreviationDisease("").abbreviationReference("").interpretation(""));
											 Map<String, Variant> coreInInterpretation = new HashMap<>();
											 Map<String, Variant> ifInInterpretation = new HashMap<>();
											 List<Variant> cores = new LinkedList<>();
											 List<Variant> ifs = new LinkedList<>();
											 if(dto.variants()!=null) for (Variant v : dto.variants()) {
												 if(!proven.containsKey(v.snv())) continue;
												 for (SnvProven<Variant> s :snvs){
													 if(v.snv().equals(s.variant().snv())){
														 if(v.originHgvsc() == null) v.originHgvsc(s.variant().originHgvsc());
														 if(v.originHgvsp() == null) v.originHgvsp(s.variant().originHgvsp());
													 }
												 }
												 coreInInterpretation.put(v.snv(), v);
												 cores.add(v);
											 }
											 if(dto.incidentalFindings().variants()!=null) for (Variant v : dto.incidentalFindings().variants()) {
												 if(!proven.containsKey(v.snv())) continue;
												 for (SnvProven<Variant> s :snvs){
													 if(v.snv().equals(s.variant().snv())){
														 if(v.originHgvsc() == null) v.originHgvsc(s.variant().originHgvsc());
														 if(v.originHgvsp() == null) v.originHgvsp(s.variant().originHgvsp());
													 }
												 }
												 ifInInterpretation.put(v.snv(), v);
												 ifs.add(v);
											 }
											 for(SnvProven<Variant> p: snvs) {
												 Variant v = p.variant();
												 String snv = v.snv();
												 if(coreInInterpretation.containsKey(snv)) continue;
												 else if(ifInInterpretation.containsKey(snv)) continue;
												 else if(if59.contains(v.gene())) ifs.add(v);
												 else cores.add(v);
											 }
											 dto.variants(cores.stream().toArray(Variant[]::new));
											 dto.incidentalFindings().variants(ifs.stream().toArray(Variant[]::new));
										 }
										 update(dto, proven);
										 return null;
									 });
							 return null;
						 });
		ReportApi.state(sample, code).then(state->{
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
			iptDepth.value(Double.parseDouble(value));
		}
		if(analysis.get("10x(%)")!=null || analysis.get("10x>=(%)")!=null) {
			Double v = analysis.get("10x(%)")!=null ? Double.parseDouble(analysis.get("10x(%)")) : Double.parseDouble(analysis.get("10x>=(%)"));
			String value = NumberFormat.getFormat("0.##").format(v);
			this.result.coverage(value);
			iptCoverage.value(Double.parseDouble(value));
		}
	}
	protected Map<String, Boolean> proven = null;
	private void update(Des dto, Map<String, Boolean> proven) {
		this.result = dto;
		this.proven = proven;
		elemReport.update(dto, proven);
		if(dto.consentIncidentalFindings()!=null) elemIncidental.btnEnable.value(dto.consentIncidentalFindings());
		else elemIncidental.btnEnable.value(sample < 202209120000000L);
		if(dto.incidentalFindings()!=null && dto.incidentalFindings().variants()!=null && dto.incidentalFindings().variants().length > 0) {
			elemIncidental.update(dto.incidentalFindings(), proven);
		} else {
			elemIncidental.update(null, proven);
		}
		if(dto.coverage()!=null) iptCoverage.value(Double.parseDouble(dto.coverage()));
		else iptCoverage.value(0.0);
		if(dto.meanDepth()!=null) iptDepth.value(Double.parseDouble(dto.meanDepth()));
		else iptDepth.value(0.0);
		if(dto.reporter()!=null) iptReporter.select(dto.reporter());
		else iptReporter.select("");
		if(dto.reviewer()!=null) iptReviewer.select(dto.reviewer());
		else iptReviewer.select("");
		iptReporterComment.value(dto.comment1());
		iptReviewerComment.value(dto.comment2());
		btnRevision.value(dto.revision() != null && dto.revision());
	}
	public void save() {
		if(!DomGlobal.confirm("저장합니다.")) return;
		result = elemReport.get().meanDepth(String.valueOf(iptDepth.value())).coverage(String.valueOf(iptCoverage.value()))
				.reporter(iptReporter.value()).comment1(iptReporterComment.value())
				.reviewer(iptReviewer.value()).comment2(iptReviewerComment.value())
				.consentIncidentalFindings(elemIncidental.btnEnable.value())
				.incidentalFindings(elemIncidental.get())
				.revision(btnRevision.value() ? true : null);
		InterpretationApi.save(sample, code, result)
						 .then(callback->{
							 update();
							 DomGlobal.alert("저장되었습니다.");
							 return null;
						 });
	}
	public void auto() {
		result = elemReport.get().meanDepth(iptDepth.value().toString()).coverage(iptCoverage.value().toString())
				.reporter(iptReporter.value()).comment1(iptReporterComment.value())
				.reviewer(iptReviewer.value()).comment2(iptReviewerComment.value())
				.consentIncidentalFindings(elemIncidental.btnEnable.value())
				.incidentalFindings(elemIncidental.get())
				.revision(btnRevision.value() ? true : null);
		extra.values().stream().filter(m -> m.has("mim.inheritance")).forEach(m -> m.set("mim.disease", m.get("mim.disease") + "%" + m.get("mim.inheritance")));
		Map<String, String> map = extra.values().stream().collect(Collectors.toMap(m->(String)m.get("gene.refgene"), m->(String)m.get("mim.disease"), (p1, p2)->p1));
		List<Gene> predefined = new LinkedList<>();
		Stream.concat(
				Stream.concat(Arrays.stream(elemReport.variants.changes("disease")), Arrays.stream(elemReport.variants.changes("inheritance"))),
				Stream.concat(Arrays.stream(elemIncidental.variants.changes("disease")), Arrays.stream(elemIncidental.variants.changes("inheritance")))
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
		dialog.callback(param->InterpretationApi.auto(sample, code, param.previous(result))
												.then(result->{
													update((Des) result, proven);
													DomGlobal.alert("생성되었습니다. 저장하세요.");
													return null;
												})).build(result, map, predefined.toArray(new Gene[0]));
	}
	public void negative() {
		if(!DomGlobal.confirm("음성 결과를 입력합니다.")) return;
		InterpretationApi.negative(sample, code)
						 .then(obj->{
							 Des result = (Des)obj;
							 result.meanDepth(iptDepth.value().toString()).coverage(iptCoverage.value().toString())
								   .reporter(iptReporter.value()).comment1(iptReporterComment.value())
								   .reviewer(iptReviewer.value()).comment2(iptReviewerComment.value());
							 update(result, proven);
							 DomGlobal.alert("생성되었습니다. 저장하세요.");
							 return null;
						 });
	}
	public void preview() {
		Des result = elemReport.get().meanDepth(iptDepth.value().toString()).coverage(iptCoverage.value().toString())
				.reporter(iptReporter.value()).comment1(iptReporterComment.value())
				.reviewer(iptReviewer.value()).comment2(iptReviewerComment.value())
				.consentIncidentalFindings(elemIncidental.btnEnable.value())
				.incidentalFindings(elemIncidental.get())
				.revision(btnRevision.value() ? true : null);
		ReportApi.preview(sample, code, result).then(blob->{
			PreviewElement preview = PreviewElement.build(blob);
			preview.onConfirm(confirm->{
				if(!DomGlobal.confirm("이대로 결과지를 생성하고 완료 합니다.")) return;
				ProgressApi.open(true);
				ProgressApi.progress(0.3);
				VersionCheckApi.isNew(sample, code)
				.then(isNew->isNew?Promise.resolve(""):DescriptionDialog.dialog())
				.then(description->InterpretationApi.save(sample, code, result).finally_(()->ProgressApi.progress(0.6))
				.then(saved->ReportApi.print(sample, code, description))).then(report->{
					ProgressApi.progress(0.9);
					return ReportApi.publish(sample, code, report.createAt());
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
	public WesExpandElement that() {
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

	private final class ReportElement extends HTMLElementBuilder<HTMLDivElement, ReportElement> {
		private final TextAreaElement<String> iptReferral = TextAreaElement.textBox().outlined().css("input").text("Reason for Referral").style("width: -webkit-fill-available; height: 80px;");
		private final DropDownElement iptResult = DropDownElement.outlined(ListElement.singleLineList()
																			   .add(ListElement.singleLine().label("POSITIVE"))
																			   .add(ListElement.singleLine().label("INCONCLUSIVE"))
																			   .add(ListElement.singleLine().label("NEGATIVE")))
												   .css("input").text("Result").style("width: 200px;");
		private final TextFieldElement.TextFieldOutlined<String> iptResultText = TextFieldElement.textBox().outlined().css("input").text("Result(Text)").style("width: calc(100% - 200px);");
		private final SnvPanelTestTableElement variants = SnvPanelTestTableElement.build().style("margin-right: 0px; margin-bottom: 5px; height: fit-content;");
		private final ButtonElement btnToUpward = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-up")).text("Up").enabled(false);
		private final ButtonElement btnToDownward = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-down")).text("Down").enabled(false);
		private final ButtonElement btnToIncidental = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Solid, "fa-angle-double-down")).text("Shift to IF").enabled(false);
		private final TextFieldElement.TextFieldOutlined<String> iptReferenceSeq = TextFieldElement.textBox().outlined().css("input").text("Reference Sequence").style("width: 300px");
		private final TextFieldElement.TextFieldOutlined<String> iptDisease = TextFieldElement.textBox().outlined().css("input").text("Disease Abbr").style("width: calc(100% - 300px);");
		private final TextAreaElement<String> iptAbbr = TextAreaElement.textBox().outlined().css("input").text("Abbreviation").style("width: 100%; min-height: 50px;");
		private final TextAreaElement<String> iptInterpretation = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; height: 250px; margin: 16px;").text("Interpretation");
		private final TextAreaElement<String> iptRecommendation = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; height: 150px; margin: 16px;").text("Recommendations");

		private final HTMLContainerBuilder<HTMLDivElement> _this;
		private final Section section = new Section(IconElement.icon(IconElement.Type.Light, "fa-file-prescription"), "CORE").style("display: inherit;");
		public ReportElement(HTMLContainerBuilder<HTMLDivElement> e) {
			super(e.style("transition: all 300ms ease 0s; margin-top: none;"));
			_this = e;
			layout();
			iptResult.onSelectionChange(evt->{
				if(evt.selection() == 2) iptRecommendation.enabled(false);
				else iptRecommendation.enabled(true);
			});
			variants.onSelectionChange(evt->{
				btnToUpward.enabled(evt.selection()!=null && evt.selection().length > 0);
				btnToDownward.enabled(evt.selection()!=null && evt.selection().length > 0);
				btnToIncidental.enabled(evt.selection()!=null && evt.selection().length > 0);
			});
			btnToIncidental.onClick(evt->{
				Variant[] target = variants.selected();
				elemIncidental.variants.append(target);
				elemIncidental.iptInterpretation.value("");
				elemIncidental.values2.element().style.height = CSSProperties.HeightUnionType.of("100%");
				variants.trimming(target);
			});
			btnToUpward.onClick(evt->{
				Set<String> selections = Arrays.stream(variants.selected()).map(v->v.snv()).collect(Collectors.toSet());
				Variant[] all = variants.values();
				for(int i = 1; i < all.length; ++i) {
					Variant v = all[i];
					String snv = v.snv();
					if(selections.contains(snv)) {
						all[i] = all[i-1];
						all[i-1] = v;
					}
				}
				variants.update(all, proven);
			});
			btnToDownward.onClick(evt->{
				Set<String> selections = Arrays.stream(variants.selected()).map(v->v.snv()).collect(Collectors.toSet());
				Variant[] all = variants.values();
				for(int i = all.length-2; i >= 0; --i) {
					Variant v = all[i];
					String snv = v.snv();
					if(selections.contains(snv)) {
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
				 .add(div().style("margin-left: 15px; margin-right: 15px; margin-bottom: 10px;").add(iptReferral))
				 .add(div().style("margin-left: 15px; padding-right: 15px;").add(variants)
						   .add(div().style("margin-bottom: 16px; text-align: right;").add(btnToUpward).add(btnToDownward).add(btnToIncidental)))
				 .add(div().style("margin-left: 15px; margin-right: 15px;").add(iptReferenceSeq).add(iptDisease))
				 .add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptAbbr))
				 .add(iptInterpretation);
			if("DGS".equalsIgnoreCase(category)) _this.add(iptRecommendation);
		}
		public ReportElement update(Des result, Map<String, Boolean> proven) {
			if(result == null) {
				iptResultText.value("");
				iptReferral.value("");
				variants.update(new Variant[0], proven);
				iptReferenceSeq.value("");
				iptDisease.value("");
				iptAbbr.value("");
				iptInterpretation.value("");
				iptRecommendation.value("");
			} else {
				if("POSITIVE".equalsIgnoreCase(result.result())) {
					iptResult.select(0);
					iptRecommendation.enabled(true);
				}
				else if("INCONCLUSIVE".equalsIgnoreCase(result.result())) {
					iptResult.select(1);
					iptRecommendation.enabled(true);
				}
				else if("NEGATIVE".equalsIgnoreCase(result.result())) {
					iptResult.select(2);
					iptRecommendation.enabled(false);
				}
				if(result.resultText()!=null) iptResultText.value(result.resultText());
				else iptResultText.value("");
				if(result.reasonForReferral()!=null) iptReferral.value(result.reasonForReferral());
				else iptReferral.value("");
				if(result.variants()!=null) variants.update(result.variants(), proven);
				else variants.update(new Variant[0], proven);
				if(result.abbreviationReference()!=null) iptReferenceSeq.value(result.abbreviationReference());
				else iptReferenceSeq.value("");
				if(result.abbreviationDisease()!=null) iptDisease.value(result.abbreviationDisease());
				else iptDisease.value("");
				if(result.abbreviation()!=null) iptAbbr.value(result.abbreviation());
				else iptAbbr.value("");
				if(result.interpretation()!=null) iptInterpretation.value(result.interpretation());
				else iptInterpretation.value("");
				if(result.recommendation()!=null) iptRecommendation.value(result.recommendation());
				else iptRecommendation.value("");
			}
			return that();
		}
		public Des get() {
			Des result = new Des();
			result.result(iptResult.value()).resultText(iptResultText.value()).reasonForReferral(iptReferral.value());
			result.variants(variants.values());
			result.abbreviationReference(iptReferenceSeq.value()).abbreviationDisease(iptDisease.value()).abbreviation(iptAbbr.value())
				  .interpretation(iptInterpretation.value())
					.recommendation(iptRecommendation.value());
			return result;
		}
		@Override
		public ReportElement that() {
			return this;
		}
	}
	private final String no1 = "ACMG에서 권고한 %d개 유전자에서 (Likely) Pathogenic Variant는 발견되지 않았습니다.";
	private final String no2 = "No (Likely) Pathogenic Variant was identified in the %d genes recommended by ACMG";
	private final String no;
	private final class IncidentalFindingElement extends HTMLElementBuilder<HTMLDivElement, IncidentalFindingElement> {
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
		private final Section section = new Section(IconElement.icon(IconElement.Type.Light, "fa-file-medical"), "INCIDENTAL FINDINGS");
		private final ButtonElementToggle btnEnable = ButtonElement.toggle().css("input").style("width:100%;").value(true).text("동의");
		private final HTMLContainerBuilder<HTMLDivElement> values = div().style("overflow: hidden;");
		private final HTMLContainerBuilder<HTMLDivElement> values2 = div().style("overflow: hidden;");
		public IncidentalFindingElement(HTMLContainerBuilder<HTMLDivElement> e) {
			super(e.style("transition: all 300ms ease 0s; margin-top: 20px; max-height: none; overflow: hidden;"));
			_this = e;
			layout();
			variants.onSelectionChange(evt->{
				btnToUpward.enabled(evt.selection()!=null && evt.selection().length > 0);
				btnToDownward.enabled(evt.selection()!=null && evt.selection().length > 0);
				btnToCore.enabled(evt.selection()!=null && evt.selection().length > 0);
			});
			btnToCore.onClick(evt->{
				Variant[] target = variants.selected();
				elemReport.variants.append(target);
				variants.trimming(target);
				if(variants.values()==null || variants.values().length <= 0) {
					iptInterpretation.value(no.replace("%d", if59.size()+""));
					values2.element().style.height = CSSProperties.HeightUnionType.of("0");
					iptResultText.value("");
					iptReferenceSeq.value("");
					iptDisease.value("");
					iptAbbr.value("");
				}
			});
			btnToUpward.onClick(evt->{
				Set<String> selections = Arrays.stream(variants.selected()).map(v->v.snv()).collect(Collectors.toSet());
				Variant[] all = variants.values();
				for(int i = 1; i < all.length; ++i) {
					Variant v = all[i];
					String snv = v.snv();
					if(selections.contains(snv)) {
						all[i] = all[i-1];
						all[i-1] = v;
					}
				}
				variants.update(all, proven);
			});
			btnToDownward.onClick(evt->{
				Set<String> selections = Arrays.stream(variants.selected()).map(v->v.snv()).collect(Collectors.toSet());
				Variant[] all = variants.values();
				for(int i = all.length-2; i >= 0; --i) {
					Variant v = all[i];
					String snv = v.snv();
					if(selections.contains(snv)) {
						all[i] = all[i+1];
						all[i+1] = v;
					}
				}
				variants.update(all, proven);
			});
			btnEnable.onValueChange(evt->{
				if(evt.value()) {
					btnEnable.text("동의");
					values.element().style.height = CSSProperties.HeightUnionType.of("100%");
				} else {
					btnEnable.text("비동의");
					values.element().style.height = CSSProperties.HeightUnionType.of("0");
				}
			});
		}
		private void layout() {
			_this.add(section)
					.add(div().style("margin-left: 15px; margin-right: 15px; margin-bottom: 10px;").add(btnEnable))
					.add(values.add(values2.add(div().style("margin-left: 15px; margin-right: 15px; margin-bottom: 10px;").add(iptResultText))
									.add(div().style("margin-left: 15px; padding-right: 15px;").add(variants)
											.add(div().style("margin-bottom: 16px; text-align: right;").add(btnToUpward).add(btnToDownward).add(btnToCore)))
									.add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptReferenceSeq).add(iptDisease))
									.add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptAbbr)))
							.add(iptInterpretation));
		}
		public IncidentalFindingElement update(Des incidental, Map<String, Boolean> proven) {
			if(incidental == null) {
				iptResultText.value("");
				variants.update(new Variant[0], proven);
				iptInterpretation.value(no.replace("%d", if59.size()+""));
				values2.element().style.height = CSSProperties.HeightUnionType.of("0");
				iptReferenceSeq.value("");
				iptDisease.value("");
				iptAbbr.value("");
			} else {
				if(incidental.resultText()!=null) iptResultText.value(incidental.resultText());
				else iptResultText.value("");
				if(incidental.variants()!=null) variants.update(incidental.variants(), proven);
				else variants.update(new Variant[0], proven);
				if(incidental.abbreviationReference()!=null) iptReferenceSeq.value(incidental.abbreviationReference());
				else iptReferenceSeq.value("");
				if(incidental.abbreviationDisease()!=null) iptDisease.value(incidental.abbreviationDisease());
				else iptDisease.value("");
				if(incidental.abbreviation()!=null) iptAbbr.value(incidental.abbreviation());
				else iptAbbr.value("");
				if(incidental.interpretation()!=null) iptInterpretation.value(incidental.interpretation());
				else iptInterpretation.value("");
				values2.element().style.height = CSSProperties.HeightUnionType.of("100%");
			}
			return that();
		}
		public Des get() {
			if(!btnEnable.value()) return null;
			Des incidentalFindings = new Des();
			incidentalFindings.resultText(iptResultText.value())
							  .variants(variants.values())
							  .abbreviationReference(iptReferenceSeq.value())
							  .abbreviationDisease(iptDisease.value())
							  .abbreviation(iptAbbr.value())
							  .interpretation(iptInterpretation.value());
			if(btnRevision.value()) incidentalFindings.revision(true);
			return incidentalFindings;
		}
		@Override
		public IncidentalFindingElement that() {
			return this;
		}
	}
}
