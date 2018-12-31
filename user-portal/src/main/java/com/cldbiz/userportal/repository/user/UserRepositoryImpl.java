package com.cldbiz.userportal.repository.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cldbiz.userportal.domain.QUser;
import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.repository.BaseRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class UserRepositoryImpl extends BaseRepositoryImpl<User, UserDto, Long> implements UserRepositoryExt {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);
	
	@Override
	public List<User> findAll() {
		return jpaQueryFactory.selectFrom(QUser.user).fetch();
	}
	
	@Override
	public List<User> findAllById(List<Long> userIds) {
		QUser user = QUser.user;
		
		return jpaQueryFactory.selectFrom(user)
				.where(user.id.in(userIds))
				.fetch();
	}

	@Override
	public List<User> findByDto(UserDto userDto) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>();
		Predicate predicate = builder.findPredicate(user, userDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(user).where(predicate).fetch();
	}

	@Override
	public List<User> findPageByDto(UserDto userDto) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>();
		Predicate predicate = builder.findPredicate(user, userDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(user).where(predicate)
				.orderBy(sortBy(userDto))
				.offset(userDto.getStart().intValue())
				.limit(userDto.getLimit().intValue())
				.fetch();
	}

	public Long countSearchByDto(UserDto userDto) {
		QUser user = QUser.user;

		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>();
		Predicate predicate = builder.searchPredicate(user, userDto).asPredicate();

		return jpaQueryFactory.selectFrom(user)
				.where(predicate)
				.fetchCount();
	}

	@Override
	public List<User> searchByDto(UserDto userDto) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>();
		Predicate predicate = builder.searchPredicate(user, userDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(user).where(predicate).fetch();
	}
	
	@Override
	public List<User> searchPageByDto(UserDto userDto) {
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>();
		Predicate predicate = builder.searchPredicate(user, userDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(user).where(predicate)
				.orderBy(sortBy(userDto))
				.offset(userDto.getStart().intValue())
				.limit(userDto.getLimit().intValue())
				.fetch();
	}

	public OrderSpecifier[] sortBy(UserDto userDto) {
		PathBuilder pb = new PathBuilder<QUser>(QUser.class, "user");
		return sortOrderOf(pb, userDto);
	}

}
