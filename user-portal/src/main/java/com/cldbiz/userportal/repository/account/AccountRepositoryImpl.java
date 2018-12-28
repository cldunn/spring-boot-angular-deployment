package com.cldbiz.userportal.repository.account;

import java.util.List;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QCustomer;
import com.cldbiz.userportal.domain.QPurchaseOrder;
import com.cldbiz.userportal.domain.QTerm;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.PurchaseOrderDto;
import com.cldbiz.userportal.dto.TermDto;
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
		QTerm term = QTerm.term;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term, term).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.fetch();
	}

	@Override
	public List<Account> findAllById(List<Long> accountIds) {
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QTerm term = QTerm.term;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term, term).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.where(account.id.in(accountIds))
				.fetch();
	}

	@Override
	public List<Account> findByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		builder = builder.findPredicate(account, accountDto);

		if (accountDto.getCustomerDto() != null) {
			DynBooleanBuilder<QCustomer, CustomerDto> byCustomerBuilder = new DynBooleanBuilder<QCustomer, CustomerDto>();
			Predicate byCustomerPredicate = byCustomerBuilder.findPredicate(account.customer, accountDto.getCustomerDto()).asPredicate();
			builder.and(byCustomerPredicate);
		}

		if (accountDto.getTermDto() != null) {
			DynBooleanBuilder<QTerm, TermDto> byTermBuilder = new DynBooleanBuilder<QTerm, TermDto>();
			Predicate byTermPredicate = byTermBuilder.findPredicate(account.term, accountDto.getTermDto()).asPredicate();
			builder.and(byTermPredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term, term).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Account> findPageByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		builder = builder.findPredicate(account, accountDto);

		if (accountDto.getCustomerDto() != null) {
			DynBooleanBuilder<QCustomer, CustomerDto> byCustomerBuilder = new DynBooleanBuilder<QCustomer, CustomerDto>();
			Predicate byCustomerPredicate = byCustomerBuilder.findPredicate(account.customer, accountDto.getCustomerDto()).asPredicate();
			builder.and(byCustomerPredicate);
		}

		if (accountDto.getTermDto() != null) {
			DynBooleanBuilder<QTerm, TermDto> byTermBuilder = new DynBooleanBuilder<QTerm, TermDto>();
			Predicate byTermPredicate = byTermBuilder.findPredicate(account.term, accountDto.getTermDto()).asPredicate();
			builder.and(byTermPredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term, term).fetchJoin()
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

		if (accountDto.getTermDto() != null) {
			DynBooleanBuilder<QTerm, TermDto> byTermBuilder = new DynBooleanBuilder<QTerm, TermDto>();
			Predicate byTermPredicate = byTermBuilder.searchPredicate(account.term, accountDto.getTermDto()).asPredicate();
			builder.and(byTermPredicate);
		}

		return jpaQueryFactory.selectFrom(account)
				.where(builder.asPredicate())
				.fetchCount();
	}
	
	@Override
	public List<Account> searchByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		builder = builder.searchPredicate(account, accountDto);

		if (accountDto.getCustomerDto() != null) {
			DynBooleanBuilder<QCustomer, CustomerDto> byCustomerBuilder = new DynBooleanBuilder<QCustomer, CustomerDto>();
			Predicate byCustomerPredicate = byCustomerBuilder.searchPredicate(account.customer, accountDto.getCustomerDto()).asPredicate();
			builder.and(byCustomerPredicate);
		}

		if (accountDto.getTermDto() != null) {
			DynBooleanBuilder<QTerm, TermDto> byTermBuilder = new DynBooleanBuilder<QTerm, TermDto>();
			Predicate byTermPredicate = byTermBuilder.searchPredicate(account.term, accountDto.getTermDto()).asPredicate();
			builder.and(byTermPredicate);
		}

		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term, term).fetchJoin()
				.innerJoin(account.customer, customer).fetchJoin()
				.where(builder.asPredicate())
				.fetch();
	}

	@Override
	public List<Account> searchPageByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		QCustomer customer = QCustomer.customer;
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		builder = builder.searchPredicate(account, accountDto);

		if (accountDto.getCustomerDto() != null) {
			DynBooleanBuilder<QCustomer, CustomerDto> byCustomerBuilder = new DynBooleanBuilder<QCustomer, CustomerDto>();
			Predicate byCustomerPredicate = byCustomerBuilder.searchPredicate(account.customer, accountDto.getCustomerDto()).asPredicate();
			builder.and(byCustomerPredicate);
		}

		if (accountDto.getTermDto() != null) {
			DynBooleanBuilder<QTerm, TermDto> byTermBuilder = new DynBooleanBuilder<QTerm, TermDto>();
			Predicate byTermPredicate = byTermBuilder.searchPredicate(account.term, accountDto.getTermDto()).asPredicate();
			builder.and(byTermPredicate);
		}
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		// includes dependency's dependencies
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term, term).fetchJoin()
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
