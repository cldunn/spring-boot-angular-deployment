package com.cldbiz.userportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cldbiz.userportal.repository.AbstractRepositoryImpl;

@SpringBootApplication
// @EnableJpaRepositories(basePackages = { "com.cldbiz.userportal.repository" }, repositoryFactoryBeanClass = AbstractRepositoryFactoryBean.class)
@EnableJpaRepositories(basePackages = { "com.cldbiz.userportal.repository" }, repositoryBaseClass = AbstractRepositoryImpl.class)
public class UserPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserPortalApplication.class, args);
	}
}