package com.cldbiz.userportal.unit;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.cldbiz.userportal.config.AppExecutionContext;
import com.cldbiz.userportal.dto.UserDto;
import com.github.springtestdbunit.DbUnitTestExecutionListener;


@RunWith(SpringRunner.class)
@DataJpaTest
@TestExecutionListeners({ 
	DependencyInjectionTestExecutionListener.class, 
	DbUnitTestExecutionListener.class })
@ContextHierarchy({
	@ContextConfiguration(classes = DBUnitConfig.class)
})
@TestPropertySource("classpath:application-test.properties")
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
		
		userInfoDto.setId(Long.valueOf(env.getProperty("test.userInfo.id")));
		userInfoDto.setFirstName(env.getProperty("test.userInfo.firstName"));
		userInfoDto.setLastName(env.getProperty("test.userInfo.lastName"));
		userInfoDto.setLocale(new Locale(env.getProperty("test.userInfo.localeLanguage"), env.getProperty("test.userInfo.localeCountry")));
		
		return userInfoDto;
	}
}
