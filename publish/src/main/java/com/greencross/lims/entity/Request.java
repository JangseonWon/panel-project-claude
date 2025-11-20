package com.greencross.lims.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// @DiscriminatorFormula("(SELECT s.sheet FROM service s WHERE s.id=service)")
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
	@Column(name="date_reception")
	private LocalDateTime dateReception;
	@Column(name="date_due_publish")
	private LocalDate dateDuePublish;
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
	@OrderBy("sequence ASC")
	@Setter(AccessLevel.NONE)
	private List<File> files;
	@ElementCollection
	@CollectionTable(name = "request_info", joinColumns = {@JoinColumn(name = "sample"), @JoinColumn(name = "service")}, indexes = {@Index(columnList="sample, service"), @Index(columnList="code")})
	private List<RequestInfo> customInfos;
	@Column(name="customer_dept_name", length=64)
	private String customerDeptName;
	@Column(name="ward", length=64)
	private String ward;
	@Column(name="physician", length=64)
	private String physician;

	public Request sample(Sample sample) {
		assert sample != null;
		if(pk == null) pk = new RequestPK();
		pk.sample(sample.id());
		this.sample = sample;
		return this;
	}

	public Request service(Service svc) {
		assert svc!=null;
		if(pk == null) pk = new RequestPK();
		pk.service(svc.id());
		this.service = svc;
		return this;
	}
	public RequestPK pk() {
		return pk;
	}
	public Sample sample() {
		return sample;
	}
	public Service service() {
		return service;
	}
	public LocalDate dateSampling() {
		return dateSampling;
	}
	public LocalDate dateRequest() {
		return dateRequest;
	}
	public LocalDateTime dateReception() {
		return dateReception;
	}
	public LocalDate dateDuePublish() {
		return dateDuePublish;
	}
	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class RequestPK implements Serializable {
		@Column(name="sample", nullable=false, updatable=false)
		private Long sample;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;

		public RequestPK() {}
		@Builder
		public RequestPK(Long sample, String service) {
			this.sample = sample;
			this.service = service;
		}
		public Long sample() {
			return sample;
		}
		public String service() {
			return service;
		}
	}
}
