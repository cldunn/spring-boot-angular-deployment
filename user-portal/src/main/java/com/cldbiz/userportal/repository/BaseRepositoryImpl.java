package com.cldbiz.userportal.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Sort;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.dto.AbstractDto;
import com.cldbiz.userportal.dto.AccountDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

/* TODO: Consider find/searchByDto with Predicate ... parameters, permits service to customize/extend */
public abstract class BaseRepositoryImpl<T, D, ID> implements BaseRepository<T, D, ID>, InitializingBean {

	private Class<T> type;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	protected JPAQueryFactory jpaQueryFactory;

	@Override
	public abstract List<T> findAll();
	
	@Override
	public abstract List<T> findAllById(List<ID> ids);  //findAllByIds()
	
	@Override
	public abstract List<T> findByDto(D dto);
	
	@Override
	public abstract List<T> findPageByDto(D dto);
	
	@Override
	public abstract Long countSearchByDto(D dto);

	@Override
	public abstract List<T> searchByDto(D dto);
	
	@Override
	public abstract List<T> searchPageByDto(D dto);
	
	@Override
	public abstract OrderSpecifier[] sortBy(D dto);

	// @Override
	public OrderSpecifier[] sortOrderOf(PathBuilder pb, AbstractDto dto) {
		List<OrderSpecifier> sortOrder = new ArrayList<OrderSpecifier>();
		
		for (Sort.Order o : dto.getSortOrders()) {
			sortOrder.add(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pb.get(o.getProperty())));
        }
		
		return sortOrder.toArray(new OrderSpecifier[sortOrder.size()]);
	}
	
	@Override
	public void afterPropertiesSet() {
		jpaQueryFactory = new JPAQueryFactory(entityManager);
	}
	
	
}
