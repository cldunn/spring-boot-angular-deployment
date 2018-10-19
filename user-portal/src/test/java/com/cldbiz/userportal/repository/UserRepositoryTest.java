package com.cldbiz.userportal.repository;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.BaseTest;
import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.repository.user.UserRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;



@DatabaseSetup("/userData.xml")
public class UserRepositoryTest extends BaseTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryTest.class);

	@Autowired
	UserRepository userRepository;
	
	@Test
	public void testGetEmployee() {
		LOGGER.debug("THIS IS TEST");
		
		// User user = userRepository.findOne(2L);
		// assertNotNull(user);
		Optional<User> user = userRepository.findById(2L);
		assertNotNull(user.orElse(null));
	}

}
