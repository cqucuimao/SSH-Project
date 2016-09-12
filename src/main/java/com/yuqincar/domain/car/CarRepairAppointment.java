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

@Entity
public class CarRepairAppointment extends BaseEntity {

	@Text("车辆")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Car car;
	
	@Text("司机")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private User driver;
	
	@Text("起始时间")
	@Column(nullable=false)
	private Date fromDate;	//起始时间

	@Text("结束时间")
	private Date toDate;	//截止时间
	
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

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}	
}
