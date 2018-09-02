package com.cldbiz.userportal.repository;

import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.repository.base.BaseRepository;

public interface UserRepository extends BaseRepository<User, Long>, UserRepositoryExt {

    User findOne(Long id);

    User deleteById(Long id);
    
    User save(User user);
    
    
}
