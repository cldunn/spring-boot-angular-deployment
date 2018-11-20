package com.cldbiz.userportal.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cldbiz.userportal.dto.CustomerDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/* TODO: extends User[Profile] */
@Entity
@Table(name = "CUSTOMER", uniqueConstraints = {@UniqueConstraint(columnNames = {"account_id"}, name="UQ_CUSTOMER_ACCOUNT_ID")})
@EqualsAndHashCode(callSuper=true)
public @Data class Customer extends AbstractDomain {

	@Column
	private String company;
	
	@Column
	private String department;

	@Column
	private String jobTitle;
    
	@Column(nullable=false)
	private String firstName;
	
	@Column(nullable=false)
	private String lastName;
	
	@Column(nullable=false)
	private String workEmail;
	
	@Column(nullable=false)
	private String workPhone;
    
	@Column
	private String facebookIdentifier;
	
	@Column
	private String twitterIdentifier;
	
	@Column
	private String linkedinIdentifier;
    
	@Column
	private Boolean canCommunicate;
    
	@OneToOne(cascade= {CascadeType.ALL}, optional=false, orphanRemoval=true)
	@JoinColumn(name="ACCOUNT_ID", foreignKey=@ForeignKey(name = "FK_CUSTOMER_ACCOUNT"))
	private Account account;

    public Customer() {
    	super();
    }
    
    public Customer(CustomerDto customerDto) {
    	super(customerDto);
    	
    	this.setCompany(customerDto.getCompany());
    	this.setDepartment(customerDto.getDepartment());
    	this.setJobTitle(customerDto.getJobTitle());
    	this.setFirstName(customerDto.getFirstName());
    	this.setLastName(customerDto.getLastName());
    	this.setWorkEmail(customerDto.getWorkEmail());
    	this.setWorkPhone(customerDto.getWorkPhone());
    	this.setFacebookIdentifier(customerDto.getFacebookIdentifier());
    	this.setTwitterIdentifier(customerDto.getTwitterIdentifier());
    	this.setLinkedinIdentifier(customerDto.getLinkedinIdentifier());
    	this.setCanCommunicate(customerDto.getCanCommunicate());
    }

}
