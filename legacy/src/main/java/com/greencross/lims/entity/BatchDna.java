package com.greencross.lims.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue("BATCH-471a0005-6fef-4bcd-88fd-60b37b52d94a")
@Data
@ToString(callSuper=true, exclude = "analysis")
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class BatchDna extends Batch<BatchDna> {
	public static final UUID _ID = UUID.fromString("471a0005-6fef-4bcd-88fd-60b37b52d94a");
	@Column(name="state")
	@Enumerated(EnumType.STRING)
	private Worklist.WorklistState state = Worklist.WorklistState.CREATE;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "batch")
	@Where(clause="sheet='471a0005-6fef-4bcd-88fd-60b37b52d94a'")
	private List<AnalysisDna> analysis;
	@Override
	public List<AnalysisDna> analysis() {
		if(this.analysis == null) this.analysis = new LinkedList<>();
		return this.analysis;
	}
	@Override
	public BatchDna analysis(List<? extends Analysis<?>> analysis) {
		this.analysis = (List<AnalysisDna>) analysis;
		return this;
	}
	@Embeddable
	public static class BatchDnaPK extends BatchPK {
		protected BatchDnaPK() {}
		@Builder
		public BatchDnaPK(Integer batch) {
			super(_ID, batch);
		}
	}
}
