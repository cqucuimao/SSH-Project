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

@Entity
public class CarRefuel extends BaseEntity {

	@Text("车辆")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Car car;

	@Text("加油时间")
	@Column(nullable=false)
	private Date date;	//加油日期

	@Text("加油金额")
	@Column(nullable=false)
	private BigDecimal money;	//加油金额

	@Text("加油地点")
	private String refuelingSite;   //加油地点

	@Text("加油量")
	private int RefuelingCharge;   //加油量

	@Text("备注")
	private String memo;	//备注
	
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getRefuelingSite() {
		return refuelingSite;
	}

	public void setRefuelingSite(String refuelingSite) {
		this.refuelingSite = refuelingSite;
	}


	public int getRefuelingCharge() {
		return RefuelingCharge;
	}

	public void setRefuelingCharge(int refuelingCharge) {
		RefuelingCharge = refuelingCharge;
	}	
}
