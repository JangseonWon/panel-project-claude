package com.gcgenome.lims.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "service", indexes = {
	@Index(columnList="sheet"),
	@Index(columnList="\"group\"")
})
@Data
@Accessors(fluent = true)
public class Service implements Serializable {
	@Id
	@Column(name="id", length=8)
	private String id;
	@Column(name="name", length=64)
	private String name;
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sheet", referencedColumnName="id")
	private WorklistTemplate sheet;
	@ManyToOne
	@JoinColumn(name="\"group\"", referencedColumnName="id")
	private ServiceGroup group;
}
