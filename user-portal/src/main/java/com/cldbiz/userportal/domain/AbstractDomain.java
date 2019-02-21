package com.cldbiz.userportal.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.data.domain.Sort;

import com.cldbiz.userportal.dto.AbstractDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
// @EqualsAndHashCode
public abstract @Data class AbstractDomain implements Serializable {
    
	/* still needs uid (transient), (LOMBOCK) equals, hashcode. toString */
	/* tenant id if multiple tenants per db,  alt db per tenant */
	
	@Id
    @Column(nullable=false)
	@SequenceGenerator(name = "hibernate_sequence", initialValue = 10000, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;

	@Version
	@EqualsAndHashCode.Exclude
	private Long version;
	
	@Column(nullable=false)
	@EqualsAndHashCode.Exclude
	private String createdBy;
	
	@Column(nullable=false)
	@EqualsAndHashCode.Exclude
	private LocalDateTime createdDate;
	
	@Column
	@EqualsAndHashCode.Exclude
	private String maintainedBy;
	
	@Column
	@EqualsAndHashCode.Exclude
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
	   this.setCreatedBy("sysgen");
       this.setCreatedDate(LocalDateTime.now());
    }
      
    @PreUpdate
    public void onPreUpdate() {
    	this.setMaintainedDate(LocalDateTime.now());
    }
}
