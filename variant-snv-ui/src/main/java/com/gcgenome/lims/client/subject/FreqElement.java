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

import static org.jboss.elemento.Elements.div;

public class FreqElement extends SubjectElement<FreqElement> {
	public static FreqElement build(String id, JsPropertyMap snv) {
		return new FreqElement(id, snv, div());
	}
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final Column[] columns1 = new Column[]{
			ColumnBuilder.string("gnomad_total_af").readOnly(true).name("gnomAD Total AF").horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.string("gnomad_exomes_af_eas").readOnly(true).name("gnomAD_exomes_AF_EAS").horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.string("gnomad_genomes_af_eas").readOnly(true).name("gnomAD_genomes_AF_EAS").horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.string("gnomad_exomes_af_eas_kor").readOnly(true).name("gnomAD_exomes_AF_KOR").horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.string("gnomad_exomes_af_eas").readOnly(true).name("gnomAD_exomes_AF_EAS").horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.string("gnomad.exome.eas").readOnly(true).name("gnomAD_genome_EAS").horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build()
	};
	private final Column[] columns2 = new Column[]{
			ColumnBuilder.string("krgdb_af").name("KRG_DB_AF").horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.string("wes300_af").name("WES300_freq").horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.string("wes_als312_af").name("WES_ALS312_AF").horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.link("dbsnp", data->"https://www.ncbi.nlm.nih.gov/snp/" + data.get("dbsnp")).readOnly(true).name("dbSNP").build(),
	};
	private final TableElement table1 = TableElement.build(columns1);
	private final TableElement table2 = TableElement.build(columns2);
	private final SectionElement section = SectionElement.build(IconElement.icon(IconElement.Type.Light, "fa-chart-bar"), "Frequency");
	private final JsPropertyMap snv;
	protected FreqElement(String id, JsPropertyMap snv, HTMLContainerBuilder<HTMLDivElement> e) {
		super(id, e);
		this.snv = snv;
		_this = e;
		e.style("margin-bottom: 15px;").add(section).add(table1).add(table2);
		table2.element().style.marginTop = CSSProperties.MarginTopUnionType.of("10px");
	}

	@Override
	public void initialize() {
		Data data = map(snv);
		table1.update(new Data[] { data } );
		table2.update(new Data[] { data } );
	}
	public Data map(JsPropertyMap<Object> map) {
		return Data.create("")
				.initialize("gnomad_total_af", (String)map.get("gnomad.total"))
				.initialize("gnomad_exomes_af_eas", (String)map.get("gnomad.exome.eas"))
				.initialize("gnomad_genomes_af_eas", (String)map.get("gnomad.genome.eas"))
				.initialize("gnomad_exomes_af_eas_kor", (String)map.get("gnomad.exomes._eas_kor"))
				.initialize("krgdb_af", (String)map.get("krg_db_1100"))
				.initialize("wes300_af", (String)map.get("wes300_af"))
				.initialize("wes_als312_af", (String)map.get("wes_als312_af"))
				.initialize("dbsnp", (String)map.get("dbsnp"));
	}

	@Override
	public FreqElement that() {
		return this;
	}
}
