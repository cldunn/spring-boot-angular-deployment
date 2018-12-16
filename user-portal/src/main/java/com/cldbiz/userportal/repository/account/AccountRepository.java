package com.cldbiz.userportal.repository.account;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.repository.AbstractRepository;
import com.cldbiz.userportal.repository.term.TermRepositoryExt;

public interface AccountRepository extends AbstractRepository<Account, Long>, AccountRepositoryExt {
	
}
