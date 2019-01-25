package com.cldbiz.userportal.unit.repository.data;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.repository.account.AccountRepository;

@Component
public class AccountData {
	
	private static AccountRepository accountRepository;
	
	@Autowired
	public AccountData(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public static Account getAnotherExistingAccount() {
		Optional<Account> account = accountRepository.findById(1L);
		return account.orElse(null);
	}

	public static Account getExtraExistingAccount() {
		Optional<Account> account = accountRepository.findById(2L);
		return account.orElse(null);
	}

	public static Account getAnotherAccount() {
		Account anotherAccount = new Account();
		anotherAccount.setAccountName("John Doe");
		anotherAccount.setCreditCard("2345678934564567");
		anotherAccount.setBillingAddress("258 Pelican Dr. Carrollton TX 23455");
		anotherAccount.setShippingAddress("258 Pelican Dr. Carrollton TX 23455");
		anotherAccount.setActive(true);
		
		 return anotherAccount;
	
	}

	public static Account getExtraAccount() {
		Account extraAccount = new Account();
		extraAccount.setAccountName("Jane Doe");
		extraAccount.setCreditCard("7777888899991111");
		extraAccount.setBillingAddress("777 Absolute Dr. Carrollton TX 23455");
		extraAccount.setShippingAddress("777 Absolute Dr. Carrollton TX 23455");
		extraAccount.setActive(true);
		
		 return extraAccount;
	}
}
