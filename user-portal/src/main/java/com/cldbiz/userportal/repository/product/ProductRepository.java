package com.cldbiz.userportal.repository.product;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.Product;
import com.cldbiz.userportal.repository.AbstractRepository;

@Repository
public interface ProductRepository extends AbstractRepository<Product, Long>, ProductRepositoryExt {

}
