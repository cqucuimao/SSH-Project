package com.yuqincar.domain.message;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

@Entity
public class SMSFailRecord extends BaseEntity{
	@Text("发送日期")
	private Date date;

	@Column(nullable=false)
	@Text("手机号码")
	private String phoneNumber; 
	
	@Text("短信内容")
	private String content;

	@Text("短信内容")
	private String errorMemo;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getErrorMemo() {
		return errorMemo;
	}

	public void setErrorMemo(String errorMemo) {
		this.errorMemo = errorMemo;
	} 
}
