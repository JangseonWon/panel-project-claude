package com.gcgenome.lims.api;

import elemental2.dom.DomGlobal;

@FunctionalInterface
public interface Callback<T> extends com.google.gwt.core.client.Callback<T, Throwable> {
	void onSuccess(T result);
	@Override
	default void onFailure(Throwable reason) {
		DomGlobal.window.alert(reason.getMessage());
	}
}
