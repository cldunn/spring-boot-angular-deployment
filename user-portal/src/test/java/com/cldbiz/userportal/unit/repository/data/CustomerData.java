package com.cldbiz.userportal.unit.repository.data;

import com.cldbiz.userportal.domain.Customer;

public class CustomerData {
	
	public static Customer getAnotherCustomer() {
		Customer anotherCustomer = new Customer();
		anotherCustomer.setFirstName("Amy");
		anotherCustomer.setLastName("Winecroft");
		anotherCustomer.setWorkEmail("amy.winecroft@yahoo.com");
		anotherCustomer.setWorkPhone("(555) 777-7654");
		anotherCustomer.setCanContact(true);
		
		return anotherCustomer;
	}

	public static Customer getExtraCustomer() {
		Customer extraCustomer = new Customer();
		extraCustomer.setFirstName("Barnaby");
		extraCustomer.setLastName("Jones");
		extraCustomer.setWorkEmail("barnaby.jones@yahoo.com");
		extraCustomer.setWorkPhone("(555) 222-3333");
		extraCustomer.setCanContact(true);
		
		return extraCustomer;
	}
}
