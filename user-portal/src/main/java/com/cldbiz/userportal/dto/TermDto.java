package com.cldbiz.userportal.dto;

import java.util.Arrays;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Sort;

import com.cldbiz.userportal.domain.Term;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public @Data class TermDto extends AbstractDto {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Max(40)
	private String code;
	
	@NotNull
	@Max(255)
	private String description;
	
	public TermDto() {
		super();
		
		this.setSortOrders(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "code")));
	}
	
	public TermDto(Term term) {
		super(term);
		
		this.setCode(term.getCode());
		this.setDescription(term.getDescription());
		
		this.setSortOrders(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "code")));
	}

}
