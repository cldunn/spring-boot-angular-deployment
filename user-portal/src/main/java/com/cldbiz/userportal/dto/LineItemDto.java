package com.cldbiz.userportal.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.cldbiz.userportal.domain.LineItem;

import lombok.Data;

public @Data class LineItemDto extends AbstractDto {
	
	@NotNull
	@Min(1)
	private Long quantity;

	private ProductDto productDto;
	
	public LineItemDto() {
		super();
	}
	
	public LineItemDto(LineItem lineItem) {
		super(lineItem);
		
		this.setQuantity(lineItem.getQuantity());
	}
}
