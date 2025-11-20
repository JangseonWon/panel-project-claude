package com.greencross.lims.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
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
}
