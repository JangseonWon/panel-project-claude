package com.gcgenome.lims.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings({"serial", "unchecked"})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "state")
@Table(schema="public", name = "\"user\"")
@Data
@Accessors(fluent = true)
public class User<SELF> implements Serializable {
	@Id
	@Column(name="id")
	private String id;
	@Column(name="name")
	private String name;

	public SELF id(String id) {
		this.id = id;
		return (SELF)this;
	}

	public SELF name(String name) {
		this.name = name;
		return (SELF)this;
	}
}
