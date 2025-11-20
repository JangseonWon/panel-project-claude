package com.gcgenome.lims.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(schema="panel", name = "snv", indexes = {
	@Index(columnList = "sample,service"),
	@Index(columnList = "class")
})@Data
@Accessors(fluent = true)
public class Snv {
	@EmbeddedId
	private SnvPK pk;
	@Column(name="class")
	private String classification;
	@CreatedDate
	@Column(name="create_at", nullable = false, updatable = false)
	private LocalDateTime createTime = LocalDateTime.now();
	/*@CreatedBy
	@Column(name="create_user")
	private User<?> user;*/
	@LastModifiedDate
	@Column(name="last_modify_at")
	private LocalDateTime lastModifyAt;
	/*@LastModifiedBy
	@Column(name="last_modify_user")
	private User<?> lastModifiedBy;*/
	@Column(name="sample", insertable=false, updatable=false)
	@Setter(AccessLevel.NONE)
	private long sample;
	@Column(name="service", insertable=false, updatable=false)
	@Setter(AccessLevel.NONE)
	private String service;
	@Column(name="snv", insertable=false, updatable=false)
	@Setter(AccessLevel.NONE)
	private String snv;
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false),
			@JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)
	})@Setter(AccessLevel.NONE)
	private Request request;

	@Formula("(SELECT EXISTS(SELECT sample, service FROM panel.Interpretation I WHERE I.publish_at IS NOT NULL AND I.sample=sample AND I.service=service))")
	private boolean reported;

	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class SnvPK implements Serializable {
		@Column(name="sample", nullable=false, updatable=false)
		private Long sample;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;
		@Column(name="snv", nullable=false, updatable=false)
		private String snv;
		public SnvPK() {}

		@Builder
		public SnvPK(Long sample, String service, String snv) {
			this.sample = sample;
			this.service = service;
			this.snv = snv;
		}
	}
}
