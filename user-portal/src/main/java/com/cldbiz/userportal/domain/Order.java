package com.cldbiz.userportal.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.OrderDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ORDER")
@EqualsAndHashCode(callSuper=true)
public @Data class Order extends AbstractDomain {

	@Column(unique=true, nullable=false)
	private String orderIdentifier;

	@Column(nullable=false)
	private LocalDateTime purchaseDttm;
	
	@Column
	private Boolean invoiced;
	
	@Column
	private String status;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="ACCOUNT_ID")
	private Account account;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="ORDER_ID")
	List<LineItem> lineItems;
	
	public Order() {
		super();
	}
	
	public Order(OrderDto orderDto) {
		super(orderDto);
		
		this.setOrderIdentifier(orderDto.getOrderIdentifier());
		this.setPurchaseDttm(orderDto.getPurchaseDttm());
		this.setInvoiced(orderDto.getInvoiced());
		this.setStatus(orderDto.getStatus());
	}
}
