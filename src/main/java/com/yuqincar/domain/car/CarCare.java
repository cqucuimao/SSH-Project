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
 * 车辆保养记录
 */
@Entity
public class CarCare extends BaseEntity {

	@Text("车辆")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Car car;	//车辆
	
	@Text("司机")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private User driver;
	
	@Text("保养日期")
	@Column(nullable = false)
	private Date date;	//保养日期
	
	@Text("保养里程")
	private int careMiles;	//保养里程数
	
	@Text("保养金额")
	private BigDecimal money;	//保养花费
	
	@Text("保养内容")
	private String memo;	//备注
	
	@Text("保养单位")
	private String careDepo;	//保养单位
	
	public int getCareMiles() {
		return careMiles;
	}
	public void setCareMiles(int careMiles) {
		this.careMiles = careMiles;
	}
	public String getCareDepo() {
		return careDepo;
	}
	public void setCareDepo(String careDepo) {
		this.careDepo = careDepo;
	}	
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
		//return money.setScale(0, BigDecimal.ROUND_HALF_UP);
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
	public User getDriver() {
		return driver;
	}
	public void setDriver(User driver) {
		this.driver = driver;
	}
}
