package com.cldbiz.userportal.repository.test;

import java.util.List;

import com.cldbiz.userportal.domain.Test;
import com.cldbiz.userportal.dto.TestDto;

public interface TestRepositoryExt {
	
	public List<Test> findAll();
	
	public List<Test> findByDto(TestDto testDto);
	
	public List<Test> searchByDto(TestDto testDto);
}
