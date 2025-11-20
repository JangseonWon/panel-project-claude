package com.greencross.lims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.UUID;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("ANALYSIS")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class AnalysisTemplate extends Sheet {
	@Column(name="\"order\"", length=3)
	private String order;
	
	@Override
	public AnalysisTemplate id(UUID id) {
		super.id(id);
		return this;
	}
	@Override
	public AnalysisTemplate columns(com.greencross.lims.dto.Sheet.ColumnDefinition[] columns) {
		super.columns(columns);
		return this;
	}
}
