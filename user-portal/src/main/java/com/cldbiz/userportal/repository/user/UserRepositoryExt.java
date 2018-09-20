package com.cldbiz.userportal.repository.user;

import java.util.List;

import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.UserDto;

public interface UserRepositoryExt {

	List<User> xyz(UserDto userDto);
	
}
