package com.cldbiz.userportal.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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
	private String name;
	
	// exclude bidirectional relationships from lombok caclulation of equals/hashcode, leads to stack overflow
	// @ManyToMany relationships need to be modeled as a Set for efficiency
	@EqualsAndHashCode.Exclude 
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "CATEGORY_PRODUCT", 
         joinColumns = {@JoinColumn(name = "CATEGORY_ID", foreignKey=@ForeignKey(name = "FK_PRODUCT_CATEGORY"))}, 
         inverseJoinColumns = {@JoinColumn(name = "PRODUCT_ID", foreignKey=@ForeignKey(name = "FK_CATEGORY_PRODUCT"))})
	private Set<Product> products = new HashSet<Product>();
	
	public Category() {
		super();
	}
	
	public Category(CategoryDto categoryDto) {
		super(categoryDto);
		
		this.setName(categoryDto.getName());
	}

	public void addProduct(Product product) {
    	products.add(product);
    	product.getCategories().add(this);
    }
 
    public void removeProduct(Product product) {
    	products.remove(product);
    	product.getCategories().remove(this);
    }
}
