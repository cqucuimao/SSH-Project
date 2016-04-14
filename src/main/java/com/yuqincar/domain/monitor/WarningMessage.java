package com.yuqincar.domain.monitor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

@Entity
public class WarningMessage extends BaseEntity {
	@Text("车辆")
	@OneToOne(fetch=FetchType.LAZY)
	private Car car;
	@Text("报警类型")
	private WarningMessageTypeEnum type;
	@Text("报警时间")
	private Date date;
	@Text("是否处理")
	private boolean dealed;
	
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	public WarningMessageTypeEnum getType() {
		return type;
	}
	public void setType(WarningMessageTypeEnum type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isDealed() {
		return dealed;
	}
	public void setDealed(boolean dealed) {
		this.dealed = dealed;
	}
}
