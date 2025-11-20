package com.greencross.lims.entity;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Accessors(fluent = true)
@Table("sample")
@AllArgsConstructor
public class Sample {
	@Id
	@With
	private final Long id;
}
