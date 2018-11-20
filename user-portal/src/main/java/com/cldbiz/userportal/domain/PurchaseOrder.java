package com.cldbiz.userportal.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cldbiz.userportal.dto.PurchaseOrderDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "PURCHASE_ORDER", uniqueConstraints = {@UniqueConstraint(columnNames = {"orderIdentifier"}, name="UQ_PURCHASE_ORDER_ORDER_IDENTIFIER")})
@EqualsAndHashCode(callSuper=true)
public @Data class PurchaseOrder extends AbstractDomain {

	@Column(nullable=false)
	private String orderIdentifier;

	@Column(nullable=false)
	private LocalDateTime purchaseDttm;
	
	@Column
	private Boolean invoiced;
	
	@Column
	private String status;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="ACCOUNT_ID", foreignKey=@ForeignKey(name = "FK_PURCHASE_ORDER_ACCOUNT"))
	private Account account;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="PURCHASE_ORDER_ID", foreignKey=@ForeignKey(name = "FK_LINE_ITEM_PURCHASE_ORDER"))
	List<LineItem> lineItems;
	
	public PurchaseOrder() {
		super();
	}
	
	public PurchaseOrder(PurchaseOrderDto purchaseOrderDto) {
		super(purchaseOrderDto);
		
		this.setOrderIdentifier(purchaseOrderDto.getOrderIdentifier());
		this.setPurchaseDttm(purchaseOrderDto.getPurchaseDttm());
		this.setInvoiced(purchaseOrderDto.getInvoiced());
		this.setStatus(purchaseOrderDto.getStatus());
	}
}
