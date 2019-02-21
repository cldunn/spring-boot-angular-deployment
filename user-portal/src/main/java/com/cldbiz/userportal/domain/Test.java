package com.cldbiz.userportal.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.TestDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "TEST")
@EqualsAndHashCode(callSuper=true)
public @Data class Test extends AbstractDomain {
    
	@Column
    private Byte varByte;
    
    @Column
    private Character varCh;
    
    @Column
    private Short varShort;
    
    @Column
    private Integer varInt;
    
    @Column
    private Long varLong;
    
    @Column
    private BigInteger varBigInteger;
    
    @Column
    private Double varDouble;
    
    @Column(columnDefinition="DECIMAL(20,4)")
    private BigDecimal varBigDecimal;
    
    @Column
    private Boolean varBool;
    
    @Column
    private LocalDate varDate;
    
    @Column
    private LocalTime varTime;
    
    @Column
    private LocalDateTime varDttm;
    
    public Test() {}
    
    public Test(TestDto testDto) {
    	super(testDto);
    	
    	this.setId(testDto.getId());
    	this.setVarByte(testDto.getVarByte());
    	this.setVarCh(testDto.getVarCh());
    	this.setVarShort(testDto.getVarShort());
    	this.setVarInt(testDto.getVarInt());
    	this.setVarLong(testDto.getVarLong());
    	this.setVarBigInteger(testDto.getVarBigInteger());
    	this.setVarDouble(testDto.getVarDouble());
    	this.setVarBigDecimal(testDto.getVarBigDecimal());
    	this.setVarBool(testDto.getVarBool());
    	this.setVarDate(testDto.getVarDate());
    	this.setVarTime(testDto.getVarTime());
    	this.setVarDttm(testDto.getVarDttm());
    }
}
