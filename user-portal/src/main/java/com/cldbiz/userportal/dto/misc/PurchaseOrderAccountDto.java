package com.cldbiz.userportal.dto.misc;

import java.time.LocalDateTime;
import java.util.List;

import com.cldbiz.userportal.domain.LineItem;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

public @Data class PurchaseOrderAccountDto {
	
	private String accountName;
	
	private String orderIdentifier;
	
	private LocalDateTime purchaseDttm;
	
	private List<LineItem> lineItems;
	
}
