package com.yuqincar.domain.car;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Text;

/*
 * 车辆维修记录
 */
@Entity
public class CarRepair extends BaseEntity {

	@Text("车辆")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Car car;	//车辆
	
	@Text("司机")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private User driver;
	
	@Text("维修原因")
	private String reason;	//维修原因
	
	@Text("起始时间")
	@Column(nullable=false)
	private Date fromDate;	//起始时间

	@Text("结束时间")
	private Date toDate;	//截止时间

	@Text("维修金额")
	private BigDecimal money;	//维修花费
	
	@Text("未赔付金额")
	private BigDecimal moneyNoGuaranteed;	//未赔付金额

	@Text("维修地点")
	private String repairLocation;   //维修地点

	@Text("维修内容")
	private String memo;	//备注

	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
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
	public String getRepairLocation() {
		return repairLocation;
	}
	public void setRepairLocation(String repairLocation) {
		this.repairLocation = repairLocation;
	}
	public User getDriver() {
		return driver;
	}
	public void setDriver(User driver) {
		this.driver = driver;
	}
	public BigDecimal getMoneyNoGuaranteed() {
		return moneyNoGuaranteed;
	}
	public void setMoneyNoGuaranteed(BigDecimal moneyNoGuaranteed) {
		this.moneyNoGuaranteed = moneyNoGuaranteed;
	}	
}
