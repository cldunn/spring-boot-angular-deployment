package com.cldbiz.userportal.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
    private Float varFloat;

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

}
