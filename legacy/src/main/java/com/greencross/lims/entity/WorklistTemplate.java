package com.greencross.lims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("WORKLIST")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class WorklistTemplate extends Sheet implements Comparable<WorklistTemplate> {
	@Column(name="\"order\"", length=3)
	private String order;
	@Column(name="active")
	private Boolean active = true;
	@Column(name="values", columnDefinition="jsonb")
	@Type(type="com.greencross.lims.entity.udt.ColumnsConverter")
	private com.greencross.lims.dto.Sheet.ColumnDefinition[] values;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sheet")
	private List<Service> services;
	
	@Override
	public int compareTo(WorklistTemplate other) {
		if(order() == null) {
			if(other.order() == null) return 0;
			else return 1;
		} else return order().compareTo(other.order());
	}
}
