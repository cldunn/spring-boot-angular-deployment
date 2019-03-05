package com.cldbiz.userportal.repository.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cldbiz.userportal.domain.QLineItem;
import com.cldbiz.userportal.domain.QProduct;
import com.cldbiz.userportal.domain.QTest;
import com.cldbiz.userportal.domain.Test;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.TestDto;
import com.cldbiz.userportal.repository.AbstractDaoImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class TestRepositoryImpl extends AbstractDaoImpl<Test, TestDto, Long> implements TestRepositoryExt {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestRepositoryImpl.class);
	
	@Override
	public Boolean existsByDto(TestDto testDto, Predicate... predicates) {
		QTest test = QTest.test;
		
		DynBooleanBuilder<QTest, TestDto> builder = searchByCriteria(testDto, predicates);
		
		return jpaQueryFactory.selectFrom(test).where(builder.asPredicate()).fetchCount() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	@Override
	public Long countByDto(TestDto testDto, Predicate... predicates) {
		QTest test = QTest.test;
		
		DynBooleanBuilder<QTest, TestDto> builder = searchByCriteria(testDto, predicates);
		
		return jpaQueryFactory.selectFrom(test).where(builder.asPredicate()).fetchCount();
	}

	@Override
	public List<Test> findByIds(List<Long> testIds) {
		QTest test = QTest.test;
		
		return jpaQueryFactory.selectFrom(test)
				.where(test.id.in(testIds))
				.fetch();
	}
	
	@Override
	public List<Test> findAll() {
		QTest test = QTest.test;
		
		return jpaQueryFactory.selectFrom(test).fetch();
	}

	@Override
	public List<Test> findByDto(TestDto testDto, Predicate... predicates) {
		LOGGER.debug("Inside findByDto()");
		QTest test = QTest.test;
		
		DynBooleanBuilder<QTest, TestDto> builder = findByCriteria(testDto, predicates);
		
		return jpaQueryFactory.selectFrom(test).where(builder.asPredicate()).fetch();
	}

	@Override
	public List<Test> findPageByDto(TestDto testDto, Predicate... predicates) {
		QTest test = QTest.test;
		
		DynBooleanBuilder<QTest, TestDto> builder = findByCriteria(testDto, predicates);
		
		return jpaQueryFactory.selectFrom(test)
				.where(builder.asPredicate())
				.orderBy(sortBy(testDto))
				.offset(testDto.getStart().intValue())
				.limit(testDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public List<Test> searchByDto(TestDto testDto, Predicate... predicates) {
		QTest test = QTest.test;
		
		DynBooleanBuilder<QTest, TestDto> builder = searchByCriteria(testDto, predicates);
		
		return jpaQueryFactory.selectFrom(test).where(builder.asPredicate()).fetch();
	}
	
	@Override
	public List<Test> searchPageByDto(TestDto testDto, Predicate... predicates) {
		QTest test = QTest.test;
		
		DynBooleanBuilder<QTest, TestDto> builder = searchByCriteria(testDto, predicates);
		
		return jpaQueryFactory.selectFrom(test)
				.where(builder.asPredicate())
				.orderBy(sortBy(testDto))
				.offset(testDto.getStart().intValue())
				.limit(testDto.getLimit().intValue())
				.fetch();
	}
	
	protected DynBooleanBuilder<QTest, TestDto> findByCriteria(TestDto testDto, Predicate... predicates) {
		QTest test = QTest.test;
		
		DynBooleanBuilder<QTest, TestDto> builder = new DynBooleanBuilder<QTest, TestDto>();
		builder = builder.findPredicate(test, testDto, predicates);

		return builder;
	}
	
	protected DynBooleanBuilder<QTest, TestDto> searchByCriteria(TestDto testDto, Predicate... predicates) {
		QTest test = QTest.test;
		
		DynBooleanBuilder<QTest, TestDto> builder = new DynBooleanBuilder<QTest, TestDto>();
		builder = builder.searchPredicate(test, testDto, predicates);

		return builder;
	}

	public OrderSpecifier[] sortBy(TestDto testDto) {
		PathBuilder pb = new PathBuilder<QTest>(QTest.class, "test");
		return sortOrderOf(pb, testDto);
	}

}
