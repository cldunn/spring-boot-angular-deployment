package com.cldbiz.userportal.repository.user;

import org.springframework.stereotype.Repository;

import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.repository.AbstractRepository;

@Repository
public interface UserRepository extends AbstractRepository<User, Long>, UserRepositoryExt {
}
