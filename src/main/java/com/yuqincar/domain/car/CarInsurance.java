package com.yuqincar.domain.car;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

/*
 * 车辆保险购买记录
 */
@Entity
public class CarInsurance extends BaseEntity {

	@Text("车辆")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Car car;	//车辆
	
	@Text("缴款日期")
	@Column(nullable=false)
	private Date payDate;  //保险付款日期

	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	@Text("保险起始日期")
	@Column(nullable=false)
	private Date fromDate;	//保险起始时间

	@Text("保险过期日期")
	@Column(nullable=false)
	private Date toDate;	//保险过期时间	

	@Text("保险公司")
	@Column(nullable=false)
	private String insureCompany;	//保险公司

	@Text("保单号")
	@Column(nullable=false)
	private String policyNumber;   //保单号

	@Text("保险种类")
	@Column(nullable=false)
	private String insureType;   //保险种类

	@Text("保险金额")
	@Column(nullable=false)
	private BigDecimal money;	//保险金额

	@Text("备注")
	private String memo;	//备注
	
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getInsureCompany() {
		return insureCompany;
	}
	public void setInsureCompany(String insureCompany) {
		this.insureCompany = insureCompany;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getInsureType() {
		return insureType;
	}
	public void setInsureType(String insureType) {
		this.insureType = insureType;
	}	
}
