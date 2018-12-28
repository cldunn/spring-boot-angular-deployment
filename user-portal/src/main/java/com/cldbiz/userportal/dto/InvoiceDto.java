package com.cldbiz.userportal.dto;

import java.time.LocalDate;
import java.util.Arrays;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Sort;

import com.cldbiz.userportal.domain.Invoice;

import lombok.Data;

public @Data class InvoiceDto extends AbstractDto {

	@NotNull
	private String invoiceNbr;
	
	@NotNull
	private LocalDate dueDate;
	
	@NotNull
	private String status;
	
	private AccountDto accountDto;
	
	public InvoiceDto() {
		super();
		
		this.setSortOrders(Arrays.asList(new Sort.Order(Sort.Direction.DESC, "dueDate")));
	}

	public InvoiceDto(Invoice invoice) {
		super(invoice);
		
		this.setInvoiceNbr(invoice.getInvoiceNbr());
		this.setDueDate(invoice.getDueDate());
		
		this.setSortOrders(Arrays.asList(new Sort.Order(Sort.Direction.DESC, "dueDate")));
	}
}
