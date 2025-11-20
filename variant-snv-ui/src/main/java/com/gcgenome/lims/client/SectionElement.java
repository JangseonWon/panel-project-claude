package com.gcgenome.lims.client;

import com.gcgenome.lims.ui.IconElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HTMLContainerBuilder;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class SectionElement extends HTMLElementBuilder<HTMLDivElement, SectionElement> {
	public static  SectionElement build(IconElement icon, String title) {
		return new SectionElement(icon, title);
	}
	private final IconElement icon;
	private final HTMLContainerBuilder<HTMLLabelElement> title = label().css("title");
	private SectionElement(IconElement icon, String title) {
		this(div().css("section"), icon, title);
	}
	private SectionElement(HTMLContainerBuilder<HTMLDivElement> e, IconElement icon, String title) {
		super(e);
		this.icon = icon;
		this.title.textContent(title);
		e.add(icon).add(this.title);
	}
	@Override
	public SectionElement that() {
		return this;
	}
}
