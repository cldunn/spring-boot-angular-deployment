package com.cldbiz.userportal.repository.invoice;

import java.util.List;

import com.cldbiz.userportal.domain.Invoice;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QCustomer;
import com.cldbiz.userportal.domain.QInvoice;
import com.cldbiz.userportal.domain.QLineItem;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.domain.QTerm;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.repository.BaseRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class InvoiceRepositoryImpl extends BaseRepositoryImpl<Invoice, InvoiceDto, Long> implements InvoiceRepositoryExt {

	/* TODO: Remove from interface, findByDto(new *Dto) should be the same */
	@Override
	public List<Invoice> findAll() {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QTerm term = QTerm.term;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.fetch();
	}

	@Override
	public List<Invoice> findAllById(List<Long> invoiceIds) {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QTerm term = QTerm.term;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(invoice.id.in(invoiceIds))
				.fetch();
	}

	@Override
	public List<Invoice> findByDto(InvoiceDto invoiceDto) {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QTerm term = QTerm.term;
		

		DynBooleanBuilder<QInvoice, InvoiceDto> builder = new DynBooleanBuilder<QInvoice, InvoiceDto>();
		builder = builder.findPredicate(invoice, invoiceDto);

		if (invoiceDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.findPredicate(invoice.account, invoiceDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Invoice> findPageByDto(InvoiceDto invoiceDto) {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QTerm term = QTerm.term;

		DynBooleanBuilder<QInvoice, InvoiceDto> builder = new DynBooleanBuilder<QInvoice, InvoiceDto>();
		builder = builder.findPredicate(invoice, invoiceDto);

		if (invoiceDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.findPredicate(invoice.account, invoiceDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(invoiceDto))
				.offset(invoiceDto.getStart().intValue())
				.limit(invoiceDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public Long countSearchByDto(InvoiceDto invoiceDto) {
		QInvoice invoice = QInvoice.invoice;

		DynBooleanBuilder<QInvoice, InvoiceDto> builder = new DynBooleanBuilder<QInvoice, InvoiceDto>();
		builder = builder.searchPredicate(invoice, invoiceDto);

		if (invoiceDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.searchPredicate(invoice.account, invoiceDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}
		
		return jpaQueryFactory.selectFrom(invoice)
				.where(builder.asPredicate())
				.fetchCount();
	}

	@Override
	public List<Invoice> searchByDto(InvoiceDto invoiceDto) {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QTerm term = QTerm.term;

		DynBooleanBuilder<QInvoice, InvoiceDto> builder = new DynBooleanBuilder<QInvoice, InvoiceDto>();
		builder = builder.searchPredicate(invoice, invoiceDto);

		if (invoiceDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.searchPredicate(invoice.account, invoiceDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Invoice> searchPageByDto(InvoiceDto invoiceDto) {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QTerm term = QTerm.term;

		DynBooleanBuilder<QInvoice, InvoiceDto> builder = new DynBooleanBuilder<QInvoice, InvoiceDto>();
		builder = builder.searchPredicate(invoice, invoiceDto);

		if (invoiceDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.searchPredicate(invoice.account, invoiceDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(invoiceDto))
				.offset(invoiceDto.getStart().intValue())
				.limit(invoiceDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public OrderSpecifier[] sortBy(InvoiceDto invoiceDto) {
		PathBuilder pb = new PathBuilder<QInvoice>(QInvoice.class, "invoice");
		return sortOrderOf(pb, invoiceDto);
	}

}
