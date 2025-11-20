package com.gcgenome.lims.client;

import com.gcgenome.lims.api.ReferralApi;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.dto.Page;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.IconElement;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.span;

public class ExpandElement2 extends HTMLElementBuilder<HTMLDivElement, ExpandElement2> implements ExpandElement<HTMLDivElement> {
	public static ExpandElement2 build(String id, long sample) {
		return new ExpandElement2(div(), id, sample);
	}
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final HTMLContainerBuilder<HTMLDivElement> children = div().style("height: calc(100% - 130px); overflow: auto; display: flex;flex-direction: row;align-content: center;justify-content: space-evenly;flex-wrap: wrap;");
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller");
	private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	private final String id;
	private final long sample;
	private ExpandElement2(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample) {
		super(e);
		this.id = id;
		this.sample = sample;
		_this = e.css("work").style("height: 100vh;");
		btnHide.onClick(evt->fireStateChangeEvent());
		layout();
	}
	private void layout() {
		_this.add(children).add(controller.add(span().style("margin-left: 10px;").add(btnHide)));
	}
	@Override
	public void update() {
		Message msg =  Message.builder().id(id).type(Message.MessageType.STRETCH).build();
		DomGlobal.window.parent.postMessage(JSON.stringify(msg), "*");
		children.element().innerHTML = "";
		ReferralApi.pages(sample).then(pages->{
			if(pages!=null) for(Page page: pages) children.add(PageElement.build(sample, page));
			return null;
		});
	}
	@Override
	public ExpandElement2 that() {
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
}
