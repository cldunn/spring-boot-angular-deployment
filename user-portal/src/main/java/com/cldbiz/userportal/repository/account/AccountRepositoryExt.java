package com.cldbiz.userportal.repository.account;

import java.util.List;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.dto.AccountDto;

public interface AccountRepositoryExt {
	
	public List<Account> findAll();
	
	public List<Account> findAllById(List<Long> accountIds);
	
	public List<Account> findByDto(AccountDto accountDto);
	
	public List<Account> searchByDto(AccountDto accountDto);

	public List<Account> findPageByDto(AccountDto accountDto);
	
	public List<Account> searchPageByDto(AccountDto accountDto);
}
