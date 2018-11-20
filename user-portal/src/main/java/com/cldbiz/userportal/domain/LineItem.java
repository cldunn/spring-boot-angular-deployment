package com.cldbiz.userportal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.LineItemDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "LINE_ITEM")
@EqualsAndHashCode(callSuper=true)
public @Data class LineItem extends AbstractDomain {
	
	@Column(nullable=false)
	private Long quantity;
	
	@ManyToOne
	@JoinColumn(name="PRODUCT_ID", foreignKey=@ForeignKey(name = "FK_LINE_ITEM_PRODUCT"))
	private Product product;
	
	public LineItem() {
		super();
	}
	
	public LineItem(LineItemDto lineItemDto) {
		super(lineItemDto);
		
		this.setQuantity(lineItemDto.getQuantity());
	}
}
