package com.yuqincar.domain.car;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Text;

@Entity
public class CarRefuel extends BaseEntity {
	@Text("流水号")
	private String sn;
	
	@Text("车辆")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Car car;

	@Text("司机")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private User driver;

	@Text("加油时间")
	@Column(nullable=false)
	private Date date;
	
	@Text("加油数量")
	@Column(nullable=false)
	private float volume;
	
	@Text("加油金额")
	@Column(nullable=false)
	private BigDecimal money;

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}	
	
}
