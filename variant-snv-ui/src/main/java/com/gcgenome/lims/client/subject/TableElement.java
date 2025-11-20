package com.gcgenome.lims.client.subject;

import com.google.gwt.core.client.Scheduler;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.Column;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import org.jboss.elemento.HTMLContainerBuilder;

import static org.jboss.elemento.Elements.div;

public class TableElement extends HTMLElementBuilder<HTMLDivElement, TableElement> {
	public static TableElement build(Column[] columns) {
		return new TableElement(columns, div());
	}
	private final SheetElement.SheetConfiguration config;
	final SheetElement sheet;
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private TableElement(Column[] columns, HTMLContainerBuilder<HTMLDivElement> e) {
		super(e);
		config = SheetElement.builder()
							 .autoColSize(false)
							 .autoRowSize(false)
							 .rowHeaders(false)
							 .manualColumnMove(true)
							 .manualColumnResize(true)
							 .columns(columns)
							 .data(new Data[] {})
				.width(1200)
							 .stretchH("all");
		sheet = config.build().style("border-bottom: 1px solid #AAA;");
		_this = e.style("overflow: hidden; min-height: 7px; border-top: 1px solid #AAA; margin-right: 20px; margin-left: 20px;").add(sheet);
		((HTMLElement)sheet.element().parentElement).style.paddingBottom = CSSProperties.PaddingBottomUnionType.of("12px");
	}

	public TableElement update(Data[] data) {
		sheet.values(data);
		Scheduler.get().scheduleDeferred(()->((HTMLElement)sheet.element().parentElement).style.paddingBottom = null);
		int row = data.length;
		Scheduler.get().scheduleFixedDelay(()-> {
			int height = 28 + 24*row;
			_this.element().style.height = CSSProperties.HeightUnionType.of(Math.max(height, ((HTMLElement) sheet.element().querySelector(".ht_master .wtHider")).offsetHeight) + "px");
			sheet.refresh();
			return false;
		}, 500);
		return that();
	}
	@Override
	public TableElement that() {
		return this;
	}
}
