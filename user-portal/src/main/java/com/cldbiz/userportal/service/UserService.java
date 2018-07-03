package com.cldbiz.userportal.service;

import java.util.List;

import com.cldbiz.userportal.domain.User;

public interface UserService {

    User create(User user);

    User delete(int id);

    List<User> findAll();

    User findById(int id);

    User update(User user);
}
