package com.cldbiz.userportal.repository.account;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.repository.BaseRepository;

@Repository
public interface AccountRepository extends BaseRepository<Account, Long>, AccountRepositoryExt {
	
}
