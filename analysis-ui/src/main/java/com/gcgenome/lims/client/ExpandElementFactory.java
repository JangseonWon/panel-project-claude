package com.gcgenome.lims.client;

import com.gcgenome.lims.client.expand.DefaultExpandElement;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExpandElementFactory {
	public ExpandElement<?> create(String id, long sample, String service) {
		return DefaultExpandElement.build(sample, service);
	}
}
