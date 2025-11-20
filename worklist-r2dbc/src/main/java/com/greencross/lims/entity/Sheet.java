package com.greencross.lims.entity;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Value
@Accessors(fluent = true)
@Table("sheet")
@RequiredArgsConstructor
public class Sheet {
	@Id
	private final UUID id;
	private String order;
	private String name;
	@Column("fixed_columns_left")
	private Integer fixedColumnsLeft;
	@Column("page_size")
	private Integer pageSize;
	@Column("order_column")
	private String orderColumn;
	@Column("asc")
	private Boolean asc;
	private Boolean active = true;
}
