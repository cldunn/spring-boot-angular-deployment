package com.cldbiz.userportal.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;

import com.querydsl.jpa.impl.JPAQueryFactory;

public class BaseRepositoryExtImpl<T> implements InitializingBean {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	protected JPAQueryFactory jpaQueryFactory;

	@Override
	public void afterPropertiesSet() {
		jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

}
