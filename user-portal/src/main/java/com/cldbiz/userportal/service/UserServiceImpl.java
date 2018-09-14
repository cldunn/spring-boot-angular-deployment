package com.cldbiz.userportal.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.repository.user.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public User delete(Long id) {
        User user = findById(id);
    	repository.deleteById(id);
    	
        return user;
    }

    @Override
    public List<User> findAll() {
    	User user = new User();
    	
    	
    	UserDto userDto = new UserDto();
    	userDto.setFirstName("Cliff");
    	
    	userDto.setVarByte((byte) 174);
    	userDto.setVarCh(new Character('A'));
    	
    	userDto.setVarShort((short) 32767);
    	userDto.setVarInt(new Integer(2147483647));
    	userDto.setVarLong(9223372036854775807L);
    	userDto.setVarBigInteger(new BigInteger("2147483647"));
    	
    	// userDto.setVarFloat(22.22F);
    	userDto.setVarDouble(9999999999999999.99);
    	userDto.setVarBigDecimal(new BigDecimal("9999999999999999.9999"));
    	
    	userDto.setVarBool(true);
    	
    	userDto.setVarDate(LocalDate.parse("2018-09-01"));
    	userDto.setVarTime(LocalTime.parse("10:15:30"));
    	userDto.setVarDttm(LocalDateTime.parse("2015-03-25T12:00:00"));
    	 
    	// userDto.setVarInstant(OffsetDateTime.parse("2010-01-01 10:00:00+01").toInstant());
    	 
    	 
    	return repository.findByDto(userDto);
    }

    @Override
    public User findById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public User update(User user) {
        return null;
    }
}
