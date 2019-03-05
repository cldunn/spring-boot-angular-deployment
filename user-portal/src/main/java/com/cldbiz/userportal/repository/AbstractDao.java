package com.cldbiz.userportal.repository;

import java.util.List;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

public interface AbstractDao<T, D, ID> {

	public abstract Boolean existsByDto(D dto, Predicate... predicates);
	
	public abstract Long countByDto(D dto, Predicate... predicates);
	
	public abstract List<T> findByIds(List<ID> ids);
	
	public abstract List<T> findAll();
	
	public abstract List<T> findByDto(D dto, Predicate... predicates);
	
	public abstract List<T> findPageByDto(D dto, Predicate... predicates);
	
	public abstract List<T> searchByDto(D dto, Predicate... predicates);
	
	public abstract List<T> searchPageByDto(D dto, Predicate... predicates);
	
	public abstract OrderSpecifier[] sortBy(D dto);
}
