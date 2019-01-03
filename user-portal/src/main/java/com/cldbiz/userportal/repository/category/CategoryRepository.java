package com.cldbiz.userportal.repository.category;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.Category;
import com.cldbiz.userportal.repository.AbstractRepository;

@Repository
public interface CategoryRepository extends AbstractRepository<Category, Long>, CategoryRepositoryExt {

}
