package com.cldbiz.userportal.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@EqualsAndHashCode
public abstract @Data class AbstractDomain implements Serializable {
    
	@Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Version
	@EqualsAndHashCode.Exclude
	private Long version;
	
	@Column
	private String createdBy;
	
	@Column
	private LocalDate createdDate;
	
	@Column
	private String maintainedBy;
	
	@Column
	private LocalDate maintainedDate;
	
	protected void setVersion(Long version) {
		this.version = version;
	}
	
	@PrePersist
    public void onPrePersist() {
       this.setCreatedDate(LocalDate.now());
    }
      
    @PreUpdate
    public void onPreUpdate() {
    	this.setMaintainedDate(LocalDate.now());
    }
}
