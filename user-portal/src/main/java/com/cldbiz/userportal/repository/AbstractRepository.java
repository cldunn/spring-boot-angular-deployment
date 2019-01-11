package com.cldbiz.userportal.repository;

import java.util.List;

import com.querydsl.core.types.OrderSpecifier;

public interface AbstractRepository<T, D, ID> {

	public abstract List<T> findAll();
	
	public abstract List<T> findAllById(List<ID> ids);
	
	public abstract List<T> findByDto(D dto);
	
	public abstract List<T> findPageByDto(D dto);
	
	public abstract Long countSearchByDto(D dto);
	
	public abstract List<T> searchByDto(D dto);
	
	public abstract List<T> searchPageByDto(D dto);
	
	public abstract OrderSpecifier[] sortBy(D dto);
}
