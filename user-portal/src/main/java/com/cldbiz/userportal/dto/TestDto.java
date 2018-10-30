package com.cldbiz.userportal.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

import com.cldbiz.userportal.domain.Test;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public @Data class TestDto extends AbstractDto {
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
    
    public TestDto() {}
    
    public TestDto(Test test) {
    	super(test);
    	
    	this.setId(test.getId());
    	this.setVarByte(test.getVarByte());
    	this.setVarCh(test.getVarCh());
    	this.setVarShort(test.getVarShort());
    	this.setVarInt(test.getVarInt());
    	this.setVarLong(test.getVarLong());
    	this.setVarBigInteger(test.getVarBigInteger());
    	this.setVarDouble(test.getVarDouble());
    	this.setVarBigDecimal(test.getVarBigDecimal());
    	this.setVarBool(test.getVarBool());
    	this.setVarDate(test.getVarDate());
    	this.setVarTime(test.getVarTime());
    	this.setVarDttm(test.getVarDttm());
    }
}
