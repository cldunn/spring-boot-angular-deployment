package com.cldbiz.userportal.repository.test;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.Test;
import com.cldbiz.userportal.repository.AbstractRepository;

@Repository
public interface TestRepository extends AbstractRepository<Test, Long>, TestRepositoryExt {

}
