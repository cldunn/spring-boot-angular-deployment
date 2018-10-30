package com.cldbiz.userportal.service;

import java.util.List;

import com.cldbiz.userportal.dto.UserDto;

public interface UserService {
	public UserDto findById(Long id);
	public List<UserDto> findByDto(UserDto userDto);
	
	public UserDto save(UserDto userDto);

	public void deleteById(Long id);
	public void deleteByIds(List<Long> ids); 
}
