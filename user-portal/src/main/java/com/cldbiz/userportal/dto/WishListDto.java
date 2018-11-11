package com.cldbiz.userportal.dto;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.cldbiz.userportal.domain.WishList;

import lombok.Data;

public @Data class WishListDto extends AbstractDto {
	
	@NotNull
	@Max(255)
	private String name;
	
	private ProductDto productDto;
	
	private List<ProductDto> productDtos;
	
	public WishListDto() {
		super();
	}
	
	public WishListDto(WishList wishList) {
		super(wishList);
		
		this.setName(wishList.getName());
	}
}
