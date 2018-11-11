package com.cldbiz.userportal.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.ProductDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "PRODUCT")
@EqualsAndHashCode(callSuper=true)
public @Data class Product extends AbstractDomain {

	@Column
	private String upc;
	
	@Column
	private String sku;
	
	@Column
	private String name;
	
	@Column
	private Double price;
	
	@Column(length=4096)
	private String description;
	
	@ManyToMany(mappedBy="products")
	private List<Category> categories;
	
	public Product() {
		super();
	}
	
	public Product(ProductDto productDto) {
		super(productDto);
		
		this.setUpc(productDto.getUpc());
		this.setSku(productDto.getSku());
		this.setName(productDto.getName());
		this.setPrice(productDto.getPrice());
		this.setDescription(productDto.getDescription());
	}

}
