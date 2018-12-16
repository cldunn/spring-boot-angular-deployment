package com.cldbiz.userportal.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/* Interface appears in all descendants */

@NoRepositoryBean
public interface AbstractRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {

	public void deleteById(ID id);
	public void deleteByIds(Iterable<ID> ids);  // custom
	 
	public void delete(T entity);
	public void deleteAll(Iterable<? extends T> entities);
	 
	public <S extends T> S save(S entity);
	public <S extends T> S saveAndFlush(S entity);
	public <S extends T> List<S> saveAll(Iterable<S> entities);

	public void flush();

	public boolean existsById(ID id);
	public Optional<T> findById(ID id);
	 
	long count();
	
	public void doSql(String sqlStr, Object... parameters);
}
