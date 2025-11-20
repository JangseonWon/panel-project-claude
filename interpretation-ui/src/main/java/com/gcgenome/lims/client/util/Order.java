package com.gcgenome.lims.client.util;

import com.gcgenome.lims.dto.interpretation.Hereditary;
import com.gcgenome.lims.dto.interpretation.PanelTest;
import elemental2.core.JsRegExp;
import elemental2.core.RegExpResult;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Order {
	private static final JsRegExp DIGIT = new JsRegExp("(\\d+)");
	public int order(PanelTest.Variant v1, PanelTest.Variant v2) {
		if(v1 == null) {
			if(v2 == null) return 0;
			else return 1;
		} else if(v2 == null) return -1;
		int class1 = orderByClass(v1.clazz());
		int class2 = orderByClass(v2.clazz());
		if(class1!=class2) return Integer.compare(class1, class2);
		String gene1 = v1.gene();
		String gene2 = v2.gene();
		if(gene1!=null && gene2!=null && !gene1.equals(gene2)) return gene1.compareTo(gene2);
		if(gene1==null && gene2!=null) return 1;
		if(gene1!=null && gene2==null) return -1;
		String hgvsc1 = v1.hgvsc();
		String hgvsc2 = v2.hgvsc();
		if(hgvsc1 == null && hgvsc2 == null) return 0;
		if(hgvsc1 == null) return 1;
		if(hgvsc2 == null) return -1;
		RegExpResult m1 = DIGIT.exec(hgvsc1);
		RegExpResult m2 = DIGIT.exec(hgvsc2);
		if(m1!=null && m2!=null) return Long.compare(Long.parseLong(m1.getAt(1)), Long.parseLong(m2.getAt(1)));
		if(m1==null) return 1;
		return -1;
	}
	public int order(Hereditary.Variant v1, Hereditary.Variant v2) {
		if(v1 == null) {
			if(v2 == null) return 0;
			else return 1;
		} else if(v2 == null) return -1;
		int class1 = orderByClass(v1.clazz());
		int class2 = orderByClass(v2.clazz());
		if(class1!=class2) return Integer.compare(class1, class2);
		String gene1 = v1.gene();
		String gene2 = v2.gene();
		if(gene1!=null && gene2!=null && !gene1.equals(gene2)) return gene1.compareTo(gene2);
		if(gene1==null && gene2!=null) return 1;
		if(gene1!=null && gene2==null) return -1;
		String hgvsc1 = v1.hgvsc();
		String hgvsc2 = v2.hgvsc();
		if(hgvsc1 == null && hgvsc2 == null) return 0;
		if(hgvsc1 == null) return 1;
		if(hgvsc2 == null) return -1;
		RegExpResult m1 = DIGIT.exec(hgvsc1);
		RegExpResult m2 = DIGIT.exec(hgvsc2);
		if(m1!=null && m2!=null) return Long.compare(Long.parseLong(m1.getAt(1)), Long.parseLong(m2.getAt(1)));
		if(m1==null) return 1;
		return -1;
	}
	public int orderByClass(String clazz) {
		if(clazz == null) return 9;
		if("P".equalsIgnoreCase(clazz) || "PV".equals(clazz)) return 1;
		if("LP".equalsIgnoreCase(clazz) || "LPV".equals(clazz)) return 4;
		if("VUS".equalsIgnoreCase(clazz)) return 7;
		return 9;
	}
}
