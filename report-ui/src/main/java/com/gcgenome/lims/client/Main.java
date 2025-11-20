package com.gcgenome.lims.client;

import jsinterop.base.JsPropertyMap;

public class Main extends AbstractMain {
	@Override
	protected CollapseElement<?> collapse(String id, long sample, JsPropertyMap<?> service) {
		return CollapseElementImpl.build(id, sample, (String) service.get("code"));
	}
	@Override
	protected ExpandElement<?> expand(String id, long sample, JsPropertyMap<?> service) {
		return ExpandElementImpl.build(id, sample, (String) service.get("code"));
	}
}
