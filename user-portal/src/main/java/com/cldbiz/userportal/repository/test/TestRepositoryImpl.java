package com.cldbiz.userportal.repository.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.QAccount;
import com.cldbiz.userportal.domain.QTest;
import com.cldbiz.userportal.domain.QUser;
import com.cldbiz.userportal.domain.Test;
import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.TestDto;
import com.cldbiz.userportal.repository.BaseRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class TestRepositoryImpl extends BaseRepositoryImpl<Test, Long> implements TestRepositoryExt {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestRepositoryImpl.class);
	
	@Override
	public List<Test> findAll() {
		return jpaQueryFactory.selectFrom(QTest.test).fetch();
	}

	@Override
	public List<Test> findAllById(List<Long> testIds) {
		QTest test = QTest.test;
		
		return jpaQueryFactory.selectFrom(test)
				.where(test.id.in(testIds))
				.fetch();
	}

	@Override
	public List<Test> findByDto(TestDto testDto) {
		LOGGER.debug("Inside findByDto()");
		QTest test = QTest.test;
		
		DynBooleanBuilder<QTest, TestDto> builder = new DynBooleanBuilder<QTest, TestDto>();
		Predicate predicate = builder.findPredicate(test, testDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(test).where(predicate).fetch();
	}

	@Override
	public List<Test> searchByDto(TestDto testDto) {
		LOGGER.debug("Inside findByDto()");
		QTest test = QTest.test;
		
		DynBooleanBuilder<QTest, TestDto> builder = new DynBooleanBuilder<QTest, TestDto>();
		Predicate predicate = builder.searchPredicate(test, testDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(test).where(predicate).fetch();
	}
}
