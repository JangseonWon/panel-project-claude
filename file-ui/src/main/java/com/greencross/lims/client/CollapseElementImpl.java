package com.greencross.lims.client;

import com.greencross.lims.dto.Message;
import com.greencross.lims.dto.Report;
import com.greencross.lims.ui.IconElement;
import com.greencross.lims.util.DataTransformUtil;
import elemental2.core.Global;
import elemental2.dom.*;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.*;

import static org.jboss.elemento.Elements.*;

public class CollapseElementImpl extends HTMLElementBuilder<HTMLDivElement, CollapseElementImpl> implements CollapseElement<HTMLDivElement> {
	public static CollapseElementImpl build(String id, long sample, String service) {
		return new CollapseElementImpl(div(), id, sample, service);
	}
	private final IconElement icon = IconElement.icon(IconElement.Type.Light, "fa-file-plus");
	private final HtmlContentBuilder<HTMLElement> title = span().css("mdc-list-item__primary-text").add("File");
	private final HtmlContentBuilder<HTMLElement> info = span().css("mdc-list-item__secondary-text").add("결과 보고에 첨부할 파일을 업로드합니다.");
	private final HtmlContentBuilder<HTMLElement> meta = span().css("mdc-list-item__meta");
	private final HtmlContentBuilder<HTMLLabelElement> summaryReport = label();
	private final HtmlContentBuilder<HTMLTableElement> summary = table().style("margin-right: 40px;text-align: right; font-size: var(--mdc-typography-caption-font-size, 0.75rem);")
																		.add(tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));")
																						  .add("Report: ")).add(td().add(summaryReport)));
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private final String id;
	private final long sample;
	private final String service;
	private Report last;
	private CollapseElementImpl(HtmlContentBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e.style("margin-left: 16px;margin-right: 16px;display: flex; flex-direction: row; " +
					  "flex-wrap: nowrap; align-content: space-between; justify-content: space-between; " +
					  "align-items: center; cursor: pointer;"));
		this.id = id;
		this.sample = sample;
		this.service = service;
		_this = e;
		layout();
		this.on(EventType.click, evt->fireStateChangeEvent());
		this.summary.on(EventType.click, evt->{
			evt.stopPropagation();
			evt.preventDefault();
			//if(last!=null) ReportApi.download(last.fileUrl(), last.fileName());
		});
		this.summary.on(EventType.mouseover, evt->{
			evt.stopPropagation();
			evt.preventDefault();
		});
	}

	private void layout() {
		_this.add(span().css("mdc-list-item__graphic").add(icon))
			 .add(div().css("mdc-list-item__text").style("margin-bottom: 10px;").add(title).add(info))
			 .add(meta);
	}

	public void update() {
		Message msg = new Message().id(id).type(Message.MessageType.COLLAPSE).param("64px");
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		meta.element().innerHTML = "";
		meta.add(label("Loading...").style("margin-right: 40px;"));
		/*ReportApi.reports(sample, service).then(reports->{
			this.update(reports);
			return null;
		});*/
	}

	private void update(Report[] values) {
		meta.element().innerHTML = "";
		meta.add(summary);
		if(values != null && values.length > 0) {
			last = Arrays.stream(values).max(Comparator.comparing(Report::createAt)).get();
			summary.element().innerHTML = "";
			summary.add(tr().add(td().add(a().add(last.fileName()).attr("href", last.fileUrl()))))
				   .add(tr().add(td().add("Published at ").add(summaryReport)));
			if(last.publishAt()!=null) summaryReport.element().innerHTML = DataTransformUtil.formatDateTime(last.publishAt());
			else summaryReport.element().innerHTML = "Not yet.";
		} else summaryReport.element().innerHTML = "Not yet.";
	}
	@Override
	public CollapseElementImpl that() {
		return this;
	}

	private final Set<StateChangeEventListener<WindowState>> listeners = new HashSet<>();
	@Override
	public Collection<StateChangeEventListener<WindowState>> listeners() {
		return listeners;
	}

	@Override
	public WindowState state() {
		return WindowState.FULLSCREEN;
	}
}
