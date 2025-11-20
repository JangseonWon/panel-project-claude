package com.gcgenome.lims.client.collapse;

import com.gcgenome.lims.api.InterpretationApi;
import com.gcgenome.lims.client.CollapseElement;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.dto.interpretation.Hrd;
import com.gcgenome.lims.ui.IconElement;
import elemental2.core.Global;
import elemental2.dom.*;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.*;
import static org.jboss.elemento.Elements.label;

public class HrdCollapseElement extends HTMLElementBuilder<HTMLDivElement, HrdCollapseElement> implements CollapseElement<HTMLDivElement> {
	public static HrdCollapseElement build(String id, long sample, String service) {
		return new HrdCollapseElement(div(), id, sample, service);
	}
	private final IconElement icon = IconElement.icon(IconElement.Type.Light, "fa-comment-medical");
	private final HTMLContainerBuilder<HTMLElement> title = span().css("mdc-list-item__primary-text").add("Interpretation(Deprecated)");
	private final HTMLContainerBuilder<HTMLElement> info = span().css("mdc-list-item__secondary-text");
	private final HTMLContainerBuilder<HTMLElement> meta = span().css("mdc-list-item__meta").style("margin-right: 40px;");
	private final HTMLContainerBuilder<HTMLLabelElement> summaryGi = label();
	private final HTMLContainerBuilder<HTMLLabelElement> summaryBrca = label();
	private final HTMLContainerBuilder<HTMLTableElement> summary = table().style("text-align: right; font-size: var(--mdc-typography-caption-font-size, 0.75rem);")
																		.add(tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));")
																						  .add("GI: ")).add(td().add(summaryGi)))
																		.add(tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));")
																						  .add("BRCA: ")).add(td().add(summaryBrca)));
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final String id;
	private final long sample;
	private final String service;
	private HrdCollapseElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e.css("work-summary")
			   .style("margin-left: 16px;margin-right: 16px;display: flex; flex-direction: row; " +
					  "flex-wrap: nowrap; align-content: space-between; justify-content: space-between; " +
					  "align-items: center; cursor: pointer;"));
		this.id = id;
		this.sample = sample;
		this.service = service;
		_this = e;
		layout();
		this.on(EventType.click, evt->fireStateChangeEvent());
	}
	private void layout() {
		_this.add(span().css("mdc-list-item__graphic").style("height: auto; align-self: unset;").add(icon))
			 .add(div().css("mdc-list-item__text").style("margin-bottom: 10px;").add(title).add(info.add("변이정보를 직접 타이핑하는 구버전 인터페이스입니다. 신버전이 작동하지 않으면 이쪽을 사용하세요.")))
			 .add(meta);
	}
	public void update() {
		meta.element().innerHTML = "";
		meta.add(label("Loading...").style("margin-right: 40px;"));
		InterpretationApi.interpretation(sample, service).then(reports->{
			this.update((Hrd)reports);
			return null;
		});
	}

	private void update(Hrd dto) {
		meta.element().innerHTML = "";
		summaryGi.element().innerHTML = "";
		summaryBrca.element().innerHTML = "";
		if(dto!=null && dto.interpretation()!=null) {
			summaryGi.add(dto.gi());
			String t = Arrays.stream(dto.results()).filter(v->"BRCA".equalsIgnoreCase(v.gene())).map(v->v.result()).collect(Collectors.joining(", "));
			if(t.trim().isEmpty()) t = "Negative";
			summaryBrca.add(t);
			if("Negative".equalsIgnoreCase(dto.gi())) summaryGi.style("color: 0xEFEFEF;font-weight: bold;");
			else summaryGi.style("color: rgb(135,51,61);font-weight: bold;");
			if("Negative".equalsIgnoreCase(t)) summaryBrca.style("color: 0xEFEFEF;font-weight: bold;");
			else summaryBrca.style("color: rgb(135,51,61);font-weight: bold;");
			meta.add(summary);
			Message msg = Message.builder().id(id).type(Message.MessageType.COLLAPSE).param("64px").build();
			DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		} else {
			meta.add(label("No interpretation"));
			Message msg = Message.builder().id(id).type(Message.MessageType.COLLAPSE).param("64px").build();
			DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		}
	}
	@Override
	public HrdCollapseElement that() {
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
