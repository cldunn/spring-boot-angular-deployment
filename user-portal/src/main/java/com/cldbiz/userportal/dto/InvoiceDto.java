package com.cldbiz.userportal.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.cldbiz.userportal.domain.Invoice;

import lombok.Data;

public @Data class InvoiceDto extends AbstractDto {

	@NotNull
	private String invoiceNbr;
	
	@NotNull
	private LocalDate dueDate;
	
	private AccountDto accountDto;
	
	public InvoiceDto() {
		super();
	}

	public InvoiceDto(Invoice invoice) {
		super(invoice);
		
		this.setInvoiceNbr(invoice.getInvoiceNbr());
		this.setDueDate(invoice.getDueDate());
	}
}
