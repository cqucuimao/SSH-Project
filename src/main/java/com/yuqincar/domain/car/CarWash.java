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
 * 洗车记录
 */
@Entity
public class CarWash extends BaseEntity {
	@Text("车辆")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Car car;
	
	@Text("司机")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private User driver;
	
	@Text("洗车日期")
	@Column(nullable=false)
	private Date date;
	
	@Text("洗车点")
	@OneToOne(fetch=FetchType.LAZY)
	private CarWashShop shop;
	
	@Text("金额")
	@Column(nullable=false)
	private BigDecimal money;
	
	@Text("内饰清洁金额")
	private BigDecimal innerCleanMoney;
	
	@Text("抛光打蜡金额")
	private BigDecimal polishingMoney;
	
	@Text("清洗发动机金额")
	private BigDecimal engineCleanMoney;
	
	@Text("座套清洗金额")
	private BigDecimal cushionCleanMoney;
	
	@Text("沥青清洗金额")
	private BigDecimal pitchCleanMoney;
	
	
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public CarWashShop getShop() {
		return shop;
	}

	public void setShop(CarWashShop shop) {
		this.shop = shop;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getInnerCleanMoney() {
		return innerCleanMoney;
	}

	public void setInnerCleanMoney(BigDecimal innerCleanMoney) {
		this.innerCleanMoney = innerCleanMoney;
	}

	public BigDecimal getPolishingMoney() {
		return polishingMoney;
	}

	public void setPolishingMoney(BigDecimal polishingMoney) {
		this.polishingMoney = polishingMoney;
	}

	public BigDecimal getEngineCleanMoney() {
		return engineCleanMoney;
	}

	public void setEngineCleanMoney(BigDecimal engineCleanMoney) {
		this.engineCleanMoney = engineCleanMoney;
	}

	public BigDecimal getCushionCleanMoney() {
		return cushionCleanMoney;
	}

	public void setCushionCleanMoney(BigDecimal cushionCleanMoney) {
		this.cushionCleanMoney = cushionCleanMoney;
	}

	public BigDecimal getPitchCleanMoney() {
		return pitchCleanMoney;
	}

	public void setPitchCleanMoney(BigDecimal pitchCleanMoney) {
		this.pitchCleanMoney = pitchCleanMoney;
	}

	
}
