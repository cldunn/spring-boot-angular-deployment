package com.cldbiz.userportal.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;

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

public class AbstractRepositoryImpl<T, ID extends Serializable> extends QuerydslJpaRepository<T, ID> implements AbstractRepository<T, ID> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRepositoryImpl.class);

    protected final Class<T> tClass;
    
	@PersistenceContext
	protected EntityManager entityManager;
	
	protected QuerydslJpaRepository<T, ID> repository;
	
    public AbstractRepositoryImpl(JpaEntityInformation<T, ID> entityMetadata, EntityManager entityManager) {
        super(entityMetadata, entityManager);
        
        this.tClass = entityMetadata.getJavaType();
    	
        JpaEntityInformation<T, ID> entityInfo = new JpaMetamodelEntityInformation<T, ID>(this.tClass, entityManager.getMetamodel());
		repository = new QuerydslJpaRepository<T, ID>(entityInfo, entityManager);
    }

    @Override
    public void deleteById(ID id) {
    	repository.deleteById(id);
    }
    
    @Override
    public void deleteByIds(Iterable<ID> ids) {
    	List<T> entities = repository.findAllById(ids);
    	repository.deleteInBatch(entities);
    }
    
    @Override
    public void delete(T entity) {
    	repository.delete(entity);
    }
    
    @Override
    public void deleteAll(Iterable<? extends T> entities) {
    	repository.deleteAll(entities);
    }

    @Override
	public <S extends T> S save(S entity) {
    	return repository.save(entity);
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
    	return repository.saveAndFlush(entity);
    }
    
    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
    	return repository.saveAll(entities);
    }

	@Override
	public void flush() {
		repository.flush();
	}

	@Override
	public boolean existsById(ID id) { 
		return repository.existsById(id);
	}
	
    @Override
	public Optional<T> findById(ID id) {
		return repository.findById(id);
	}

    @Override
	public List<T> findAllById(Iterable<ID> ids) {
    	return repository.findAllById(ids);
    }
    
	@Override
    public  long count() {
    	return repository.count();
    }

	@Override
	public List<T> findAll() {
		return repository.findAll();
	}
}