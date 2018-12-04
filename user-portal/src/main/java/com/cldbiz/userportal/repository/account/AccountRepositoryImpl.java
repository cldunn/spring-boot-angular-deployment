package com.cldbiz.userportal.repository.account;

import java.util.List;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.repository.BaseRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class AccountRepositoryImpl extends BaseRepositoryImpl<Account, AccountDto, Long> implements AccountRepositoryExt {

	@Override
	public List<Account> findAll() {
		QAccount account = QAccount.account;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term).fetchJoin()
				.innerJoin(account.customer).fetchJoin()
				.fetch();
	}

	@Override
	public List<Account> findAllById(List<Long> accountIds) {
		QAccount account = QAccount.account;
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term).fetchJoin()
				.innerJoin(account.customer).fetchJoin()
				.where(account.id.in(accountIds))
				.fetch();
	}

	@Override
	public List<Account> findByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		Predicate predicate = builder.findPredicate(account, accountDto).asPredicate();
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term).fetchJoin()
				.innerJoin(account.customer).fetchJoin()
				.where(predicate)
				.fetch();
	}

	@Override
	public List<Account> findPageByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		Predicate predicate = builder.findPredicate(account, accountDto).asPredicate();
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term).fetchJoin()
				.innerJoin(account.customer).fetchJoin()
				.where(predicate)
				.orderBy(sortBy(accountDto))
				.offset(accountDto.getStart().intValue())
				.limit(accountDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public List<Account> searchByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		Predicate predicate = builder.searchPredicate(account, accountDto).asPredicate();
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term).fetchJoin()
				.innerJoin(account.customer).fetchJoin()
				.where(predicate)
				.fetch();
	}

	@Override
	public List<Account> searchPageByDto(AccountDto accountDto) {
		QAccount account = QAccount.account;
		
		DynBooleanBuilder<QAccount, AccountDto> builder = new DynBooleanBuilder<QAccount, AccountDto>();
		Predicate predicate = builder.searchPredicate(account, accountDto).asPredicate();
		
		// join the entity to all "OnetoOne/ManyToOne" relationships via and innerJoin/fetchJoin
		// forces all columns for all tables in one select which is more efficient
		return jpaQueryFactory.selectFrom(account)
				.innerJoin(account.term).fetchJoin()
				.innerJoin(account.customer).fetchJoin()
				.where(predicate)
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
