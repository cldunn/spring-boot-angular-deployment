package com.cldbiz.userportal.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Max;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.domain.PurchaseOrder;

import lombok.Data;

public @Data class PurchaseOrderDto extends AbstractDto {
	
	@Max(255)
	private String orderIdentifier;

	private LocalDateTime purchaseDttm;

	private Boolean invoiced;
	
	@Max(255)
	private String status;
	
	private AccountDto accountDto;
	
	private LineItemDto lineItemDto;
	
	private List<LineItemDto> lineItemDtos;
	
	public PurchaseOrderDto() {
		super();
	}
	
	public PurchaseOrderDto(PurchaseOrder purchaseOrder) {
		super(purchaseOrder);
		
		this.setOrderIdentifier(purchaseOrder.getOrderIdentifier());
		this.setPurchaseDttm(purchaseOrder.getPurchaseDttm());
		this.setInvoiced(purchaseOrder.getInvoiced());
		this.setStatus(purchaseOrder.getStatus());
	}

}
