package com.cldbiz.userportal.repository.term;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cldbiz.userportal.domain.QTerm;
import com.cldbiz.userportal.domain.Term;
import com.cldbiz.userportal.dto.TermDto;
import com.cldbiz.userportal.repository.BaseRepositoryExtImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.Predicate;

public class TermRepositoryImpl extends BaseRepositoryExtImpl<Term> implements TermRepositoryExt {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TermRepositoryImpl.class);
	
	@Override
	public List<Term> findByDto(TermDto termDto) {
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QTerm, TermDto> builder = new DynBooleanBuilder<QTerm, TermDto>();
		Predicate predicate = builder.findPredicate(term, termDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(term).where(predicate).fetch();
	}
	
	@Override
	public List<Term> searchByDto(TermDto termDto) {
		QTerm term = QTerm.term;
		
		DynBooleanBuilder<QTerm, TermDto> builder = new DynBooleanBuilder<QTerm, TermDto>();
		Predicate predicate = builder.searchPredicate(term, termDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(term).where(predicate).fetch();
	}

}
