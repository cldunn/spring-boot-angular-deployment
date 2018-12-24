package com.cldbiz.userportal.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.LineItemDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "LINE_ITEM",
	indexes = { @Index(name = "IDX_LINE_ITEM_PURCHASE_ORDER_ID_FK", columnList = "PURCHASE_ORDER_ID") }
)
@EqualsAndHashCode(callSuper=true)
public @Data class LineItem extends AbstractDomain {
	
	@Column(nullable=false)
	private Long quantity;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="PRODUCT_ID", nullable = false, foreignKey=@ForeignKey(name = "FK_LINE_ITEM_PRODUCT"))
	private Product product;
	
	public LineItem() {
		super();
	}
	
	public LineItem(LineItemDto lineItemDto) {
		super(lineItemDto);
		
		this.setQuantity(lineItemDto.getQuantity());
	}
}
