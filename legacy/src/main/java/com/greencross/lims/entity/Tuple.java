package com.greencross.lims.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("serial")
@Entity
@Table(name = "tuple", indexes = {
	@Index(columnList="row")
	, @Index(columnList="sort")
})
@Data
@Accessors(fluent = true)
public class Tuple implements Serializable, Comparable<Tuple> {
	@EmbeddedId
	private TuplePK pk;
	@JoinColumns({
			@JoinColumn(name="sheet", referencedColumnName="sheet", insertable=false, updatable=false)
			, @JoinColumn(name="batch", referencedColumnName="batch", insertable=false, updatable=false)
	})@ManyToOne(fetch = FetchType.LAZY)
	private Batch batch;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="\"column\"", referencedColumnName="id", insertable=false, updatable=false)
	private TupleTemplate column;
	@Column(name="sort", insertable=false, updatable=false)
	private String sort;
	@Column(name="\"row\"")
	private Integer row;
	@Column(name="create_time")
	private LocalDateTime createTime = LocalDateTime.now();
	@Column(name="serial", length=64)
	private String serial;
	@Column(name="value", columnDefinition="jsonb")
	@Type(type="com.greencross.lims.entity.udt.MapConverter")
	private Map<UUID, String> value;

	public Tuple batch(Batch batch) {
		if(pk == null) pk = new TuplePK();
		pk.sheet(batch.sheet().id()).batch(batch.batch());
		this.batch = batch;
		return this;
	}

	public Tuple column(TupleTemplate template) {
		if(pk == null) pk = new TuplePK();
		pk.column(template.id());
		this.column = template;
		return this;
	}

	public Tuple sort(String sort) {
		if(pk == null) pk = new TuplePK();
		pk.sort(sort);
		this.sort = sort;
		return this;
	}

	@Override
	public int compareTo(Tuple other) {
		if(!pk.sheet().equals(other.pk().sheet())) return pk.sheet().compareTo(other.pk().sheet());
		else if(!pk.batch().equals(other.pk().batch())) return pk.batch().compareTo(other.pk().batch());
		else return pk.sort.compareTo(other.pk().sort());
	}
	@Embeddable
	@Data
	@Accessors(fluent= true)
	public static class TuplePK implements Serializable {
		@Column(name="sheet", columnDefinition="uuid", nullable=false, updatable=false)
		private UUID sheet;
		@Column(name="batch", nullable=false, updatable=false)
		private Integer batch;
		@Column(name="\"column\"", nullable=false, updatable=false)
		private UUID column;
		@Column(name="sort", nullable=false, updatable=false)
		private String sort;

		public TuplePK() {}

		@Builder
		public TuplePK(UUID sheet, Integer batch, UUID column, String sort) {
			this.sheet = sheet;
			this.batch = batch;
			this.column = column;
			this.sort = sort;
		}
	}
}
