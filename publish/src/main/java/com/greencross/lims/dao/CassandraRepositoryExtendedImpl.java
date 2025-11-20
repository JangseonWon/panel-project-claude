package com.greencross.lims.dao;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.data.cassandra.repository.query.CassandraEntityInformation;
import org.springframework.data.cassandra.repository.support.SimpleCassandraRepository;

public class CassandraRepositoryExtendedImpl <T, ID> extends SimpleCassandraRepository<T, ID> implements CassandraRepositoryExtended<T, ID> {
	private final CassandraEntityInformation<T, ID> entityInformation;
	private final CassandraOperations operations;
	public CassandraRepositoryExtendedImpl(CassandraEntityInformation metadata, CassandraOperations operations) {
		super(metadata, operations);
		this.entityInformation = metadata;
		this.operations = operations;
	}

	@Override
	public <S extends T> S save(S entity, int ttl) {
		InsertOptions insertOptions = InsertOptions.builder().ttl(ttl).build();
		operations.insert(entity, insertOptions);
		return entity;
	}
}
