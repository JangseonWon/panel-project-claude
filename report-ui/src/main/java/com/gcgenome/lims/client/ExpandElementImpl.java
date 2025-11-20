package com.gcgenome.lims.client;

import com.gcgenome.lims.api.ReportApi;
import com.gcgenome.lims.api.VersionCheckApi;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.dto.Report;
import com.gcgenome.lims.ui.IconElement;
import com.gcgenome.lims.util.DataTransformUtil;
import elemental2.dom.*;
import elemental2.promise.Promise;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.SheetElementSelectableSingle;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnText;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.*;

public class ExpandElementImpl extends HTMLElementBuilder<HTMLDivElement, ExpandElementImpl> implements ExpandElement<HTMLDivElement> {
	public static ExpandElementImpl build(String id, long sample, String service) {
		return new ExpandElementImpl(div(), id, sample, service);
	}
	private final Section info = new Section(IconElement.icon(IconElement.Type.Light, "fa-clipboard-prescription"), "Report");
	private final ReportTableElement reports = new ReportTableElement(div());
	private final HTMLContainerBuilder<HTMLIFrameElement> pdf = iframe().style("width: 100%; height: 100%; border: 1px solid #ddd;");
	private final HTMLContainerBuilder<HTMLDivElement> container = div().style("left: 15px; right: 15px; position: absolute; bottom: 60px;").add(pdf);
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller");
	private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	private final ButtonElement btnCreate = ButtonElement.outline().css("button").before(IconElement.icon("description")).text("Print");
	private final ButtonElement btnPublish = ButtonElement.outline().css("button").before(IconElement.icon("publish")).text("Publish");
	private final ButtonElement btnLogDownload = ButtonElement.outline().css("button").before(IconElement.icon("download")).text("Logs");
	private final String id;
	private final long sample;
	private final String service;
	private String selected = null;
	private ExpandElementImpl(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e);
		this.id = id;
		this.sample = sample;
		this.service = service;
		e.css("work").style("height: 100vh;")
		 .add(info)
		 .add(reports)
		 .add(container)
		 .add(controller.add(span().style("margin-left: 10px;").add(btnLogDownload).add(btnCreate).add(btnPublish).add(btnHide)));
		btnCreate.onClick(evt->print());
		btnPublish.onClick(evt->publish());
		btnHide.onClick(evt->fireStateChangeEvent());
		btnLogDownload.onClick(evt->logDownload());
		reports.onSelectionChange(evt->{
			selected = evt.selection().idx();
			String url = evt.selection().get("url");
			ReportApi.download(url).then(blob->{
				pdf.element().setAttribute("src", URL.createObjectURL(blob));
				return null;
			});
		});
	}

	private static native String blobDownload(Blob blob, String fileName) /*-{
		var a = document.createElement("a");
		document.body.appendChild(a);
    	a.style = "display: none";
    	var url = $wnd.URL.createObjectURL(blob);
    	a.href = url;
    	a.download = fileName;
    	a.click();
    	$wnd.URL.revokeObjectURL(url);
    	a.remove();
    	return "";
    }-*/;
	private void logDownload() {
		VersionCheckApi.isNew(sample, service)
		.then(isNew->{
			if(isNew) {
				DomGlobal.alert("변경 이력이 없습니다.");
				return null;
			} else return ReportApi.downloadLogs(sample,service)
					.then(b -> Promise.resolve(blobDownload(b, sample +"-"+service+".pdf")));
		});

	}

	private void print() {
		if(!DomGlobal.confirm("결과지를 생성합니다.")) return;
		VersionCheckApi.isNew(sample, service)
		.then(isNew->isNew?Promise.resolve(""):DescriptionDialog.dialog())
		.then(description->ReportApi.print(sample, service, description))
		.then(report->{
			DomGlobal.alert("결과지가 생성되었습니다.");
			update();
			return null;
		});
	}
	private void publish() {
		long date = Long.parseLong(selected);
		if(!DomGlobal.confirm("결과지를 전송하고 검사를 완료합니다.")) return;
		ReportApi.publish(sample, service, date).then(d->{
			DomGlobal.alert("완료되었습니다.");
			update();
			return null;
		});
	}
	public void update() {
		Message msg = Message.builder().id(id).type(Message.MessageType.STRETCH).build();
		DomGlobal.window.parent.postMessage(JSON.stringify(msg), "*");
		ReportApi.reports(sample, service).then(values->{
			Data[] data = Arrays.stream(values).map(this::map).toArray(Data[]::new);
			reports.update(data);
			return null;
		});
		ReportApi.state(sample, service).then(state->{
			boolean isFinal = "F".equalsIgnoreCase(state);
			btnCreate.enabled(!isFinal);
			btnPublish.enabled(!isFinal);
			btnPublish.text(isFinal?"검사 완료":"Publish");
			return null;
		});
	}
	private Data map(Report dto) {
		return Data.create(dto.createAt()+"")
					   .initialize("create", DataTransformUtil.formatDateTime(dto.createAt()))
					   .initialize("creator", dto.creator())
					   .initialize("fileName", dto.fileName())
					   .initialize("url", dto.fileUrl())
					   .initialize("size", DataTransformUtil.formatPrecision(dto.fileSize()/1024.0, 2) + " kb")
					   .initialize("publish", DataTransformUtil.formatDateTime(dto.publishAt()))
					   .initialize("publisher", dto.publisher())
					   .initialize("description", dto.description());
	}

	@Override
	public ExpandElementImpl that() {
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
	private static ColumnText column(String id, String name){
		return ColumnBuilder.text(id, 10, 100).name(name).horizontal("center").vertical("middle");
	}
	public class ReportTableElement extends HTMLElementBuilder<HTMLDivElement, ReportTableElement> implements HasSelectionChangeHandlers<Data> {
		private final SheetElement.SheetConfiguration config = SheetElement.builder()
				.autoColSize(true)
				.autoRowSize(false)
				.viewportColumnRenderingOffset(100.0)
				.rowHeaderWidth(30)
				.rowHeaders(false)
				.manualColumnMove(false)
				.manualColumnResize(true)
				.columns(column("create", "Printed at").build(),
						column("creator", "Printed by").build(),
						column("fileName", "File Name").horizontal("left").build(),
						column("size", "File Size").horizontal("right").build(),
						column("publish", "Published at").build(),
						column("publisher", "Published by").build(),
						column("description", "Revision history").horizontal("left").build())
				.data(new Data[] {})
				.colWidths(new double[]{80, 100, 100, 80, 80, 80})
				.stretchH("all");
		private final SheetElement sheet = config.build();
		private final SheetElementSelectableSingle selection = SheetElementSelectableSingle.wrap(sheet);
		private final HTMLContainerBuilder<HTMLDivElement> _this;
		//private Map<String, Report> values;
		private ReportTableElement(HTMLContainerBuilder<HTMLDivElement> e) {
			super(e.style("overflow: hidden; margin-left: auto; margin-right: auto; margin-bottom: 16px; border-top: 1px solid #AAA; border-bottom: 1px solid #AAA; color: var(--mdc-theme-text-primary-on-background, rgba(0, 0, 0, 0.87)); transition: all 300ms;"));
			_this = e.add(sheet);
			HasSelectionChangeHandlers.SelectionChangeEventListener<Optional<Data>> wrapper = evt->{
				SelectionChangeEvent<Data> evt2 = SelectionChangeEvent.event(evt.event(), evt.selection().orElse(null));
				for(SelectionChangeEventListener<Data> listener: selectionChangeEventListeners) listener.handle(evt2);
			};
			selection.onSelectionChange(wrapper);
		}
		public void update(Data[] values) {
			_this.element().style.height = CSSProperties.HeightUnionType.of((calculateTableHeight(values)+2) + "px");
			container.element().style.top = (calculateTableHeight(values) + 70) + "px";
			sheet.values(values);
			if(values!=null && values.length > 0) {
				values[0].select(true);
				SelectionChangeEvent<Data> evt2 = SelectionChangeEvent.event(null, values[0]);
				for (SelectionChangeEventListener<Data> listener : selectionChangeEventListeners) listener.handle(evt2);
			}
		}
		private int calculateTableHeight(Data[] values) {
			if(values == null) return 0;
			return Math.min(120, 27 + values.length*22);
		}
		@Override
		public ReportTableElement that() {
			return this;
		}

		@Override
		public Data selection() {
			return selection.selection().orElse(null);
		}

		private final Set<SelectionChangeEventListener<Data>> selectionChangeEventListeners = new HashSet<>();
		@Override
		public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Data> selectionChangeEventListener) {
			selectionChangeEventListeners.add(selectionChangeEventListener);
			return ()->selectionChangeEventListeners.remove(selectionChangeEventListener);
		}
	}
}
