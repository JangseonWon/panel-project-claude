package com.greencross.lims.client;

import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.IconElement;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.jboss.elemento.Elements.div;

public class ExpandElement2 extends HTMLElementBuilder<HTMLDivElement, ExpandElement2> implements ExpandElement<HTMLDivElement> {
	public static ExpandElement2 build(long sample, String service) {
		return new ExpandElement2(div(), sample, service);
	}

	private final HtmlContentBuilder<HTMLDivElement> controller = div().css("controller");
	private final ButtonElement hide = ButtonElement.outline().before(IconElement.icon("close")).text("Close");
	private ExpandElement2(HtmlContentBuilder<HTMLDivElement> e, long sample, String service) {
		super(e);
		e/*.add(dna).add(div().style("border-top: 1px solid #AAA;border-bottom: 1px solid #AAA;margin-left: 16px;margin-right: 16px; margin-bottom: 16px;" +
										   "position: relative;top: 0px;height: auto;right: 0px;left: 0px;overflow: hidden;").add(sheetDna))
		 .add(library).add(div().style("border-top: 1px solid #AAA;border-bottom: 1px solid #AAA;margin-left: 16px;margin-right: 16px; margin-bottom: 16px;" +
											   "position: relative;top: 0px;height: auto;right: 0px;left: 0px;overflow: hidden;").add(sheetLibrary))
		 */.add(controller.add(hide));
		hide.onClick(evt->fireStateChangeEvent());
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
