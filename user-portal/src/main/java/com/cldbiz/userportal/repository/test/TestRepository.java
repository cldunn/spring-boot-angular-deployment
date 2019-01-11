package com.cldbiz.userportal.repository.test;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.Test;
import com.cldbiz.userportal.repository.BaseRepository;

@Repository
public interface TestRepository extends BaseRepository<Test, Long>, TestRepositoryExt {

}
