package com.gcgenome.lims.client.collapse;

import com.gcgenome.lims.api.AnalysisApi;
import com.gcgenome.lims.client.CollapseElement;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.dto.Analysis2;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.ui.IconElement;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.i18n.client.NumberFormat;
import elemental2.core.Global;
import elemental2.dom.*;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.*;

public class SingleCollapseElement extends HTMLElementBuilder<HTMLDivElement, SingleCollapseElement> implements CollapseElement<HTMLDivElement> {
	public static SingleCollapseElement build(String id, long sample, String service) {
		return new SingleCollapseElement(div(), id, sample, service);
	}
	private final IconElement icon = IconElement.icon(IconElement.Type.Light, "fa-server");
	private final HTMLContainerBuilder<HTMLElement> title = span().css("mdc-list-item__primary-text").add("BI Analysis");
	private final HTMLContainerBuilder<HTMLElement> info = span().css("mdc-list-item__secondary-text").add("BI 분석 데이터에 대한 QC 결과를 확인합니다.");
	private final HTMLContainerBuilder<HTMLElement> meta = span().css("mdc-list-item__meta").style("margin-right: 40px;");
	private final HTMLContainerBuilder<HTMLLabelElement> serial = label();
	private final HTMLContainerBuilder<HTMLLabelElement> depth = label();
	private final HTMLContainerBuilder<HTMLLabelElement> x10 = label();
	private final HTMLContainerBuilder<HTMLAnchorElement> bam = a();
	private final HTMLContainerBuilder<HTMLTableElement> summary = table().style("text-align: right; font-size: var(--mdc-typography-caption-font-size, 0.75rem);")
			.add(tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));")
					.add("Analysis ID: ")).add(td().add(serial)))
			.add(tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));")
					.add("Depth: ")).add(td().add(depth)))
			.add(tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));")
					.add("Coverage(10X): ")).add(td().add(x10)))
			.add(tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));")
					.add("BAM: ")).add(td().add(bam)));
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final String id;
	private final long sample;
	private final String service;
	private SingleCollapseElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e.css("work-summary")
			   .style("margin-left: 16px;margin-right: 16px;display: flex; flex-direction: row; " +
					  "flex-wrap: wrap; align-content: space-between; justify-content: flex-start; " +
					  "align-items: center; cursor: pointer;"));
		this.id = id;
		this.sample = sample;
		this.service = service;
		_this = e;
		bam.on(EventType.click, evt->{
			evt.stopPropagation();
			evt.preventDefault();
			Window popup = DomGlobal.window.open(bam.element().href, "_blank", "left=10000,top=100000,width=2,height=2,toolbar=0,resizable=0");
			Scheduler.get().scheduleFixedDelay(()->{
				popup.close();
				return false;
			}, 1000);
		});
		layout();
		this.on(EventType.click, evt->fireStateChangeEvent());
		this.summary.on(EventType.click, evt->{
			evt.stopPropagation();
			evt.preventDefault();
			evt.stopImmediatePropagation();
		});
	}
	private void layout() {
		_this.add(span().css("mdc-list-item__graphic").style("height: auto; align-self: unset;").add(icon))
			 .add(div().css("mdc-list-item__text").style("margin-bottom: 10px;").add(title).add(info))
			 .add(meta);
	}
	public void update() {
		Scheduler.get().scheduleFixedDelay(()->{
			try {
				meta.element().innerHTML = "";
				meta.add(label("Loading...").style("margin-right: 40px;"));
				AnalysisApi.analysis(sample)
						.then(as -> {
							if (as == null || as.length <= 0) update(null);
							else {
								List<Analysis2> sorted = Arrays.stream(as)
										.filter(c->c.service().equals(service))
										.sorted(Comparator.comparing(c -> c.batch()))
										.collect(Collectors.toList());
								update(sorted.get(sorted.size() - 1));
							}
                            return null;
                        });
			} catch(Exception e) {
				e.printStackTrace();
				update();
			}
			return false;
		}, 300);

	}

	private void update(Analysis2 dto) {
		meta.element().innerHTML = "";
		depth.element().innerHTML = "";
		x10.element().innerHTML = "";
		serial.element().innerHTML = "";
		bam.element().innerHTML = "";
		int height = 74;
		if(dto!=null) {
			serial.element().innerHTML = dto.batch() + " - #" + dto.row();
			if(dto.get("depth(x)")!=null) {
				Double v = Double.parseDouble(dto.get("depth(x)"));
				depth.add(NumberFormat.getFormat("0.##").format(v));
				if(v <= 100) depth.style("color: #EFEFEF;font-weight: bold;");
				else depth.style("color: rgb(135,51,61);font-weight: bold;");
			} else AnalysisApi.depth(dto.sample(), dto.service(), dto.batch(), dto.row()).then(v->{
				depth.add(NumberFormat.getFormat("0.##").format(v));
				if(v == null || v <= 100) depth.style("color: #EFEFEF;font-weight: bold;");
				else depth.style("color: rgb(135,51,61);font-weight: bold;");
                return null;
            });
			if(dto.get("10x(%)")!=null || dto.get("10x>=(%)")!=null) {
				Double v = dto.get("10x(%)")!=null ? Double.parseDouble(dto.get("10x(%)")) : Double.parseDouble(dto.get("10x>=(%)"));
				x10.add(NumberFormat.getFormat("0.##").format(v));
				if(v <= 100) x10.style("color: #EFEFEF;font-weight: bold;");
				else x10.style("color: rgb(135,51,61);font-weight: bold;");
			} else AnalysisApi.x10(dto.sample(), dto.service(), dto.batch(), dto.row()).then(v->{
				x10.add(NumberFormat.getFormat("0.##").format(v));
				if(v == null || v <= 100) x10.style("color: #EFEFEF;font-weight: bold;");
				else x10.style("color: rgb(135,51,61);font-weight: bold;");
                return null;
            });
			if(dto.get("bam")!=null && dto.get("bam").trim().contains(".bam")) {
				height = 98;
				bam.element().parentElement.parentElement.removeAttribute("style");
				String fileName = dto.get("bam").trim();
				bam.element().href = "http://localhost:60151\\load?file=" + fileName;
				fileName = fileName.substring(Math.max(fileName.lastIndexOf("/"), fileName.lastIndexOf("\\")) +1);
				bam.add(fileName);
			} else bam.element().parentElement.parentElement.setAttribute("style", "display: none;");
			meta.add(summary);
		} else {
			meta.add(label("Not analyzed"));
		}
		Message msg = Message.builder().id(id).type(Message.MessageType.COLLAPSE).param(height + "px").build();
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
	}
	@Override
	public SingleCollapseElement that() {
		return this;
	}
	private final Set<StateChangeEventListener<WindowState>> listeners = new HashSet<>();
	@Override
	public Collection<StateChangeEventListener<WindowState>> listeners() {
		return listeners;
	}
	@Override
	public WindowState state() {
		return WindowState.FULLSCREEN;
	}
}
