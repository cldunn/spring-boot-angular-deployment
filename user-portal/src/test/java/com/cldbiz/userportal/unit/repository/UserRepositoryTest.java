package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.TestDto;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.repository.user.UserRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

// @DatabaseSetup("/userData.xml")
public class UserRepositoryTest extends BaseRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryTest.class);

	@Autowired
	UserRepository userRepository;

	@Test
	public void whenFindById_thenReturnUser() {
		Optional<User> user = userRepository.findById(2L);
		assertThat(user.orElse(null)).isNotNull();
	}

	@Test
	public void whenFindByDto_thenReturnUser() {
		UserDto userDto = new UserDto();
		userDto.setFirstName("Sam");
		userDto.setLastName("Houston");
		userDto.setEmail("samhoustontx@yahoo.com");
		
		List<User> users = userRepository.findByDto(userDto);
		assertThat(users).isNotEmpty();
	}

}
