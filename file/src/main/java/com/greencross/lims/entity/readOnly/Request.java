package com.greencross.lims.entity.readOnly;

import com.greencross.lims.entity.File;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
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
	}
}
