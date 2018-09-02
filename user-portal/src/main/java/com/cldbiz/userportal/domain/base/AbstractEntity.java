package com.cldbiz.userportal.domain.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class AbstractEntity implements Entity {

	@Column(name = "RECORD_CREATE_USER_ID")
	private String recordCreateUserId;
	
	@Column(name = "RECORD_CREATE_TIME", length = 6)
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordCreateTime;

	@Column(name = "RECORD_UPDATE_USER_ID")
	private String recordUpdateUserId;

	@Column(name = "RECORD_UPDATE_TIME", length = 6)
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordUpdateTime;

	public String getRecordCreateUserId() {
		return recordCreateUserId;
	}

	public void setRecordCreateUserId(String recordCreateUserId) {
		this.recordCreateUserId = recordCreateUserId;
	}

	public Date getRecordCreateTime() {
		return recordCreateTime;
	}

	public void setRecordCreateTime(Date recordCreateTime) {
		this.recordCreateTime = recordCreateTime;
	}

	public String getRecordUpdateUserId() {
		return recordUpdateUserId;
	}

	public void setRecordUpdateUserId(String recordUpdateUserId) {
		this.recordUpdateUserId = recordUpdateUserId;
	}

	public Date getRecordUpdateTime() {
		return recordUpdateTime;
	}

	public void setRecordUpdateTime(Date recordUpdateTime) {
		this.recordUpdateTime = recordUpdateTime;
	}

}
