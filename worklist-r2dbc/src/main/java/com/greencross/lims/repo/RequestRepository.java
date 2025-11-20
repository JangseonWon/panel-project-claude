package com.greencross.lims.repo;

import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.RequestPK;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface RequestRepository extends ReactiveSortingRepository<Request, RequestPK>, ReactiveQuerydslPredicateExecutor<Request>, QuerydslR2dbcFragment<Request> {
}
