package com.cldbiz.userportal.unit.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.cldbiz.userportal.controller.UserController;
import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
 
    @MockBean
    private UserService userService;
    
    @Before
    public void setUp() {
        User user = new User();
    	user.setFirstName("Cliff");
    	user.setLastName("Dunn");
    	user.setEmail("cliffdunntx@yahoo.com");
    			
        Mockito.when(userService.findById(1L)).thenReturn(user);
    }

    @Test
    public void whenFindById_thenReturnUser() throws Exception {

    	this.mockMvc.perform(get("/api/{id}", "1")
    		.contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.firstName").value("Cliff"));
    }
}
