package com.greencross.lims.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(schema="public", name = "sample")
@Data
@Accessors(fluent = true)
public class Sample {
	@Id
	@Column
	private long id;	// Sample Barcode ID
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient", referencedColumnName = "id")
	private Patient patient;
	@Column(name="sample_type", length=32, columnDefinition="varchar(32)")
	private String sampleType;
	@Column(name="sample_type2", length=32, columnDefinition="varchar(32)")
	private String sampleType2;
	@Column(name="sample_type3", length=32, columnDefinition="varchar(32)")
	private String sampleType3;
	@Column(name="remark", length=64, columnDefinition="varchar(64)")
	private String remark;
	@Column(name="barcode", columnDefinition="bigint")
	private Long barcode;
	@Column(name="id_lis")
	private Long idLis;
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy="sample")
	private List<Request> requests;
	public Long id() {
		return id;
	}
	public Patient patient() {
		return patient;
	}
	public String sampleType() {
		return sampleType;
	}
	public String remark() {
		return remark;
	}
}
