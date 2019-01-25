package com.cldbiz.userportal.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ForeignKey;

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
	
	// exclude bidirectional relationships from lombok caclulation of equals/hashcode, leads to stack overflow
	// @ManyToMany relationships need to be modeled as a Set for efficiency
	@EqualsAndHashCode.Exclude 
	@ManyToMany(mappedBy="products", cascade = { CascadeType.PERSIST })
	private Set<Category> categories = new HashSet<Category>();
	
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
