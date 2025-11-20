package com.gcgenome.lims.client;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLLabelElement;
import elemental2.dom.HTMLTableElement;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HTMLContainerBuilder;

import static com.gcgenome.lims.client.Main.unwrap;
import static org.jboss.elemento.Elements.*;

public class SnvElement extends HTMLElementBuilder<HTMLDivElement, SnvElement> {
	public static SnvElement build(String id, JsPropertyMap snv) {
		return new SnvElement(id, snv, div());
	}
	private final HTMLContainerBuilder<HTMLTableElement> table = table().style("width: 60%;");
	private final HTMLContainerBuilder<HTMLLabelElement> gene = label();
	private final HTMLContainerBuilder<HTMLLabelElement> pos = label().css("id");
	private final HTMLContainerBuilder<HTMLLabelElement> hgvsc = label();
	private final HTMLContainerBuilder<HTMLLabelElement> hgvsp = label();
	private final HTMLContainerBuilder<HTMLLabelElement> tier = label();
	private final HTMLContainerBuilder<HTMLLabelElement> clazz = label();
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private SnvElement(String id, JsPropertyMap snv, HTMLContainerBuilder<HTMLDivElement> e) {
		super(e.css("sample").style("height: 112px; border-bottom-left-radius: 0;"));
		_this = e.add(table);
		gene.add((String) snv.get("gene.refgene"));
		pos.add(snv.get("reference") + ":" + snv.get("chrom") + ":" + snv.get("pos"));
		Object hc = snv.get("hgvsc_in_mane");
		if(hc == null) hc = snv.get("hgvsc");
		Object hp = snv.get("hgvsp_in_mane");
		if(hp == null) hp = snv.get("hgvsp");
		hgvsc.add(unwrap(hc));
		hgvsp.add(unwrap(hp));
		//tier.add((String) snv.get("gene.refgene"));
		//clazz.add((String) snv.get("gene.refgene"));
		table.add(colgroup().add(col().style("width: 20%;")).add(col().style("width: 30%;"))
							.add(col().style("width: 20%;")).add(col().style("width: 30%;")))
			 .add(thead().add(tr().add(th().add(label().css("label").add("SNV ID:"))).add(th().attr("colspan", "2").add(id))))
			 .add(tbody().add(tr().add(td().add(label().css("label").add("Gene:"))).add(td().add(gene)).add(td().add(label().css("label").add("Position:"))).add(td().add(pos)))
						 .add(tr().add(td().add(label().css("label").add("HGVS.C:"))).add(td().add(hgvsc)).add(td().add(label().css("label").add("HGVS.P:"))).add(td().add(hgvsp))));
	}

	@Override
	public SnvElement that() {
		return this;
	}
}
