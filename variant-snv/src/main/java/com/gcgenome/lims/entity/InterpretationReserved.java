package com.gcgenome.lims.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(schema="panel", name = "interpretation_reserved")
@Data
@Accessors(fluent = true)
public class InterpretationReserved {
	@EmbeddedId
	private InterpretationReservedPK pk;
	@Column(name="create_at", insertable=false, updatable=false)
	private LocalDateTime createTime;
	@CreatedBy
	@ManyToOne
	@JoinColumn(name="create_user")
	private User<?> user;
	@Column
	private String interpretation;
	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class InterpretationReservedPK implements Serializable {
		@Column(name="snv", nullable=false, updatable=false)
		private String snv;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;
		@Column(name="create_at", nullable=false, updatable=false)
		private LocalDateTime createTime = LocalDateTime.now();
	}
}
