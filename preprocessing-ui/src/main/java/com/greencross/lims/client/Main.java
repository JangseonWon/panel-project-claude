package com.greencross.lims.client;

public class Main extends AbstractMain {
	@Override
	protected CollapseElement<?> collapse(String id, long sample, String service) {
		return CollapseElement2.build(sample, service);
	}

	@Override
	protected ExpandElement<?> expand(String id, long sample, String service) {
		return ExpandElement2.build(sample, service);
	}
}
