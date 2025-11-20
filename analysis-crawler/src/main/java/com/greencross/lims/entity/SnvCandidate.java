package com.greencross.lims.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(schema="panel", name = "snv")
@Data
@Accessors(fluent = true)
public class SnvCandidate {
	@EmbeddedId
	private SnvPK pk;
	@Column(name="class")
	private String classification;
	@CreatedDate
	@Column(name="create_at", nullable = false, updatable = false)
	private LocalDateTime createTime = LocalDateTime.now();
	@LastModifiedDate
	@Column(name="last_modify_at")
	private LocalDateTime lastModifyAt;
	@Column(name="sample", insertable=false, updatable=false)
	@Setter(AccessLevel.NONE)
	private long sample;
	@Column(name="service", insertable=false, updatable=false)
	@Setter(AccessLevel.NONE)
	private String service;
	@Column(name="snv", insertable=false, updatable=false)
	@Setter(AccessLevel.NONE)
	private String snv;
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
