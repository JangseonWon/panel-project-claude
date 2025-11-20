package com.greencross.lims.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Random;
import com.greencross.lims.dto.Message;
import elemental2.core.Global;
import elemental2.dom.DomGlobal;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.jboss.elemento.Elements;

public abstract class AbstractMain implements EntryPoint {
	protected abstract CollapseElement<?> collapse(String id, long sample, String service);
	protected abstract ExpandElement<?> expand(String id, long sample, String service);
	@Override
	public void onModuleLoad() {
		JsPropertyMap<String> params = params(DomGlobal.window.location.search);
		String id = params.get("id");
		long sample = Long.parseLong(params.get("sample").replace("-", ""));
		String service = params.get("service");
		CollapseElement<?> elemCollapsed = collapse(id, sample, service);
		ExpandElement<?> elemExpand = expand(id, sample, service);
		Elements.body().add(elemCollapsed).add(elemExpand);
		elemCollapsed.onStateChange(evt->{
			elemExpand.update();
			elemCollapsed.element().style.display = "none";
			elemExpand.element().style.display = null;
		});
		elemExpand.onStateChange(evt->{
			elemCollapsed.update();
			elemCollapsed.element().style.display = "flex";
			elemExpand.element().style.display = "none";
		});
		Message msg = new Message().id(id).type(Message.MessageType.COLLAPSE).param("64px");
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		elemExpand.element().style.display = "none";
		elemCollapsed.update();
	}
	private static native JsPropertyMap<String> params(String queryString) /*-{
		var params = {};
		queryString.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, value) { params[key] = value; });
		return params;
	}-*/;
}
