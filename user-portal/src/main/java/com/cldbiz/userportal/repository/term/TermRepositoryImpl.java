package com.cldbiz.userportal.repository.term;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cldbiz.userportal.domain.QPurchaseOrder;
import com.cldbiz.userportal.domain.QTerm;
import com.cldbiz.userportal.domain.Term;
import com.cldbiz.userportal.dto.PurchaseOrderDto;
import com.cldbiz.userportal.dto.TermDto;
import com.cldbiz.userportal.repository.BaseRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public class TermRepositoryImpl extends BaseRepositoryImpl<Term, TermDto, Long> implements TermRepositoryExt {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TermRepositoryImpl.class);
	
	@Override
	public List<Term> findAll() {
		return jpaQueryFactory.selectFrom(QTerm.term).fetch();
	}
	
	@Override
	public List<Term> findAllById(List<Long> termIds) {
		QTerm term = QTerm.term;
		
		return jpaQueryFactory.selectFrom(term)
				.where(term.id.in(termIds))
				.fetch();
	}

	@Override
	public List<Term> findByDto(TermDto termDto) {
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QTerm, TermDto> builder = new DynBooleanBuilder<QTerm, TermDto>();
		Predicate predicate = builder.findPredicate(term, termDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(term).where(predicate).fetch();
	}
	
	@Override
	public List<Term> findPageByDto(TermDto termDto) {
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QTerm, TermDto> builder = new DynBooleanBuilder<QTerm, TermDto>();
		Predicate predicate = builder.findPredicate(term, termDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(term).where(predicate)
				.orderBy(sortBy(termDto))
				.offset(termDto.getStart().intValue())
				.limit(termDto.getLimit().intValue())
				.fetch();
	}

	public Long countSearchByDto(TermDto termDto) {
		QTerm term = QTerm.term;

		DynBooleanBuilder<QTerm, TermDto> builder = new DynBooleanBuilder<QTerm, TermDto>();
		Predicate predicate = builder.searchPredicate(term, termDto).asPredicate();

		return jpaQueryFactory.selectFrom(term)
				.where(predicate)
				.fetchCount();
	}

	@Override
	public List<Term> searchByDto(TermDto termDto) {
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QTerm, TermDto> builder = new DynBooleanBuilder<QTerm, TermDto>();
		Predicate predicate = builder.searchPredicate(term, termDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(term).where(predicate).fetch();
	}

	@Override
	public List<Term> searchPageByDto(TermDto termDto) {
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QTerm, TermDto> builder = new DynBooleanBuilder<QTerm, TermDto>();
		Predicate predicate = builder.searchPredicate(term, termDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(term).where(predicate)
				.orderBy(sortBy(termDto))
				.offset(termDto.getStart().intValue())
				.limit(termDto.getLimit().intValue())
				.fetch();
	}
	
	public OrderSpecifier[] sortBy(TermDto termDto) {
		PathBuilder pb = new PathBuilder<QTerm>(QTerm.class, "term");
		return sortOrderOf(pb, termDto);
	}

}
