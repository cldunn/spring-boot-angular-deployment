package com.cldbiz.userportal.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/* Interface appears in all descendants */

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

	// Returns whether an entity with the given id exists.
	public boolean	existsById(ID id);
	
	// Returns the number of entities available. {count)
	public Long	countAll();
	
	// Retrieves an entity by its id.  Check @ManyToOne and @OneToOne
	public Optional<T> findById(ID id);
	
	// Deletes the entity with the given id.
	public void deleteById(ID id);
	
	// Use deleteById(id) in loop, [in(ids) will cause FK violation] 
	public void deleteByIds(Iterable<ID> ids);
	
	// Deletes a given entity. (delete)
	public void deleteByEntity(T entity);
	
	// Deletes the given entities. (deleteAll)
	public void deleteByEntities(Iterable<? extends T> entities);
	
	// Saves a given entity. (save)
	public <S extends T> S	saveEntity(S entity);
	
	// Saves all given entities. (saveAll)
	public <S extends T> List<S> saveEntities(Iterable<S> entities);

	// Flushes all pending changes to the database.
	public void flush();

	public void doSql(String sqlStr, Object... parameters);
}
