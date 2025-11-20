package com.greencross.lims.client;

public class Main extends AbstractMain {
	@Override
	protected CollapseElement<?> collapse(String id, long sample, String service) {
		return CollapseElementFactory.create(id, sample, service);
	}

	@Override
	protected ExpandElement<?> expand(String id, long sample, String service) {
		return ExpandElementFactory.create(id, sample, service);
	}
}
