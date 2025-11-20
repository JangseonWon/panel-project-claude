package com.greencross.lims.client;

import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.IconElement;
import org.jboss.elemento.HtmlContentBuilder;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class MiscMenuElement extends HTMLElementBuilder<HTMLDivElement, MiscMenuElement> {
	public static MiscMenuElement build(IconElement icon, String label) {
		return new MiscMenuElement(div(), icon, label);
	}
	private MiscMenuElement(HtmlContentBuilder<HTMLDivElement> e, IconElement icon, String label) {
		super(e);
		e.css("btn").add(icon.css("btn-icon")).add(label(label).css("btn-label"));
	}

	@Override
	public MiscMenuElement that() {
		return this;
	}
}
