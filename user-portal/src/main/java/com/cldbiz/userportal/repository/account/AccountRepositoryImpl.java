package com.cldbiz.userportal.repository.account;

import java.util.List;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QCustomer;
import com.cldbiz.userportal.domain.QInvoice;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.domain.QPurchaseOrder;
import com.cldbiz.userportal.domain.QContact;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.PurchaseOrderDto;
import com.cldbiz.userportal.dto.ContactDto;
import com.cldbiz.userportal.repository.BaseRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class AccountRepositoryImpl extends BaseRepositoryImpl<Account, AccountDto, Long> implements AccountRepositoryExt {

	@Override
	public List<Account> findAll() {
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.contact, contact).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.fetch();
	}

	@Override
	public List<Account> findAllById(List<Long> accountIds) {
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.contact, contact).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.where(account.id.in(accountIds))
				.fetch();
	}

	@Override
	public List<Account> findByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		builder = builder.findPredicate(account, accountDto);

		if (accountDto.getContactDto() != null) {
			DynBooleanBuilder<QContact, ContactDto> byContactBuilder = new DynBooleanBuilder<QContact, ContactDto>();
			Predicate byContactPredicate = byContactBuilder.findPredicate(account.contact, accountDto.getContactDto()).asPredicate();
			builder.and(byContactPredicate);
		}

		if (accountDto.getCustomerDto() != null) {
			DynBooleanBuilder<QCustomer, CustomerDto> byCustomerBuilder = new DynBooleanBuilder<QCustomer, CustomerDto>();
			Predicate byCustomerPredicate = byCustomerBuilder.findPredicate(account.customer, accountDto.getCustomerDto()).asPredicate();
			builder.and(byCustomerPredicate);
		}

		/*  Marginal benefit because purchaseOrder has account, could just find purchaseOrders and filter accounts */
		if (accountDto.getPurchaseOrderDto() != null) {
			DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto> byPurchaseOrderBuilder = new DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto>();
			Predicate byPurchaseOrderPredicate = byPurchaseOrderBuilder.findPredicate(account.purchaseOrders.any(), accountDto.getPurchaseOrderDto()).asPredicate();
			builder.and(byPurchaseOrderPredicate);
		}

		/* Marginal benefit because invoice has account, could just find invoices and filter accounts */
		if (accountDto.getInvoiceDto() != null) {
			DynBooleanBuilder<QInvoice, InvoiceDto> byInvoiceBuilder = new DynBooleanBuilder<QInvoice, InvoiceDto>();
			Predicate byInvoicePredicate = byInvoiceBuilder.findPredicate(account.invoices.any(), accountDto.getInvoiceDto()).asPredicate();
			builder.and(byInvoicePredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.contact, contact).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Account> findPageByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		builder = builder.findPredicate(account, accountDto);

		if (accountDto.getCustomerDto() != null) {
			DynBooleanBuilder<QCustomer, CustomerDto> byCustomerBuilder = new DynBooleanBuilder<QCustomer, CustomerDto>();
			Predicate byCustomerPredicate = byCustomerBuilder.findPredicate(account.customer, accountDto.getCustomerDto()).asPredicate();
			builder.and(byCustomerPredicate);
		}

		if (accountDto.getContactDto() != null) {
			DynBooleanBuilder<QContact, ContactDto> byContactBuilder = new DynBooleanBuilder<QContact, ContactDto>();
			Predicate byContactPredicate = byContactBuilder.findPredicate(account.contact, accountDto.getContactDto()).asPredicate();
			builder.and(byContactPredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.contact, contact).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(accountDto))
				.offset(accountDto.getStart().intValue())
				.limit(accountDto.getLimit().intValue())
				.fetch();
	}

	public Long countSearchByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;

		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		builder = builder.searchPredicate(account, accountDto);

		if (accountDto.getCustomerDto() != null) {
			DynBooleanBuilder<QCustomer, CustomerDto> byCustomerBuilder = new DynBooleanBuilder<QCustomer, CustomerDto>();
			Predicate byCustomerPredicate = byCustomerBuilder.searchPredicate(account.customer, accountDto.getCustomerDto()).asPredicate();
			builder.and(byCustomerPredicate);
		}

		if (accountDto.getContactDto() != null) {
			DynBooleanBuilder<QContact, ContactDto> byContactBuilder = new DynBooleanBuilder<QContact, ContactDto>();
			Predicate byContactPredicate = byContactBuilder.searchPredicate(account.contact, accountDto.getContactDto()).asPredicate();
			builder.and(byContactPredicate);
		}

		return jpaQueryFactory.selectFrom(account)
				.where(builder.asPredicate())
				.fetchCount();
	}
	
	@Override
	public List<Account> searchByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		builder = builder.searchPredicate(account, accountDto);

		if (accountDto.getCustomerDto() != null) {
			DynBooleanBuilder<QCustomer, CustomerDto> byCustomerBuilder = new DynBooleanBuilder<QCustomer, CustomerDto>();
			Predicate byCustomerPredicate = byCustomerBuilder.searchPredicate(account.customer, accountDto.getCustomerDto()).asPredicate();
			builder.and(byCustomerPredicate);
		}

		if (accountDto.getContactDto() != null) {
			DynBooleanBuilder<QContact, ContactDto> byContactBuilder = new DynBooleanBuilder<QContact, ContactDto>();
			Predicate byContactPredicate = byContactBuilder.searchPredicate(account.contact, accountDto.getContactDto()).asPredicate();
			builder.and(byContactPredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.contact, contact).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Account> searchPageByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		builder = builder.searchPredicate(account, accountDto);

		if (accountDto.getCustomerDto() != null) {
			DynBooleanBuilder<QCustomer, CustomerDto> byCustomerBuilder = new DynBooleanBuilder<QCustomer, CustomerDto>();
			Predicate byCustomerPredicate = byCustomerBuilder.searchPredicate(account.customer, accountDto.getCustomerDto()).asPredicate();
			builder.and(byCustomerPredicate);
		}

		if (accountDto.getContactDto() != null) {
			DynBooleanBuilder<QContact, ContactDto> byContactBuilder = new DynBooleanBuilder<QContact, ContactDto>();
			Predicate byContactPredicate = byContactBuilder.searchPredicate(account.contact, accountDto.getContactDto()).asPredicate();
			builder.and(byContactPredicate);
		}
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.contact, contact).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(accountDto))
				.offset(accountDto.getStart().intValue())
				.limit(accountDto.getLimit().intValue())
				.fetch();
	}
	
	public OrderSpecifier[] sortBy(AccountDto accountDto) {
		PathBuilder pb = new PathBuilder<QAccount>(QAccount.class, "account");
		return sortOrderOf(pb, accountDto);
	}
}
