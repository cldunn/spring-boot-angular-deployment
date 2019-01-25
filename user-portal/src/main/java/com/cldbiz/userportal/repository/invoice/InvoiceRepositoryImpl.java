package com.cldbiz.userportal.repository.invoice;

import java.util.List;

import com.cldbiz.userportal.domain.Invoice;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QCustomer;
import com.cldbiz.userportal.domain.QInvoice;
import com.cldbiz.userportal.domain.QContact;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.ContactDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.repository.AbstractRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class InvoiceRepositoryImpl extends AbstractRepositoryImpl<Invoice, InvoiceDto, Long> implements InvoiceRepositoryExt {

	@Override
	public Boolean existsByDto(InvoiceDto invoiceDto, Predicate... predicates) {
		QInvoice invoice = QInvoice.invoice;
		
		DynBooleanBuilder<QInvoice, InvoiceDto> builder = searchByCriteria(invoiceDto, predicates);
		
		return jpaQueryFactory.selectFrom(invoice).where(builder.asPredicate()).fetchCount() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	@Override
	public Long countByDto(InvoiceDto invoiceDto, Predicate... predicates) {
		QInvoice invoice = QInvoice.invoice;
		
		DynBooleanBuilder<QInvoice, InvoiceDto> builder = searchByCriteria(invoiceDto, predicates);
		
		return jpaQueryFactory.selectFrom(invoice).where(builder.asPredicate()).fetchCount();
	}

	@Override
	public List<Invoice> findByIds(List<Long> invoiceIds) {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.where(invoice.id.in(invoiceIds))
				.fetch();
	}

	@Override
	public List<Invoice> findAll() {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.fetch();
	}

	@Override
	public List<Invoice> findByDto(InvoiceDto invoiceDto, Predicate... predicates) {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QInvoice, InvoiceDto> builder = findByCriteria(invoiceDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Invoice> findPageByDto(InvoiceDto invoiceDto, Predicate... predicates) {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;

		DynBooleanBuilder<QInvoice, InvoiceDto> builder = findByCriteria(invoiceDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(invoiceDto))
				.offset(invoiceDto.getStart().intValue())
				.limit(invoiceDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public List<Invoice> searchByDto(InvoiceDto invoiceDto, Predicate... predicates) {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;

		DynBooleanBuilder<QInvoice, InvoiceDto> builder = searchByCriteria(invoiceDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Invoice> searchPageByDto(InvoiceDto invoiceDto, Predicate... predicates) {
		QInvoice invoice = QInvoice.invoice;
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QContact contact = QContact.contact;

		DynBooleanBuilder<QInvoice, InvoiceDto> builder = searchByCriteria(invoiceDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(invoice)
				.innerJoin(invoice.account, account).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(invoiceDto))
				.offset(invoiceDto.getStart().intValue())
				.limit(invoiceDto.getLimit().intValue())
				.fetch();
	}

	protected DynBooleanBuilder<QInvoice, InvoiceDto> findByCriteria(InvoiceDto invoiceDto, Predicate... predicates) {
		QInvoice invoice = QInvoice.invoice;

		DynBooleanBuilder<QInvoice, InvoiceDto> builder = new DynBooleanBuilder<QInvoice, InvoiceDto>();
		builder = builder.findPredicate(invoice, invoiceDto, predicates);

		if (invoiceDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.findPredicate(invoice.account, invoiceDto.getAccountDto(), predicates).asPredicate();
			builder.and(byAccountPredicate);
		}

		return builder;
	}
	
	protected DynBooleanBuilder<QInvoice, InvoiceDto> searchByCriteria(InvoiceDto invoiceDto, Predicate... predicates) {
		QInvoice invoice = QInvoice.invoice;

		DynBooleanBuilder<QInvoice, InvoiceDto> builder = new DynBooleanBuilder<QInvoice, InvoiceDto>();
		builder = builder.searchPredicate(invoice, invoiceDto, predicates);

		if (invoiceDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.searchPredicate(invoice.account, invoiceDto.getAccountDto(), predicates).asPredicate();
			builder.and(byAccountPredicate);
		}

		return builder;
	}

	@Override
	public OrderSpecifier[] sortBy(InvoiceDto invoiceDto) {
		PathBuilder pb = new PathBuilder<QInvoice>(QInvoice.class, "invoice");
		return sortOrderOf(pb, invoiceDto);
	}

}
