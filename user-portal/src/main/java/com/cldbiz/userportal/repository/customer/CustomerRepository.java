package com.cldbiz.userportal.repository.customer;

import com.cldbiz.userportal.domain.Customer;
import com.cldbiz.userportal.repository.AbstractRepository;

public interface CustomerRepository extends AbstractRepository<Customer, Long>, CustomerRepositoryExt {

}
