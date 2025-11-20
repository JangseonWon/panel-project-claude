package com.greencross.lims.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Entity
@DiscriminatorValue("ANALYSIS-471a0005-6fef-4bcd-88fd-60b37b52d94a")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class AnalysisDna extends Analysis<AnalysisDna> {
	public static final UUID _ID = UUID.fromString("2ce776da-ea3b-4064-b5cd-152ef8fc5892");
	@Setter(AccessLevel.NONE)
	@JoinColumns({
			@JoinColumn(name="sheet", referencedColumnName="sheet", insertable=false, updatable=false)
			, @JoinColumn(name="batch", referencedColumnName="batch", insertable=false, updatable=false)
	})@ManyToOne(fetch = FetchType.LAZY)
	private Batch<?> batch;
	protected AnalysisDna(){}
	public AnalysisDna(AnalysisDnaPK pk) {
		super(pk);
	}
	@Embeddable
	public static class AnalysisDnaPK extends AnalysisPK {
		protected AnalysisDnaPK(){}
		@Builder
		public AnalysisDnaPK(Integer batch, Integer row) {
			super(BatchDna._ID, batch, row);
		}
	}
	@PostPersist
	@PostUpdate
	protected void synchronizeWorkCode() {
		// System.out.println("A");
	}
}
