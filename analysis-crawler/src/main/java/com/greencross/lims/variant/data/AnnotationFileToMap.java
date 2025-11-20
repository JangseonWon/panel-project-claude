package com.greencross.lims.variant.data;

import lombok.experimental.UtilityClass;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class AnnotationFileToMap {
	public Map<String, Map<String, String>> merge(Iterator<String> rows) {
		Map<String, Map<String, String>> called = new HashMap<>();
		if(!rows.hasNext()) return called;
		Map<String, Integer> header = header(rows.next());
		rows.forEachRemaining(row -> {
			Map<String, String> map = AnnotationRowToMap.map(header, row);
			String snvId = IdGenerator.snvId(map);
			called.put(snvId, map);
		});
		return called;
	}

	private Map<String, Integer> header(String line) {
		Map<String, Integer> map = new HashMap<>();
		String[] split = line.split("\t", -1);
		for (int i = 0; i < split.length; ++i) map.put(split[i].toLowerCase(), i);
		return Collections.unmodifiableMap(map);
	}
}
