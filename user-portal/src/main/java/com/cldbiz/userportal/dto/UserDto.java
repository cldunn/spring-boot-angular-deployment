package com.cldbiz.userportal.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public @Data class UserDto extends AbstractDto {

    private static final long serialVersionUID = 1L;

	private String firstName;
    
    private String lastName;
    
    private String email;

    private Byte varByte;
    
    private Character varCh;
    
    private Short varShort;
    
    private Integer varInt;
    
    private Long varLong;
    
    private BigInteger varBigInteger;
    
    private Float varFloat;

    private Double varDouble;
    
    private BigDecimal varBigDecimal;
    
    private Boolean varBool;
    
    private LocalDate varDate;
    
    private LocalTime varTime;
    
    private LocalDateTime varDttm;
}
