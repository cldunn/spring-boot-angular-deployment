package com.cldbiz.userportal.integration;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.hamcrest.core.Is.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.cldbiz.userportal.UserPortalApplication;
import com.cldbiz.userportal.repository.user.UserRepository;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK, classes = UserPortalApplication.class)
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
 
    @Test
    public void whenFindById_thenReturnUser() throws Exception {
     
        mvc.perform(get("/api/{id}", "1")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.firstName").value("Cliff"));
    }
}
