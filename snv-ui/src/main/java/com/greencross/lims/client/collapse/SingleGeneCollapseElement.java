package com.greencross.lims.client.collapse;

import com.google.gwt.core.client.Scheduler;
import com.greencross.lims.api.SnvApi;
import com.greencross.lims.client.CollapseElement;
import com.greencross.lims.client.WindowState;
import com.greencross.lims.dto.Message;
import com.greencross.lims.test.single.TestInfo;
import com.greencross.lims.ui.IconElement;
import elemental2.core.Global;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.jboss.elemento.Elements.*;

public class SingleGeneCollapseElement extends HTMLElementBuilder<HTMLDivElement, SingleGeneCollapseElement> implements CollapseElement<HTMLDivElement> {
	public static SingleGeneCollapseElement build(String id, long sample, String service) {
		return new SingleGeneCollapseElement(div(), id, sample, service);
	}
	private final IconElement icon = IconElement.icon(IconElement.Type.Light, "fa-search");
	private final HtmlContentBuilder<HTMLElement> title = span().css("mdc-list-item__primary-text").add("SNV/InDel");
	private final HtmlContentBuilder<HTMLElement> info = span().css("mdc-list-item__secondary-text").add("발견된 SNV/InDel 정보를 열람하고 변이의 병원성을 판별하여 입력합니다.");
	private final HtmlContentBuilder<HTMLElement> meta = span().css("mdc-list-item__meta");
	private final SnvSimpleTableElement summary = SnvSimpleTableElement.build().style("max-width: 1168px;");
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private final TestInfo test;
	private final String id;
	private final long sample;
	private final String service;
	private SingleGeneCollapseElement(HtmlContentBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e.css("work-summary")
			   .style("margin-left: 16px;margin-right: 16px;display: flex; flex-direction: row; " +
					  "flex-wrap: wrap; align-content: space-between; justify-content: flex-start; " +
					  "align-items: center; cursor: pointer;"));
		this.id = id;
		this.sample = sample;
		this.service = service;
		_this = e;
		test = Arrays.stream(TestInfo.TESTS).filter(t->t.code().equals(service)).findAny().orElse(null);
		layout();
		this.on(EventType.click, evt->fireStateChangeEvent());
		this.summary.on(EventType.click, evt->{
			evt.stopPropagation();
			evt.preventDefault();
			evt.stopImmediatePropagation();
		});
	}
	private void layout() {
		_this.add(span().css("mdc-list-item__graphic").style("height: auto; align-self: unset;").add(icon))
			 .add(div().css("mdc-list-item__text").style("margin-bottom: 10px;").add(title)
					   .add(info))
			 .add(meta);
	}
	public void update() {
		meta.element().innerHTML = "";
		meta.add(label("Loading...").style("margin-right: 40px;"));
		SnvApi.reported(sample, service).then(values-> {
			update(values);
			return null;
		});
	}

	private void update(Object[] values) {
		meta.element().innerHTML = "";
		if(values!=null && values.length > 0) {
			meta.add(summary.update(values)).style("flex-basis: 100%;");
			Scheduler.get().scheduleDeferred(()->{
				Message msg = new Message().id(id).type(Message.MessageType.COLLAPSE).param((summary.element().offsetHeight+80) + "px");
				DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
			});
		} else {
			meta.add(label("No variant").style("margin-right: 40px;"));
			Message msg = new Message().id(id).type(Message.MessageType.COLLAPSE).param("64px");
			DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		}
	}
	@Override
	public SingleGeneCollapseElement that() {
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
