package com.cldbiz.userportal.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.WishListDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "WISH_LIST")
@EqualsAndHashCode(callSuper=true)
public @Data class WishList extends AbstractDomain {
	
	@Column
	private String Name;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "WISH_LIST_PRODUCT", 
         joinColumns = {@JoinColumn(name = "WISH_LIST_ID")}, 
         inverseJoinColumns = {@JoinColumn(name = "PRODUCT_ID")})
	private List<Product> products;
	
	public WishList() {
		super();
	}
	
	public WishList(WishListDto wishListDto) {
		super(wishListDto);
		
		this.setName(wishListDto.getName());
	}

}
