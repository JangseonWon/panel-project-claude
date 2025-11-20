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
@DiscriminatorValue("BATCH-7f0495b6-5ce0-498b-8c7b-6c3d78f941ad")
@Data
@ToString(callSuper=true, exclude = "analysis")
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class BatchLibrary extends Batch<BatchLibrary> {
	public static final UUID _ID = UUID.fromString("7f0495b6-5ce0-498b-8c7b-6c3d78f941ad");
	@Column(name="state")
	@Enumerated(EnumType.STRING)
	private Worklist.WorklistState state = Worklist.WorklistState.CREATE;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "batch")
	@Where(clause="sheet='7f0495b6-5ce0-498b-8c7b-6c3d78f941ad'")
	private List<AnalysisLibrary> analysis;
	public List<AnalysisLibrary> analysis() {
		if(this.analysis == null) this.analysis = new LinkedList<>();
		return this.analysis;
	}
	@Override
	public BatchLibrary analysis(List<? extends Analysis<?>> analysis) {
		this.analysis = (List<AnalysisLibrary>) analysis;
		return this;
	}
	@Embeddable
	public static class BatchLibraryPK extends BatchPK {
		protected BatchLibraryPK() {}
		@Builder
		public BatchLibraryPK(Integer batch) {
			super(_ID, batch);
		}
	}
}
