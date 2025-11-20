package com.greencross.lims.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Entity
@Table(schema="public", name = "patient")
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
	public String name() {
		return name;
	}
	public String customerCode() {
		return customerCode;
	}
	public String customerName() {
		return customerName;
	}
	public String customerCode2() {
		return customerCode2;
	}
	public String customerName2() {
		return customerName2;
	}
	public String mrn() {
		return mrn;
	}
	public Sex sex() {
		if(this.sex == null) return null;
		else return Sex.valueOf(this.sex);
	}
	public Patient sex(Sex sex) {
		if(sex == null) this.sex = null;
		else this.sex = sex.name();
		return this;
	}
	public LocalDate birth() {
		return birth;
	}
	public enum Sex {
		M, F
	}
}
