package com.cldbiz.userportal.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/* Interface appears in all descendants */

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {
// public interface AbstractRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, QuerydslPredicateExecutor {

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
