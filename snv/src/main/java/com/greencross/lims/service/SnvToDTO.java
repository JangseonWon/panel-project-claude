package com.greencross.lims.service;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class SnvToDTO {
	public Map<String, Object> map(com.greencross.lims.entity.Snv entity, Map<String, Object> variant) {
		if(entity == null) return null;
		variant.put("sample", entity.pk().sample());
		variant.put("service", entity.pk().service());
		variant.put("class", entity.classification());
		return variant;
	}
}
