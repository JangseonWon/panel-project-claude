package com.greencross.lims.client;

import com.greencross.lims.api.ProgressApi;
import com.greencross.lims.api.RouteApi;
import com.greencross.lims.api.SnvApi;
import com.greencross.lims.dto.Query;
import com.greencross.lims.ui.IconElement;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLLabelElement;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.BreadcumbElement;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.label;

public class SnvElement extends AbstractScenePageable<SnvElement> {
	private final HtmlContentBuilder<HTMLLabelElement> title = label().add("SNV");
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
	}).add("SNV", evt->{
		evt.preventDefault();
		evt.stopPropagation();
		Router.location("snv", true);
	});
	private final SnvGridElement grid = SnvGridElement.builder().build().style("position: absolute; top: 95px; bottom: 45px; left: 20px; right: 20px; overflow: hidden;" +
																						 "border-top: 1px solid #AAA; border-bottom: 1px solid #AAAA; transition: all 150ms;");
	public SnvElement(Query query) {
		super(query);
	}
	@Override
	protected IsElement<?> grid() {
		return grid;
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
			new IsElement<?>[]{  }
		};
	}
	@Override
	public void update() {
		ProgressApi.open(false);
		SnvApi.search(query().limit(show()).page((int) page()).sortBy("chrom").asc(true))
				.then(response->{
					total(Long.parseLong(response.headers.get("X-TOTAL-COUNT")));
					return Promise.resolve(response);
				}).then(Response::json)
				.then(json->Promise.resolve((JsPropertyMap<String>[])json))
				.then(grid::update)
				.last(v->ProgressApi.close());
	}

	@Override
	public SnvElement that() {
		return this;
	}
}
