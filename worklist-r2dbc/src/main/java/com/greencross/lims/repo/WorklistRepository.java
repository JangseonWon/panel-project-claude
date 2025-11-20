package com.greencross.lims.repo;

import com.greencross.lims.entity.Worklist;
import com.greencross.lims.entity.WorklistPK;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.greencross.lims.entity.QWorklist.worklist;

public interface WorklistRepository extends ReactiveSortingRepository<Worklist, WorklistPK>, ReactiveQuerydslPredicateExecutor<Worklist>, QuerydslR2dbcFragment<Worklist> {
	default Mono<Integer> findLastIndex() {
		return this.query(query->query.select(worklist.worklistId.max()).from(worklist)).one();
	}
	/*
	public Integer findLastIndex() {
		CriteriaBuilder cb = builder();
		CriteriaQuery<Integer> q = cb.createQuery(Integer.class);
		Root<WorklistRareDisease> c = q.from(WorklistRareDisease.class);
		Expression<Integer> countExp = cb.max(c.get("worklist"));
		q.select(countExp);
		return em().createQuery(q).getSingleResult();
	}
	 */
	/*
	@ResponseStatus(HttpStatus.OK)
	public Flux<Sample> test() {
		return Flux.interval(Duration.ofSeconds(1))
				   .take(5)
				   .flatMap(n->repo.query(query->query.select(repo.entityProjection())
												  .from(sample).limit(n+1)).all());
	}
	 */
}
