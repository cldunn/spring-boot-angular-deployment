package com.cldbiz.userportal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.UserDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "USER_PROFILE")
@EqualsAndHashCode(callSuper=true)
public @Data class User extends AbstractDomain {

    @Column
    private String firstName;
    
    @Column
    private String lastName;
    
    @Column
    private String email;
    
    public User() {
    	super();
    }
    
    public User(UserDto userDto) {
    	super(userDto);
    	
    	this.setFirstName(userDto.getFirstName());
    	this.setLastName(userDto.getLastName());
    	this.setEmail(userDto.getEmail());
    }
}
