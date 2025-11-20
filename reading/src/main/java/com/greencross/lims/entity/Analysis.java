package com.greencross.lims.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
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
		@Index(columnList="sample, service"),
		@Index(columnList="sample, service, batch, row")
})
@Data
@Accessors(fluent = true)
public class Analysis {
	@EmbeddedId
	@Setter(AccessLevel.PRIVATE)
	private AnalysisPK pk;
	@ManyToOne
	@Setter(AccessLevel.NONE)
	@JoinColumn(name="sheet", referencedColumnName="id", insertable=false, updatable=false)
	private Sheet sheet;
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
	@Column(name="value", columnDefinition="jsonb")
	@Type(type="com.greencross.lims.entity.udt.MapConverter2")
	private Map<String, Object> value;
	@Column(name="result", length=64)
	private String result;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sample", referencedColumnName="id", insertable=false, updatable=false)
	private Sample sample;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="service", referencedColumnName="id", insertable=false, updatable=false)
	private Service service;
	@Formula("(CASE" +
					 " WHEN (SELECT R.delete FROM public.request R WHERE R.sample=sample AND R.service=service) THEN '검사삭제'" +
					 " WHEN (SELECT R.cancel FROM public.request R WHERE R.sample=sample AND R.service=service) THEN '검사취소'" +
					 " WHEN EXISTS (SELECT 1 FROM panel.report R WHERE R.sample=sample AND R.service=service AND R.publish_at IS NOT NULL) THEN '결과전송완료'" +
					 " WHEN EXISTS (SELECT 1 FROM panel.report R WHERE R.sample=sample AND R.service=service) THEN '판독완료'" +
					 " WHEN EXISTS (SELECT 1 FROM panel.interpretation I WHERE I.sample=sample AND I.service=service AND I.publish_at IS NOT NULL) THEN '결과전송완료'" +
					 " WHEN EXISTS (SELECT 1 FROM panel.interpretation I WHERE I.sample=sample AND I.service=service) THEN '판독 중'" +
					 " ELSE 'BI 분석완료'" +
					 " END)")
	private String state;
	@Column(name = "activity_state")
	private String activityState;
	@LastModifiedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="last_modify_user")
	private User<?> lastModifiedBy;
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false),
			@JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)
	})@Setter(AccessLevel.NONE)
	private Request requests;

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
