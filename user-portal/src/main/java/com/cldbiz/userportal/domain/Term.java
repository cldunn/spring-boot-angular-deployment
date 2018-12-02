package com.cldbiz.userportal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cldbiz.userportal.dto.TermDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/************************************************
 * 
 * @author cliff
 * 
 * INVOICE PAYMENT TERMS
 * Net monthly account	          Payment due on last day of the month following the one in which the invoice is dated
 * PIA	                          Payment in advance
 * Net 7	                      Payment seven days after invoice date
 * Net 10	                      Payment ten days after invoice date
 * Net 30	                      Payment 30 days after invoice date
 * Net 60	                      Payment 60 days after invoice date
 * Net 90	                      Payment 90 days after invoice date
 * EOM	                          End of month
 * 21 MFI	                      21st of the month following invoice date
 * 1% 10 Net 30	                  1% discount if payment received within ten days otherwise payment 30 days after invoice date
 * COD	                          Cash on delivery
 * Cash account	(CA)              Account conducted on a cash basis, no credit
 * Letter of credit	(LC)          A documentary credit confirmed by a bank, often used for export
 * Bill of exchange	(BE)          A promise to pay at a later date, usually supported by a bank
 * CND	                          Cash next delivery
 * CBS	                          Cash before shipment
 * CIA	                          Cash in advance
 * CWO	                          Cash with order
 * 1MD	                          Monthly credit payment of a full month's supply
 * 2MD	                          As above plus an extra calendar month
 * Contra	                      Payment from the customer offset against the value of supplies purchased from the customer
 * Stage payment                  Payment of agreed amounts at stage
*************************************************/

@Entity
@Table(name = "TERM")
@EqualsAndHashCode(callSuper=true)
public @Data class Term extends AbstractDomain {

	@Column(length=40, nullable=false)
	private String code;
	
	@Column(length=255)
	private String description;
	
	public Term() {
		super();
	}
	
	public Term(TermDto termDto) {
		super(termDto);
		
		this.setCode(termDto.getCode());
		this.setDescription(termDto.getDescription());
	}
}
