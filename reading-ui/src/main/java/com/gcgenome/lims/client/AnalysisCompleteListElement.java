package com.gcgenome.lims.client;

import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.RouteApi;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.ui.IconElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.BreadcrumbElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.ButtonElementToggle;
import org.jboss.elemento.HTMLContainerBuilder;
import org.jboss.elemento.IsElement;

import java.util.concurrent.atomic.AtomicInteger;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class AnalysisCompleteListElement extends AbstractScene<AnalysisCompleteListElement> {
	private final HTMLContainerBuilder<HTMLLabelElement> title = label().add("Reading");
	private final BreadcrumbElement breadcrumb = BreadcrumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
				RouteApi.location("", true, false);
			}).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
			.add("패널검사", evt->{
				evt.preventDefault();
				evt.stopPropagation();
			}).add("Reading", evt->{
				evt.preventDefault();
				evt.stopPropagation();
				Router.location("", true);
			});
	private final ButtonElementToggle btnProgressOnly= ButtonElement.toggle().css("button").text("Not complete only").value(true);

	private final ButtonElementToggle btnBRCA		= ButtonElement.toggle().css("button").text("BRCA").value(false);
	private final ButtonElementToggle btnCancer		= ButtonElement.toggle().css("button").text("Cancer").value(false);
	private final ButtonElementToggle btnRare		= ButtonElement.toggle().css("button").text("RD").value(false);
	private final ButtonElementToggle btnHema		= ButtonElement.toggle().css("button").text("HEMA").value(false);
	private final ButtonElementToggle btnSolid		= ButtonElement.toggle().css("button").text("ST").value(false);
	private final ButtonElementToggle btnTso		= ButtonElement.toggle().css("button").text("TSO").value(false);
	private final ButtonElementToggle btnWes		= ButtonElement.toggle().css("button").text("WES").value(false);
	private final ButtonElementToggle btnDes		= ButtonElement.toggle().css("button").text("DES").value(false);

	private final ButtonElementToggle btnDgs		= ButtonElement.toggle().css("button").text("DGS").value(false);
	private final ButtonElementToggle btnHrd		= ButtonElement.toggle().css("button").text("HRD").value(false);
	private final WindowElement brca;
	private final WindowElement cancer;
	private final WindowElement rare;
	private final WindowElement hema;
	private final WindowElement solid;
	private final WindowElement tso;
	private final WindowElement wes;
	private final WindowElement des;
	private final WindowElement dgs;
	private final WindowElement hrd;
	private final WindowElement[] windows;
	private final HTMLContainerBuilder<HTMLDivElement> layout;
	public AnalysisCompleteListElement(Query query) {
		super(query);
		brca = WindowElement.builder().id("brca").query(query).prefix("../").build().sort("ID").sort("ID");
		cancer = WindowElement.builder().id("cancer").query(query).prefix("../").build().sort("ID").sort("ID");
		rare = WindowElement.builder().id("rare").query(query).prefix("../").build().sort("ID").sort("ID");
		hema = WindowElement.builder().id("hema").query(query).prefix("../").build().sort("ID").sort("ID");
		solid = WindowElement.builder().id("solid tumor").prefix("../").query(query).build();
		tso = WindowElement.builder().id("tso").query(query).prefix("../").build().sort("ID").sort("ID");
		wes = WindowElement.builder().id("wes").query(query).prefix("../").build().sort("ID").sort("ID");
		des = WindowElement.builder().id("des").query(query).prefix("../").build();
		dgs = WindowElement.builder().id("dgs").query(query).prefix("../").build();
		hrd = WindowElement.builder().id("hrd").query(query).prefix("../").build();
		windows = new WindowElement[] {brca, cancer, rare, hema, solid, tso, wes, des, dgs, hrd};
		layout = div().css("layout")
				.add(brca).add(cancer)
				.add(des).add(wes).add(dgs)
				.add(hema).add(solid).add(tso)
				.add(rare).add(hrd);
		initialize();
		element().style.overflow = "auto";
		link(btnBRCA, brca);
		link(btnCancer, cancer);
		link(btnDes, des);
		link(btnWes, wes);
		link(btnDgs, dgs);
		link(btnHema, hema);
		link(btnSolid, solid);
		link(btnTso, tso);
		link(btnRare, rare);
		link(btnHrd, hrd);
		btnProgressOnly.onValueChange(evt->{
			for(var w: windows) w.complteOpt(evt.value()).update();
		});
	}
	private void link(ButtonElementToggle btn, WindowElement elem) {
		btn.onValueChange(evt->{
			if(evt.value()) {
				elem.element().style.display = null;
				elem.update();
			} else elem.element().style.display = "none";
		});
		elem.element().style.display = "none";
	}
	@Override
	protected IconElement icon() {
		return IconElement.icon(IconElement.Type.Light, "fa-comment-medical");
	}
	@Override
	protected HTMLContainerBuilder<HTMLLabelElement> title() {
		return title;
	}
	@Override
	protected BreadcrumbElement breadcrumb() {
		return breadcrumb;
	}
	@Override
	protected IsElement<?>[][] controls() {
		return new IsElement<?>[][]{
				new IsElement<?>[]{ btnProgressOnly },
				new IsElement<?>[]{ btnBRCA, btnCancer, btnDes, btnWes, btnDgs, btnHema, btnSolid, btnTso, btnRare, btnHrd}
		};
	}
	@Override
	protected IsElement<?>[] contents() {
		return new IsElement<?>[] {
				layout
		};
	}
	public AnalysisCompleteListElement parent(int idx) {
		while(breadcrumb.element().childElementCount > 5) ((HTMLElement)breadcrumb.element().childNodes.getAt(5)).remove();
		breadcrumb.add(String.valueOf(idx), evt->{
			evt.preventDefault();
			evt.stopPropagation();
			Router.location(String.valueOf(idx), false);
		});
		return that();
	}
	@Override
	public void update() {
		ProgressApi.open(true);
		AtomicInteger i = new AtomicInteger();
		for(var window: windows) {
			window.page1();
			window.update(c -> {
				if (i.incrementAndGet() < windows.length) ProgressApi.progress(i.get() / (double) windows.length);
				else ProgressApi.close();
			});
		}
	}
	@Override
	public AnalysisCompleteListElement that() {
		return this;
	}
}
