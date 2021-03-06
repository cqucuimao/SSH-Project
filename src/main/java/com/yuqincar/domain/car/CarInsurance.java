package com.yuqincar.domain.car;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
	
	@Text("保险起始日期")
	@Column(nullable=false)
	private Date fromDate;	

	@Text("保险过期日期")
	@Column(nullable=false)
	private Date toDate;	

	@Text("保险公司")
	@Column(nullable=false)
	private String insureCompany;	

	@Text("交强险保单号")
	@Column(nullable=false)
	private String compulsoryPolicyNumber;   
	
	@Text("商业险保单号")
	@Column(nullable=false)
	private String commercialPolicyNumber; 
	
	@Text("交强险金额")
	@Column(nullable=false)
	private BigDecimal compulsoryMoney;
	
	@Text("交强险生效日期")
	@Column(nullable=false)
	private Date compulsoryBeginDate;
	
	@Text("交强险截至日期")
	@Column(nullable=false)
	private Date compulsoryEndDate;
	
	@Text("车船税金额")
	@Column(nullable=false)
	private BigDecimal vehicleTaxMoney;
	
	@Text("车船税生效日期")
	@Column(nullable=false)
	private Date vehicleTaxBeginDate;
	
	@Text("车船税截至日期")
	@Column(nullable=false)
	private Date vehicleTaxEndDate;
	
	@Text("商业保险")
	@OneToMany(mappedBy="insurance")
	private List<CommercialInsurance> commercialInsuranceList;

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
	
	public String getCompulsoryPolicyNumber() {
		return compulsoryPolicyNumber;
	}
	public void setCompulsoryPolicyNumber(String compulsoryPolicyNumber) {
		this.compulsoryPolicyNumber = compulsoryPolicyNumber;
	}
	public String getCommercialPolicyNumber() {
		return commercialPolicyNumber;
	}
	public void setCommercialPolicyNumber(String commercialPolicyNumber) {
		this.commercialPolicyNumber = commercialPolicyNumber;
	}
	public BigDecimal getCompulsoryMoney() {
		return compulsoryMoney;
	}
	public void setCompulsoryMoney(BigDecimal compulsoryMoney) {
		this.compulsoryMoney = compulsoryMoney;
	}
	public List<CommercialInsurance> getCommercialInsuranceList() {
		return commercialInsuranceList;
	}
	public void setCommercialInsuranceList(
			List<CommercialInsurance> commercialInsuranceList) {
		this.commercialInsuranceList = commercialInsuranceList;
	}
	public Date getCompulsoryBeginDate() {
		return compulsoryBeginDate;
	}
	public void setCompulsoryBeginDate(Date compulsoryBeginDate) {
		this.compulsoryBeginDate = compulsoryBeginDate;
	}
	public Date getCompulsoryEndDate() {
		return compulsoryEndDate;
	}
	public void setCompulsoryEndDate(Date compulsoryEndDate) {
		this.compulsoryEndDate = compulsoryEndDate;
	}
	public BigDecimal getVehicleTaxMoney() {
		return vehicleTaxMoney;
	}
	public void setVehicleTaxMoney(BigDecimal vehicleTaxMoney) {
		this.vehicleTaxMoney = vehicleTaxMoney;
	}
	public Date getVehicleTaxBeginDate() {
		return vehicleTaxBeginDate;
	}
	public void setVehicleTaxBeginDate(Date vehicleTaxBeginDate) {
		this.vehicleTaxBeginDate = vehicleTaxBeginDate;
	}
	public Date getVehicleTaxEndDate() {
		return vehicleTaxEndDate;
	}
	public void setVehicleTaxEndDate(Date vehicleTaxEndDate) {
		this.vehicleTaxEndDate = vehicleTaxEndDate;
	}	
}
