package com.yuqincar.domain.message;

import java.util.Date;

import javax.persistence.Entity;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

@Entity
public class SMSQueue extends BaseEntity {
	@Text("手机号码")
	private String phoneNumber;
	
	@Text("模版ID")
	private String templateId;
	
	@Text("短信参数")
	private String params;
	
	@Text("进队列时间")
	private Date inQueueDate;
	
	@Text("发送次数")
	private int tryTimes;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Date getInQueueDate() {
		return inQueueDate;
	}

	public void setInQueueDate(Date inQueueDate) {
		this.inQueueDate = inQueueDate;
	}

	public int getTryTimes() {
		return tryTimes;
	}

	public void setTryTimes(int tryTimes) {
		this.tryTimes = tryTimes;
	}
}
