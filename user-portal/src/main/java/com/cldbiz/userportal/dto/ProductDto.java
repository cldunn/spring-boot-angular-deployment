package com.cldbiz.userportal.dto;

import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.cldbiz.userportal.domain.Product;

import lombok.Data;

public @Data class ProductDto extends AbstractDto {

	private String upc;
	
	private String sku;
	
	@NotNull
	@Max(255)
	private String name;
	
	@NotNull
	@Min(0)
	private Double price;
	
	@NotNull
	@Max(4096)
	private String description;
	
	private CategoryDto categoryDto;
	
	private List<CategoryDto> categoryDtos;
	
	public ProductDto() {
		super();
	}
	
	public ProductDto(Product product) {
		super(product);
		
		this.setUpc(product.getUpc());
		this.setSku(product.getSku());
		this.setName(product.getName());
		this.setPrice(product.getPrice());
		this.setDescription(product.getDescription());
	}

}
