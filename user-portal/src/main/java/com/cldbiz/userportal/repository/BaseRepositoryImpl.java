/*
 * Copyright 2017-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cldbiz.userportal.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

/*
 * Exposes JpaRepository - PagingAndSortingRepository - CrudRepository - QueryByExampleExecutor
 * Exposese QueryDslPredicateExecutor (ignore)
 * Exposes Ext (ignore) 
 
 void deleteById(ID id)
 void deleteById(Iterable<ID> ids)  // custom
 
 void delete(T entity)
 void deleteAll(Iterable<? extends T> entities)
 
 <S extends T> S save(S entity)
 <S extends T> S saveAndFlush(S entity)
 <S extends T> Iterable<S> saveAll(Iterable<S> entities)

 void flush()

 boolean existsById(ID id)
 Optional<T> findById(ID id)
 Iterable<T> findAllById(Iterable<ID> ids)
 
 long count()
 Iterable<T> findAll()
 */
@Slf4j
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    protected final Class<T> tClass;
    
	// @PersistenceContext
	// protected EntityManager entityManager;
	
	// protected JPAQueryFactory jpaQueryFactory;
	
	protected SimpleJpaRepository<T, ID> repository;
	
    public BaseRepositoryImpl(JpaEntityInformation<T, ID> entityMetadata, EntityManager entityManager) {
	    super(entityMetadata, entityManager);
		
    	// this.entityManager = entityManager;
    	this.tClass = entityMetadata.getJavaType();
    	
    	JpaEntityInformation<T, ID> entityInfo = new JpaMetamodelEntityInformation<T, ID>(this.tClass, entityManager.getMetamodel());
		repository = new SimpleJpaRepository<T, ID>(entityInfo, entityManager);
    	
		// jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    // Returns whether an entity with the given id exists.
    @Override
	public boolean existsById(ID id) { 
		return repository.existsById(id);
	}
	
    @Override
	public Long countAll() { 
		return repository.count();
	}

    // Retrieves an entity by its id.
    @Override
	public Optional<T> findById(ID id) {
		return repository.findById(id);
	}
	
	// Deletes the entity with the given id.
    @Override
    public void deleteById(ID id) {
    	repository.deleteById(id);
    }
    
    // Use deleteById(id) in loop, [in(ids) will cause FK violation, deleteById applies cascadeTypes] 
    @Override
    public void deleteByIds(Iterable<ID> ids) {
    	for(ID id: ids) {
    		repository.deleteById(id);
    	}
    }
    
	// Deletes a given entity.
    @Override
	public void deleteByEntity(T entity) {
    	repository.delete(entity);
    }
    
	// Deletes the given entities.
    @Override
	public void deleteByEntities(Iterable<? extends T> entities) {
		repository.deleteAll(entities);
	}

	// Saves a given entity.
    @Override
	public <S extends T> S	saveEntity(S entity) {
		return repository.save(entity);
	}

	// Saves all given entities. (saveAll)
	public <S extends T> List<S> saveEntities(Iterable<S> entities) {
		return repository.saveAll(entities);
	}

	// Flushes all pending changes to the database.
	@Override
	public void flush() {
		repository.flush();
	}
	
	/*
	@Override
	public void doSql(String sqlStr, Object... parameters) {
		entityManager.flush();
		entityManager.clear();
		
		Query query = entityManager.createQuery(sqlStr);
		for(int i = 1; i <= parameters.length; i++) {
			query.setParameter(i, parameters[i-1]);
		}

		query.executeUpdate();
	}
	*/
}
