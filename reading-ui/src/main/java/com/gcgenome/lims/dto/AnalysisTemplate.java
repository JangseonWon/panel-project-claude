package com.gcgenome.lims.dto;

import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLTableCellElement;
import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.sayaya.ui.chart.SheetElement;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class AnalysisTemplate {
	private String id;
	private String order;
	private String name;
	private SheetType type;
	@JsProperty(name="fixed_columns_left")
	private Integer fixedColumnsLeft;
	@JsProperty(name="page_size")
	private Integer pageSize;
	@JsProperty(name="order_column")
	private String orderColumn;
	@JsProperty(name="asc")
	private Boolean asc;
	private ColumnDefinition[] columns;
	private EventHandler[] handlers;

	public enum SheetType {
		WORKLIST, BATCH, ANALYSIS, TABLE, KNOWLEDGE
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public final static class ColumnDefinition {
		private String id;
		private String name;
		private int order;
		private ColumnType type;
		@JsProperty(name="style_text")
		private StyleText styleText;
		@JsProperty(name="style_color")
		private StyleColor styleColor;
		@JsProperty(name="number_format")
		private String numberFormat;
		@JsProperty(name="date_format")
		private String dateFormat;
		private String formula;
		private String href;
		private String target;
		private Integer width;
		private Integer bag;
		@JsProperty(name="file_type")
		private String fileType;
		@JsProperty(name="init_value")
		private String initValue;
		private String sheet;
		private boolean expand;
		private boolean print;
		private Boolean readonly;
		private boolean settable;
		private Sort sort;
		private CellRenderer renderer;
		private LinkClickCallbackFn callback;
		public enum ColumnType {
			STRING, TEXT, DROPDOWN, SELECT, SELECT_ONE, SELECT_MULTI, NUMERIC, PROGRESS, DATE, FORMULA, IMAGE, SAMPLE, TABLE, FILE, LINK, LIST, OPTIONAL_VARIABLE, CHECKBOX
		}
	}
	@JsFunction
	public interface CellRenderer {
		HTMLElement render(SheetElement.Handsontable instance, HTMLTableCellElement td, int row, int col, String prop, String value, ColumnDefinition columnInfo);
		@JsOverlay
		default String getFont() {
			return "'Montserrat', 'Noto Sans KR', sans-serif";
		}
		@JsOverlay
		default int getFontSize() {
			return 12;
		}
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public final static class StyleText {
		private String font;
		@JsProperty(name="font_size")
		private int fontSize;
		private Alignment align;
		private boolean bold;
		private boolean italic;
	}
	@JsFunction
	public interface LinkClickCallbackFn {
		void onClick(Link link);
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public final static class Link {
		private String label;
		private String href;
		private String target;
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public final static class StyleColor {
		@JsProperty(name="color_fg")
		private String colorFg;
		@JsProperty(name="color_bg")
		private String colorBg;
		private StyleConditional[] conditions;

		@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
		@Setter(onMethod_={@JsOverlay, @JsIgnore})
		@Getter(onMethod_={@JsOverlay, @JsIgnore})
		@Accessors(fluent=true)
		public final static class StyleConditional {
			private Condition type;
			private String param;
			@JsProperty(name="color_fg")
			private String colorFg;
			@JsProperty(name="color_bg")
			private String colorBg;

			public enum Condition {
				EQ, LT, LE, GE, GT, BW, LK
			}
		}
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public final static class Sort {
		private String key;
		@JsProperty(name="asc")
		private boolean isAsc;
	}
	public enum Alignment {
		CENTER, LEFT, RIGHT
	}
	public enum Event {
		CREATE, UPDATE, DELETE
	}

	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public final static class EventHandler {
		private Event event;
		private String column;
		private String plugin;
	}
}
