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
	
	@Text("缴款日期")
	@Column(nullable=false)
	private Date payDate;  //保险付款日期
	
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
	
	@Text("交强险金额")
	@Column(nullable=false)
	private BigDecimal compulsoryMoney;
	
	@Text("交强险生效日期")
	@Column(nullable=false)
	private Date compulsoryBeginDate;
	
	@Text("交强险截至日期")
	@Column(nullable=false)
	private Date compulsoryEndDate;
	
	@Text("交强险承保金额")
	@Column(nullable=false)
	private BigDecimal compulsoryCoverageMoney;
	
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
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
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
	public BigDecimal getCompulsoryCoverageMoney() {
		return compulsoryCoverageMoney;
	}
	public void setCompulsoryCoverageMoney(BigDecimal compulsoryCoverageMoney) {
		this.compulsoryCoverageMoney = compulsoryCoverageMoney;
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
