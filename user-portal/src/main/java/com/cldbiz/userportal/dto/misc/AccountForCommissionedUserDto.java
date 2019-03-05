package com.cldbiz.userportal.dto.misc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.cldbiz.userportal.domain.Invoice;
import com.cldbiz.userportal.domain.PurchaseOrder;

import lombok.Data;

public @Data class AccountForCommissionedUserDto {

	private String firstName;
    
    private String lastName;
    
    private String email;

    private BigDecimal rate;
    
	private String accountName;
	
	private List<PurchaseOrder> purchaseOrders;
	
	private List<Invoice> invoices;
}
