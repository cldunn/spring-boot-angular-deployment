package com.cldbiz.userportal.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.repository.user.UserRepository;
import com.cldbiz.userportal.repository.user.UserRepositoryImpl;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

	@Override
	public UserDto findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		
		UserDto userDto = null;
		if (user.isPresent()) {
			userDto = new UserDto(user.get());
		}

		return userDto;
	}

	@Override
	public List<UserDto> findByDto(UserDto userDto) {
		List<User> users =  userRepository.findByDto(userDto);
		return users.stream().map(u -> new UserDto(u)).collect(Collectors.toList());
	}

	@Override
	public UserDto save(UserDto userDto) {
		User user = new User(userDto);
		user =  userRepository.saveEntity(user);

		return new UserDto(user);
	}

	@Override
	public void deleteById(Long id) {
		 userRepository.deleteById(id);
	}

	@Override
	public void deleteByIds(List<Long> ids) {
		userRepository.deleteByIds(ids);
	}
}
