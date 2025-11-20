package com.greencross.lims.api;

import com.google.gwt.dom.client.Document;
import com.greencross.lims.dto.Message;
import elemental2.core.Global;
import elemental2.dom.*;
import elemental2.promise.Promise;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FetchApi {
	public Promise<Response> request(String url) {
		return request(url, null);
	}
	public Promise<Response> request(String url, RequestInit param) {
		return url(url).then(u->DomGlobal.fetch(u, param));
	}
	public Promise<String> url(String url) {
		return url(Document.get().createUniqueId(), url);
	}
	private Promise<String> url(String id, String url) {
		return new Promise<>((resolve, reject)->{
			Message message = new Message().id(id).type(Message.MessageType.REQUEST).param(url);
			EventListener listener = new EventListener() {
				@Override
				public void handleEvent(Event evt) {
					if(evt==null) return;
					JsPropertyMap<Object> map = Js.asPropertyMap(evt);
					if(!map.has("data")) return;
					String data = (String) map.get("data");
					if(!data.contains("___id")) return;
					Message msg = Js.uncheckedCast(Global.JSON.parse(data));
					if(!id.equals(msg.id())) return;
					if(msg.type() == Message.MessageType.RESPONSE) {
						DomGlobal.window.removeEventListener("message", this);
						resolve.onInvoke((String)msg.param());
					}
				}
			};
			DomGlobal.window.addEventListener("message", listener);
			DomGlobal.window.parent.postMessage(Global.JSON.stringify(message), "*");
		});
	}
}
