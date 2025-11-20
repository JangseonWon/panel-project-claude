package com.greencross.lims.client;

import com.greencross.lims.api.WindowApi;
import com.greencross.lims.dto.Promise;
import elemental2.core.Global;
import elemental2.dom.HTMLDivElement;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.Builder;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnString;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.div;

@Setter
@Accessors(fluent=true)
public class SnvGridElement extends HTMLElementBuilder<HTMLDivElement, SnvGridElement> {
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private static ColumnString column(COLUMN_KEY key) {
		return ColumnBuilder.string(key.name()).name(key.label).readOnly(true);
	}
	enum COLUMN_KEY {
		id("ID"),
		chr("Chrom"),
		pos("Pos"),
		ref("Ref"),
		alt("Alt"),
		gene("Gene"),
		hgvsc("HGVS.C"),
		hgvsp("HGVS.P"),
		dclass("D.Class");
		private final String label;
		COLUMN_KEY(String label) {
			this.label = label;
		}
	}
	private final SheetElement sheet;
	private final SheetElement.SheetConfiguration config;
	@Builder
	private SnvGridElement() {
		this(div());
	}
	public SnvGridElement(HtmlContentBuilder<HTMLDivElement> e) {
		super(e);
		_this = e;
		config = SheetElement.builder().rowHeaders(false).autoColSize(true).autoRowSize(false).manualColumnMove(true).manualColumnResize(true);
		config.columns(
				ColumnBuilder.link(COLUMN_KEY.id.name(), Data::idx).name(COLUMN_KEY.id.label).readOnly(true).horizontal("left").onClick(data->WindowApi.open("varsnv.html#" + data.idx(), "_blank", null, false)).build(),
				column(COLUMN_KEY.chr).width(120).build(),
				column(COLUMN_KEY.pos).width(120).build(),
				column(COLUMN_KEY.ref).width(120).build(),
				column(COLUMN_KEY.alt).width(120).build(),
				column(COLUMN_KEY.hgvsc).width(120).build(),
				column(COLUMN_KEY.hgvsp).width(120).build(),
				column(COLUMN_KEY.gene).width(120).build(),
				column(COLUMN_KEY.dclass).width(120).build()
		).stretchH("all");
		sheet = config.build();
		//SheetElementSelectableMulti.header(sheet);
		e.add(div().style("border-top: 1px solid #AAA;").add(sheet));
	}

	public Promise<SnvGridElement> update(JsPropertyMap<String>[] data) {
		sheet.values(Arrays.stream(data).map(this::map).toArray(Data[]::new));
		return Promise.resolve(that());
	}
	private Data map(JsPropertyMap<String> map) {
		Object hc = map.get("hgmd.hgvsc");
		if(hc == null) hc = map.get("hgvsc");
		Object hp = map.get("hgmd.hgvsp");
		if(hp == null) hp = map.get("hgvsp");
		Data data = Data.create(String.valueOf(map.get("id")));
		data.put(COLUMN_KEY.id.name(), map.get("id"))
				.put(COLUMN_KEY.chr.name(), map.get("chrom"))
				.put(COLUMN_KEY.pos.name(), map.get("pos"))
				.put(COLUMN_KEY.ref.name(), map.get("ref"))
				.put(COLUMN_KEY.alt.name(), map.get("alt"))
				.put("tier", map.get("tier"))
				.put(COLUMN_KEY.hgvsc.name(), unwrap(hc))
				.put(COLUMN_KEY.hgvsp.name(), unwrap(hp))
				.put(COLUMN_KEY.gene.name(),unwrap(map.get("gene.refgene")))
				.put(COLUMN_KEY.dclass.name(), unwrap(map.get("d.class")));
		return data;
	}
	static String unwrap(Object obj) {
		if(obj == null) return null;
		String p = (String)obj;
		if(p.startsWith("[") && p.endsWith("]")) {
			Any[] arr = Js.asArray(Global.JSON.parse(p));
			return Arrays.stream(arr).map(s->String.valueOf(s)).distinct().collect(Collectors.joining(", "));
		} else return p;
	}
	@Override
	public SnvGridElement that() {
		return this;
	}
}
