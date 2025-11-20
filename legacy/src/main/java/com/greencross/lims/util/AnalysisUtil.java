package com.greencross.lims.util;

import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Batch;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.UtilityClass;

import javax.persistence.EntityManager;
import java.util.*;

@UtilityClass
public class AnalysisUtil {
	// 변경된 순서로 Rownum을 재조정한다.
	public <T extends Analysis<T>, A extends Analysis<T>> List<A> rebuild(EntityManager em, List<A> list, Batch<?> batch) {
		int batchNew = batch.pk().batch();
		Set<AnalysisReference> references = new HashSet<>();
		Set<Change> changeSet = new HashSet<>();
		for(int i = 1; i < list.size()+1; ++i) {
			A item = list.get(i-1);
			AnalysisReference ref = AnalysisReference.builder().batch(item.pk().batch()).row(item.pk().row()).build();
			references.add(ref);
			if(item.pk().row()!=i || item.pk().batch()!=batchNew) {
				changeSet.add(Change.builder().ref(ref).sheet(item.pk().sheet())
									.batchOld(item.pk().batch()).rowOld(item.pk().row())
									.batchNew(batchNew).rowNew(i).sort(String.format("%04d", i*5)).build());
				em.detach(item);
			}
		}
		while(!changeSet.isEmpty()) {
			Iterator<Change> iter = changeSet.iterator();
			boolean hit = false;
			while(iter.hasNext()) {
				Change c1 = iter.next();
				if(references.stream().filter(c->c1.ref!=c).noneMatch(c1::chkCollision)) {
					updateAnalysisPk(em, c1);
					iter.remove();
					references.remove(c1.ref);
					hit = true;
				}
			}
			// Swap
			if(!hit) {
				iter = changeSet.iterator();
				while(iter.hasNext()) {
					Change c1 = iter.next();
					updateAnalysisPk(em, Change.builder().sheet(c1.sheet)
											   .batchOld(c1.batchOld).batchNew(c1.batchNew)
											   .rowOld(c1.rowOld).rowNew(-c1.rowOld).sort(c1.sort).build());
					c1.rowOld=-c1.rowOld;
					c1.ref.row = c1.rowOld;
				}
			}
		}
		return list;
	}
	@Builder
	private static final class AnalysisReference {
		private int batch;
		private int row;
	}
	@Data
	@Builder
	private static final class Change {
		private UUID sheet;
		private int batchOld;
		private int rowOld;
		private String sort;
		private int batchNew;
		private int rowNew;
		private AnalysisReference ref;
		private boolean chkCollision(AnalysisReference reference) {
			return batchNew == reference.batch && rowNew == reference.row;
		}
	}
	private void updateAnalysisPk(EntityManager em, Change change) {
		em.createNamedQuery("Analysis.updatePrimaryKey")
		  .setParameter("sheet", change.sheet)
		  .setParameter("batch_old", change.batchOld)
		  .setParameter("row_old", change.rowOld)
		  .setParameter("batch_new", change.batchNew)
		  .setParameter("row_new", change.rowNew)
		  .setParameter("sort", change.sort)
		  .executeUpdate();
	}
}