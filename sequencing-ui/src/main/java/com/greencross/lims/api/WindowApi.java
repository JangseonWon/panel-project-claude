package com.greencross.lims.api;

import elemental2.dom.DomGlobal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WindowApi {
	public void open(String url, String name, String feature, boolean shouldReplace) {
		DomGlobal.window.open(url, name, feature, shouldReplace);
	}
}
