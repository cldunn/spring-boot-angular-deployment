package com.cldbiz.userportal.unit.repository.data;

import com.cldbiz.userportal.domain.Account;

public class AccountData {
	
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
