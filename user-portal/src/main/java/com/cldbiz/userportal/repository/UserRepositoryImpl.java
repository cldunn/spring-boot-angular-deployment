package com.cldbiz.userportal.repository;

import java.util.List;

import com.cldbiz.userportal.domain.QUser;
import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.repository.base.BaseRepositoryExtImpl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.PredicateOperation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanOperation;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleExpression;

public class UserRepositoryImpl extends BaseRepositoryExtImpl<User> implements UserRepositoryExt {

	
	@Override
	public List<User> findByDto(UserDto userDto) {
		QUser user = QUser.user;
		
		QdslBooleanBuilder<QUser, UserDto> builder = new QdslBooleanBuilder<QUser, UserDto>(new BooleanBuilder());
		BooleanExpression b1 = user.firstName.equalsIgnoreCase(userDto.getFirstName());
		// BooleanExpression b2 = user.varInt.eq(userDto.getVarInt());
		Predicate predicate = builder.findPredicate(user, userDto);

		return jpaQueryFactory.selectFrom(user).where(predicate).fetch();
	}

}
