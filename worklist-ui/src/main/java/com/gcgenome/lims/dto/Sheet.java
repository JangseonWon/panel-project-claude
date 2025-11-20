package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Sheet {
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

    @JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
    @Setter(onMethod_={@JsOverlay, @JsIgnore})
    @Getter(onMethod_={@JsOverlay, @JsIgnore})
    @Accessors(fluent=true)
    public final static class ColumnDefinition {
        private String id;
        private String name;
        private int order;
        private String type;
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
        //private CellRenderer renderer;
        private LinkClickCallbackFn callback;
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
    public final static class Option {
        private Value[] options;

        @JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
        @Setter(onMethod_={@JsOverlay, @JsIgnore})
        @Getter(onMethod_={@JsOverlay, @JsIgnore})
        @Accessors(fluent=true)
        public final static class Value {
            private String text;
            private Boolean select;
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
        private String align;
        private boolean bold;
        private boolean italic;
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

    public enum SheetType {
        WORKLIST, BATCH, ANALYSIS, TABLE, KNOWLEDGE
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

    @JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
    @Setter(onMethod_={@JsOverlay, @JsIgnore})
    @Getter(onMethod_={@JsOverlay, @JsIgnore})
    @Accessors(fluent=true)
    public final static class Sort {
        private String key;
        @JsProperty(name="asc")
        private boolean isAsc;
    }
}
