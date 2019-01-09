package com.cldbiz.userportal.repository.account;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.repository.AbstractRepository;

@Repository
public interface AccountRepository extends AbstractRepository<Account, Long>, AccountRepositoryExt {
	
}
