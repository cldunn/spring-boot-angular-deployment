package com.cldbiz.userportal.repository.customer;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.Customer;
import com.cldbiz.userportal.repository.BaseRepository;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, Long>, CustomerRepositoryExt {

}
