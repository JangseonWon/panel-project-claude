package com.greencross.lims.entity;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings({ "unchecked", "serial" })
@Entity
@DiscriminatorColumn(name = "\"type\"")
@Table(name = "plugin", indexes = {
	@Index(columnList="type")
	, @Index(columnList="name")
	, @Index(columnList="\"order\"")
}) @Getter
@Accessors(fluent = true)
public abstract class Plugin<SELF extends Plugin<SELF>> implements Serializable {
	@Id
	@Column(name="id")
	private UUID id;
	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "sheet_plugin", joinColumns = @JoinColumn(name = "plugin"), inverseJoinColumns = @JoinColumn(name = "sheet"), indexes={@Index(columnList="plugin"), @Index(columnList="sheet")})
	private Set<Sheet> sheets;
	@Column(name="name", length=32)
	private String name;
	@Column(name="slot")
	private int slot;
	@Column(name="exec")
	private int exec;
	@Column(name="color", length=16)
	private String color;
	@Column(name="colorBg", length=16)
	private String colorBg;

	public SELF id(UUID id) {
		this.id = id;
		return (SELF)this;
	}
	public SELF sheets(Set<Sheet> sheets) {
		this.sheets = sheets;
		return (SELF) this;
	}
	public SELF name(String name) {
		this.name = name;
		return (SELF)this;
	}
	public SELF slot(int slot) {
		this.slot = slot;
		return (SELF)this;
	}

	public SELF exec(int exec) {
		this.exec = exec;
		return (SELF)this;
	}

	public SELF color(String color) {
		this.color = color;
		return (SELF)this;
	}

	public SELF colorBg(String colorBg) {
		this.colorBg = colorBg;
		return (SELF)this;
	}
}
