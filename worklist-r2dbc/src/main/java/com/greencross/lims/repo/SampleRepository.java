package com.greencross.lims.repo;

import com.greencross.lims.entity.Sample;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface SampleRepository extends ReactiveSortingRepository<Sample, Long>, ReactiveQuerydslPredicateExecutor<Sample>, QuerydslR2dbcFragment<Sample> {
}
