package com.cldbiz.userportal.repository.user;

import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.repository.AbstractRepository;

public interface UserRepository extends AbstractRepository<User, Long>, UserRepositoryExt {
}
