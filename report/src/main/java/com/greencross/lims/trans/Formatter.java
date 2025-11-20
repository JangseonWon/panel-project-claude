package com.greencross.lims.trans;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Formatter {
	public String formatSampleId(Long id) {
		if (id == null) return null;
		else {
			String cast = String.valueOf(id);
			if (cast.length() == 15) {
				String var10000 = cast.substring(0, 8);
				return var10000 + "-" + cast.substring(8, 11) + "-" + cast.substring(11);
			} else return cast;
		}
	}
}
