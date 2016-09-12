package com.yuqincar.domain.car;

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
 * 车辆保养预约记录
 */
@Entity
public class CarCareAppointment extends BaseEntity {

	@Text("车辆")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Car car;
	
	@Text("司机")
	@OneToOne(fetch=FetchType.LAZY)
	private User driver;
	
	@Text("保养日期")
	@Column(nullable = false)
	private Date date;
	
	@Text("是否完成")
	private boolean done;

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

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}	
}
