package com.greencross.lims.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@Accessors(fluent = true)
public class Gene {
	@Column
	private String gene;
	@Column
	private String filter;
}
