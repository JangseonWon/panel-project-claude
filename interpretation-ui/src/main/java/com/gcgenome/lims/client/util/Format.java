package com.gcgenome.lims.client.util;

import elemental2.core.Global;
import jsinterop.base.Any;
import jsinterop.base.Js;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class Format {
	public String unwrap(Object obj) {
		if(obj == null) return null;
		String p = (String)obj;
		if(p.startsWith("[") && p.endsWith("]")) {
			Any[] arr = Js.asArray(Global.JSON.parse(p));
			return Arrays.stream(arr).map(s->String.valueOf(s)).distinct().collect(Collectors.joining(", "));
		} else return p;
	}
}
