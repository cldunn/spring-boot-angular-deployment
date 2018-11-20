package com.cldbiz.userportal.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cldbiz.userportal.dto.AccountDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ACCOUNT",  
	indexes = { @Index(name = "IDX_ACCOUNT_NAME", columnList = "accountName") }, 
	uniqueConstraints = {@UniqueConstraint(columnNames = {"accountName"}, name="UQ_ACCOUNT_ACCOUNT_NAME")})
@EqualsAndHashCode(callSuper=true)
public @Data class Account extends AbstractDomain {

	@Column(nullable=false)
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
	@JoinColumn(name="TERM_ID", foreignKey=@ForeignKey(name = "FK_ACCOUNT_TERM"))
	private Term term;
	
	@OneToOne(mappedBy="account", cascade={CascadeType.ALL}, optional=false, orphanRemoval=true)
	private Customer customer;

	@OneToMany(mappedBy="account", cascade={CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval=true)
	private List<PurchaseOrder> purchaseOrders;
	
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