package com.gcgenome.lims.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(schema="public", name = "request")
@Data
@Accessors(fluent = true)
public class Request implements Serializable {
	@EmbeddedId
	private RequestPK pk;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sample", referencedColumnName="id", insertable=false, updatable=false)
	private Sample sample;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="service", referencedColumnName="id", insertable=false, updatable=false)
	private Service service;
	@Column(name="date_request")
	private LocalDate dateRequest;
	@Column(name="date_start")
	private LocalDate dateStart;
	@Column(name="date_due")
	private LocalDate dateDue;
	@Column(name="date_sampling")
	private LocalDate dateSampling;
	@Column(name="tat")
	private Integer tat;
	@Column(name="sync_time")
	private LocalDateTime syncTime = LocalDateTime.now();
	@Column(name="register")
	private Boolean registered = false;
	@Column(name="cancel")
	private Boolean canceled = false;
	@Column(name="delete")
	private Boolean deleted = false;
	@Column(name="info")
	private String info;
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "request")
	@OrderBy("batch DESC")
	@Setter(AccessLevel.NONE)
	private List<Analysis> analysis;
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "request")
	@Setter(AccessLevel.NONE)
	private List<Snv> snvs;
	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class RequestPK implements Serializable {
		@Column(name="sample", nullable=false, updatable=false)
		private Long sample;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;
	}
}
