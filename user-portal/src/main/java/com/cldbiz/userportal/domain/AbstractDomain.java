package com.cldbiz.userportal.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import com.cldbiz.userportal.dto.AbstractDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@EqualsAndHashCode
public abstract @Data class AbstractDomain implements Serializable {
    
	/* still needs uid (transient), (LOMBOCK) equals, hashcode. toString */
	/* tenant id if multiple tenants per db,  alt db per tenant */
	
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
	private LocalDateTime createdDate;
	
	@Column
	private String maintainedBy;
	
	@Column
	private LocalDateTime maintainedDate;
	
	protected AbstractDomain() {}
	
	protected AbstractDomain(AbstractDto dto) {
		this.id = dto.getId();
	}
	
	/* set to protected */
	protected void setVersion(Long version) {
		this.version = version;
	}
	
	@PrePersist
    public void onPrePersist() {
       this.setCreatedDate(LocalDateTime.now());
    }
      
    @PreUpdate
    public void onPreUpdate() {
    	this.setMaintainedDate(LocalDateTime.now());
    }
}
