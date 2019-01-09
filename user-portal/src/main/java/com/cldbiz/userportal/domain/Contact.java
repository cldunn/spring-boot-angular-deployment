package com.cldbiz.userportal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.ContactDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "CONTACT")
@EqualsAndHashCode(callSuper=true)
public @Data class Contact extends AbstractDomain {

	@Column(length=80, nullable=false)
	private String name;
	
	@Column(length=80, nullable=false)
	private String email;

	@Column(length=40, nullable=false)
	private String phone;
	
	public Contact() {
		super();
	}
	
	public Contact(ContactDto contactDto) {
		super(contactDto);
		
		this.setName(contactDto.getName());
		this.setEmail(contactDto.getEmail());
		this.setPhone(contactDto.getPhone());
	}

}
