package com.cldbiz.userportal.dto;

import java.util.Arrays;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Sort;

import com.cldbiz.userportal.domain.Contact;

import lombok.Data;
import lombok.EqualsAndHashCode;

public @Data class ContactDto extends AbstractDto {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Max(80)
	private String name;
	
	@NotNull
	@Max(80)
	private String email;

	@NotNull
	@Max(40)
	private String phone;

	public ContactDto() {
		super();
		
		this.setSortOrders(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "name")));
	}
	
	public ContactDto(Contact contact) {
		super(contact);
		
		this.setName(contact.getName());
		this.setEmail(contact.getEmail());
		this.setPhone(contact.getPhone());
		
		this.setSortOrders(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "name")));
	}
}
