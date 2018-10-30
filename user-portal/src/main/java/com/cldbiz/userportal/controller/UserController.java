package com.cldbiz.userportal.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.service.UserService;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping({"/api"})
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    private UserService userService;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto){
        return userService.save(userDto);
    }

    @GetMapping(path = {"/{id}"})
    public UserDto findOne(@PathVariable("id") Long id){
        return userService.findById(id);
    }

    @PutMapping
    public UserDto update(@RequestBody UserDto userDto){
        return userService.save(userDto);
    }

    @DeleteMapping(path ={"/{id}"})
    public UserDto delete(@PathVariable("id") Long id) {
    	UserDto userDto = userService.findById(id);
        userService.deleteById(id);
        return userDto;
    }

    // @GetMapping
    @RequestMapping("/users")
    public List<UserDto> findAll() {
        return userService.findByDto(new UserDto());
    }
}
