package com.cldbiz.userportal.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

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

public class AbstractRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements AbstractRepository<T, ID> {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRepositoryImpl.class);

    protected final Class<T> tClass;
    
	@PersistenceContext
	protected EntityManager entityManager;
	
	protected JPAQueryFactory jpaQueryFactory;
	
	protected SimpleJpaRepository<T, ID> repository;
	
    public AbstractRepositoryImpl(JpaEntityInformation<T, ID> entityMetadata, EntityManager entityManager) {
	    super(entityMetadata, entityManager);
		
    	this.entityManager = entityManager;
    	this.tClass = entityMetadata.getJavaType();
    	
    	JpaEntityInformation<T, ID> entityInfo = new JpaMetamodelEntityInformation<T, ID>(this.tClass, entityManager.getMetamodel());
		repository = new SimpleJpaRepository<T, ID>(entityInfo, entityManager);
    	
		jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public void deleteById(ID id) {
    	repository.deleteById(id);
    }
    
    @Override
    public void deleteByIds(Iterable<ID> ids) {
    	// Avoid "where id in ids", causes FK violation when entity has child relationships as deletes are in order (parent, then children)
    	// Single delete will apply cascadeTypes
    	for(ID id: ids) {
    		repository.deleteById(id);
    	}
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
    public  long count() {
    	return repository.count();
    }
	
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

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAllById(Iterable<ID> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteInBatch(Iterable<T> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T getOne(ID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends T> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends T> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends T> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional findOne(Predicate predicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable findAll(Predicate predicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable findAll(Predicate predicate, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable findAll(Predicate predicate, OrderSpecifier... orders) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable findAll(OrderSpecifier... orders) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page findAll(Predicate predicate, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count(Predicate predicate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean exists(Predicate predicate) {
		// TODO Auto-generated method stub
		return false;
	}

}
