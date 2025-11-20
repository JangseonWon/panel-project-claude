package com.gcgenome.lims.client.expand.tmp;

import com.gcgenome.lims.api.InterpretationApi;
import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.ReportApi;
import com.gcgenome.lims.api.VersionCheckApi;
import com.gcgenome.lims.client.ExpandElement;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.client.interpretation.PreviewElement;
import com.gcgenome.lims.client.expand.DescriptionDialog;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.dto.interpretation.InterpretationParam;
import com.gcgenome.lims.dto.interpretation.tmp.BallondorDto;
import com.gcgenome.lims.test.tmp.Ballondor;
import elemental2.core.Global;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.promise.Promise;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.*;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.span;

public class BallondorExpandElement extends HTMLElementBuilder<HTMLDivElement, BallondorExpandElement> implements ExpandElement<HTMLDivElement> {
	public static BallondorExpandElement build(String id, long sample, String service) {
		return new BallondorExpandElement(div(), id, sample, service);
	}
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	protected final ReportElement elemReport;
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller");
	private final ButtonElement btnAuto = ButtonElement.outline().css("button").before(IconElement.icon("smart_button")).text("Interpretation");
	private final ButtonElement btnNegative = ButtonElement.outline().css("button").before(IconElement.icon("search_off")).text("Negative");
	private final ButtonElement btnSave = ButtonElement.outline().css("button").before(IconElement.icon("save")).text("Save");
	private final ButtonElement btnPreview = ButtonElement.outline().css("button").before(IconElement.icon("preview")).text("Preview");
	private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	protected final Map<String, JsPropertyMap<Object>> extra = new HashMap<>();
	private final String id;
	protected final long sample;
	protected final String service;
	protected BallondorDto result;
	protected BallondorExpandElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e);
		this.id = id;
		this.sample = sample;
		this.service = service;
		_this = e.css("work").style("height: 100vh;");
		Ballondor test = Arrays.stream(Ballondor.TESTS).filter(t->t.code().equalsIgnoreCase(service)).findFirst().get();
		elemReport = new ReportElement(div(), test);
		btnHide.onClick(evt->fireStateChangeEvent());
		btnSave.onClick(evt->save());
		btnAuto.onClick(evt->auto());
		btnPreview.onClick(evt->preview());
		btnNegative.onClick(evt->negative());
		layout();
	}
	private void layout() {
		_this.add(div().style("height: calc(100% - 55px); overflow: auto;").add(elemReport))
			 .add(controller.add(span().add(btnAuto).add(btnNegative))
							.add(span().style("margin-left: 10px;").add(btnSave).add(btnPreview).add(btnHide)));
	}
	@Override
	public void update() {
		Message msg = Message.builder().id(id).type(Message.MessageType.STRETCH).build();
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		InterpretationApi.interpretation(sample, service)
						 .then(obj->{
							 BallondorDto dto = (BallondorDto)obj;
							 if(dto == null) dto = new BallondorDto();
							 update(dto);
							 return null;
						 });
	}
	protected void update(BallondorDto dto) {
		this.result = dto;
		elemReport.update(dto);
	}
	public void save() {
		if(!DomGlobal.confirm("저장합니다.")) return;
		result = elemReport.get();
		if(result!=null) InterpretationApi.save(sample, service, result)
						 .then(callback->{
							 update();
							 DomGlobal.alert("저장되었습니다.");
							 return null;
						 });
	}
	public void auto() {
		result = elemReport.get();
		if(result!=null) InterpretationApi.auto(sample, service, new InterpretationParam().previous(result))
						 .then(r->{
							 update((BallondorDto) r);
							 DomGlobal.alert("생성되었습니다. 저장하세요.");
							 return null;
						 });
	}
	public void negative() {
		if(!DomGlobal.confirm("음성 결과를 입력합니다.")) return;
		InterpretationApi.negative(sample, service)
						 .then(obj->{
							 BallondorDto result = (BallondorDto)obj;
							 update(result);
							 DomGlobal.alert("생성되었습니다. 저장하세요.");
							 return null;
						 });
	}
	public void preview() {
		BallondorDto result = elemReport.get();
		if(result!=null) ReportApi.preview(sample, service, result).then(blob->{
			PreviewElement preview = PreviewElement.build(blob);
			preview.onConfirm(confirm->{
				if(!DomGlobal.confirm("이대로 결과지를 생성하고 완료 합니다.")) return;
				ProgressApi.open(true);
				ProgressApi.progress(0.3);
				VersionCheckApi.isNew(sample, service)
				.then(isNew->isNew? Promise.resolve(""): DescriptionDialog.dialog())
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
	public BallondorExpandElement that() {
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
		private final TextFieldElement.TextFieldOutlined<String> iptHeader = TextFieldElement.textBox().outlined().css("input").text("Result(Text)").style("width: calc(100% - 200px);").required(true);
		private final DropDownElement iptResult = DropDownElement.outlined(ListElement.singleLineList()
																 .add(ListElement.singleLine().label("Detected"))
																 .add(ListElement.singleLine().label("Not Detected")))
												   .css("input").text("Result").style("width: 200px;");
		private final DropDownElement iptHgvsc;
		private final TextFieldElement.TextFieldOutlined<String> iptVaf = TextFieldElement.textBox().outlined().css("input").text("VAF(%)").style("width: 200px;").required(true);
		private final TextFieldElement.TextFieldOutlined<String> iptSample = TextFieldElement.textBox().outlined().css("input").text("Sample").style("width: calc(100% - 800px);").required(true);
		private final DropDownElement iptInfo = DropDownElement.outlined(ListElement.singleLineList()
						.add(ListElement.singleLine().label("스크리닝"))
						.add(ListElement.singleLine().label("R-VRD 유도항암화학요법 종료 후"))
						.add(ListElement.singleLine().label("유지요법 12개월 후"))
						.add(ListElement.singleLine().label("유지요법 24개월 종료 후")))
				.css("input").text("Info").style("width: 400px;");
		private final HTMLContainerBuilder<HTMLDivElement> _this;
		public ReportElement(HTMLContainerBuilder<HTMLDivElement> e, Ballondor test) {
			super(e.style("transition: all 300ms ease 0s;"));
			_this = e;
			ListElement<ListElement.SingleLineItem> hgvscs = ListElement.singleLineList();
			hgvscs.add(ListElement.singleLine().label(""));
			for(String hgvsc: test.hgvsc()) hgvscs.add(ListElement.singleLine().label(hgvsc));
			iptHgvsc = DropDownElement.outlined(hgvscs).css("input").text("HGVS.c").style("width: 200px;");
			layout();
			iptResult.onSelectionChange(evt->{
				if(evt.selection() == 1) {
					iptHgvsc.select(0).enabled(false);
					iptVaf.value("-").enabled(false);
				} else {
					iptHgvsc.enabled(true);
					iptVaf.enabled(true);
				}
			});
		}
		private void layout() {
			_this.add(div().style("margin-left: 15px; margin-right: 15px; margin-bottom: 10px; margin-top: 10px;").add(iptResult).add(iptHeader))
					.add(div().style("margin-left: 15px; margin-right: 15px; margin-bottom: 10px; margin-top: 10px;").add(iptHgvsc).add(iptVaf).add(iptSample).add(iptInfo));
		}
		public ReportElement update(BallondorDto result) {
			if(result == null) {
				iptHeader.value("");
				iptResult.select(0);
				iptHgvsc.select(0);
				iptVaf.value("");
				iptSample.value("");
				iptInfo.select(0);
			} else {
				iptHeader.value(result.summary());
				if("Detected".equalsIgnoreCase(result.result())) iptResult.select(0);
				else if("Not Detected".equalsIgnoreCase(result.result())) iptResult.select(1);
				iptHgvsc.select(result.hgvsc());
				iptVaf.value(result.vaf());
				iptSample.value(result.sample());
				if("스크리닝".equalsIgnoreCase(result.info())) iptInfo.select(0);
				else if("R-VRD 유도항암화학요법 종료 후".equalsIgnoreCase(result.info())) iptInfo.select(1);
				else if("유지요법 12개월 후".equalsIgnoreCase(result.info())) iptInfo.select(2);
				else if("유지요법 24개월 종료 후".equalsIgnoreCase(result.info())) iptInfo.select(3);
			}
			return that();
		}
		public BallondorDto get() {
			if(iptHeader.value().isEmpty() || iptResult.value().isEmpty() || iptSample.value().isEmpty() || iptInfo.value().isEmpty() ||
			  ("Detected".equalsIgnoreCase(iptResult.value())&&iptVaf.value().isEmpty()) ||
			  ("Detected".equalsIgnoreCase(iptResult.value())&&iptHgvsc.value().isEmpty())) {
				DomGlobal.alert("값을 모두 채우세요.");
				return null;
			} else return new BallondorDto().summary(iptHeader.value()).result(iptResult.value()).sample(iptSample.value())
					.info(iptInfo.value()).vaf(iptVaf.value()).hgvsc(iptHgvsc.value());
		}
		@Override
		public ReportElement that() {
			return this;
		}
	}
}
