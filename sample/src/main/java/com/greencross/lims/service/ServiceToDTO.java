package com.greencross.lims.service;

import com.greencross.lims.dto.Service;
import com.greencross.lims.entity.ServiceGroup;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceToDTO {
	public Service map(com.greencross.lims.entity.Service entity) {
		if(entity == null) return null;
		Service dto = new Service().id(entity.id()).name(entity.name());
		if(entity.group()!=null) {
			ServiceGroup group = entity.group();
			dto.group(group.id()).groupColor(group.color()).order(group.order());
		}
		return dto;
	}
}
