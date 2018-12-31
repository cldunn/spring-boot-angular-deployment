package com.cldbiz.userportal.repository.purchaseOrder;

import java.util.List;

import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QCustomer;
import com.cldbiz.userportal.domain.QInvoice;
import com.cldbiz.userportal.domain.QLineItem;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.domain.QPurchaseOrder;
import com.cldbiz.userportal.domain.QTerm;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.PurchaseOrderDto;
import com.cldbiz.userportal.repository.BaseRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class PurchaseOrderRepositoryImpl extends BaseRepositoryImpl<PurchaseOrder, PurchaseOrderDto, Long> implements PurchaseOrderRepositoryExt {

	@Override
	public List<PurchaseOrder> findAll() {
		QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(purchaseOrder)
				.innerJoin(purchaseOrder.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.fetch();
	}

	@Override
	public List<PurchaseOrder> findAllById(List<Long> purchaseOrderIds) {
		QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(purchaseOrder)
				.innerJoin(purchaseOrder.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(purchaseOrder.id.in(purchaseOrderIds))
				.fetch();
	}

	@Override
	public List<PurchaseOrder> findByDto(PurchaseOrderDto purchaseOrderDto) {
		QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto> builder = new DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto>();
		builder = builder.findPredicate(purchaseOrder, purchaseOrderDto);

		if (purchaseOrderDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.findPredicate(purchaseOrder.account, purchaseOrderDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}

		if (purchaseOrderDto.getLineItemDto() != null) {
			DynBooleanBuilder<QLineItem, LineItemDto> byLineItemBuilder = new DynBooleanBuilder<QLineItem, LineItemDto>();
			Predicate byLineItemPredicate = byLineItemBuilder.findPredicate(purchaseOrder.lineItems.any(), purchaseOrderDto.getLineItemDto()).asPredicate();
			builder.and(byLineItemPredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(purchaseOrder)
				.innerJoin(purchaseOrder.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<PurchaseOrder> findPageByDto(PurchaseOrderDto purchaseOrderDto) {
		QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;

		DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto> builder = new DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto>();
		builder = builder.findPredicate(purchaseOrder, purchaseOrderDto);

		if (purchaseOrderDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.findPredicate(purchaseOrder.account, purchaseOrderDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(purchaseOrder)
				.innerJoin(purchaseOrder.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(purchaseOrderDto))
				.offset(purchaseOrderDto.getStart().intValue())
				.limit(purchaseOrderDto.getLimit().intValue())
				.fetch();
	}

	public Long countSearchByDto(PurchaseOrderDto purchaseOrderDto) {
		QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;

		DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto> builder = new DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto>();
		builder = builder.searchPredicate(purchaseOrder, purchaseOrderDto);

		if (purchaseOrderDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.searchPredicate(purchaseOrder.account, purchaseOrderDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}

		return jpaQueryFactory.selectFrom(purchaseOrder)
				.where(builder.asPredicate())
				.fetchCount();
	}

	@Override
	public List<PurchaseOrder> searchByDto(PurchaseOrderDto purchaseOrderDto) {
		QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;

		DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto> builder = new DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto>();
		builder = builder.searchPredicate(purchaseOrder, purchaseOrderDto);

		if (purchaseOrderDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.searchPredicate(purchaseOrder.account, purchaseOrderDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(purchaseOrder)
				.innerJoin(purchaseOrder.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<PurchaseOrder> searchPageByDto(PurchaseOrderDto purchaseOrderDto) {
		QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;

		DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto> builder = new DynBooleanBuilder<QPurchaseOrder, PurchaseOrderDto>();
		builder = builder.searchPredicate(purchaseOrder, purchaseOrderDto);

		if (purchaseOrderDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.searchPredicate(purchaseOrder.account, purchaseOrderDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(purchaseOrder)
				.innerJoin(purchaseOrder.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(purchaseOrderDto))
				.offset(purchaseOrderDto.getStart().intValue())
				.limit(purchaseOrderDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public OrderSpecifier[] sortBy(PurchaseOrderDto purchaseOrderDto) {
		PathBuilder pb = new PathBuilder<QPurchaseOrder>(QPurchaseOrder.class, "purchaseOrder");
		return sortOrderOf(pb, purchaseOrderDto);
	}

}
