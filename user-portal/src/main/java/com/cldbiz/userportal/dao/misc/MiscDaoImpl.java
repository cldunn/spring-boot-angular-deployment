package com.cldbiz.userportal.dao.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cldbiz.userportal.dao.BaseDaoImpl;
import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QCommission;
import com.cldbiz.userportal.domain.QContact;
import com.cldbiz.userportal.domain.QCustomer;
import com.cldbiz.userportal.domain.QInvoice;
import com.cldbiz.userportal.domain.QLineItem;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.domain.QPurchaseOrder;
import com.cldbiz.userportal.domain.QUser;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.dto.misc.AccountForCommissionedUserDto;
import com.cldbiz.userportal.dto.misc.PurchaseOrderAccountDto;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;

/**********************************************************************
 * 1 adhoc pojo without lists use projection
 * 2 adhoc pojo with list uses transformer
 * 3 adhoc pojo with multiple lists uses transformer
 * 4 to join unrelated entities you will need to use from and where.
 **********************************************************************/
 
@Component
public class MiscDaoImpl extends BaseDaoImpl implements MiscDao {
	/******************************************************************* 
	 * adhoc pojo without lists use projection
	 *******************************************************************/
	
	/* Collect account / purchaseOrder data for which a particular product is a line Item */
	public List<PurchaseOrderAccountDto> findPurchaseOrdersForProduct(ProductDto productDto) {
		QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
		QAccount account = QAccount.account;

		List<PurchaseOrderAccountDto> results =  jpaQueryFactory
				.select(Projections.bean(PurchaseOrderAccountDto.class, 
					account.accountName.as("accountName"), 
					purchaseOrder.orderIdentifier.as("orderIdentifier"), 
					purchaseOrder.purchaseDttm.as("purchaseDttm")))
				.from(purchaseOrder, account)
				.where(purchaseOrder.lineItems.any().product.id.eq(productDto.getId())
					.and(account.eq(purchaseOrder.account)))
				.fetch();

		entityManager.flush();
		
		return results;

	}

	/**************************************************************************** 
	 * 2 adhoc pojo with list uses transformer
	 ****************************************************************************/
	
	/* Collect account / purchase order and its line items for which a particular product is one of the line Items */
	public List<PurchaseOrderAccountDto> findPurchaseOrderAccountsForProduct(ProductDto productDto) {
		QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
		QAccount account = QAccount.account;
		QLineItem lineItem = QLineItem.lineItem;
		
		Map<Long, PurchaseOrderAccountDto> results = jpaQueryFactory
				.from(purchaseOrder, account, lineItem)
				.where(purchaseOrder.lineItems.any().product.id.eq(productDto.getId())
						.and(account.eq(purchaseOrder.account))
						.and(purchaseOrder.lineItems.contains(lineItem)))
				.transform(GroupBy.groupBy(purchaseOrder.id).as(Projections.bean(PurchaseOrderAccountDto.class, 
						purchaseOrder.orderIdentifier.as("orderIdentifier"), 
						purchaseOrder.account.accountName.as("accountName"), 
						GroupBy.list(lineItem).as("lineItems"))));

		
		entityManager.flush();
		
		return new ArrayList(results.values());
	}
	

	/****************************************************************************
	 * 4 to join unrelated entities you will need to use from and where. 
	 ****************************************************************************/
	
	/* Find purchase orders for which a particular user had a commission for a particular product */
	public List<PurchaseOrder> findPurchaseOrdersForUserForProduct(UserDto userDto, ProductDto productDto) {
		QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
		QCommission commission = QCommission.commission;
		QCustomer customer = QCustomer.customer;
		QLineItem lineItem = QLineItem.lineItem;
		QAccount account = QAccount.account;
		QContact contact = QContact.contact;
		QProduct product = QProduct.product;
		QUser user = QUser.user;
		
		List<PurchaseOrder> results =  jpaQueryFactory.select(purchaseOrder)
				.from(commission, purchaseOrder, lineItem, product)
				.join(purchaseOrder.account, account).fetchJoin()
				.join(account.customer, customer).fetchJoin()
				.join(account.contact, contact).fetchJoin()
				.where(commission.userEmail.eq(userDto.getEmail())
				.and(purchaseOrder.orderIdentifier.eq(commission.orderIdentifier))
				.and(purchaseOrder.lineItems.any().product.id.eq(productDto.getId())))
				.fetch();
		
		entityManager.flush();
		
		return results;

	}
	
	/******************************************************************* 
	 * 3 adhoc pojo with multiple lists uses transformer
	 * 4 to join unrelated entities you will need to use from and where.
	 *******************************************************************/
	
	/* Collect user / account info, where account has purchase order commissioned by particular user */
	public List<AccountForCommissionedUserDto> findAccountCommissionedByUser(UserDto userDto) {
		
		QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
		QCommission commission= QCommission.commission;
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QUser user = QUser.user;
		
		Map<List<?>, AccountForCommissionedUserDto> results = jpaQueryFactory
				.from(user, commission, account, purchaseOrder, invoice)
				.where(user.email.eq(userDto.getEmail())
					.and(commission.userEmail.eq(user.email))
					.and(purchaseOrder.orderIdentifier.eq(commission.orderIdentifier))
					.and(account.id.eq(purchaseOrder.account.id)))
				.leftJoin(account.invoices, invoice)
				.transform(GroupBy.groupBy(user.id, account.id).as(Projections.bean(AccountForCommissionedUserDto.class, 
					user.firstName.as("firstName"),
					user.lastName.as("lastName"),
					user.email.as("email"),
					account.accountName.as("accountName"), 
					GroupBy.list(purchaseOrder).as("purchaseOrders"),
					GroupBy.list(invoice).as("invoices"))));

		entityManager.flush();
		
		return new ArrayList(results.values());
		
	}
	
	
	/*****************************************************************************
	 * Example of accessing adhoc pojo based upon un-modeled relationship criteria
	 * adhoc uses projections and un-modeled uses from and where
	 * ***************************************************************************/
	
	/* Collect purchase order and account info for user with commissions for purchase orders  */
	
	/******************************************************************************************************
	 * Example of accessing adhoc pojo based upon un-modeled relationship criteria using groupBy and having
	 * adhoc uses projections and un-modeled uses from and where
	 * ****************************************************************************************************/
	
	/* Collect line item sums, purchase order, account info for product, commissioned by particular user */
	
	/******************************************************************************************************************
	 * Example of accessing adhoc pojo based upon un-modeled relationship criteria with sub-select in where and groupBy
	 * adhoc uses projections and un-modeled uses from and where and sub-selects use JPAExpression
	 ******************************************************************************************************************/

	/* Collect line item sums, purchase order, account info for [most recent purchase order(s) commissioned by user for product];
	   and sum [purchase order(s) line items for product] */
	
	/*******************************************************************************
	 * Example of accessing adhoc pojo with subselects in list based upon sql string
	 * adhoc uses projections but sub-select in list require sql string
	 *******************************************************************************
	
	/* Collect purchase order info, sum all line items and commissions and for all purchase orders between dates for account  */
	
}
