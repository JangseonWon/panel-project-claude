package com.greencross.lims.client.trello;

import com.greencross.lims.ui.IconElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HtmlContentBuilder;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class TrackElement extends HTMLElementBuilder<HTMLDivElement, TrackElement> {
	public static TrackElement build(String title, String color) {
		TrackElement e = new TrackElement(title, color, div());
		return e;
	}
	private final HtmlContentBuilder<HTMLLabelElement> title = label();
	private final HtmlContentBuilder<HTMLDivElement> header = div().css("title").add(IconElement.icon(IconElement.Type.Solid, "fa-caret-right")).add(title);
	private final HtmlContentBuilder<HTMLDivElement> body = div().css("contents");
	private TrackElement(String title, String color, HtmlContentBuilder<HTMLDivElement> e) {
		super(e.css("track"));
		e.add(header).add(body);
		this.title.textContent(title);
		this.header.style("background-color:" + color);
	}
	public TrackElement add(DeckElement deck) {
		body.add(deck);
		return that();
	}
	public TrackElement clear() {
		body.element().innerHTML = "";
		return this;
	}
	@Override
	public TrackElement that() {
		return this;
	}
}
