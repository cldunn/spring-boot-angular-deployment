package com.cldbiz.userportal.config;

import com.cldbiz.userportal.dto.UserDto;

public class AppContext {
	private String dsKey;
	private UserDto userDto;
	
	public String getDsKey() {
		return dsKey;
	}

	public void setDsKey(String dsKey) {
		this.dsKey = dsKey;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}
}

