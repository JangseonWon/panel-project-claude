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
@DiscriminatorValue("BATCH-114cfcf5-2bdb-4c71-bfa2-97f54a45c1f2")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class BatchSequencing extends Batch<BatchSequencing> {
	public static final UUID _ID = UUID.fromString("114cfcf5-2bdb-4c71-bfa2-97f54a45c1f2");
	@Column(name="state")
	@Enumerated(EnumType.STRING)
	private Worklist.WorklistState state = Worklist.WorklistState.CREATE;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "batch")
	@Where(clause="sheet='114cfcf5-2bdb-4c71-bfa2-97f54a45c1f2'")
	private List<AnalysisSequencing> analysis;
	@Override
	public List<AnalysisSequencing> analysis() {
		if(this.analysis == null) this.analysis = new LinkedList<>();
		return this.analysis;
	}
	@Override
	public BatchSequencing analysis(List<? extends Analysis<?>> analysis) {
		this.analysis = (List<AnalysisSequencing>) analysis;
		return this;
	}
	@Embeddable
	public static class BatchSequencingPK extends BatchPK {
		public BatchSequencingPK() {}
		@Builder
		public BatchSequencingPK(Integer batch) {
			super(_ID, batch);
		}
	}
}
