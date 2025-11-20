package com.greencross.lims.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(schema="public", name = "patient", indexes = {
	@Index(columnList = "name")
	, @Index(columnList = "mrn")
	, @Index(columnList = "customer_code")
	, @Index(columnList = "customer_code2")
	, @Index(columnList = "customer_name")
	, @Index(columnList = "customer_name2")
})
@Data
@Accessors(fluent = true)
public class Patient {
	@Id
	@Column(columnDefinition="varchar(255)")
	private String id;		// Fingerprint
	@Column(columnDefinition="varchar(64)")
	private String name;
	@Column(length=64)
	private String code;
	@Column(length=1)
	private String sex;
	@Column
	private LocalDate birth;
	@Column(name="customer_code", length=5)
	private String customerCode;
	@Column(name="customer_name", length=64)
	private String customerName;
	@Column(name="customer_code2", length=5)
	private String customerCode2;
	@Column(name="customer_name2", length=64)
	private String customerName2;
	@Column(length=64)
	private String mrn;
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy="patient")
	@OrderBy("id")
	private List<Sample> samples;

	public Sex sex() {
		if(this.sex == null) return null;
		else return Sex.valueOf(this.sex);
	}
	public Patient sex(Sex sex) {
		if(sex == null) this.sex = null;
		else this.sex = sex.name();
		return this;
	}
	public enum Sex {
		M, F
	}
}
