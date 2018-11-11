package com.cldbiz.userportal.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.CategoryDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "CATEGORY")
@EqualsAndHashCode(callSuper=true)
public @Data class Category extends AbstractDomain {
	
	@Column
	private String Name;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "CATEGORY_PRODUCT", 
         joinColumns = {@JoinColumn(name = "CATEGORY_ID")}, 
         inverseJoinColumns = {@JoinColumn(name = "PRODUCT_ID")})
	private List<Product> products;
	
	public Category() {
		super();
	}
	
	public Category(CategoryDto categoryDto) {
		super(categoryDto);
		
		this.setName(categoryDto.getName());
	}
}
