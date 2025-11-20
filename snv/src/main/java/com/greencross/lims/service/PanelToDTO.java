package com.greencross.lims.service;

import com.gcgenome.lims.dto.Gene;
import com.gcgenome.lims.dto.Panel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PanelToDTO {
	public Panel map(com.greencross.lims.entity.Panel entity) {
		return new Panel().id(entity.id()).tag("panel:" + entity.id())
						  .genes(entity.genes().stream().map(PanelToDTO::map).toArray(Gene[]::new));
	}
	public Gene map(com.greencross.lims.entity.Gene entity) {
		return new Gene().id(entity.gene()).tag(entity.filter());
	}
}
