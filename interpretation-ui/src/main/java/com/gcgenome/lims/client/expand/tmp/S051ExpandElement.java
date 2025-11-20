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
import com.gcgenome.lims.dto.interpretation.tmp.S051Dto;
import com.gcgenome.lims.test.tmp.S051;
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

public class S051ExpandElement extends HTMLElementBuilder<HTMLDivElement, S051ExpandElement> implements ExpandElement<HTMLDivElement> {
	public static S051ExpandElement build(String id, long sample, String service) {
		return new S051ExpandElement(div(), id, sample, service);
	}
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	protected final ReportElement elemReport = new ReportElement(div());
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller");
	private final ButtonElement btnAuto = ButtonElement.outline().css("button").before(IconElement.icon("smart_button")).text("Interpretation");
	private final ButtonElement btnNegative = ButtonElement.outline().css("button").before(IconElement.icon("search_off")).text("Negative");
	private final ButtonElement btnSave = ButtonElement.outline().css("button").before(IconElement.icon("save")).text("Save");
	private final ButtonElement btnPreview = ButtonElement.outline().css("button").before(IconElement.icon("preview")).text("Preview");
	private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	protected final Map<String, JsPropertyMap<Object>> extra = new HashMap<>();
	private final S051 test = S051.instance;
	private final String id;
	protected final long sample;
	protected final String service;
	protected S051Dto result;
	protected S051ExpandElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
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
							 S051Dto dto = (S051Dto)obj;
							 if(dto == null) dto = new S051Dto();
							 update(dto);
							 return null;
						 });
	}
	protected void update(S051Dto dto) {
		this.result = dto;
		elemReport.update(dto);
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
		InterpretationApi.auto(sample, service, new InterpretationParam().previous(result))
						 .then(r->{
							 update((S051Dto) r);
							 DomGlobal.alert("생성되었습니다. 저장하세요.");
							 return null;
						 });
	}
	public void negative() {
		if(!DomGlobal.confirm("음성 결과를 입력합니다.")) return;
		InterpretationApi.negative(sample, service)
						 .then(obj->{
							 S051Dto result = (S051Dto)obj;
							 update(result);
							 DomGlobal.alert("생성되었습니다. 저장하세요.");
							 return null;
						 });
	}
	public void preview() {
		S051Dto result = elemReport.get();
		ReportApi.preview(sample, service, result).then(blob->{
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
	public S051ExpandElement that() {
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
		private final TextFieldElement.TextFieldOutlined<String> iptHeader = TextFieldElement.textBox().outlined().css("input").text("Result(Text)").style("width: calc(100% - 600px);");
		private final DropDownElement iptResult = DropDownElement.outlined(ListElement.singleLineList()
																 .add(ListElement.singleLine().label("Detected"))
																 .add(ListElement.singleLine().label("Not Detected")))
												   .css("input").text("Result").style("width: 200px;");
		private final TextFieldElement.TextFieldOutlined<String> iptDetails = TextFieldElement.textBox().outlined().css("input").text("Info").style("width: 400px;");
		private final TextAreaElement<String> iptInterpretation = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; height: 250px; margin: 16px;").text("Interpretation");
		private final HTMLContainerBuilder<HTMLDivElement> _this;
		public ReportElement(HTMLContainerBuilder<HTMLDivElement> e) {
			super(e.style("transition: all 300ms ease 0s;"));
			_this = e;

			layout();
		}
		private void layout() {
			_this.add(div().style("margin-left: 15px; margin-right: 15px; margin-bottom: 10px; margin-top: 10px;").add(iptHeader).add(iptResult).add(iptDetails))
				 .add(iptInterpretation);
		}
		public ReportElement update(S051Dto result) {
			if(result == null) {
				iptHeader.value("[FLT3-ITD mutation 중간 보고 입니다.]");
				iptDetails.value("");
				iptInterpretation.value("");
			} else {
				if(result.header()!=null) iptHeader.value(result.header());
				else iptHeader.value("[FLT3-ITD mutation 중간 보고 입니다.]");
				if("Detected".equalsIgnoreCase(result.result())) iptResult.select(0);
				if("Not Detected".equalsIgnoreCase(result.result())) iptResult.select(1);
				if(result.summary()!=null) iptDetails.value(result.summary());
				else iptDetails.value("");
				if(result.interpretation()!=null) iptInterpretation.value(result.interpretation());
				else iptInterpretation.value("");
			}
			return that();
		}
		public S051Dto get() {
			S051Dto result = new S051Dto();
			result.header(iptHeader.value())
				  .result(iptResult.value())
				  .summary(iptDetails.value())
				  .interpretation(iptInterpretation.value());
			return result;
		}
		@Override
		public ReportElement that() {
			return this;
		}
	}
}
