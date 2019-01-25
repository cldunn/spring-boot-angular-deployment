package com.cldbiz.userportal.repository.customer;

import java.util.List;

import com.cldbiz.userportal.domain.Customer;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QCategory;
import com.cldbiz.userportal.domain.QCustomer;
import com.cldbiz.userportal.domain.QInvoice;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.domain.QContact;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.CategoryDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.repository.AbstractRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class CustomerRepositoryImpl extends AbstractRepositoryImpl<Customer, CustomerDto, Long> implements CustomerRepositoryExt {

	@Override
	public Boolean existsByDto(CustomerDto customerDto, Predicate... predicates) {
		QCustomer customer = QCustomer.customer;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = searchByCriteria(customerDto, predicates);
		
		return jpaQueryFactory.selectFrom(customer).where(builder.asPredicate()).fetchCount() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	@Override
	public Long countByDto(CustomerDto customerDto, Predicate... predicates) {
		QCustomer customer = QCustomer.customer;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = searchByCriteria(customerDto, predicates);
		
		return jpaQueryFactory.selectFrom(customer).where(builder.asPredicate()).fetchCount();
	}
	
	@Override
	public List<Customer> findByIds(List<Long> customerIds) {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QContact contact = QContact.contact;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.where(customer.id.in(customerIds))
				.fetch();
	}
	
	@Override
	public List<Customer> findAll() {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QContact contact = QContact.contact;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.fetch();
	}

	@Override
	public List<Customer> findByDto(CustomerDto customerDto, Predicate... predicates) {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = findByCriteria(customerDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Customer> findPageByDto(CustomerDto customerDto, Predicate... predicates) {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = findByCriteria(customerDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(customerDto))
				.offset(customerDto.getStart().intValue())
				.limit(customerDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public List<Customer> searchByDto(CustomerDto customerDto, Predicate... predicates) {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = searchByCriteria(customerDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Customer> searchPageByDto(CustomerDto customerDto, Predicate... predicates) {
		QCustomer customer = QCustomer.customer;
		QAccount account = QAccount.account;
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = searchByCriteria(customerDto, predicates);
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(customer)
				.innerJoin(customer.account, account).fetchJoin()
				.innerJoin(account.contact, contact).fetchJoin()
				.where(builder.asPredicate())
				.orderBy(sortBy(customerDto))
				.offset(customerDto.getStart().intValue())
				.limit(customerDto.getLimit().intValue())
				.fetch();
	}

	protected DynBooleanBuilder<QCustomer, CustomerDto> findByCriteria(CustomerDto customerDto, Predicate... predicates) {
		QCustomer customer = QCustomer.customer;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = new DynBooleanBuilder<QCustomer, CustomerDto>();
		builder = builder.findPredicate(customer, customerDto, predicates);

		if (customerDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.findPredicate(customer.account, customerDto.getAccountDto(), predicates).asPredicate();
			builder.and(byAccountPredicate);
		}

		return builder;
	}
	
	protected DynBooleanBuilder<QCustomer, CustomerDto> searchByCriteria(CustomerDto customerDto, Predicate... predicates) {
		QCustomer customer = QCustomer.customer;
		
		DynBooleanBuilder<QCustomer, CustomerDto> builder = new DynBooleanBuilder<QCustomer, CustomerDto>();
		builder = builder.searchPredicate(customer, customerDto, predicates);

		if (customerDto.getAccountDto() != null) {
			DynBooleanBuilder<QAccount, AccountDto> byAccountBuilder = new DynBooleanBuilder<QAccount, AccountDto>();
			Predicate byAccountPredicate = byAccountBuilder.searchPredicate(customer.account, customerDto.getAccountDto(), predicates).asPredicate();
			builder.and(byAccountPredicate);
		}

		return builder;
	}


	@Override
	public OrderSpecifier[] sortBy(CustomerDto customerDto) {
		PathBuilder pb = new PathBuilder<QCustomer>(QCustomer.class, "customer");
		return sortOrderOf(pb, customerDto);
	}

}
