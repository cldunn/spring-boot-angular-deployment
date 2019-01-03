package com.cldbiz.userportal.repository.term;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.Term;
import com.cldbiz.userportal.repository.AbstractRepository;

@Repository
public interface TermRepository extends AbstractRepository<Term, Long>, TermRepositoryExt {

}
