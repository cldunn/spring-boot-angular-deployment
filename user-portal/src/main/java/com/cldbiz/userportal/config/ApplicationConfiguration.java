package com.cldbiz.userportal.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cldbiz.userportal.repository.BaseRepositoryImpl;

@Configuration
@EntityScan(basePackages = "com.cldbiz.userportal.domain")
@EnableJpaRepositories(basePackages = { "com.cldbiz.userportal.repository" }, repositoryBaseClass = BaseRepositoryImpl.class)
public class ApplicationConfiguration {

}
