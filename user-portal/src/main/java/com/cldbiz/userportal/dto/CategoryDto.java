package com.cldbiz.userportal.dto;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.cldbiz.userportal.domain.Category;

import lombok.Data;

public @Data class CategoryDto extends AbstractDto {

	@NotNull
	@Max(255)
	private String name;
	
	private ProductDto productDto;
	
	private List<ProductDto> productDtos;
	
	public CategoryDto() {
		super();
	}
	
	public CategoryDto(Category category) {
		super(category);
		
		this.setName(category.getName());
	}

}
