package com.gcgenome.lims.client.expand;

import com.gcgenome.lims.api.InterpretationApi;
import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.ReportApi;
import com.gcgenome.lims.api.VersionCheckApi;
import com.gcgenome.lims.client.ExpandElement;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.client.interpretation.PreviewElement;
import com.gcgenome.lims.client.interpretation.SnvHrdTableElement;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.dto.interpretation.Hrd;
import com.gcgenome.lims.dto.interpretation.InterpretationParam;
import elemental2.core.Global;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.promise.Promise;
import net.sayaya.ui.*;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.jboss.elemento.Elements.*;

public class HrdExpandElement extends HTMLElementBuilder<HTMLDivElement, HrdExpandElement> implements ExpandElement<HTMLDivElement> {
	public static HrdExpandElement build(String id, long sample, String service) {
		return new HrdExpandElement(div(), id, sample, service);
	}
	private final HTMLContainerBuilder<HTMLDivElement> _this;

	private final TextFieldElement.TextFieldOutlined<String> iptCancerType = TextFieldElement.textBox().outlined().style("width: -webkit-fill-available; margin: 16px;").css("input").text("Cancer Type");
	private final DropDownElement iptHrd		= DropDownElement.outlined(ListElement.singleLineList().add(ListElement.singleLine().label("Positive")).add(ListElement.singleLine().label("Negative")).add(ListElement.singleLine().label("Fail"))).css("input").text("HRD").enabled(false).style("min-width: 150px;");
	private final DropDownElement iptGi		= DropDownElement.outlined(ListElement.singleLineList().add(ListElement.singleLine().label("Positive")).add(ListElement.singleLine().label("Negative")).add(ListElement.singleLine().label("Fail"))).css("input").text("유전체 불안정성").style("min-width: 150px;");
	private final TextFieldElement.TextFieldOutlined<Double> iptGiScore = TextFieldElement.numberBox().outlined().css("input").text("유전체 불안정성 점수").style("min-width: 150px;");
	private final DropDownElement iptBrca		= DropDownElement.outlined(ListElement.singleLineList().add(ListElement.singleLine().label("Positive")).add(ListElement.singleLine().label("Negative")).add(ListElement.singleLine().label("Fail"))).css("input").text("BRCA").style("min-width: 150px;");
	private final DropDownElement iptSnv		= DropDownElement.outlined(ListElement.singleLineList().add(ListElement.singleLine().label("Pass")).add(ListElement.singleLine().label("Non-Pass")).add(ListElement.singleLine().label("Fail"))).css("input").text("DNA(SNV) QC").style("min-width: 150px;");
	private final DropDownElement iptCnv		= DropDownElement.outlined(ListElement.singleLineList().add(ListElement.singleLine().label("Pass")).add(ListElement.singleLine().label("Non-Pass")).add(ListElement.singleLine().label("Fail"))).css("input").text("DNA(CNV) QC").style("min-width: 150px;");
	private final TextAreaElement<String> iptInterpretation = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; height: 130px; margin: 16px;").text("Interpretation");
	private final VariantResultElement iptVariantBrca = new VariantResultElement("BRCA");
	private final VariantResultElement iptVariantTier1 = new VariantResultElement("TIER1");
	private final VariantResultElement iptVariantTier2 = new VariantResultElement("TIER2");
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller");
	private final ButtonElement btnAuto = ButtonElement.outline().css("button").before(IconElement.icon("smart_button")).text("Interpretation");
	private final ButtonElement btnNegative = ButtonElement.outline().css("button").before(IconElement.icon("search_off")).text("Negative");
	private final ButtonElement btnSave = ButtonElement.outline().css("button").before(IconElement.icon("save")).text("Save");
	private final ButtonElement btnPreview = ButtonElement.outline().css("button").before(IconElement.icon("preview")).text("Preview");
	private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	private final String id;
	private final long sample;
	private final String service;
	private Hrd result;
	private HrdExpandElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e);
		this.id = id;
		this.sample = sample;
		this.service = service;
		_this = e.css("work").style("height: 100vh;");
		layout();
		btnHide.onClick(evt->fireStateChangeEvent());
		btnSave.onClick(evt->save());
		btnAuto.onClick(evt->auto());
		btnPreview.onClick(evt->preview());
		btnNegative.onClick(evt->negative());
		iptGi.onValueChange(evt->{
			if("Fail".equalsIgnoreCase(evt.value())) fail();
		});
		iptSnv.onValueChange(evt->{
			if("Fail".equalsIgnoreCase(evt.value())) fail();
		});
		iptCnv.onValueChange(evt->{
			if("Fail".equalsIgnoreCase(evt.value())) fail();
		});
	}

	private void layout() {
		_this.add(div().style("height: calc(100% - 55px); overflow: auto;")
				.add(iptCancerType)
				.add(div().style("margin-left: 15px; margin-top: 10px; margin-right: 15px;").add(iptHrd).add(iptGi).add(iptGiScore).add(iptBrca).add(iptSnv).add(iptCnv))
				.add(iptInterpretation)
				.add(iptVariantBrca)
				.add(iptVariantTier1)
				.add(iptVariantTier2)
				.add(controller.add(span().add(btnAuto).add(btnNegative))
						.add(span().style("margin-left: 10px;").add(btnSave).add(btnPreview).add(btnHide))));
	}
	@Override
	public void update() {
		InterpretationApi.interpretation(sample, service).then(reports->{
			this.update((Hrd)reports);
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
	private void update(Hrd dto) {
		Message msg = Message.builder().id(id).type(Message.MessageType.STRETCH).build();
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		if(dto == null) dto = new Hrd();
		if(dto.results()==null) dto.results(new Hrd.GeneResult[]{new Hrd.GeneResult()});
		this.result = dto;
		iptInterpretation.value(dto.interpretation());
		iptCancerType.value(dto.cancerType()!=null?dto.cancerType():"");
		iptGi.select(dto.gi());
		if(dto.giScore()!=null) iptGiScore.value(dto.giScore() + 0.0);
		else iptGiScore.value(null);
		Hrd.GeneResult brca = Arrays.stream(dto.results()).filter(g->"BRCA".equalsIgnoreCase(g.tier())).findAny().orElse(new Hrd.GeneResult().tier("BRCA"));
		iptVariantBrca.update(brca);
		iptBrca.select(brca.result());
		if("Positive".equalsIgnoreCase(dto.gi()) || "Positive".equalsIgnoreCase(brca.result())) iptHrd.select("Positive");
		else iptHrd.select("Negative");
		iptSnv.select(dto.snv());
		iptCnv.select(dto.cnv());
		Hrd.GeneResult tier1 = Arrays.stream(dto.results()).filter(g->"TIER1".equalsIgnoreCase(g.tier())).findAny().orElse(new Hrd.GeneResult().tier("TIER1"));
		iptVariantTier1.update(tier1);
		Hrd.GeneResult tier2 = Arrays.stream(dto.results()).filter(g->"TIER2".equalsIgnoreCase(g.tier())).findAny().orElse(new Hrd.GeneResult().tier("TIER2"));
		iptVariantTier2.update(tier2);
	}
	public void save() {
		if(!DomGlobal.confirm("저장합니다.")) return;
		result = result.cancerType(iptCancerType.value()).gi(iptGi.value())
					   .giScore(iptGiScore.value()!=null?iptGiScore.value().intValue():0)
					   .snv(iptSnv.value()).cnv(iptCnv.value())
					   .interpretation(iptInterpretation.value())
					   .results(new Hrd.GeneResult[] {
							   iptVariantBrca.value(),
							   iptVariantTier1.value(),
							   iptVariantTier2.value()});
		InterpretationApi.save(sample, service, result).then(callback->{
			update();
			DomGlobal.alert("저장되었습니다.");
			return null;
		});
	}
	public void auto() {
		if(!DomGlobal.confirm("입력된 정보를 바탕으로 판독 문구를 자동 생성합니다.")) return;
		result = result.cancerType(iptCancerType.value()).gi(iptGi.value())
					   .giScore(iptGiScore.value()!=null?iptGiScore.value().intValue():0)
					   .snv(iptSnv.value()).cnv(iptCnv.value())
					   .interpretation(iptInterpretation.value())
					   .results(new Hrd.GeneResult[] {
							   iptVariantBrca.value(),
							   iptVariantTier1.value(),
							   iptVariantTier2.value()});
		InterpretationApi.auto(sample, service, new InterpretationParam().previous(result)).then(callback->{
			update((Hrd) callback);
			DomGlobal.alert("생성되었습니다. 저장하세요.");
			return null;
		});
	}
	public void negative() {
		if(!DomGlobal.confirm("음성 결과를 입력합니다.")) return;
		InterpretationApi.negative(sample, service).then(callback->{
			update((Hrd) callback);
			DomGlobal.alert("입력되었습니다. 저장하세요.");
			return null;
		});
	}
	public void preview() {
		result = result.cancerType(iptCancerType.value()).gi(iptGi.value())
					   .giScore(iptGiScore.value()!=null?iptGiScore.value().intValue():0)
					   .snv(iptSnv.value()).cnv(iptCnv.value())
					   .interpretation(iptInterpretation.value())
					   .results(new Hrd.GeneResult[] {
							   iptVariantBrca.value(),
							   iptVariantTier1.value(),
							   iptVariantTier2.value()});
		ReportApi.preview(sample, service, result).then(blob->{
			PreviewElement preview = PreviewElement.build(blob);
			preview.onConfirm(confirm->{
				if(!DomGlobal.confirm("이대로 결과지를 생성하고 완료 합니다.")) return;
				ProgressApi.open(true);
				ProgressApi.progress(0.3);
				VersionCheckApi.isNew(sample, service)
				.then(isNew->isNew? Promise.resolve(""):DescriptionDialog.dialog())
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
	private void fail() {
		iptHrd.select("Fail");
		iptGi.select("Fail");
		iptGiScore.value(null);
		iptBrca.select("Fail");
		iptSnv.select("Fail");
		iptCnv.select("Fail");
		iptInterpretation.value("상동재조합결핍(Homologous Recombination Deficiency, HRD) 검사를 진행한 결과, Tumor burden이 " +
								"작아 QC failure 되었습니다. 재검을 원하시면 보다 높은 Tumor burden의 다른 Block에서 slide를 " +
								"제작하여 주시기 바랍니다. Tumor burden 50% 이상의 검체가 권장됩니다.");
	}
	@Override
	public HrdExpandElement that() {
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

	private final static class VariantResultElement extends HTMLElementBuilder<HTMLDivElement, VariantResultElement> {
		private final String gene;
		private final ButtonElement btnAddVariant = ButtonElement.outline().css("button").before(IconElement.icon("plus_one")).text("Append Variant");
		private final ButtonElement btnRemVariant = ButtonElement.outline().css("button").before(IconElement.icon("remove_circle")).text("Remove Variant");
		private final SnvHrdTableElement variants = SnvHrdTableElement.build().style("margin-right: 0px; margin-bottom: 5px; height: fit-content;");
		private final TextAreaElement<String> iptInterpretationGene = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; height: 250px; margin-bottom: 16px; margin-top: 5px;").text("Interpretation");
		public VariantResultElement(String gene) {
			this(div(), gene);
		}
		public VariantResultElement(HTMLContainerBuilder<HTMLDivElement> e, String gene) {
			super(e);
			this.gene = gene;
			e.style("margin-left: 15px; padding-right: 15px;")
					.add(label(gene).style("font-size: 1.5em;"))
					.add(variants)
					.add(div().style("margin-bottom: 3px;display: flex; justify-content: flex-end;")
							.add(btnAddVariant).add(btnRemVariant))
					.add(iptInterpretationGene);
			btnAddVariant.onClick(evt->variants.append());
			btnRemVariant.onClick(evt->variants.trimming());
		}
		private void update(Hrd.GeneResult dto) {
			this.variants.update(dto.variants());
			iptInterpretationGene.value(dto.interpretation());
		}
		private Hrd.GeneResult value() {
			Hrd.GeneResult gene = new Hrd.GeneResult().tier(this.gene).interpretation(iptInterpretationGene.value());
			gene.variants(variants.values());
			if(gene.variants()!=null && gene.variants().length > 0 && Arrays.stream(gene.variants()).anyMatch(g->!"VUS".equalsIgnoreCase(g.clazz())))
				gene.result("Positive").variants(variants.values());
			else gene.result("Negative");
			return gene;
		}
		@Override
		public VariantResultElement that() {
			return this;
		}
	}
}
