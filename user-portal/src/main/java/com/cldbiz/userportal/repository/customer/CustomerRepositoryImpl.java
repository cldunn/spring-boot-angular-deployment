package com.cldbiz.userportal.repository.customer;

import java.util.List;

import com.cldbiz.userportal.domain.Customer;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QCustomer;
import com.cldbiz.userportal.domain.QInvoice;
import com.cldbiz.userportal.domain.QTerm;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.repository.BaseRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class CustomerRepositoryImpl extends BaseRepositoryImpl<Customer, CustomerDto, Long> implements CustomerRepositoryExt {

	@Override
	public List<Customer> findAll() {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.fetch();
	}

	@Override
	public List<Customer> findAllById(List<Long> customerIds) {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(customer.id.in(customerIds))
				.fetch();
	}

	@Override
	public List<Customer> findByDto(CustomerDto customerDto) {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = new DynBooleanBuilder<QCustomer, CustomerDto>();
		builder = builder.findPredicate(customer, customerDto);

		if (customerDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.findPredicate(customer.account, customerDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Customer> findPageByDto(CustomerDto customerDto) {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = new DynBooleanBuilder<QCustomer, CustomerDto>();
		builder = builder.findPredicate(customer, customerDto);

		if (customerDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.findPredicate(customer.account, customerDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(customerDto))
				.offset(customerDto.getStart().intValue())
				.limit(customerDto.getLimit().intValue())
				.fetch();
	}

	public Long countSearchByDto(CustomerDto customerDto) {
		QCustomer customer = QCustomer.customer;

		DynBooleanBuilder<QCustomer, CustomerDto> builder = new DynBooleanBuilder<QCustomer, CustomerDto>();
		builder = builder.searchPredicate(customer, customerDto);

		if (customerDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.searchPredicate(customer.account, customerDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}
		
		return jpaQueryFactory.selectFrom(customer)
				.where(builder.asPredicate())
				.fetchCount();
	}
	
	@Override
	public List<Customer> searchByDto(CustomerDto customerDto) {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = new DynBooleanBuilder<QCustomer, CustomerDto>();
		builder = builder.searchPredicate(customer, customerDto);

		if (customerDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.searchPredicate(customer.account, customerDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Customer> searchPageByDto(CustomerDto customerDto) {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = new DynBooleanBuilder<QCustomer, CustomerDto>();
		builder = builder.searchPredicate(customer, customerDto);

		if (customerDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.searchPredicate(customer.account, customerDto.getAccountDto()).asPredicate();
			builder.and(byAccountPredicate);
		}
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.term, term).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(customerDto))
				.offset(customerDto.getStart().intValue())
				.limit(customerDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public OrderSpecifier[] sortBy(CustomerDto customerDto) {
		PathBuilder pb = new PathBuilder<QCustomer>(QCustomer.class, "customer");
		return sortOrderOf(pb, customerDto);
	}

}
