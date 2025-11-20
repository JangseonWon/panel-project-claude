package com.gcgenome.lims.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(schema="public", name = "service_group") @Data
@Accessors(fluent = true)
public class ServiceGroup implements Serializable {
	@Id
	@Column(name="id", length=16)
	private String id;
	@Column(name="\"order\"", length=4)
	private String order;
	@Column(name="color", length=16)
	private String color;
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
	private List<Service> services;
}
