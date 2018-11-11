package com.cldbiz.userportal.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.InvoiceDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "INVOICE")
@EqualsAndHashCode(callSuper=true)
public @Data class Invoice extends AbstractDomain {
	
	@Column
	private String invoiceNbr;
	
	@Column
	private LocalDate dueDate;
	
	@Column
	private String status;

	@ManyToOne(optional=false)
	@JoinColumn(name="ACCOUNT_ID")
	private Account account;
	
	public Invoice() {
		super();
	}

	public Invoice(InvoiceDto invoiceDto) {
		super(invoiceDto);
		
		this.setInvoiceNbr(invoiceDto.getInvoiceNbr());
		this.setDueDate(invoiceDto.getDueDate());
	}

}
