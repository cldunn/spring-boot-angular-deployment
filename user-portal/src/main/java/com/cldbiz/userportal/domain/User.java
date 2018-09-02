package com.cldbiz.userportal.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "USER_PROFILE")
public @Data class User  {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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
