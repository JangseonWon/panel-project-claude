package com.greencross.lims.api;

import com.greencross.lims.dto.Progress;
import elemental2.dom.DomGlobal;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class ProgressApi {
	public void open() {
		open(true);
	}
	public void open(boolean determinate) {
		Progress p = new Progress();
		p.determinate(determinate).closed(false).progress(0.0);
		DomGlobal.window.parent.postMessage(JSON.stringify(p), "*");
	}
	public void progress(double progress) {
		Progress p = new Progress();
		p.determinate(true).closed(false).progress(progress);
		DomGlobal.window.parent.postMessage(JSON.stringify(p), "*");
	}
	public void close() {
		Progress p = new Progress();
		p.determinate(false).closed(true).progress(0.0);
		DomGlobal.window.parent.postMessage(JSON.stringify(p), "*");
	}
}
