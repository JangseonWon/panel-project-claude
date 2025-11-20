package com.greencross.lims.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "issue", indexes = {
	@Index(columnList="sample, service"),
	@Index(columnList="sample"),
	@Index(columnList="service"),
	@Index(columnList="activated")
}) @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("(SELECT s.sheet FROM service s WHERE s.id=service)")
@Data
@Accessors(fluent = true)
public abstract class Issue implements Serializable {
	@EmbeddedId
	private Request.RequestPK pk = new Request.RequestPK();
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sample", referencedColumnName="id", insertable=false, updatable=false)
	private Sample sample;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="service", referencedColumnName="id", insertable=false, updatable=false)
	private Service service;
	@Column(name="create_time")
	private LocalDateTime createTime = LocalDateTime.now();
	@Column(name="update_time")
	private LocalDateTime updateTime;
	@Column
	private boolean activated;
	@Formula("(SELECT concat(A.sheet::char(36), LPAD(A.batch::varchar, 8, '0'), B.title) FROM analysis A, batch B WHERE A.sample=sample AND A.service=service AND A.sheet=B.sheet AND A.batch=B.batch ORDER BY create_time DESC LIMIT 1)")
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private String parent_;
	@Formula("(SELECT concat(A.sheet::char(36), LPAD(A.worklist::varchar, 8, '0'), B.title) FROM work A, worklist B WHERE A.sample=sample AND A.service=service AND A.sheet=B.sheet AND A.worklist=B.worklist ORDER BY create_time DESC LIMIT 1)")
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private String parent2_;
	@Transient
	@Setter(AccessLevel.NONE)
	private UUID sheet;
	@Transient
	@Setter(AccessLevel.NONE)
	private Integer parent;
	@Transient
	@Setter(AccessLevel.NONE)
	private String title;
	@PostLoad
	protected void initialize() {
		if(parent_ == null || parent_.isEmpty()) {
			if(parent2_ == null || parent2_.isEmpty()) return;
			sheet = UUID.fromString(parent2_.substring(0, 36));
			parent = Integer.parseInt(parent2_.substring(36, 44));
			title = parent2_.substring(44);
		} else {
			sheet = UUID.fromString(parent_.substring(0, 36));
			parent = Integer.parseInt(parent_.substring(36, 44));
			title = parent_.substring(44);
		}
	}
}
