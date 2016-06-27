package com.yuqincar.domain.car;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

/*
 * 路桥费
 */
@Entity
public class TollCharge extends BaseEntity {
	@Text("车辆")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Car car;
	@Text("缴款日期")
	private Date payDate;
	@Text("缴款金额")
	private BigDecimal money;
	@Text("滞纳金")
	private BigDecimal overdueFine;
	@Text("补卡工本费")
	private BigDecimal moneyForCardReplace;
	@Text("下次缴纳日期")
	private Date nextPayDate;
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public BigDecimal getOverdueFine() {
		return overdueFine;
	}
	public void setOverdueFine(BigDecimal overdueFine) {
		this.overdueFine = overdueFine;
	}
	public BigDecimal getMoneyForCardReplace() {
		return moneyForCardReplace;
	}
	public void setMoneyForCardReplace(BigDecimal moneyForCardReplace) {
		this.moneyForCardReplace = moneyForCardReplace;
	}
	public Date getNextPayDate() {
		return nextPayDate;
	}
	public void setNextPayDate(Date nextPayDate) {
		this.nextPayDate = nextPayDate;
	}	
}
