package com.yuqincar.domain.order;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;
/*
 * 他人（实际乘车人）
 */
@Entity
public class OtherPassenger extends BaseEntity {
	@Text("所属客户")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Customer customer;
		
	@Text("姓名")
	private String name;
	
	@Text("手机号码")
	private String phoneNumber;
	
	@Text("是否被删除")
	private boolean deleted;
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}		
}
