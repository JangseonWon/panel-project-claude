package com.greencross.lims.api;

import com.greencross.lims.dto.Window;
import elemental2.dom.DomGlobal;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class WindowApi {
	public void open(String url, String name, String feature, boolean shouldReplace) {
		Window w = new Window().url(url).name(name).feature(feature).shouldReplace(shouldReplace);
		DomGlobal.window.parent.postMessage(JSON.stringify(w), "*");
	}
}
