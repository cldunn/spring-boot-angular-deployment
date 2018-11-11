package com.cldbiz.userportal.repository.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cldbiz.userportal.domain.QTest;
import com.cldbiz.userportal.domain.Test;
import com.cldbiz.userportal.dto.TestDto;
import com.cldbiz.userportal.repository.BaseRepositoryExtImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class TestRepositoryImpl extends BaseRepositoryExtImpl<Test> implements TestRepositoryExt {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestRepositoryImpl.class);
	
	@Override
	public List<Test> findByDto(TestDto testDto) {
		LOGGER.debug("Inside findByDto()");
		QTest test = QTest.test;
		
		DynBooleanBuilder<QTest, TestDto> builder = new DynBooleanBuilder<QTest, TestDto>();
		Predicate predicate = builder.findPredicate(test, testDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(test).where(predicate).fetch();
	}

}
