package com.greencross.lims.client.expand;

import com.greencross.lims.api.Callback;
import com.greencross.lims.sheet.SpreadSheet;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import lombok.Data;
import lombok.experimental.Accessors;
import net.sayaya.ui.*;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class SelectGEPanelDialog implements IsElement<HTMLDivElement> {
	@Data
	@Accessors(fluent = true)
	public static class GenomicEnglandPanel {
		private String panel;
		private String[] genes;
	}
	public static SelectGEPanelDialog instance() {
		return new SelectGEPanelDialog();
	}
	private static final String[] PANELS = new String[] {
			"*", "Amelogenesis imperfecta", "Adult onset movement disorder"
	};
	private final HtmlContentBuilder<HTMLDivElement> contents = div().style("width: 800px;color: var(--mdc-theme-text-primary-on-background);");
	private final ListElement listPanel = net.sayaya.ui.ListElement.singleLineList();
	private final DropDownElement iptPanels;
	private final TextAreaElement<String> iptGenes = TextAreaElement.textBox().outlined().text("Gene").readOnly(true).style("width: 800px; height: 150px;margin-top: 10px;");
	private final ButtonElementText cancel = ButtonElement.flat().text("Cancel");
	private final ButtonElementText submit = ButtonElement.contain().text("Apply");
	private final Dialog dialog;
	private final Map<String, String[]> t = new HashMap<>();
	private Callback<GenomicEnglandPanel> callback;
	private SelectGEPanelDialog() {
		for(String panel: PANELS) listPanel.add(ListElement.singleLine().label(panel));
		iptPanels = DropDownElement.outlined(listPanel).css("input").text("Panel").style("width: 800px; margin-top: 10px;");
		iptPanels.onSelectionChange(evt->{
			String panel = PANELS[evt.selection()];
			String[] genes = t.get(panel);
			iptGenes.value(Arrays.stream(genes).map(c->c.trim()).distinct().collect(Collectors.joining(", ")));
		});
		dialog =  Dialog.confirmation("Choose Panel", cancel, submit)
						.add(contents.add(iptPanels).add(iptGenes));
		cancel.onClick(evt->dialog.close());
		submit.onClick(evt->{
			String panel = PANELS[iptPanels.selection()];
			String[] genes = iptGenes.value().contains(",")?Arrays.stream(iptGenes.value().split(",")).map(t->t.trim()).toArray(String[]::new):new String[] {iptGenes.value()};
			callback.onSuccess(new GenomicEnglandPanel().genes(genes).panel(panel));
			dialog.close();
		});
		HTMLElement surface = (HTMLElement) contents.element().parentElement.parentElement;
		surface.style.maxWidth = CSSProperties.MaxWidthUnionType.of("none");
	}
	public SelectGEPanelDialog callback(Callback<GenomicEnglandPanel> callback) {
		this.callback = callback;
		return this;
	}

	public SelectGEPanelDialog build(String type) {
		t.clear();
		if("Green".equalsIgnoreCase(type)) {
			iptGenes.text("Green Genes");
			t.put("*", new String[]{"ELANE", "AGL", "AGK", "HSPA9", "NYX", "AGA", "MT-ND4L", "B2M", "DUOXA2","RELT", "KLK4","GNAL"});
			t.put("Amelogenesis imperfecta", new String[]{"RELT", "KLK4"});
			t.put("Adult onset movement disorder", new String[]{"GNAL"});
		} else if("Amber".equalsIgnoreCase(type)) {
			iptGenes.text("Amber Genes");
			t.put("*", new String[]{"H4C3", "RNF13", "H4C11", "TOMM70", "GPR183", "ZC3H14", "SP6", "CEP89", "SP6", "AMTN", "EIF4G1"});
			t.put("Amelogenesis imperfecta", new String[]{"SP6", "AMTN"});
			t.put("Adult onset movement disorder", new String[]{"EIF4G1"});
		}
		iptPanels.select(0);
		iptGenes.value(Arrays.stream(t.get("*")).map(c->c.trim()).distinct().collect(Collectors.joining(", ")));
		try {dialog.open();} catch(Exception ignore){}
		return this;
	}
	@Override
	public HTMLDivElement element() {
		return dialog.element();
	}
}
