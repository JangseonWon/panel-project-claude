package com.gcgenome.lims.client.subject;

import com.gcgenome.lims.client.SectionElement;
import com.gcgenome.lims.client.SubjectElement;
import com.gcgenome.lims.ui.IconElement;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.chart.Column;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.column.ColumnBuilder;
import org.jboss.elemento.HTMLContainerBuilder;

import static com.gcgenome.lims.client.Main.unwrap;
import static org.jboss.elemento.Elements.div;

public class DiseaseElement extends SubjectElement<DiseaseElement> {
	public static DiseaseElement build(String id, JsPropertyMap snv) {
		return new DiseaseElement(id, snv, div());
	}
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final Column[] columns1 = new Column[]{
			ColumnBuilder.text("mim.disease", 150, 400).width(200).readOnly(true).name("MIM_disease").build(),
			ColumnBuilder.string("mim.inheritance").width(80).readOnly(true).name("MIM_Inheritance").build()
	};
	private final Column[] columns2 = new Column[]{
			//new Sheet.ColumnDefinition().width(200).readonly(true).id("clinvar.clndn").name("ClinVar Disease").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
			ColumnBuilder.text("hgmd.web.literature", 150, 400).width(200).readOnly(true).name("HGMD_Web_Literature").build()
			//new Sheet.ColumnDefinition().width(200).readonly(true).id("hgmd.codon.disease").name("HGMD_Codon_Disease").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT))
	};
	/*
	private final Sheet.ColumnDefinition[] columns2 = new Sheet.ColumnDefinition[]{
			new Sheet.ColumnDefinition().width(200).readonly(true).id("clinvar.clndn").name("ClinVar Disease").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
			new Sheet.ColumnDefinition().width(200).readonly(true).id("hgmd.web.literature").name("HGMD_Web_Literature").type(Sheet.ColumnDefinition.ColumnType.TEXT).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
			new Sheet.ColumnDefinition().width(200).readonly(true).id("hgmd.codon.disease").name("HGMD_Codon_Disease").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT))
	};
	 */
	private final TableElement table1 = TableElement.build(columns1);
	private final TableElement table2 = TableElement.build(columns2);
	private final SectionElement section = SectionElement.build(IconElement.icon(IconElement.Type.Light, "fa-disease"), "Disease");
	private final JsPropertyMap snv;
	protected DiseaseElement(String id, JsPropertyMap snv, HTMLContainerBuilder<HTMLDivElement> e) {
		super(id, e);
		this.snv = snv;
		_this = e;
		e.style("margin-bottom: 15px;").add(section).add(table1).add(table2);
		table2.element().style.marginTop = CSSProperties.MarginTopUnionType.of("10px");
	}

	@Override
	public void initialize() {
		String disease = (String) snv.get("mim.disease");
		try {if(disease!=null && !disease.trim().isEmpty()) {
			String[] d = disease.split("\\|");
			String[] h = unwrap(snv.get("mim.inheritance")).split(",");
			Data[] data = new Data[d.length];
			for(int i = 0; i < d.length; ++i) data[i] = Data.create(String.valueOf(i)).initialize("mim.disease", d[i]);
			for(int i = 0; i < h.length; ++i) data[i].initialize("mim.inheritance", h[i].trim());
			table1.update(data);
		}} catch(Exception e) {}

		String literature = (String) snv.get("hgmd.web.literature");
		if(literature!=null && !literature.trim().isEmpty()) {
			String[] l = literature.split(",");
			Data[] data = new Data[l.length];
			for(int i = 0; i < data.length; ++i) data[i] = Data.create(String.valueOf(i)).initialize("hgmd.web.literature", l[i]);
			table2.update(data);
		}

	}
	public Data map(JsPropertyMap<Object> map) {
		return Data.create("")
				.put("splicing_distance", unwrap(map.get("splicing_distance")))
				.put("mim.disease", unwrap(map.get("mim.disease")))
				.put("disease_description", unwrap(map.get("disease_description")))
				.put("mim.inheritance", unwrap(map.get("mim.inheritance")))
				.put("clinvar.clndn", unwrap(map.get("clinvar.clndn")))
				.put("hgmd.codon.disease", unwrap(map.get("hgmd.codon.disease")))
				.put("hgmd.web.literature", unwrap(map.get("hgmd.web.literature")));
	}

	@Override
	public DiseaseElement that() {
		return this;
	}
}
