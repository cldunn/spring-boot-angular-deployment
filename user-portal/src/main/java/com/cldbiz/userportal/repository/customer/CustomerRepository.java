package com.cldbiz.userportal.repository.customer;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.Customer;
import com.cldbiz.userportal.repository.AbstractRepository;

@Repository
public interface CustomerRepository extends AbstractRepository<Customer, Long>, CustomerRepositoryExt {

}
