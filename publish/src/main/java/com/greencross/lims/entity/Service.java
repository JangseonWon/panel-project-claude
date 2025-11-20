package com.greencross.lims.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Entity
@Table(schema="public", name = "service")
@Data
@Accessors(fluent = true)
public class Service implements Serializable {
	@Id
	@Column(name="id", length=8)
	private String id;
	@Column(name="name", length=64)
	private String name;
	public String name() {
		return name;
	}
}
