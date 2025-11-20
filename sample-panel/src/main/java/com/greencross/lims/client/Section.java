package com.greencross.lims.client;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.IconElement;
import org.jboss.elemento.HtmlContentBuilder;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class Section extends HTMLElementBuilder<HTMLDivElement, Section> {
	private final IconElement icon;
	private final HtmlContentBuilder<HTMLLabelElement> title = label().css("title");
	public Section(IconElement icon, String title) {
		this(div().css("section"), icon, title);
	}
	private Section(HtmlContentBuilder<HTMLDivElement> e, IconElement icon, String title) {
		super(e);
		this.icon = icon;
		this.title.textContent(title);
		e.add(icon).add(this.title);
	}
	@Override
	public Section that() {
		return this;
	}
}
