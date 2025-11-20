package com.gcgenome.lims.client.collapse;

import com.gcgenome.lims.api.AnalysisApi;
import com.gcgenome.lims.client.CollapseElement;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.dto.Analysis2;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.ui.IconElement;
import com.google.gwt.core.client.Scheduler;
import elemental2.core.Global;
import elemental2.dom.*;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.*;

public class SolidTumor2CollapseElement extends HTMLElementBuilder<HTMLDivElement, SolidTumor2CollapseElement> implements CollapseElement<HTMLDivElement> {
	public static SolidTumor2CollapseElement build(String id, long sample, String service) {
		return new SolidTumor2CollapseElement(div(), id, sample, service);
	}
	private final IconElement icon = IconElement.icon(IconElement.Type.Light, "fa-server");
	private final HTMLContainerBuilder<HTMLElement> title = span().css("mdc-list-item__primary-text").add("BI Analysis");
	private final HTMLContainerBuilder<HTMLElement> info = span().css("mdc-list-item__secondary-text").add("BI 분석 데이터에 대한 QC 결과를 확인합니다.");
	private final HTMLContainerBuilder<HTMLElement> meta = span().css("mdc-list-item__meta").style("margin-right: 40px;");
	private final HTMLContainerBuilder<HTMLLabelElement> serial = label();

	private final HTMLContainerBuilder<HTMLTableElement> summary = table().style("text-align: right; font-size: var(--mdc-typography-caption-font-size, 0.75rem);")
																		.add(tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));")
																						  .add("Analysis ID: ")).add(td().add(serial)));
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final SolidTumorInfo contents;
	private final String id;
	private final long sample;
	private final String service;
	private SolidTumor2CollapseElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e.css("work-summary")
			   .style("margin-left: 16px;margin-right: 16px;display: flex; flex-direction: row; " +
					  "flex-wrap: wrap; align-content: space-between; justify-content: flex-start; " +
					  "align-items: center; cursor: pointer;"));
		if("N198".equalsIgnoreCase(service) || "ON198".equalsIgnoreCase(service)) contents = new SolidTumor2CollapseElement.N198();
		else if("N199".equalsIgnoreCase(service) || "ON199".equalsIgnoreCase(service) || "G0022402".equalsIgnoreCase(service)) contents = new SolidTumor2CollapseElement.N199();
		else if("N200".equalsIgnoreCase(service) || "ON200".equalsIgnoreCase(service)) contents = new SolidTumor2CollapseElement.N200();
		else contents = new Null();
		this.id = id;
		this.sample = sample;
		this.service = service;
		_this = e;
		layout();
		this.on(EventType.click, evt->fireStateChangeEvent());
		this.summary.on(EventType.click, evt->{
			evt.stopPropagation();
			evt.preventDefault();
			evt.stopImmediatePropagation();
		});
	}
	private void layout() {
		for(HTMLContainerBuilder<?> row: contents.rows()) summary.add(row);
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
								List<Analysis2> sorted = Arrays.stream(as).filter(Objects::nonNull)
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
		serial.element().innerHTML = "";
		if(dto!=null) {
			serial.element().innerHTML = dto.batch() + " - #" + dto.row();
			meta.add(summary);
			contents.update(dto);
		} else {
			meta.add(label("Not analyzed"));
		}
		Message msg = Message.builder().id(id).type(Message.MessageType.COLLAPSE).param(contents.height() + "px").build();
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
	}
	@Override
	public SolidTumor2CollapseElement that() {
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

	private interface SolidTumorInfo {
		default HTMLContainerBuilder<HTMLTableRowElement> row(String label, HTMLContainerBuilder<?> elem) {
			return tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));").add(label)).add(td().add(elem));
		}
		HTMLContainerBuilder<?>[] rows();
		void update(Analysis2 dto);
		default int height() { return 64; }
	}
	private final static class Null implements SolidTumorInfo {
		@Override
		public HTMLContainerBuilder<?>[] rows() {
			return new HTMLContainerBuilder[0];
		}
		@Override
		public void update(Analysis2 dto) {}
	}
	private final static class N198 implements SolidTumorInfo {
		private final HTMLContainerBuilder<HTMLLabelElement> cancerType = label();
		private final HTMLContainerBuilder<HTMLLabelElement> tmb = label();
		private final HTMLContainerBuilder<HTMLLabelElement> msi = label();
		private final HTMLContainerBuilder<HTMLLabelElement> snv = label();
		private final HTMLContainerBuilder<HTMLLabelElement> cnv = label();
		private final HTMLContainerBuilder<HTMLLabelElement> msaf = label();
		private int height = 64;
		public HTMLContainerBuilder<?>[] rows() {
			return new HTMLContainerBuilder<?>[] {
					row("Cancer Type", cancerType),
					row("TMB", tmb),
					row("MSI", msi),
					row("QC", snv),
					row("CNV QC", cnv),
					row("MSAF", msaf)
			};
		}
		@Override
		public int height() { return height; }
		@Override
		public void update(Analysis2 dto) {
			cancerType.element().innerHTML = "";
			tmb.element().innerHTML = "";
			msi.element().innerHTML = "";
			snv.element().innerHTML = "";
			cnv.element().innerHTML = "";
			msaf.element().innerHTML = "";
			if(dto.get("cancer_type")!=null) cancerType.add(dto.get("tissue") + "/" + dto.get("cancer_type"));
			if(dto.get("tmb")!=null) {
				tmb.add(dto.get("tmb"));
				height = 180;
			}
			if(dto.get("msi_status")!=null) msi.add(dto.get("msi_status") + " (score: " + dto.get("msi_score") + ")");
			if(dto.get("qc_snv_tmb")!=null) {
				snv.add(dto.get("qc_snv_tmb"));
				if(!"pass".equalsIgnoreCase(dto.get("qc_snv_tmb"))) snv.style("color: #EFEFEF;font-weight: bold;");
			}
			if(dto.get("qc_cnv")!=null) {
				cnv.add(dto.get("qc_cnv"));
				if(!"pass".equalsIgnoreCase(dto.get("qc_cnv"))) cnv.style("color: #EFEFEF;font-weight: bold;");
			}
			if(dto.get("msaf")!=null) msaf.add(dto.get("msaf"));
		}
	}
	private final static class N199 implements SolidTumorInfo {
		private final HTMLContainerBuilder<HTMLLabelElement> cancerType = label();
		private final HTMLContainerBuilder<HTMLLabelElement> tmb = label();
		private final HTMLContainerBuilder<HTMLLabelElement> msi = label();
		private final HTMLContainerBuilder<HTMLLabelElement> snv = label();
		private final HTMLContainerBuilder<HTMLLabelElement> cnv = label();
		private final HTMLContainerBuilder<HTMLLabelElement> msi2 = label();
		private final HTMLContainerBuilder<HTMLLabelElement> rna = label();
		private final HTMLContainerBuilder<HTMLLabelElement> purity = label();
		private int height = 64;
		public HTMLContainerBuilder<?>[] rows() {
			return new HTMLContainerBuilder<?>[] {
					row("Cancer Type", cancerType),
					row("TMB", tmb),
					row("MSI", msi),
					row("SNV QC", snv),
					row("CNV QC", cnv),
					row("MSI QC", msi2),
					row("RNA QC", rna),
					row("Tumor Purity", purity)
			};
		}
		@Override
		public int height() { return height; }
		@Override
		public void update(Analysis2 dto) {
			cancerType.element().innerHTML = "";
			tmb.element().innerHTML = "";
			msi.element().innerHTML = "";
			snv.element().innerHTML = "";
			cnv.element().innerHTML = "";
			msi2.element().innerHTML = "";
			rna.element().innerHTML = "";
			purity.element().innerHTML = "";
			if(dto.get("cancer_type")!=null) cancerType.add(dto.get("tissue") + "/" + dto.get("cancer_type"));
			if(dto.get("tmb")!=null) {
				tmb.add(dto.get("tmb"));
				height = 220;
			}
			if(dto.get("msi_status")!=null) msi.add(dto.get("msi_status") + " (score: " + dto.get("msi_score") + ")");
			if(dto.get("qc_snv_tmb")!=null) {
				snv.add(dto.get("qc_snv_tmb"));
				if(!"pass".equalsIgnoreCase(dto.get("qc_snv_tmb"))) snv.style("color: #EFEFEF;font-weight: bold;");
			}
			if(dto.get("qc_cnv")!=null) {
				cnv.add(dto.get("qc_cnv"));
				if(!"pass".equalsIgnoreCase(dto.get("qc_cnv"))) cnv.style("color: #EFEFEF;font-weight: bold;");
			}
			if(dto.get("qc_msi")!=null) {
				msi2.add(dto.get("qc_msi"));
				if(!"pass".equalsIgnoreCase(dto.get("qc_msi"))) msi2.style("color: #EFEFEF;font-weight: bold;");
			}
			if(dto.get("qc_rna")!=null) {
				rna.add(dto.get("qc_rna"));
				if(!"pass".equalsIgnoreCase(dto.get("qc_rna"))) rna.style("color: #EFEFEF;font-weight: bold;");
			}
			if(dto.get("tumor_purity")!=null) purity.add(dto.get("tumor_purity"));
		}
	}
	private final static class N200 implements SolidTumorInfo {
		private final HTMLContainerBuilder<HTMLLabelElement> cancerType = label();
		private final HTMLContainerBuilder<HTMLLabelElement> tmb = label();
		private final HTMLContainerBuilder<HTMLLabelElement> msi = label();
		private final HTMLContainerBuilder<HTMLLabelElement> snv = label();
		private final HTMLContainerBuilder<HTMLLabelElement> cnv = label();
		private final HTMLContainerBuilder<HTMLLabelElement> msi2 = label();
		private final HTMLContainerBuilder<HTMLLabelElement> purity = label();
		private int height = 64;
		public HTMLContainerBuilder<?>[] rows() {
			return new HTMLContainerBuilder<?>[] {
					row("Cancer Type", cancerType),
					row("TMB", tmb),
					row("MSI", msi),
					row("SNV QC", snv),
					row("CNV QC", cnv),
					row("MSI QC", msi2),
					row("Tumor Purity", purity)
			};
		}
		@Override
		public int height() { return height; }
		@Override
		public void update(Analysis2 dto) {
			cancerType.element().innerHTML = "";
			tmb.element().innerHTML = "";
			msi.element().innerHTML = "";
			snv.element().innerHTML = "";
			cnv.element().innerHTML = "";
			msi2.element().innerHTML = "";
			purity.element().innerHTML = "";
			if(dto.get("cancer_type")!=null) cancerType.add(dto.get("tissue") + "/" + dto.get("cancer_type"));
			if(dto.get("tmb")!=null) {
				tmb.add(dto.get("tmb"));
				height = 200;
			}
			if(dto.get("msi_status")!=null) msi.add(dto.get("msi_status") + " (score: " + dto.get("msi_score") + ")");
			if(dto.get("qc_snv_tmb")!=null) {
				snv.add(dto.get("qc_snv_tmb"));
				if(!"pass".equalsIgnoreCase(dto.get("qc_snv_tmb"))) snv.style("color: #EFEFEF;font-weight: bold;");
			}
			if(dto.get("qc_cnv")!=null) {
				cnv.add(dto.get("qc_cnv"));
				if(!"pass".equalsIgnoreCase(dto.get("qc_cnv"))) cnv.style("color: #EFEFEF;font-weight: bold;");
			}
			if(dto.get("qc_msi")!=null) {
				msi2.add(dto.get("qc_msi"));
				if(!"pass".equalsIgnoreCase(dto.get("qc_msi"))) msi2.style("color: #EFEFEF;font-weight: bold;");
			}
			if(dto.get("tumor_purity")!=null) purity.add(dto.get("tumor_purity"));
		}
	}
}
