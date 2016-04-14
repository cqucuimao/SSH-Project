package com.yuqincar.domain.monitor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.BaseEntity;
/**
 * 用于异常行驶车辆的二次验证时第一次结果的存储
 * @author xuweiliang
 *
 */
@Entity
public class TemporaryWarning extends BaseEntity {

	@OneToOne(fetch=FetchType.LAZY)
	private Car car;
	private WarningMessageTypeEnum type;
	
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
}
