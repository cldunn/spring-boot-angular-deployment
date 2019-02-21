package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import org.hamcrest.collection.IsEmptyCollection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.Test;
import com.cldbiz.userportal.dto.TestDto;
import com.cldbiz.userportal.repository.test.TestRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

// @DatabaseSetup("/testData.xml")
public class TestRepositoryTest extends BaseRepositoryTest {
	@Autowired
	TestRepository testRepository;

	@org.junit.Test
	public void whenFindById_thenReturnTest() {
		Optional<Test> test = testRepository.findById(2L);
		assertThat(test.orElse(null)).isNotNull(); 
	}

	@org.junit.Test
	public void whenFindByDto_thenReturnTest() {
		TestDto testDto = new TestDto();
		testDto.setVarCh('B');
		testDto.setVarShort((short) 10);	
		testDto.setVarInt(100);
		testDto.setVarLong(1000L);
		testDto.setVarBigInteger(BigInteger.valueOf(10000));
		testDto.setVarDouble(420.12);
		testDto.setVarBigDecimal(BigDecimal.valueOf(240.7800));
		testDto.setVarBool(false);
		testDto.setVarDate(LocalDate.parse("2016-08-03"));
		testDto.setVarTime(LocalTime.parse("06:45:05"));
		testDto.setVarTime(LocalTime.parse("06:45:05"));
		testDto.setVarDttm(LocalDateTime.parse("2018-10-01T07:15:00"));
		List<Test> tests = testRepository.findByDto(testDto);
		assertThat(tests).isNotEmpty();
	}
}
