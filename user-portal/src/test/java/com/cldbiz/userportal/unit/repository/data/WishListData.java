package com.cldbiz.userportal.unit.repository.data;

import com.cldbiz.userportal.domain.WishList;

public class WishListData {

	public static WishList getAnotherWishList() {
		WishList wishList = new WishList();
		
		wishList.setName("Monthly");
		
		return wishList;
	}
	
	public static WishList getExtraWishList() {
		WishList wishList = new WishList();
		
		wishList.setName("Technology");
		
		return wishList;
	}
}
