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
	
	@Text("是否做内饰清洁")
	private boolean doInnerClean;
	
	@Text("是否抛光打蜡")
	private boolean doPolishing;
	
	@Text("是否清洗发动机")
	private boolean doEngineClean;

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

	public boolean isDoInnerClean() {
		return doInnerClean;
	}

	public void setDoInnerClean(boolean doInnerClean) {
		this.doInnerClean = doInnerClean;
	}

	public boolean isDoPolishing() {
		return doPolishing;
	}

	public void setDoPolishing(boolean doPolishing) {
		this.doPolishing = doPolishing;
	}

	public boolean isDoEngineClean() {
		return doEngineClean;
	}

	public void setDoEngineClean(boolean doEngineClean) {
		this.doEngineClean = doEngineClean;
	}
}
