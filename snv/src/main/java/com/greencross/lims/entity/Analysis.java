package com.greencross.lims.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(schema="panel", name = "analysis")
@Data
@Accessors(fluent = true)
public class Analysis {
	@EmbeddedId
	@Setter(AccessLevel.PRIVATE)
	private AnalysisPK pk;

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
