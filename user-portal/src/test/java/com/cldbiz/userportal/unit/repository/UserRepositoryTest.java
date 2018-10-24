package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.repository.user.UserRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;



@DatabaseSetup("/userData.xml")
public class UserRepositoryTest extends BaseRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryTest.class);

	@Autowired
	UserRepository userRepository;
	
	@Test
	public void whenFindById_thenReturnUser() {
		Optional<User> user = userRepository.findById(2L);
		assertThat(user.orElse(null)).isNotNull(); 
	}

}
