package com.greencross.lims.client;

import com.greencross.lims.api.RouteApi;
import com.greencross.lims.dto.Query;
import com.greencross.lims.ui.IconElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.BreadcumbElement;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class MiscListElement extends AbstractScene<MiscListElement> {
	private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Misc.");
	private final BreadcumbElement breadcumb = BreadcumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
		RouteApi.location("", true, false);
	}).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
	 .add("패널검사", evt->{
		 evt.preventDefault();
		 evt.stopPropagation();
	 }).add("Misc.", evt->{
		evt.preventDefault();
		evt.stopPropagation();
		Router.location("", true);
	});
	private final MiscMenuElement btnVariant;
	private final MiscMenuElement btnPanel = MiscMenuElement.build(IconElement.icon(IconElement.Type.Regular,"fa-vials"), "Panel");
	private final HtmlContentBuilder<HTMLDivElement> layout;
	public MiscListElement(Query query) {
		super(query);
		btnVariant = MiscMenuElement.build(IconElement.icon(IconElement.Type.Regular,"fa-dna"), "SNV");
		btnVariant.on(EventType.click, evt->Router.location("snv", true));
		layout = div().css("layout").add(btnVariant).add(btnPanel);
		initialize();
		element().style.overflow = "auto";
	}
	@Override
	protected IconElement icon() {
		return IconElement.icon(IconElement.Type.Light, "fa-database");
	}
	@Override
	protected HtmlContentBuilder<HTMLLabelElement> title() {
		return title;
	}
	@Override
	protected BreadcumbElement breadcumb() {
		return breadcumb;
	}
	@Override
	protected IsElement<?>[][] controls() {
		return new IsElement<?>[][]{

		};
	}
	@Override
	protected IsElement<?>[] contents() {
		return new IsElement<?>[] {
			layout
		};
	}
	public MiscListElement parent(int idx) {
		while(breadcumb.element().childElementCount > 5) ((HTMLElement)breadcumb.element().childNodes.getAt(5)).remove();
		breadcumb.add(String.valueOf(idx), evt->{
			evt.preventDefault();
			evt.stopPropagation();
			Router.location(String.valueOf(idx), false);
		});
		return that();
	}
	@Override
	public void update() {

	}
	@Override
	public MiscListElement that() {
		return this;
	}
}
