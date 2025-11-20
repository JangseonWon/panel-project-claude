package com.greencross.lims.dao;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CassandraRepositoryExtended<T, ID> extends CassandraRepository<T, ID> {
	<S extends T> S save(S entity, int ttl);
}
