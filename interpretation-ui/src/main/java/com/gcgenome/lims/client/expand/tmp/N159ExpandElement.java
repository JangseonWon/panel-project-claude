package com.gcgenome.lims.client.expand.tmp;

import com.gcgenome.lims.api.InterpretationApi;
import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.ReportApi;
import com.gcgenome.lims.api.VersionCheckApi;
import com.gcgenome.lims.client.ExpandElement;
import com.gcgenome.lims.client.Section;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.client.expand.DescriptionDialog;
import com.gcgenome.lims.client.interpretation.PreviewElement;
import com.gcgenome.lims.client.interpretation.SelectDiseaseDialog;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.dto.interpretation.tmp.N159Dto;
import com.gcgenome.lims.test.tmp.N159;
import com.gcgenome.lims.ui.IconElement;
import elemental2.core.Global;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.promise.Promise;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.*;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.span;

public class N159ExpandElement extends HTMLElementBuilder<HTMLDivElement, N159ExpandElement> implements ExpandElement<HTMLDivElement> {
	public static N159ExpandElement build(String id, long sample, String service) {
		return new N159ExpandElement(div(), id, sample, service);
	}
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	protected final ReportElement elemReport = new ReportElement(div());
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller");
	private final ButtonElement btnAuto = ButtonElement.outline().css("button").before(IconElement.icon("smart_button")).text("Interpretation");
	private final ButtonElement btnNegative = ButtonElement.outline().css("button").before(IconElement.icon("search_off")).text("Negative");
	private final ButtonElement btnSave = ButtonElement.outline().css("button").before(IconElement.icon("save")).text("Save");
	private final ButtonElement btnPreview = ButtonElement.outline().css("button").before(IconElement.icon("preview")).text("Preview");
	private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	protected final SelectDiseaseDialog dialog = SelectDiseaseDialog.instance();
	protected final Map<String, JsPropertyMap<Object>> extra = new HashMap<>();
	private final N159 test = N159.builder().build();
	private final String id;
	protected final long sample;
	protected final String service;
	protected N159Dto result;
	protected N159ExpandElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e);
		this.id = id;
		this.sample = sample;
		this.service = service;
		_this = e.css("work").style("height: 100vh;");
		btnHide.onClick(evt->fireStateChangeEvent());
		btnSave.onClick(evt->save());
		btnAuto.onClick(evt->auto());
		btnPreview.onClick(evt->preview());
		btnNegative.onClick(evt->negative());
		elemReport.exclusive(true);
		layout();
	}
	private void layout() {
		_this.add(div().style("height: calc(100% - 55px); overflow: auto;").add(elemReport))
			 .add(controller.add(span().add(btnAuto).add(btnNegative))
							.add(span().style("margin-left: 10px;").add(btnSave).add(btnPreview).add(btnHide)))
			 .add(dialog);
	}
	private Promise<List<N159Dto.Variant>> snvs(Object[] values) {
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
			extra.put(snv, map);
			if("P".equals(clazz)) clazz = "PV";
			else if("LP".equals(clazz)) clazz = "LPV";
			return new N159Dto.Variant().snv(snv).analysis(analysis).gene(gene).originHgvsc(originHgvsc)
										  .originHgvsp(originHgvsp)/*.zygosity(zygosity).clazz(clazz)*/;
		})/*.filter(c->variantClassReportable.contains(c.clazz()))*/.collect(Collectors.toList()));
	}
	private N159Dto.Variant[] merge(List<N159Dto.Variant> snv, List<N159Dto.Variant> interpretation) {
		List<N159Dto.Variant> tmp2 = new LinkedList<>();
		// SNV, Interpretation 양쪽에 있으면 Interpretation을 채택
		List<N159Dto.Variant> tmp1 = interpretation.stream()
				.map(v -> snv.stream().filter(s -> s.snv().equals(v.snv())).findFirst().map(matchedSnv -> {
					if (v.originHgvsc() == null) v.originHgvsc(matchedSnv.originHgvsc());
					if (v.originHgvsp() == null) v.originHgvsp(matchedSnv.originHgvsp());
					return v;
				}).orElse(null))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		// SNV에만 있고 Interpretation에 없으면 SNV를 추가
		for(N159Dto.Variant v: snv) if(interpretation.stream().noneMatch(c->c.snv().equals(v.snv()))) tmp2.add(v);
		for(N159Dto.Variant v: tmp2) if(tmp1.stream().noneMatch(c->c.snv().equals(v.snv()))) tmp1.add(v);
		return tmp1.stream().toArray(N159Dto.Variant[]::new);
	}
	private static N159Dto initialValue(List<N159Dto.Variant> coreInSnv) {
		N159Dto value = new N159Dto();
		value.variants(coreInSnv.stream().toArray(N159Dto.Variant[]::new));
		return value;
	}
	@Override
	public void update() {
		Message msg = Message.builder().id(id).type(Message.MessageType.STRETCH).build();
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		//SnvApi.reported(sample, service)
		//	  .then(this::snvs)
		//	  .last(snvs->{
				  List<N159Dto.Variant> coreInSnv = new LinkedList<>();
				  InterpretationApi.interpretation(sample, service)
								   .then(obj->{
									   N159Dto dto = (N159Dto)obj;
									   if(dto == null) dto = initialValue(coreInSnv);
									   else {
									   		List<N159Dto.Variant> coreInInterpretation = new LinkedList<>();
										   if (dto.variants() != null) Arrays.asList(dto.variants()).stream().forEach(coreInInterpretation::add);
										   dto.variants(coreInInterpretation.stream().toArray(N159Dto.Variant[]::new));
									   }
									   update(dto);
									   return null;
								   });
		//	  });
	}
	protected void update(N159Dto dto) {
		this.result = dto;
		elemReport.update(dto);
		elemReport.exclusive(true);
	}
	public void save() {
		if(!DomGlobal.confirm("저장합니다.")) return;
		result = elemReport.get();
		InterpretationApi.save(sample, service, result)
						 .then(callback->{
							 update();
							 DomGlobal.alert("저장되었습니다.");
							 return null;
						 });
	}
	public void auto() {
		result = elemReport.get();
		extra.values().stream().filter(m -> m.has("mim.inheritance")).forEach(m -> m.set("mim.disease", m.get("mim.disease") + "%" + m.get("mim.inheritance")));
		Map<String, String> map = extra.values().stream().collect(Collectors.toMap(m->(String)m.get("gene.refgene"), m->(String)m.get("mim.disease"), (p1, p2)->p1));
		dialog.callback(param->InterpretationApi.auto(sample, service, param.previous(result))
												.then(result->{
													update((N159Dto) result);
													DomGlobal.alert("생성되었습니다. 저장하세요.");
													return null;
												})).build(map);
	}
	public void negative() {
		if(!DomGlobal.confirm("음성 결과를 입력합니다.")) return;
		InterpretationApi.negative(sample, service)
						 .then(obj->{
							 N159Dto result = (N159Dto)obj;
							 update(result);
							 DomGlobal.alert("생성되었습니다. 저장하세요.");
							 return null;
						 });
	}
	public void preview() {
		N159Dto result = elemReport.get();
		ReportApi.preview(sample, service, result).then(blob->{
			PreviewElement preview = PreviewElement.build(blob);
			preview.onConfirm(confirm->{
				if(!DomGlobal.confirm("이대로 결과지를 생성하고 완료 합니다.")) return;
				ProgressApi.open(true);
				ProgressApi.progress(0.3);
				VersionCheckApi.isNew(sample, service)
				.then(isNew->isNew?Promise.resolve(""): DescriptionDialog.dialog())
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
	public N159ExpandElement that() {
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

	protected static final class ReportElement extends HTMLElementBuilder<HTMLDivElement, ReportElement> {
		private final DropDownElement iptResult = DropDownElement.outlined(ListElement.singleLineList()
																 .add(ListElement.singleLine().label("POSITIVE"))
																 .add(ListElement.singleLine().label("NEGATIVE")))
												   .css("input").text("Result").style("width: 200px;");
		private final TextFieldElement.TextFieldOutlined<String> iptResultText = TextFieldElement.textBox().outlined().css("input").text("Result(Text)").style("width: calc(100% - 200px);");
		private final ButtonElement btnAddVariant = ButtonElement.outline().css("button").before(net.sayaya.ui.IconElement.icon("plus_one")).text("Append Variant");
		private final ButtonElement btnRemVariant = ButtonElement.outline().css("button").before(net.sayaya.ui.IconElement.icon("remove_circle")).text("Remove Variant");
		private final SnvN159TestTableElement variants = SnvN159TestTableElement.build().style("margin-right: 0px;");
		private final TextFieldElement.TextFieldOutlined<String> iptReferenceSeq = TextFieldElement.textBox().outlined().css("input").text("Reference Sequence").style("width: 100%; min-height: 50px;");
		private final TextAreaElement<String> iptInterpretation = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; height: 250px; margin: 16px;").text("Interpretation");
		private final HTMLContainerBuilder<HTMLDivElement> _this;
		private final Section section = new Section(IconElement.icon(IconElement.Type.Light, "fa-file-prescription"), "CORE").style("display: none;");
		public ReportElement(HTMLContainerBuilder<HTMLDivElement> e) {
			super(e.style("transition: all 300ms ease 0s;"));
			_this = e;
			btnAddVariant.onClick(evt->variants.append());
			btnRemVariant.onClick(evt->variants.trimming());
			layout();
		}
		private void layout() {
			_this.add(section)
				 .add(div().style("margin-left: 15px; margin-right: 15px; margin-bottom: 10px;").add(iptResult).add(iptResultText))
				 .add(div().style("margin-left: 15px; padding-right: 15px;").add(variants))
				 .add(div().style("margin-bottom: 16px; text-align: right;").add(btnAddVariant).add(btnRemVariant))
				 .add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptReferenceSeq))
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
		public ReportElement update(N159Dto result) {
			if(result == null) {
				iptResultText.value("");
				variants.update(new N159Dto.Variant[0]);
				iptReferenceSeq.value("");
				iptInterpretation.value("");
			} else {
				if("POSITIVE".equalsIgnoreCase(result.result())) iptResult.select(0);
				if("NEGATIVE".equalsIgnoreCase(result.result())) iptResult.select(1);
				if(result.resultText()!=null) iptResultText.value(result.resultText());
				else iptResultText.value("");
				if(result.variants()!=null) variants.update(result.variants());
				else variants.update(new N159Dto.Variant[0]);
				if(result.abbreviationReference()!=null) iptReferenceSeq.value(result.abbreviationReference());
				else iptReferenceSeq.value("");
				if(result.interpretation()!=null) iptInterpretation.value(result.interpretation());
				else iptInterpretation.value("");
			}
			return that();
		}
		public N159Dto get() {
			N159Dto result = new N159Dto();
			result.result(iptResult.value())
				  .resultText(iptResultText.value())
				  .variants(variants.values())
				  .abbreviationReference(iptReferenceSeq.value())
				  .interpretation(iptInterpretation.value());
			return result;
		}
		@Override
		public ReportElement that() {
			return this;
		}
	}
}
