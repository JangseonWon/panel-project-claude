package com.greencross.lims.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Entity
@DiscriminatorValue("ANALYSIS-7f0495b6-5ce0-498b-8c7b-6c3d78f941ad")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class AnalysisLibrary extends Analysis<AnalysisLibrary> {
	public static final UUID _ID = UUID.fromString("8eda20d7-653c-437d-9923-efadb2156bfd");
	@Setter(AccessLevel.NONE)
	@JoinColumns({
			@JoinColumn(name="sheet", referencedColumnName="sheet", insertable=false, updatable=false)
			, @JoinColumn(name="batch", referencedColumnName="batch", insertable=false, updatable=false)
	})@ManyToOne(fetch = FetchType.LAZY)
	private Batch<?> batch;
	protected AnalysisLibrary(){}
	public AnalysisLibrary(AnalysisLibraryPK pk) {
		super(pk);
	}
	@Embeddable
	public static class AnalysisLibraryPK extends AnalysisPK {
		protected AnalysisLibraryPK() {}
		@Builder
		public AnalysisLibraryPK(Integer batch, Integer row) {
			super(BatchLibrary._ID, batch, row);
		}
	}
}
