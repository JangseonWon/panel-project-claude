package com.greencross.lims.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings({"serial", "unchecked"})
@Entity
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorFormula("concat(case when batch<0 then 'QUEUE_ITEM-' else 'ANALYSIS-' end, sheet::varchar)")
@Table(name = "analysis", indexes = {
		@Index(columnList="sheet")
		, @Index(columnList="sheet,batch")
		, @Index(columnList="sample")
		, @Index(columnList="serial")
		, @Index(columnList="result")
}) @NamedNativeQueries({
	@NamedNativeQuery(name="Analysis.updatePrimaryKey", query="UPDATE Analysis SET batch=:batch_new, row=:row_new, sort=:sort WHERE sheet=:sheet AND batch=:batch_old AND row=:row_old")
}) @Data
@ToString(exclude = {"lastModifyBy", "user", "requests", "batch", "sample"})
@Accessors(fluent = true)
public class Analysis<SELF extends Analysis<SELF>> implements Serializable, Comparable<SELF> {
	@EmbeddedId
	@Setter(AccessLevel.PRIVATE)
	private AnalysisPK pk;
	@Column(name="row", insertable=false, updatable=false)
	private Integer row;
	@CreatedDate
	@Column(name="create_time", nullable = false, updatable = false)
	private LocalDateTime createTime = LocalDateTime.now();
	@LastModifiedDate
	@Column(name="last_modify_at")
	private LocalDateTime lastModifyAt;
	@LastModifiedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="last_modify_user")
	private User<?> lastModifiedBy;
	@CreatedBy
	@ManyToOne
	@JoinColumn(name="\"user\"", referencedColumnName="id")
	private User<?> user;
	@Column(name="serial", length=64)
	private String serial;
	@Column(name="\"sort\"", length=8)
	private String sort;
	@Column(name="value", columnDefinition="jsonb")
	@Type(type="com.greencross.lims.entity.udt.MapConverter")
	private Map<UUID, String> value;
	@Column(name="result", length=64)
	private String result;
	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "request_analysis",
			joinColumns = {@JoinColumn(name="sheet", referencedColumnName = "sheet"), @JoinColumn(name="batch", referencedColumnName = "batch"), @JoinColumn(name="row", referencedColumnName = "row")},
			inverseJoinColumns = {@JoinColumn(name="sample"), @JoinColumn(name="service")},
			indexes={@Index(columnList="sample, service"), @Index(columnList="sheet, batch, row")})
	@OrderBy("service")
	private Set<Request> requests;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="sheet", referencedColumnName = "sheet", insertable=false, updatable=false),
			@JoinColumn(name="batch", referencedColumnName = "batch", insertable=false, updatable=false)})
	private Batch<?> batch;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sample", referencedColumnName="id")
	private Sample sample;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="service", referencedColumnName="id")
	private Service service;
	protected Analysis(){}
	protected Analysis(AnalysisPK pk) {
		this.pk = pk;
	}
	public SELF result(String result) {
		this.result = result;
		return (SELF) this;
	}
	public SELF request(Request request) {
		if(requests == null) requests = new HashSet<>();
		if(!requests.contains(request)) requests.add(request);
		if(sample == null) sample = request.sample();
		return (SELF) this;
	}
	public SELF requests(Collection<Request> requests) {
		if(requests == null) this.requests = new HashSet<>();
		else for(Request req: requests) request(req);
		return (SELF) this;
	}
	public Batch<?> batch() {
		return batch;
	}
	@Override
	public int compareTo(SELF other) {
		if(!pk.sheet().equals(other.pk().sheet())) return pk.sheet().compareTo(other.pk().sheet());
		else if(!pk.batch().equals(other.pk().batch())) return pk.batch().compareTo(other.pk().batch());
		else {
			String s1 = sort();
			String s2 = other.sort();
			if(Objects.equals(s1, s2)) return pk.row().compareTo(other.pk().row());
			else return s1.compareTo(s2);
		}
	}
	public Map<UUID, String> value() {
		if(this.value==null) this.value = new HashMap<>();
		return this.value;
	}
	@Embeddable
	@Getter
	@Accessors(fluent = true)
	public static class AnalysisPK implements Serializable {
		@Column(name="sheet", columnDefinition="uuid", nullable=false, updatable=false)
		private UUID sheet;
		@Column(name="batch", nullable=false, updatable=false)
		private Integer batch;
		@Column(name="row", nullable=false, updatable=false)
		private Integer row;
		protected AnalysisPK(){}
		public AnalysisPK(UUID sheet, Integer batch, Integer row) {
			this.sheet = sheet;
			this.batch = batch;
			this.row = row;
		}
	}
}
