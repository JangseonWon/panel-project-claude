package com.greencross.lims.client.snv;

import com.greencross.lims.dto.Sheet;
import com.greencross.lims.sheet.Data;
import elemental2.core.Global;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

import java.util.Arrays;
import java.util.stream.Collectors;

public interface ColumnSet {
    boolean select();
    void select(boolean value);
    String name();
    Sheet.ColumnDefinition[] columns();
    Data map(JsPropertyMap<Object> map, Data data);

    static String unwrap(Object obj) {
        if (obj == null) return null;
        String p = (String) obj;
        if (p.startsWith("[") && p.endsWith("]")) try {
            Any[] arr = Js.asArray(Global.JSON.parse(p));
            return Arrays.stream(arr).map(s -> String.valueOf(s)).distinct().collect(Collectors.joining(", "));
        } catch (Exception e) {return p;}
        else return p;
    }
}
