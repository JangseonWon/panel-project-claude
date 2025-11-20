package com.greencross.lims.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorFormula("concat(case when batch<0 then 'QUEUE-' else 'BATCH-' end, sheet::varchar)")
@Table(name = "batch", indexes = {
		@Index(columnList="sheet")
		, @Index(columnList="title")
		, @Index(columnList="create_time")
}) @Data
@ToString(exclude = "analysis")
@Accessors(fluent = true)
public abstract class Batch<SELF extends Batch<SELF>> implements Serializable, Comparable<SELF> {
	@EmbeddedId
	private BatchPK pk;
	@ManyToOne
	@JoinColumn(name="sheet", referencedColumnName="id", insertable=false, updatable=false)
	private BatchTemplate sheet;
	@Column(name="batch", insertable=false, updatable=false)
	private Integer batch;
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
	@Column(name="date_exec")
	private LocalDate dateExec;
	@Column(name="title", length=128)
	private String title;
	@Column(name="value", columnDefinition="jsonb")
	@Type(type="com.greencross.lims.entity.udt.MapConverter")
	private Map<UUID, String> value;
	@Formula("(SELECT count(*) FROM analysis a WHERE a.sheet=sheet AND a.batch=batch)")
	@Setter(AccessLevel.NONE)
	private long sampleCnt;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "batch")
	private List<Analysis<?>> analysis;

	public List<? extends Analysis<?>> analysis() {
		if(this.analysis == null) this.analysis = new LinkedList<>();
		return this.analysis;
	}
	public SELF sheet(BatchTemplate template) {
		assert template != null;
		if(pk == null) pk = new BatchPK();
		pk.sheet(template.id());
		this.sheet = template;
		return (SELF) this;
	}
	public SELF batch(int batch) {
		if(pk == null) pk = new BatchPK();
		pk.batch(batch);
		this.batch = batch;
		return (SELF) this;
	}
	public SELF analysis(List<? extends Analysis<?>> analysis) {
		this.analysis = (List<Analysis<?>>) analysis;
		return (SELF) this;
	}
	@Override
	public int compareTo(SELF other) {
		if(!pk.sheet().equals(other.pk().sheet())) return pk.sheet().compareTo(other.pk().sheet());
		else return pk.batch().compareTo(other.pk().batch());
	}

	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class BatchPK implements Serializable {
		@Column(name="sheet", columnDefinition="uuid", nullable=false, updatable=false)
		private UUID sheet;
		@Column(name="batch", nullable=false, updatable=false)
		private Integer batch;
		public BatchPK() {}
		public BatchPK(UUID sheet, Integer batch) {
			this.sheet = sheet;
			this.batch = batch;
		}
	}
}
