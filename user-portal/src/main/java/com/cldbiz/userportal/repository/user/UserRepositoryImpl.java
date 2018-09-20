package com.cldbiz.userportal.repository.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cldbiz.userportal.domain.QUser;
import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.repository.BaseRepositoryExtImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

public class UserRepositoryImpl extends BaseRepositoryExtImpl<User> implements UserRepositoryExt {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);
	
	@Override
	public List<User> xyz(UserDto userDto) {
		LOGGER.debug("Inside findByDto()");
		QUser user = QUser.user;
		
		DynBooleanBuilder<QUser, UserDto> builder = new DynBooleanBuilder<QUser, UserDto>(new BooleanBuilder());
		BooleanExpression b1 = user.firstName.equalsIgnoreCase("ASDF");
		// BooleanExpression b2 = user.varInt.eq(userDto.getVarInt());
		Predicate predicate = builder.findPredicate(user, userDto);

		return jpaQueryFactory.selectFrom(user).where(predicate).fetch();
	}

}
