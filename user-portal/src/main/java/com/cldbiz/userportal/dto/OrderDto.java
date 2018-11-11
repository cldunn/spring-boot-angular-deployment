package com.cldbiz.userportal.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Max;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.domain.Order;

import lombok.Data;

public @Data class OrderDto extends AbstractDto {
	
	@Max(255)
	private String orderIdentifier;

	private LocalDateTime purchaseDttm;

	private Boolean invoiced;
	
	@Max(255)
	private String status;
	
	private AccountDto accountDto;
	
	private LineItemDto lineItemDto;
	
	private List<LineItemDto> lineItemDtos;
	
	public OrderDto() {
		super();
	}
	
	public OrderDto(Order order) {
		super(order);
		
		this.setOrderIdentifier(order.getOrderIdentifier());
		this.setPurchaseDttm(order.getPurchaseDttm());
		this.setInvoiced(order.getInvoiced());
		this.setStatus(order.getStatus());
	}

}
