package com.yuqincar.domain.common;

import javax.persistence.Entity;

@Entity
public class VerificationCode extends BaseEntity {
	
	private String phoneNumber;
	private String code;	//验证码
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}
