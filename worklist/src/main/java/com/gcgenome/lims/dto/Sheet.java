package com.gcgenome.lims.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sheet implements Serializable {
	private String id;
	private String order;
	private String name;
	private SheetType type;
	private Integer fixedColumnsLeft;
	private Integer pageSize;
	private String orderColumn;
	private Boolean asc;
	private List<ColumnDefinition> columns;
	private List<EventHandler> handlers;
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class ColumnDefinition implements Serializable {
		private String id;
		private String name;
		private int order;
		private ColumnType type;
		private StyleText styleText;
		private StyleColor styleColor;
		private String numberFormat;
		private String dateFormat;
		private String formula;
		private String href;
		private String target;
		private Integer bag;
		private String fileType;
		private String initValue;
		private String sheet;
		private boolean expand;
		private boolean print;

		public enum ColumnType {
			STRING, TEXT, DROPDOWN, SELECT_ONE, SELECT_MULTI, NUMERIC, PROGRESS, DATE, FORMULA, IMAGE, SAMPLE, TABLE, FILE, LINK, LIST, OPTIONAL_VARIABLE
		}
	}

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Link implements Serializable {
		private String label;
		private String href;
		private String target;
	}

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Option implements Serializable {
		private Value[] options;

		@Setter
		@Getter
		@Accessors(fluent=true)
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static final class Value implements Serializable {
			private String text;
			private Boolean select;
		}
	}

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class StyleText implements Serializable {
		private String font;
		private int fontSize;
		private Alignment align;
		private boolean bold;
		private boolean italic;
	}

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class StyleColor implements Serializable {
		private String colorFg;
		private String colorBg;
		private StyleConditional[] conditions;

		@Setter
		@Getter
		@Accessors(fluent=true)
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static final class StyleConditional implements Serializable {
			private Condition type;
			private String param;
			private String colorFg;
			private String colorBg;

			public enum Condition {
				EQ, LT, LE, GE, GT, BW, LK
			}
		}
	}

	public enum SheetType {
		WORKLIST, BATCH, ANALYSIS, TABLE, KNOWLEDGE
	}

	public enum Alignment {
		CENTER, LEFT, RIGHT
	}

	public enum Event {
		CREATE, UPDATE, DELETE
	}

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class EventHandler implements Serializable {
		private Event event;
		private String column;
		private String plugin;
	}
}
