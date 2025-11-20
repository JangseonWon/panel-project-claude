package com.greencross.lims.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("sheet")
@Table(name = "work", indexes = {
		@Index(columnList="sheet,worklist")
		, @Index(columnList="service")
		, @Index(columnList="user")
		, @Index(columnList="serial,sheet,worklist", unique=true)
})
@NamedQuery(name="Work.findByRequest", query="SELECT e FROM Work e WHERE e.request=:request")
@Data
@Accessors(fluent = true)
public class Work implements Serializable {
	@EmbeddedId
	private WorkPK pk;
	@Column(name="serial", length=13)
	private String serial;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false)
			, @JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)})
	private Request request;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sample", referencedColumnName="id", insertable=false, updatable=false)
	private Sample sample;
	@Column(name="service", insertable=false, updatable=false)
	private String service;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="sheet", referencedColumnName="sheet", insertable=false, updatable=false)
			, @JoinColumn(name="worklist", referencedColumnName="worklist", insertable=false, updatable=false)})
	private Worklist worklist;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sheet", referencedColumnName="id", insertable=false, updatable=false)
	private WorklistTemplate sheet;
	@ManyToOne
	@JoinColumn(name="\"user\"", referencedColumnName="id")
	private User<?> user;
	@Column(name="create_time")
	private LocalDateTime createTime = LocalDateTime.now();
	@Column(name="value", columnDefinition="jsonb")
	@Type(type="com.greencross.lims.entity.udt.MapConverter")
	private Map<UUID, String> value;

	public Work request(Request request) {
		assert request != null;
		if(pk == null) pk = new WorkPK();
		pk.sample(request.pk().sample()).service(request.pk().service());
		this.request = request;
		this.service = request.pk().service();
		return this;
	}

	public Work sheet(WorklistTemplate work) {
		assert work != null;
		if(pk == null) pk = new WorkPK();
		pk.sheet(work.id());
		this.sheet = work;
		return this;
	}

	public Work worklist(Worklist worklist) {
		assert worklist != null;
		if(pk == null) pk = new WorkPK();
		pk.sheet(worklist.pk().sheet());
		this.worklist = worklist;
		return this;
	}

	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class WorkPK implements Serializable {
		@Column(name="sample", nullable=false, updatable=false)
		private Long sample;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;
		@Column(name="sheet", nullable=false, updatable=false)
		private UUID sheet;
		@Column(name="worklist", nullable=false, updatable=false)
		private Integer worklist;
		public WorkPK() {}

		@Builder
		public WorkPK(UUID sheet, Integer worklist, Long sample, String service) {
			this.sheet = sheet;
			this.worklist = worklist;
			this.sample = sample;
			this.service = service;
		}
	}
}
