package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class WorklistTemplate {
    private String id;
    private String order;
    private String name;
    private Sheet.SheetType type;
    @JsProperty(name="fixed_columns_left")
    private Integer fixedColumnsLeft;
    @JsProperty(name="page_size")
    private Integer pageSize;
    @JsProperty(name="order_column")
    private String orderColumn;
    @JsProperty(name="asc")
    private Boolean asc;
    private Sheet.ColumnDefinition[] columns;
    private Sheet.EventHandler[] handlers;

    private boolean active;
    private Sheet.ColumnDefinition[] values;
}
