package com.cldbiz.userportal.dao.misc;

import java.util.List;

import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.dto.misc.AccountForCommissionedUserDto;
import com.cldbiz.userportal.dto.misc.PurchaseOrderAccountDto;

public interface MiscDao {
	public List<PurchaseOrderAccountDto> findPurchaseOrdersForProduct(ProductDto productDto);
	public List<PurchaseOrderAccountDto> findPurchaseOrderAccountsForProduct(ProductDto productDto);
	public List<PurchaseOrder> findPurchaseOrdersForUserForProduct(UserDto userDto, ProductDto productDto);
	public List<AccountForCommissionedUserDto> findAccountCommissionedByUser(UserDto userDto);
}
