package com.gcgenome.lims.client;

import jsinterop.base.JsPropertyMap;

public class Main extends AbstractMain {

	@Override
	protected CollapseElement<?> collapse(String id, long sample, JsPropertyMap<?> service) {
		return CollapseElement2.build(id, sample);
	}

	@Override
	protected ExpandElement<?> expand(String id, long sample, JsPropertyMap<?> service) {
		return ExpandElement2.build(id, sample);
	}
}

