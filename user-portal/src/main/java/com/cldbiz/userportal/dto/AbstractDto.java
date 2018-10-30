package com.cldbiz.userportal.dto;

import java.io.Serializable;

import com.cldbiz.userportal.domain.AbstractDomain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract @Data class AbstractDto implements Serializable {
 
	private static final long serialVersionUID = 1L;
	
	private Long id;

	@EqualsAndHashCode.Exclude
	private Integer start = 0;
	
	@EqualsAndHashCode.Exclude
	private Integer limit = 100;
	
	protected AbstractDto() {}
	
	protected AbstractDto(AbstractDomain domain) {
		this.id = domain.getId();
	}
}
