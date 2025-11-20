package com.greencross.lims.api;

import com.greencross.lims.dto.Route;
import elemental2.dom.DomGlobal;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class RouteApi {
	public void location(String location, boolean shoudUpdate, boolean shouldReplace) {
		Route r = new Route().location(location).shouldUpdate(shoudUpdate).shouldReplace(shouldReplace);
		DomGlobal.window.parent.postMessage(JSON.stringify(r), "*");
	}
}
