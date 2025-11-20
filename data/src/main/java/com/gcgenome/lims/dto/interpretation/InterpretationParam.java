package com.gcgenome.lims.dto.interpretation;

import jsinterop.annotations.*;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class InterpretationParam {
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class InterpretationParamDisease {
		@JsProperty(name="full_name")
		private String fullName;
		private String abbreviation;
		private String[] inheritance;
	}

	@JsProperty(name="previous")
	private Object previous;
	@JsProperty(name="disease")
	private Object disease;
	@JsProperty(name="suffix")
	private String suffix;
	@JsOverlay
	@JsIgnore
	public InterpretationParam append(String gene, InterpretationParamDisease disease) {
		if(this.disease == null) this.disease = new Object();
		JsPropertyMap<Object> map = Js.asPropertyMap(this.disease);
		if(map.has(gene)) {
			InterpretationParamDisease[] prev = (InterpretationParamDisease[]) map.get(gene);
			List<InterpretationParamDisease> tmp = new LinkedList<>();
			for(InterpretationParamDisease p: prev) tmp.add(p);
			tmp.add(disease);
			map.set(gene, tmp.toArray(new InterpretationParamDisease[0]));
		} else map.set(gene, new InterpretationParamDisease[] {disease});
		return this;
	}
}
