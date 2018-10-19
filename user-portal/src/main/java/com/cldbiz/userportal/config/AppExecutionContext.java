package com.cldbiz.userportal.config;

import org.springframework.stereotype.Component;

import com.cldbiz.userportal.dto.UserDto;

@Component
public class AppExecutionContext {

	private static final ThreadLocal<AppContext> appContext =
        new ThreadLocal<AppContext>() {
            @Override protected AppContext initialValue() {
                return new AppContext();
        }
    };

	public static String getDsKey() {
		return appContext.get().getDsKey();
	}

	public static void setDsKey(String dsKey) {
		appContext.get().setDsKey(dsKey);
	}
	
	public static UserDto getUserDto() {
		return appContext.get().getUserDto();
	}

	public static void setUserDto(UserDto userDto) {
		appContext.get().setUserDto(userDto);
	}

	public static void remove() {
		appContext.remove();
	}
}
