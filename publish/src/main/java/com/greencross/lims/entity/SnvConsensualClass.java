package com.greencross.lims.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(schema="panel", name = "snv_consensual_class")
@Data
@Accessors(fluent = true)
public class SnvConsensualClass {
	@EmbeddedId
	private SnvConsensualClassPK pk;
	@Column(name="snv", insertable=false, updatable=false)
	private String snv;
	@Column(name="create_at", insertable=false, updatable=false)
	private LocalDateTime createTime;
	@CreatedBy
	@ManyToOne
	@JoinColumn(name="create_user")
	private User<?> user;
	@LastModifiedDate
	@Column(name="last_modify_at")
	private LocalDateTime lastModifyAt;
	@LastModifiedBy
	@ManyToOne
	@JoinColumn(name="last_modify_user")
	private User<?> lastModifiedBy;
	@Column(name="class")
	private String classification;

	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class SnvConsensualClassPK implements Serializable {
		@Column(name="snv", nullable=false, updatable=false)
		private String snv;
		@Column(name="create_at", nullable=false, updatable=false)
		private LocalDateTime createTime = LocalDateTime.now();
	}
}
