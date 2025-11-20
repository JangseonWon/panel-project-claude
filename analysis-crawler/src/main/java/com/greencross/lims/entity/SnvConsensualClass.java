package com.greencross.lims.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(schema="panel", name = "snv_consensual_class")
@Data
@Accessors(fluent = true)
public class SnvConsensualClass {
	@EmbeddedId
	private SnvConsensualClassPK pk;
	@Column(name="snv", insertable=false, updatable=false)
	private String snv;
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
