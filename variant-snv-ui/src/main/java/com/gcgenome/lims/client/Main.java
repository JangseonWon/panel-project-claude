package com.gcgenome.lims.client;

import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.SnvApi;
import com.google.gwt.core.client.EntryPoint;
import elemental2.core.Global;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.jboss.elemento.Elements;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main implements EntryPoint {
    private final HTMLContainerBuilder<HTMLDivElement> div = Elements.div().css("top");
    @Override
    public void onModuleLoad() {
        Elements.body().add(div);
        String hash = DomGlobal.window.location.hash;
        String vid = hash.substring(1);
        ProgressApi.open(false);
        SnvApi.snv(vid).then(snv->{
            if(snv == null) return null;
            JsPropertyMap map = Js.asPropertyMap(snv);
            div.add(SnvElement.build(vid, map)).add(InfoElement.build(vid, map));
            return null;
        }).finally_(ProgressApi::close);
    }
    public static String unwrap(Object obj) {
        if(obj == null) return null;
        String p = (String)obj;
        if(p.startsWith("[") && p.endsWith("]")) {
            Any[] arr = Js.asArray(Global.JSON.parse(p));
            return Arrays.stream(arr).map(s->String.valueOf(s)).distinct().collect(Collectors.joining(", "));
        } else return p;
    }
}

