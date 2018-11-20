package com.cldbiz.userportal.repository.term;

import java.util.List;

import com.cldbiz.userportal.domain.Term;
import com.cldbiz.userportal.dto.TermDto;

public interface TermRepositoryExt {
	
	public List<Term> findByDto(TermDto termDto);
	
	public List<Term> searchByDto(TermDto termDto);
	
}
