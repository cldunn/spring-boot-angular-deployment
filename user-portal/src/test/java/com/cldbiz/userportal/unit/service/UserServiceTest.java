package com.cldbiz.userportal.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.repository.user.UserRepository;
import com.cldbiz.userportal.service.UserService;
import com.cldbiz.userportal.service.UserServiceImpl;

@RunWith(SpringRunner.class)
public class UserServiceTest {
	
    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
  
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;
 
    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        User user = new User();
    	user.setFirstName("Cliff");
    	user.setLastName("Dunn");
    	user.setEmail("cliffdunntx@yahoo.com");
    			
        List<User> users = new ArrayList<User>();
        users.add(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        Mockito.when(userRepository.findByDto(new UserDto())).thenReturn(users);
    }

    @Test
    public void whenFindById_thenReturnUserDto() {
    	UserDto userDto = userService.findById(1L);
    	assertThat(userDto).isNotNull();
     }
    
    @Test
    public void whenFindByDto_thenReturnUserDtos() {
    	List<UserDto> userDtos = userService.findByDto(new UserDto());
    	assertThat(userDtos).isNotEmpty();
     }

}
