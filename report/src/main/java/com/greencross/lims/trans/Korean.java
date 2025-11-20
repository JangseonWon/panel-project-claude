package com.greencross.lims.trans;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Korean {
	public String 한글조사(String 주어, String 이을은와, String 가를는과) {
		char 종성 = 주어.charAt(주어.length() - 1);
		if (종성 < 0xAC00 || 종성 > 0xD7A3) return 가를는과;
		String 조사 = (종성 - 0xAC00) % 28 > 0 ? 가를는과 : 이을은와;
		return 주어 + 조사;
	}
}
