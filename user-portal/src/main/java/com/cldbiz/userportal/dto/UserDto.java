package com.cldbiz.userportal.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

import com.cldbiz.userportal.domain.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public @Data class UserDto extends AbstractDto {

    private static final long serialVersionUID = 1L;

	private String firstName;
    
    private String lastName;
    
    private Locale locale;
    
    private String email;

    public UserDto() {
    	super();
    }
    
    public UserDto(User user) {
    	super(user);
    	
    	this.setFirstName(user.getFirstName());
    	this.setLastName(user.getLastName());
    	this.setEmail(user.getEmail());
    }
}
