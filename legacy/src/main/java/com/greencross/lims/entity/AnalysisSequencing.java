package com.greencross.lims.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.UUID;

@Entity
@DiscriminatorValue("ANALYSIS-114cfcf5-2bdb-4c71-bfa2-97f54a45c1f2")
@Data
@ToString(callSuper=true, exclude = "batch")
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class AnalysisSequencing extends Analysis<AnalysisSequencing> {
	public static final UUID _ID = UUID.fromString("2768e26d-9044-4f93-9083-93ffbb3e5080");
	@Setter(AccessLevel.NONE)
	@JoinColumns({
			@JoinColumn(name="sheet", referencedColumnName="sheet", insertable=false, updatable=false)
			, @JoinColumn(name="batch", referencedColumnName="batch", insertable=false, updatable=false)
	})@ManyToOne(fetch = FetchType.LAZY)
	private Batch<?> batch;
	@Formula("value->>'f46136d7-7cfc-4f79-adb0-c254edd5c72a'")
	private String fileName;
	protected AnalysisSequencing(){}
	public AnalysisSequencing(AnalysisSequencingPK pk) {
		super(pk);
	}
	@Embeddable
	public static class AnalysisSequencingPK extends AnalysisPK {
		protected AnalysisSequencingPK() {}
		@Builder
		public AnalysisSequencingPK(Integer batch, Integer row) {
			super(BatchSequencing._ID, batch, row);
		}
	}
}
