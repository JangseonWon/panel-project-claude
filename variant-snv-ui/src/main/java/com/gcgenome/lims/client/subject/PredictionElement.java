package com.gcgenome.lims.client.subject;

import com.gcgenome.lims.client.SectionElement;
import com.gcgenome.lims.client.SubjectElement;
import com.gcgenome.lims.ui.IconElement;
import com.google.gwt.i18n.client.NumberFormat;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.chart.Column;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.column.ColumnBuilder;
import org.jboss.elemento.HTMLContainerBuilder;

import static com.gcgenome.lims.client.Main.unwrap;
import static org.jboss.elemento.Elements.div;

public class PredictionElement extends SubjectElement<PredictionElement> {
	public static PredictionElement build(String id, JsPropertyMap snv) {
		return new PredictionElement(id, snv, div());
	}
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final Column[] columns1 = new Column[]{
			ColumnBuilder.number("sift.pred").name("SIFT_pred").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.number("sift.score").name("SIFT_score").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.number("polyphen.pred").name("PolyPhen_pred").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.number("polyphen.score").name("PolyPhen_score").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.string("mutationtaster.pred").readOnly(true).name("MutationTaster_pred").build(),
			ColumnBuilder.number("mutationtaster.score").name("MutationTaster_score").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
	};
	private final Column[] columns2 = new Column[]{
			ColumnBuilder.number("ada_score").name("ada_score").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.number("cadd.pred").name("CADD_pred").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.number("dann.score").name("DANN_score").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.number("gerp++.rs").name("GERP++_RS").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.number("gerp++.gt2").name("GERP++gt2").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.number("phylop20way_mammalian").name("phyloP20way_mammalian").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.number("phastcons20way_mammalian").name("phastCons20way_mammalian").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build(),
			ColumnBuilder.number("siphy_29way_logodds").name("SiPhy_29way_logOdds").readOnly(true).format(NumberFormat.getFormat("0.000000")).horizontal("right").font("JetBrains Mono").fontSize(CSSProperties.FontSizeUnionType.of(11)).build()
	};
	private final TableElement table1 = TableElement.build(columns1);
	private final TableElement table2 = TableElement.build(columns2);
	private final SectionElement section = SectionElement.build(IconElement.icon(IconElement.Type.Light, "fa-analytics"), "Prediction");
	private final JsPropertyMap snv;
	protected PredictionElement(String id, JsPropertyMap snv, HTMLContainerBuilder<HTMLDivElement> e) {
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
				.put("sift.pred", unwrap(map.get("sift.pred")))
				.put("sift.score",unwrap(map.get("sift.score")))
				.put("polyphen.pred", unwrap(map.get("polyphen.pred")))
				.put("polyphen.score", unwrap(map.get("polyphen.score")))
				.put("mutationtaster.pred", unwrap(map.get("mutationtaster.pred")))
				.put("mutationtaster.score", unwrap(map.get("mutationtaster.score")))
				.put("ada_.pred", (String)map.get("ada_pred"))
				.put("cadd.pred", (String)map.get("cadd.pred"))
				.put("dann.score", (String)map.get("dann.score"))
				.put("gerp++.rs", (String)map.get("gerp++.rs"))
				.put("gerp++.gt2", (String)map.get("gerp++.gt2"))
				.put("phylop20way_mammalian", (String)map.get("phylop20way_mammalian"))
				.put("phastcons20way_mammalian", (String)map.get("phastcons20way_mammalian"))
				.put("siphy_29way_logodds", (String)map.get("siphy_29way_logodds"));
	}

	@Override
	public PredictionElement that() {
		return this;
	}
}
