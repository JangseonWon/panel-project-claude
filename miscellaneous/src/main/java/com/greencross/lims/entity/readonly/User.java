package com.greencross.lims.entity.readonly;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

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
	@Column(name="password")
	private String password;
	@Column(name="key")
	private UUID key = UUID.randomUUID();
	@Column(name="department")
	private String department;
	@Column(name="email")
	private String email;
	@Column(name="serial")
	private String serial;
	public SELF id(String id) {
		this.id = id;
		return (SELF)this;
	}
	
	public SELF name(String name) {
		this.name = name;
		return (SELF)this;
	}
	
	public SELF password(String password) {
		this.password = password;
		return (SELF)this;
	}
	
	public SELF key(UUID key) {
		this.key = key;
		return (SELF)this;
	}

	public SELF department(String department) {
		this.department = department;
		return (SELF)this;
	}

	public SELF email(String email) {
		this.email = email;
		return (SELF)this;
	}
	
	public SELF serial(String serial) {
		this.serial = serial;
		return (SELF)this;
	}
}
