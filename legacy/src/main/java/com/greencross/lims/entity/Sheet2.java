package com.greencross.lims.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "\"type\"")
@Table(name = "sheet2", indexes = {
		@Index(columnList="\"type\"")
		, @Index(columnList="\"order\"")
		, @Index(columnList="name")
		, @Index(columnList= "active")
})
@Data
@Accessors(fluent = true)
public class Sheet2 {
	@Id
	@Column(name="id")
	private UUID id = UUID.randomUUID();
	@Column(name="\"order\"", length=4)
	private String order;
	@Column(name="name", length=32)
	private String name;
	@Column(name="fixed_columns_left", columnDefinition = "SMALLINT")
	private Integer fixedColumnsLeft;
	@Column(name="page_size", columnDefinition = "SMALLINT")
	private Integer pageSize;
	@Column(name="order_column", length=32)
	private String orderColumn;
	@Column(name="\"asc\"")
	private Boolean asc;
	@Column(name="columns", columnDefinition="jsonb[]")
	@Type(type="com.greencross.lims.entity.udt.ColumnsConverter")
	private com.greencross.lims.dto.Sheet.ColumnDefinition[] columns;
	@Column(name="event_handlers", columnDefinition="jsonb[]")
	@Type(type="com.greencross.lims.entity.udt.EventHandlerConverter")
	private com.greencross.lims.dto.Sheet.EventHandler[] handlers;
	@Column(name="active")
	private Boolean active = true;
}
