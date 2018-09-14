package com.cldbiz.userportal.repository;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;

public class AbstractRepositoryImpl<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID> implements AbstractRepository<T, ID> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRepositoryImpl.class);

    protected final Class<T> tClass;
    
	@PersistenceContext
	protected EntityManager entityManager;
	
	protected QueryDslJpaRepository<T, ID> repository;
	
    public AbstractRepositoryImpl(JpaEntityInformation<T, ID> entityMetadata, EntityManager entityManager) {
        super(entityMetadata, entityManager);
    	this.tClass = entityMetadata.getJavaType();
    }

	@PostConstruct
	public void init() {
		JpaEntityInformation<T, ID> entityInfo = 
				new JpaMetamodelEntityInformation<T, ID>(tClass, entityManager.getMetamodel());
		
		repository = new QueryDslJpaRepository<T, ID>(entityInfo, entityManager);
		
		
	}

    @Override
    public Long deleteByIds(List<ID> ids) {
    	Long count = (long) ids.size();

    	for(ID id: ids) {
    		repository.delete(id);
    	}

        return count;
    }

}
