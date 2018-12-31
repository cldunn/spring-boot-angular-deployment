package com.cldbiz.userportal.dto;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.data.domain.Sort;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.Customer;
import com.cldbiz.userportal.domain.Invoice;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public @Data class AccountDto extends AbstractDto {
	
	@NotNull
	@Max(255)
	private String accountName;
		
	@CreditCardNumber
	@Max(255)
	private String creditCard;
	
	@NotNull
	@Max(255)
	private String billingAddress;
	
	@NotNull
	@Max(255)
	private String shippingAddress;
	
	@NotNull
	private Boolean active;
	
	private TermDto termDto;

	private CustomerDto customerDto;
	
	private PurchaseOrderDto purchaseOrderDto;

	private List<PurchaseOrderDto> purchaseOrderDtos;
	
	private InvoiceDto invoiceDto;
	
	private List<InvoiceDto> invoiceDtos;
	
	public AccountDto() {
		super();
		
		this.setSortOrders(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "accountName"))); 
	}
	
	public AccountDto(Account account) {
		super(account);
		
		this.setAccountName(account.getAccountName());
		this.setCreditCard(account.getCreditCard());
		this.setBillingAddress(account.getBillingAddress());
		this.setShippingAddress(account.getShippingAddress());
		
		this.setSortOrders(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "accountName"))); 
	}
}
