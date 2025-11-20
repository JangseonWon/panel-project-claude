package com.greencross.lims.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.greencross.lims.api.SampleApi;
import com.greencross.lims.dto.Message;
import com.greencross.lims.dto.Request;
import elemental2.core.Global;
import elemental2.dom.*;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.TabBarElement;
import net.sayaya.ui.event.HasClickHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.Elements;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.jboss.elemento.Elements.div;

public class RequestTabElement extends HTMLElementBuilder<HTMLDivElement, RequestTabElement> {
	public static RequestTabElement build(Request[] dtos) {
		return new RequestTabElement(dtos, div());
	}
	private final Request[] dtos;
	private final TabBarElement.Tab[] tabs;
	private final TabBarElement tab;
	private final HTMLContainerBuilder<HTMLDivElement> stack = div().style("min-height: calc(100% - 182px);" +
																		 "background: #FFFFFF;" +
																		 "overflow: auto;" +
																		 "display: flex;" +
																		 "flex-wrap: nowrap;" +
																		 "flex-direction: column;" +
																		 "align-content: stretch;" +
																		 "align-items: stretch;");
	private final List<SubjectPanel> children;
	private RequestTabElement(Request[] dtos, HTMLContainerBuilder<HTMLDivElement> e) {
		super(e.css("request"));
		this.dtos = dtos;
		children = new LinkedList<>();
		DomGlobal.window.addEventListener("message", evt->{
			try {
				JsPropertyMap<Object> map = Js.asPropertyMap(evt);
				if(!map.has("data")) return;
				String data = (String)map.get("data");
				if(data == null || data.trim().isEmpty()) return;

				if(data.contains("___id")) {
					Message msg = Js.uncheckedCast(Global.JSON.parse(data));
					evt.stopPropagation();
					evt.preventDefault();
					if(msg.type() == Message.MessageType.REQUEST) {
						DomGlobal.window.parent.postMessage(data, "*");
					} else if(msg.type() == Message.MessageType.RESPONSE) {
						children.stream().forEach(child -> child.iframe.element().contentWindow.postMessage(data, "*"));
					} else if(msg.type() == Message.MessageType.COLLAPSE ||
						msg.type() == Message.MessageType.STRETCH ||
					    msg.type() == Message.MessageType.FULLSCREEN ||
					    msg.type() == Message.MessageType.REFRESH ||
						msg.type() == Message.MessageType.PROGRESS) children.stream().filter(child -> child.id.equals(msg.id()))
																		  .findFirst().ifPresent(child -> child.processMessage(msg));
				} else {
					DomGlobal.window.parent.postMessage(data, "*");
				}
			} catch(Exception ex) {
				DomGlobal.console.error(ex.getMessage());
			}
		});
		tabs = Arrays.stream(dtos)
					 .map(dto->TabBarElement.tab().text(dto.serviceName()))
					 .toArray(TabBarElement.Tab[]::new);
		tab = TabBarElement.tabBar(tabs);
		tab.onValueChange(evt->layout(evt.value()));
		tab.activate(0);
		e.add(tab).add(stack);
		Scheduler.get().scheduleDeferred(()->layout(0));
	}
	private void layout(int tab) {
		Request req = dtos[tab];
		stack.element().innerHTML = "";
		children.clear();
		SampleApi.subjects(req.sample(), req.serviceCode(), urls->{
			for(int i = 0; i < urls.length; ++i) {
				if(i > 0) stack.add(divider());
				SubjectPanel child = new SubjectPanel(Document.get().createUniqueId(), req.sample(), req.serviceCode(), "" + urls[i]);
				stack.add(child);
				children.add(child);
				child.update();
			}
		});
	}
	@Override
	public RequestTabElement that() {
		return this;
	}
	private HTMLDivElement divider() {
		return div().css("mdc-list-divider").attr("role", "separator").element();
	}
	private final class SubjectPanel extends HTMLElementBuilder<HTMLDivElement, SubjectPanel> implements HasClickHandlers {
		private final HTMLContainerBuilder<HTMLElement> ripple = Elements.span().css("mdc-list-item__ripple");
		private HTMLContainerBuilder<HTMLIFrameElement> iframe;
		private final String id;
		private final long sample;
		private final String service;
		private final String url;
		public SubjectPanel(String id, long sample, String service, String url) {
			this(div(), id, sample, service, url);
		}
		protected SubjectPanel(HTMLContainerBuilder<HTMLDivElement> div, String id, long sample, String service,String url) {
			super(div.css("mdc-list-item").style("height: 100%; padding:0").attr("role", "option"));
			this.id = id;
			this.sample = sample;
			this.service = service;
			this.url = url;
			iframe = Elements.iframe()
							 .id(id)
							 .attr("frameborder", "0")
							 .attr("scrolling", "auto")
							 .style("width:0px; min-width: 1200px; height:0px; max-height: calc(100vh - 183px); transition: all 150ms ease 0s;");
			div.add(ripple).add(iframe);
		}
		public SubjectPanel enabled(boolean enabled) {
			if (!enabled) this.css("mdc-list-item--disabled");
			else this.ncss("mdc-list-item--disabled");
			return this.that();
		}
		private void update() {
			iframe.element().src = url + "?id=" + id + "&service=" + service + "&sample=" + sample;
		}
		private void processMessage(Message msg) {
			if(!id.equalsIgnoreCase(msg.id())) return;
			if(msg.type() == Message.MessageType.COLLAPSE) {
				String height = msg.param().toString();
				iframe.element().style.height = CSSProperties.HeightUnionType.of(height);
				iframe.element().style.width = CSSProperties.WidthUnionType.of("0px");
				ripple.element().style.display = null;
				showAll();
			} else if(msg.type() == Message.MessageType.STRETCH) {
				iframe.element().style.height = CSSProperties.HeightUnionType.of("100vh");
				ripple.element().style.display = "none";
				hideExceptMe();
			} else if(msg.type() == Message.MessageType.FULLSCREEN) {
				iframe.element().style.height = CSSProperties.HeightUnionType.of("100vh");
				iframe.element().style.width = CSSProperties.WidthUnionType.of(Elements.body().element().clientWidth + "px");
				ripple.element().style.display = "none";
				hideExceptMe();
			} else if(msg.type() == Message.MessageType.REFRESH) {
				for(SubjectPanel child: children) child.update();
			}
		}
		private void hideExceptMe() {
			for(int i = 0; i < stack.element().childElementCount; ++i) {
				HTMLElement e = (HTMLElement) stack.element().childNodes.getAt(i);
				e.style.maxHeight = CSSProperties.MaxHeightUnionType.of("0px");
			}
			this.element().style.maxHeight = null;
			Scheduler.get().scheduleFixedDelay(()->{
				for(int i = 0; i < stack.element().childElementCount; ++i) {
					HTMLElement e = (HTMLElement) stack.element().childNodes.getAt(i);
					e.style.display = "none";
				}
				this.element().style.display = null;
				return false;
			}, 100);
		}
		private void showAll() {
			for(int i = 0; i < stack.element().childElementCount; ++i) {
				HTMLElement e = (HTMLElement) stack.element().childNodes.getAt(i);
				e.style.maxHeight = null;
				e.style.display = null;
			}
		}
		@Override
		public SubjectPanel that() {
			return this;
		}
		public final HandlerRegistration onClick(EventListener listener) {
			return this.onClick(this.that().element(), listener);
		}
	}
}
