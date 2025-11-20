package com.greencross.lims.publish;

import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.User;

public interface Publisher<T> {
	Class<T> clazz();
	boolean match(Request request);
	default void preprocessing(Request request, User<?> user) {
		
	}
	default VariantReference[] variantReferences(Request request, Object interpretation) {
		return variantReferences(request, (T)interpretation, clazz());
	}
	default VariantReference[] variantReferences(Request request, T interpretation, Class<T> clazz) {
		return null;
	}

	default String mapCls(String clazz) {
		if("P".equalsIgnoreCase(clazz)) return "Pathogenic";
		if("PV".equalsIgnoreCase(clazz)) return "Pathogenic";
		if("LP".equalsIgnoreCase(clazz)) return "Likely Pathogenic";
		if("LPV".equalsIgnoreCase(clazz)) return "Likely Pathogenic";
		if("VUS".equalsIgnoreCase(clazz)) return "VUS";
		if("LB".equalsIgnoreCase(clazz)) return "Likely Benign";
		if("LBV".equalsIgnoreCase(clazz)) return "Likely Benign";
		if("B".equalsIgnoreCase(clazz)) return "Benign";
		if("BV".equalsIgnoreCase(clazz)) return "Benign";
		if("FP".equalsIgnoreCase(clazz)) return "False Positive";
		return null;
	}
}
