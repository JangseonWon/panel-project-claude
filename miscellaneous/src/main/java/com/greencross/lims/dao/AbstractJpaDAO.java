package com.greencross.lims.dao;

import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

public class AbstractJpaDAO<E> {
	@PersistenceContext
	private EntityManager em;
	private final Class<E> entityClass;
	
	@SuppressWarnings("unchecked")
	public AbstractJpaDAO() {
		Type daoSuperClass = getClass().getGenericSuperclass();
		entityClass = (Class<E>) (((ParameterizedType)daoSuperClass).getActualTypeArguments()[0]);
	}
	public EntityManager em() {
		return em;
	}
	public Class<E> entityClass() {
		return entityClass;
	}
	public CriteriaBuilder builder() {
		return em().getCriteriaBuilder();
	}
	public <T extends E> T merge(T entity) {
		return em().merge(entity);
	}
	public <T extends Serializable> T merge(T entity) {
		return em().merge(entity);
	}
	public void remove(Object entity) {
		em().remove(entity);
	}
	public E execute(CriteriaQuery<E> query) {
		return em().createQuery(query).getSingleResult();
	}
	
	public Stream<E> list(CriteriaQuery<E> query) {
		return em().createQuery(query).getResultStream();
	}
	public Optional<E> find(Object id) {
		return Optional.ofNullable(em().find(entityClass, id));
	}
	public boolean exists(Object id) {
		Metamodel metamodel = em().getMetamodel();
		EntityType<E> entity = metamodel.entity(entityClass);
		SingularAttribute<E, ? extends Object> declaredId = entity.getDeclaredId(id.getClass());

		CriteriaBuilder cb = builder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<E> from = cq.from(entityClass);
		cq.select(cb.count(from));
		cq.where(cb.equal(from.get(declaredId), id));
		return em().createQuery(cq).getSingleResult() > 0;
	}
	public Stream<E> all() {
		CriteriaBuilder cb = builder();
		CriteriaQuery<E> q = cb.createQuery(entityClass);
		q.from(entityClass);
		return em.createQuery(q).getResultStream();
	}
	protected static <Y, E> Path<Y> parse(Path<E> root, String attribute) {
		if(attribute.contains(".")) {
			String prefix = attribute.substring(0, attribute.indexOf("."));
			String suffix = attribute.substring(attribute.indexOf(".")+1);
			Path<?> child = root.get(prefix);
			if(root instanceof From) {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				From<?, E> from = (From)root;
				child = from.join(prefix);
			}
			return parse(child, suffix);
		} else return root.get(attribute);
	}
	protected static Predicate isNull(CriteriaBuilder cb, Root<?> c, String header) {
		Expression<String> ex = parse(c, header);
		return cb.isNull(cb.lower(ex));
	}
	protected static Predicate like(CriteriaBuilder cb, Root<?> c, String value, String header) {
		Expression<String> ex = parse(c, header);
		if(!StringUtils.isEmpty(value)) return cb.like(cb.lower(ex), "%" + value.toLowerCase() + "%");
		else return cb.isNull(ex);
	}
	protected static Predicate equal(CriteriaBuilder cb, Root<?> c, String value, String header) {
		Expression<String> ex = parse(c, header);
		if(!StringUtils.isEmpty(value)) return cb.equal(ex, value);
		else return cb.isNull(ex);
	}
	protected static Predicate equal(CriteriaBuilder cb, Root<?> c, boolean value, String header) {
		Expression<Boolean> ex = parse(c, header);
		return cb.equal(ex, value);
	}
	protected static Predicate equal(CriteriaBuilder cb, Root<?> c, Object value, String header) {
		Expression<String> ex = parse(c, header);
		if(!StringUtils.isEmpty(value)) return cb.equal(ex, value);
		else return cb.isNull(ex);
	}
	protected static <T extends Number> Predicate between(CriteriaBuilder cb, Root<?> c, T min, T max, String header) {
		return cb.and(greaterThanOrEqual(cb, c, min, header), lessThanOrEqual(cb, c, max, header));
	}
	protected static <T extends Number> Predicate greaterThanOrEqual(CriteriaBuilder cb, Root<?> c, T value, String header) {
		Expression<T> ex = parse(c, header);
		if(value != null) return cb.ge(ex, value);
		else return cb.isNotNull(ex);
	}
	protected static <T extends Number> Predicate lessThanOrEqual(CriteriaBuilder cb, Root<?> c, T value, String header) {
		Expression<T> ex = parse(c, header);
		if(value != null) return cb.le(ex, value);
		else return cb.isNotNull(ex);
	}
	protected static Predicate between(CriteriaBuilder cb, Root<?> c, LocalDate min, LocalDate max, String header) {
		return cb.and(greaterThanOrEqual(cb, c, min, header), lessThanOrEqual(cb, c, max, header));
	}
	protected static Predicate greaterThanOrEqual(CriteriaBuilder cb, Root<?> c, LocalDate value, String header) {
		Expression<LocalDate> ex = parse(c, header);
		if(value != null) return cb.greaterThanOrEqualTo(ex, value);
		else return cb.isNotNull(ex);
	}
	protected static Predicate lessThanOrEqual(CriteriaBuilder cb, Root<?> c, LocalDate value, String header) {
		Expression<LocalDate> ex = parse(c, header);
		if(value != null) return cb.lessThanOrEqualTo(ex, value);
		else return cb.isNotNull(ex);
	}
}
