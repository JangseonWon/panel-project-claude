package com.gcgenome.lims.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema="panel", name = "analysis")
@Data
@Accessors(fluent = true)
public class Analysis {
	@EmbeddedId
	@Setter(AccessLevel.PRIVATE)
	private AnalysisPK pk;
	@Column(name="batch", insertable=false, updatable=false)
	@Setter(AccessLevel.NONE)
	private String batch;
	@Column(name="row", insertable=false, updatable=false)
	@Setter(AccessLevel.NONE)
	private Integer row;
	@Column
	private String panel;
	@Formula("concat(batch, '-', lpad(row::text, 4, '0'))")
	private String sort;
	@CreatedDate
	@Column(name="create_time", nullable = false, updatable = false)
	private LocalDateTime createTime = LocalDateTime.now();
	@LastModifiedDate
	@Column(name="last_modify_at")
	private LocalDateTime lastModifyAt;
	@Column(name="serial", length=64)
	private String serial;
	@Column(name="result", length=64)
	private String result;
	@LastModifiedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="last_modify_user")
	private User<?> lastModifiedBy;
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false),
			@JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)
	})@Setter(AccessLevel.NONE)
	private Request request;
	protected Analysis(){}
	public Analysis(AnalysisPK pk) {
		this.pk = pk;
	}
	@Embeddable
	@Getter
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
