package com.cldbiz.userportal.repository.test;

import java.util.List;

import com.cldbiz.userportal.domain.Test;
import com.cldbiz.userportal.dto.TestDto;

public interface TestRepositoryExt {
	List<Test> findByDto(TestDto testDto);
}
