package com.cldbiz.userportal.repository.user;

import java.util.List;

import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.UserDto;

public interface UserRepositoryExt {

	public List<User> findAll();
	
	public List<User> findByDto(UserDto userDto);
	
	public List<User> searchByDto(UserDto userDto);
}
