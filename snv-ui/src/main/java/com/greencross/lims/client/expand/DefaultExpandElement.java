package com.greencross.lims.client.expand;

import com.greencross.lims.client.ExpandElement;
import com.greencross.lims.client.WindowState;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.jboss.elemento.Elements.div;

public class DefaultExpandElement extends HTMLElementBuilder<HTMLDivElement, DefaultExpandElement> implements ExpandElement<HTMLDivElement> {
	public static DefaultExpandElement build(long sample, String service) {
		return new DefaultExpandElement(div(), sample, service);
	}
	private DefaultExpandElement(HtmlContentBuilder<HTMLDivElement> e, long sample, String service) {
		super(e.add("Not supported yet:" + service));
		this.on(EventType.click, evt->fireStateChangeEvent());
	}

	@Override
	public DefaultExpandElement that() {
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
