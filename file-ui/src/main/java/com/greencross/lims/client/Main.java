package com.greencross.lims.client;

public class Main extends AbstractMain {
	@Override
	protected CollapseElement<?> collapse(String id, long sample, String service) {
		return CollapseElementImpl.build(id, sample, service);
	}

	@Override
	protected ExpandElement<?> expand(String id, long sample, String service) {
		return ExpandElementImpl.build(id, sample, service);
	}
}
