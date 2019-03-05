package com.cldbiz.userportal.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "COMMISSION")
@EqualsAndHashCode(callSuper=true)
public @Data class Commission extends AbstractDomain {
	
	@Column
	private BigDecimal rate;
	
	@Column
	private String userEmail;
	
	@Column
	private String orderIdentifier;
	
	public Commission() {
		super();
	}
}
