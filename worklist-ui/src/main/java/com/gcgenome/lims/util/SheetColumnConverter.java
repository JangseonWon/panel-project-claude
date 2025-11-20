package com.gcgenome.lims.util;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.gcgenome.lims.dto.Sheet;
import elemental2.core.JsRegExp;
import elemental2.core.RegExpResult;
import elemental2.dom.CSSProperties;
import elemental2.dom.DomGlobal;
import net.sayaya.ui.ListElement;
import net.sayaya.ui.chart.Column;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.column.ColumnBuilder;

import java.util.LinkedList;
import java.util.List;

public class SheetColumnConverter {
    public static Column convert(Sheet.ColumnDefinition def) {
        switch (def.type()) {
            case "WORKLIST": return convertFromLink(def);
            case "LINK": return convertFromLink(def);
            case "STRING": return convertFromString(def);
            case "TEXT": return convertFromText(def);
            case "NUMERIC": return convertFromNumber(def);
            case "DATE": return convertFromDate(def);
            case "DROPDOWN": return convertFromDropdown(def);
            default: throw new RuntimeException("Unknown column type: " + def.type());
        }
    }
    private static Column convertFromString(Sheet.ColumnDefinition def) {
        var builder = ColumnBuilder.string(def.id()).name(def.name());
        if(def.styleText()!=null) {
            var style = def.styleText();
            builder.bold(style.bold()).italic(style.italic())
                   .font(style.font())
                   .fontSize(style.fontSize() > 0? CSSProperties.FontSizeUnionType.of(style.fontSize() + "px") : null)
                   .horizontal(style.align())
                   .vertical("middle");
        }
        if(def.styleColor()!=null) {
            var style = def.styleColor();
            builder.colorBackground(style.colorBg()).color(style.colorFg());
            if(style.conditions()!=null) for(var condition: style.conditions()) {
                if(condition.type() == Sheet.StyleColor.StyleConditional.Condition.EQ) {
                    var escaped = RegExp.quote(condition.param());
                    builder.pattern("^" + escaped + "$").than(condition.colorFg(), condition.colorBg());
                } else if(condition.type() == Sheet.StyleColor.StyleConditional.Condition.LK) {
                    builder.pattern(condition.param()).than(condition.colorFg(), condition.colorBg());
                }
            }
        }
        return builder.build();
    }
    private static Column convertFromText(Sheet.ColumnDefinition def) {
        var builder = ColumnBuilder.text(def.id(), 10, 100).name(def.name());
        if(def.styleText()!=null) {
            var style = def.styleText();
            builder.bold(style.bold()).italic(style.italic())
                   .font(style.font())
                   .fontSize(style.fontSize() > 0? CSSProperties.FontSizeUnionType.of(style.fontSize() + "px") : null)
                   .horizontal(style.align())
                   .vertical("middle");
        }
        if(def.styleColor()!=null) {
            var style = def.styleColor();
            builder.colorBackground(style.colorBg()).color(style.colorFg());
            if(style.conditions()!=null) for(var condition: style.conditions()) {
                if(condition.type() == Sheet.StyleColor.StyleConditional.Condition.EQ) {
                    var escaped = RegExp.quote(condition.param());
                    builder.pattern("^" + escaped + "$").than(condition.colorFg(), condition.colorBg());
                } else if(condition.type() == Sheet.StyleColor.StyleConditional.Condition.LK) {
                    builder.pattern(condition.param()).than(condition.colorFg(), condition.colorBg());
                }
            }
        }
        return builder.build();
    }
    private static Column convertFromNumber(Sheet.ColumnDefinition def) {
        var builder = ColumnBuilder.number(def.id()).name(def.name());
        if(def.styleText()!=null) {
            var style = def.styleText();
            builder.bold(style.bold()).italic(style.italic())
                    .font(style.font())
                    .fontSize(style.fontSize() > 0? CSSProperties.FontSizeUnionType.of(style.fontSize() + "px") : null)
                    .horizontal(style.align())
                    .vertical("middle");
        }
        return builder.build();
    }
    private static Column convertFromDate(Sheet.ColumnDefinition def) {
        var builder = ColumnBuilder.date(def.id()).name(def.name()).format(DateTimeFormat.getFormat(def.dateFormat()));
        if(def.styleText()!=null) {
            var style = def.styleText();
            builder.bold(style.bold()).italic(style.italic())
                   .font(style.font())
                   .fontSize(style.fontSize() > 0? CSSProperties.FontSizeUnionType.of(style.fontSize() + "px") : null)
                   .horizontal(style.align())
                   .vertical("middle");
        }
        return builder.build();
    }
    private static Column convertFromDropdown(Sheet.ColumnDefinition def) {
        List<ListElement.SingleLineItem> children = new LinkedList<>();
        if(def.styleColor()!=null) {
            var style = def.styleColor();
            if(style.conditions()!=null) for(var condition: style.conditions()) {
                var item = ListElement.singleLine().label(condition.param());
                children.add(item);
            }
        }
        ListElement.SingleLineItem[] array = new ListElement.SingleLineItem[children.size()];
        array = children.toArray(array);
        var builder = ColumnBuilder.dropdown(def.id(), array).name(def.name());
        if(def.styleText()!=null) {
            var style = def.styleText();
            builder.horizontal(style.align())
                   .vertical("middle");
        }
        if(def.styleColor()!=null) {
            var style = def.styleColor();
            builder.colorBackground(style.colorBg()).color(style.colorFg());
            if(style.conditions()!=null) for(var condition: style.conditions()) {
                if(condition.type() == Sheet.StyleColor.StyleConditional.Condition.EQ) {
                    var escaped = RegExp.quote(condition.param());
                    builder.pattern("^" + escaped + "$").than(condition.colorFg(), condition.colorBg());
                } else if(condition.type() == Sheet.StyleColor.StyleConditional.Condition.LK) {
                    builder.pattern(condition.param()).than(condition.colorFg(), condition.colorBg());
                }
            }
        }
        return builder.build();
    }
    private static Column convertFromLink(Sheet.ColumnDefinition def) {
        String href = def.href();
        var builder = ColumnBuilder.link(def.id(), data->toActualHref(href, data)).name(def.name());
        builder.onClick(data->{
            var hrefActual = toActualHref(href, data);
            if("_self()".equalsIgnoreCase(def.target())) DomGlobal.window.location.replace(hrefActual);
            else DomGlobal.window.open(hrefActual, def.target(), null);
        });
        if(def.styleText()!=null) {
            var style = def.styleText();
            builder.bold(style.bold()).italic(style.italic())
                    .font(style.font())
                    .fontSize(style.fontSize() > 0? CSSProperties.FontSizeUnionType.of(style.fontSize() + "px") : null)
                    .horizontal(style.align())
                    .vertical("middle");
        }
        if(def.styleColor()!=null) {
            var style = def.styleColor();
            builder.colorBackground(style.colorBg()).color(style.colorFg());
            if(style.conditions()!=null) for(var condition: style.conditions()) {
                if(condition.type() == Sheet.StyleColor.StyleConditional.Condition.EQ) {
                    var escaped = RegExp.quote(condition.param());
                    builder.pattern("^" + escaped + "$").than(condition.colorFg(), condition.colorBg());
                } else if(condition.type() == Sheet.StyleColor.StyleConditional.Condition.LK) {
                    builder.pattern(condition.param()).than(condition.colorFg(), condition.colorBg());
                }
            }
        }
        return builder.build();
    }
    private final static JsRegExp CHK_REFERENCES = new JsRegExp("(\\[\\\"\\w+\\\"\\])");
    private static String toActualHref(String href, Data data) {
        var chk = CHK_REFERENCES.exec(href);
        if(chk!=null && !chk.asList().isEmpty()) {
            for(int i = 0; i < chk.length-1; ++i) {
                String match = chk.getAt(i+1);
                String key = match.substring(2, match.length()-2);
                String v = data.get(key);
                href = href.replace(match, v!=null?v:"");
            }
        }
        return href.replace("[idx]", data.idx());
    }
}
