package com.greencross.lims.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(schema="panel", name = "analysis", indexes = {
	@Index(columnList="sheet"),
	@Index(columnList="panel"),
	@Index(columnList="sample"),
	@Index(columnList="sample, service")
}) @Data
@Accessors(fluent = true)
public class Analysis {
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
	@Column
	private String panel;
	@Column(name="serial", length=64)
	private String serial;
	@Column(name="value", columnDefinition="jsonb")
	@Type(type="com.greencross.lims.entity.udt.MapConverter2")
	private Map<String, Object> value;
	@Column(name="sample")
	private Long sample;
	@Column(name="service")
	private String service;
	protected Analysis(){}
	public Analysis(AnalysisPK pk) {
		this.pk = pk;
	}
	public Map<String, Object> value() {
		if(this.value==null) this.value = new HashMap<>();
		return this.value;
	}
	@Embeddable
	@Getter
	@EqualsAndHashCode
	@Accessors(fluent = true)
	public static class AnalysisPK implements Serializable {
		@Column(name="sheet", columnDefinition="uuid", nullable=false, updatable=false)
		private UUID sheet;
		@Column(name="batch", nullable=false, updatable=false)
		private String batch;
		@Column(name="row", nullable=false, updatable=false)
		private Integer row;
		@Column(name="request", nullable=false, updatable=false)
		private String request;
		protected AnalysisPK(){}
		public AnalysisPK(UUID sheet, String batch, Integer row, String ext) {
			this.sheet = sheet;
			this.batch = batch;
			this.row = row;
			this.request = ext;
		}
	}
}
