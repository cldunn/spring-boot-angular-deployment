package com.cldbiz.userportal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.UserDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/************************************************
 * 
 * @author cliff
 *
 * loginId
 * password
 *   
 * active 
 * locked
 * attempts
 * lastLogin
 *   
 * Title
 * First Name
 * Last Name
 * Designatory Letters
 *   
 * DOB
 * Gender
 *
 * Address lines
 * City
 * County
 * Zip
 *   
 * Home Phone
 * Mobile Phone
 *   
 * Personal email
 * 
 * TODO: Rename UserProfile
*************************************************/

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
