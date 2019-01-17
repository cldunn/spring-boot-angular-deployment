package com.cldbiz.userportal.repository.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cldbiz.userportal.domain.QLineItem;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.domain.QUser;
import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.repository.AbstractRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class UserRepositoryImpl extends AbstractRepositoryImpl<User, UserDto, Long> implements UserRepositoryExt {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);
	
	@Override
	public Boolean existsByDto(UserDto userDto, Predicate... predicates) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = searchByCriteria(userDto, predicates);
		
		return jpaQueryFactory.selectFrom(user).where(builder.asPredicate()).fetchCount() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	@Override
	public Long countByDto(UserDto userDto, Predicate... predicates) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = searchByCriteria(userDto, predicates);
		
		return jpaQueryFactory.selectFrom(user).where(builder.asPredicate()).fetchCount();
	}


	/*
	@Override
	public User findById(Long userId) {
		QUser user = QUser.user;
		
		return jpaQueryFactory.selectFrom(user)
				.where(user.id.eq(userId))
				.fetch();
	}
	*/
	
	@Override
	public List<User> findByIds(List<Long> userIds) {
		QUser user = QUser.user;
		
		return jpaQueryFactory.selectFrom(user)
				.where(user.id.in(userIds))
				.fetch();
	}
	
	@Override
	public List<User> findAll() {
		QUser user = QUser.user;
		
		return jpaQueryFactory.selectFrom(user).fetch();
	}

	@Override
	public List<User> findByDto(UserDto userDto, Predicate... predicates) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = findByCriteria(userDto, predicates);
		
		/*
		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>();
		Predicate predicate = builder.findPredicate(user, userDto).asPredicate();
		*/
		
		return jpaQueryFactory.selectFrom(user).where(builder.asPredicate()).fetch();
	}

	@Override
	public List<User> findPageByDto(UserDto userDto, Predicate... predicates) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = findByCriteria(userDto, predicates);
		
		/*
		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>();
		Predicate predicate = builder.findPredicate(user, userDto).asPredicate();
		*/
		
		return jpaQueryFactory.selectFrom(user).where(builder.asPredicate())
				.orderBy(sortBy(userDto))
				.offset(userDto.getStart().intValue())
				.limit(userDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public List<User> searchByDto(UserDto userDto, Predicate... predicates) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = searchByCriteria(userDto, predicates);
		
		/*
		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>();
		Predicate predicate = builder.searchPredicate(user, userDto).asPredicate();
		*/
		
		return jpaQueryFactory.selectFrom(user).where(builder.asPredicate()).fetch();
	}
	
	@Override
	public List<User> searchPageByDto(UserDto userDto, Predicate... predicates) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = searchByCriteria(userDto, predicates);
		
		/*
		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>();
		Predicate predicate = builder.searchPredicate(user, userDto).asPredicate();
		*/
		
		return jpaQueryFactory.selectFrom(user).where(builder.asPredicate())
				.orderBy(sortBy(userDto))
				.offset(userDto.getStart().intValue())
				.limit(userDto.getLimit().intValue())
				.fetch();
	}

	protected DynBooleanBuilder<QUser, UserDto> findByCriteria(UserDto userDto, Predicate... predicates) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>();
		builder = builder.findPredicate(user, userDto, predicates);
		
		return builder;
	}
	
	protected DynBooleanBuilder<QUser, UserDto> searchByCriteria(UserDto userDto, Predicate... predicates) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>();
		builder = builder.searchPredicate(user, userDto, predicates);
		
		return builder;
	}

	public OrderSpecifier[] sortBy(UserDto userDto) {
		PathBuilder pb = new PathBuilder<QUser>(QUser.class, "user");
		return sortOrderOf(pb, userDto);
	}

}
