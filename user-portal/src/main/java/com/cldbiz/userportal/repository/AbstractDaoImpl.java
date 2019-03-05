package com.cldbiz.userportal.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Sort;

import com.cldbiz.userportal.dao.BaseDaoImpl;
import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.WishList;
import com.cldbiz.userportal.dto.AbstractDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

public abstract class AbstractDaoImpl<T, D, ID> extends BaseDaoImpl implements AbstractDao<T, D, ID>, InitializingBean {

	// @PersistenceContext
	// protected EntityManager entityManager;
	
	// protected JPAQueryFactory jpaQueryFactory;

	@Override
	public abstract Boolean existsByDto(D dto, Predicate... predicates);
	
	@Override
	public abstract Long countByDto(D dto, Predicate... predicates);
	
	@Override
	public abstract List<T> findByIds(List<ID> ids);
	
	@Override
	public abstract List<T> findAll();
	
	@Override
	public abstract List<T> findByDto(D dto, Predicate... predicates);
	
	@Override
	public abstract List<T> findPageByDto(D dto, Predicate... predicates);
	
	@Override
	public abstract List<T> searchByDto(D dto, Predicate... predicates);
	
	@Override
	public abstract List<T> searchPageByDto(D dto, Predicate... predicates);
	
	@Override
	public abstract OrderSpecifier[] sortBy(D dto);

	public OrderSpecifier[] sortOrderOf(PathBuilder pb, AbstractDto dto) {
		List<OrderSpecifier> sortOrder = new ArrayList<OrderSpecifier>();
		
		for (Sort.Order o : dto.getSortOrders()) {
			sortOrder.add(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pb.get(o.getProperty())));
        }
		
		return sortOrder.toArray(new OrderSpecifier[sortOrder.size()]);
	}
	
	// @Override
	// public void afterPropertiesSet() {
    //		jpaQueryFactory = new JPAQueryFactory(entityManager);
	// }
}
