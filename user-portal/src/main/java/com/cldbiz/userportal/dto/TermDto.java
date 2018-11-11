package com.cldbiz.userportal.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

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
	}
	
	public TermDto(Term term) {
		super(term);
		
		this.setCode(term.getCode());
		this.setDescription(term.getDescription());
	}

}
