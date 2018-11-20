package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.Term;
import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.TermDto;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.repository.term.TermRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup("/termData.xml")
public class TermRepositoryTest extends BaseRepositoryTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryTest.class);
	
	private static final Long TOTAL_ROWS = 3L;
	
	@Autowired
	TermRepository termRepository;

	@Test
	public void whenCount_thenReturnCount() {
		long termCnt = termRepository.count();
		assertThat(termCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenDelete_thenRemoveTerm() {
		List<Term> terms = termRepository.findAll();
		Term term = terms.get(0);
		
		termRepository.delete(term);
		
		terms = termRepository.findAll();

		assertThat(terms.contains(term)).isFalse();
	}

	@Test
	public void whenDeleteAll_thenRemoveAllTerms() {
		List<Term> terms = termRepository.findAll();
		
		termRepository.deleteAll(terms);
		
		long termCnt = termRepository.count();

		assertThat(termCnt).isZero();
	}
	
	@Test
	public void whenDeleteById_thenRemoveTerm() {
		List<Term> terms = termRepository.findAll();
		Term term = terms.get(0);
		
		termRepository.deleteById(term.getId());
		
		terms = termRepository.findAll();

		assertThat(terms.contains(term)).isFalse();
	}

	@Test
	public void whenDeleteByIds_thenRemoveAllTerms() {
		List<Term> terms = termRepository.findAll();
		List<Long> termIds = terms.stream().map(Term::getId).collect(Collectors.toList());
		
		termRepository.deleteByIds(termIds);
		
		long termCnt = termRepository.count();

		assertThat(termCnt).isZero();
	}

	@Test
	public void whenExistsById_thenReturnTrue() {
		List<Term> terms = termRepository.findAll();
		Term term = terms.get(0);
		
		assertThat(termRepository.existsById(term.getId())).isTrue();
	}
	
	@Test
	public void whenFindAll_thenReturnAllTerms() {
		List<Term> terms = termRepository.findAll();
		
		assertThat(terms.size()).isEqualTo(TOTAL_ROWS.intValue());
	}

	@Test
	public void whenFindAllById_thenReturnAllTerms() {
		List<Term> terms = termRepository.findAll();
		List<Long> termIds = terms.stream().map(Term::getId).collect(Collectors.toList());
		
		terms = termRepository.findAllById(termIds);
		
		assertThat(termIds.size() == terms.size()).isTrue();
	}

	@Test
	public void whenFindById_thenReturnTerm() {
		List<Term> terms = termRepository.findAll();
		Term term = terms.get(0);
		
		Optional<Term> sameTerm = termRepository.findById(term.getId());
		
		assertThat(sameTerm.orElse(null)).isNotNull();
		assertThat(term.equals(sameTerm.get())).isTrue();
	}

	@Test
	public void whenSave_thenReturnSavedTerm() {
		Term anotherTerm = new Term();
		anotherTerm.setCode("EOM");
		anotherTerm.setDescription("End of month");
		
		
		Term savedTerm = termRepository.save(anotherTerm);
		assertThat(savedTerm.equals(anotherTerm)).isTrue();
		
		long termCnt = termRepository.count();
		assertThat(termCnt).isEqualTo(TOTAL_ROWS + 1);
	}

	@Test
	public void whenSaveAll_thenReturnSavedTerms() {
		Term anotherTerm = new Term();
		anotherTerm.setCode("EOM");
		anotherTerm.setDescription("End of month");
		
		Term extraTerm = new Term();
		extraTerm.setCode("CND");
		extraTerm.setDescription("Cash next delivery");
		
		List<Term> terms = new ArrayList<Term>();
		terms.add(anotherTerm);
		terms.add(extraTerm);
		
		List<Term> savedTerms = termRepository.saveAll(terms);
		
		assertThat(savedTerms.size() == 2).isTrue();
		
		assertThat(terms.stream().allMatch(t -> savedTerms.contains(t))).isTrue();
		assertThat(savedTerms.stream().allMatch(t -> terms.contains(t))).isTrue();
		
		long termCnt = termRepository.count();
		assertThat(termCnt).isEqualTo(TOTAL_ROWS + 2);
		
		assertThat(termRepository.existsById(savedTerms.get(0).getId())).isTrue();
		assertThat(termRepository.existsById(savedTerms.get(1).getId())).isTrue();
	}
	
	@Test
	public void whenSaveAndFlush_thenReturnSavedTerm() {
		Term anotherTerm = new Term();
		anotherTerm.setCode("EOM");
		anotherTerm.setDescription("End of month");
		
		
		Term savedTerm = termRepository.saveAndFlush(anotherTerm);
		assertThat(savedTerm.equals(anotherTerm)).isTrue();
		
		long termCnt = termRepository.count();
		assertThat(termCnt).isEqualTo(TOTAL_ROWS + 1);
	}

	@Test
	public void whenFindByDto_thenReturnTerms() {
		TermDto termDto = new TermDto();
		termDto.setCode("PIA");
		
		List<Term> terms = termRepository.findByDto(termDto);
		
		assertThat(terms).isNotEmpty();
	}

	@Test
	public void whenSearchByDto_thenReturnTerms() {
		TermDto termDto = new TermDto();
		termDto.setDescription("in advance");
		
		List<Term> terms = termRepository.searchByDto(termDto);
		
		assertThat(terms).isNotEmpty();
	}

}
