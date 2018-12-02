package com.cldbiz.userportal.repository;

import java.util.List;

public interface BaseRepository<T, ID> {

	public List<T> findAll();
	
	public abstract List<T> findAllById(List<ID> ids);
	
	// public OrderSpecifier[] sortOrderOf(PathBuilder pb, Sort.Order... sortOrders);
}
