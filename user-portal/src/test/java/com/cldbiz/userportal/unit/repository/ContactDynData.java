package com.cldbiz.userportal.unit.repository;

import com.cldbiz.userportal.domain.Contact;

public class ContactDynData {

	public static Contact getAnotherContact() {
		Contact anotherContact = new Contact();
		
		anotherContact.setName("Bob Ford");
		anotherContact.setEmail("bob.ford@gmail.com");
		anotherContact.setPhone("(555) 783-8829");

		return anotherContact;
	}
	
	public static Contact getExtraContact() {
		Contact extraContact = new Contact();
		
		extraContact.setName("Axel Rhodes");
		extraContact.setEmail("axel.rhodes@gmail.com");
		extraContact.setPhone("(555) 883-3321");

		return extraContact;
	}


}
