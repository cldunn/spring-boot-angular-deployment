package com.cldbiz.userportal.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.AccountDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ACCOUNT")
@EqualsAndHashCode(callSuper=true)
public @Data class Account extends AbstractDomain {

	@Column(unique=true, nullable=false)
	private String accountName;
	
	@Column
	private String creditCard;
	
	@Column( nullable=false)
	private String billingAddress;
	
	@Column(nullable=false)
	private String shippingAddress;

	@Column(nullable=false)
	private Boolean active;
	
	@OneToOne
	@JoinColumn(name="TERM_ID")
	private Term term;
	
	@OneToOne(mappedBy="account", cascade={CascadeType.ALL}, optional=false, orphanRemoval=true)
	private Customer customer;

	@OneToMany(mappedBy="account", cascade={CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval=true)
	private List<Order> orders;
	
	@OneToMany(mappedBy="account", cascade={CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval=true)
	private List<Invoice> invoices;
	
	public Account() {
		super();
	}
	
	public Account(AccountDto accountDto) {
		super(accountDto);
		
		this.setAccountName(accountDto.getAccountName());
		this.setCreditCard(accountDto.getCreditCard());
		this.setBillingAddress(accountDto.getBillingAddress());
		this.setShippingAddress(accountDto.getShippingAddress());
	}

}
