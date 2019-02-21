package com.cldbiz.userportal.unit;

import javax.annotation.PostConstruct;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.cldbiz.userportal.config.AppExecutionContext;
import com.cldbiz.userportal.dto.UserDto;
import com.github.springtestdbunit.DbUnitTestExecutionListener;

@RunWith(SpringRunner.class)
@Transactional
@TestExecutionListeners({ 
	DependencyInjectionTestExecutionListener.class, 
	TransactionalTestExecutionListener.class,
	DbUnitTestExecutionListener.class })
@SpringBootTest(classes = {TestConfig.class})
@ContextHierarchy({
	@ContextConfiguration(classes = TestConfig.class)
})
public abstract class BaseRepositoryTest {
	@Autowired
	Environment env;
	
	@PostConstruct
	public void initAfwBaseTest() {
		AppExecutionContext.setUserDto(getUserDto());
		AppExecutionContext.setDsKey(env.getProperty("ds.test"));
	}
	
	private UserDto getUserDto() {
		UserDto userInfoDto = new UserDto();
		
		// userInfoDto.setId(Long.valueOf(env.getProperty("test.userInfo.id")));
		// userInfoDto.setFirstName(env.getProperty("test.userInfo.firstName"));
		// userInfoDto.setLastName(env.getProperty("test.userInfo.lastName"));
		// userInfoDto.setLocale(new Locale(env.getProperty("test.userInfo.localeLanguage"), env.getProperty("test.userInfo.localeCountry")));
		
		return userInfoDto;
	}
}
