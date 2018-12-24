package com.cldbiz.userportal.dto;

import java.util.Arrays;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Sort;

import com.cldbiz.userportal.domain.LineItem;

import lombok.Data;

public @Data class LineItemDto extends AbstractDto {
	
	@NotNull
	@Min(1)
	private Long quantity;

	private ProductDto productDto;
	
	public LineItemDto() {
		super();
		
		this.setSortOrders(Arrays.asList(new Sort.Order(Sort.Direction.DESC, "quantity"))); 
	}
	
	public LineItemDto(LineItem lineItem) {
		super(lineItem);
		
		this.setQuantity(lineItem.getQuantity());
		
		this.setSortOrders(Arrays.asList(new Sort.Order(Sort.Direction.DESC, "quantity"))); 
	}
}
