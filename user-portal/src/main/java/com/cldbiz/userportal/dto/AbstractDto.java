package com.cldbiz.userportal.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.data.domain.Sort;

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
	
	@EqualsAndHashCode.Exclude
	private List<Sort.Order> sortOrders;
	
	protected AbstractDto() {}
	
	protected AbstractDto(AbstractDomain domain) {
		this.id = domain.getId();
	}
}
