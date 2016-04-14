package com.yuqincar.domain.car;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

/*
 * 车辆违章记录
 */
@Entity
public class CarViolation extends BaseEntity {

	@Text("车辆")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Car car;	//车辆

	@Text("违章时间")
	@Column(nullable=false)
	private Date date;	//违章日期

	@Text("违章地点")
	@Column(nullable=false)
	private String place;	//违章地点

	@Text("违章事实")
	@Column(nullable=false)
	private String description;	//违章事实

	@Text("罚分")
	private int penaltyPoint;	//罚分

	@Text("罚款")
	private BigDecimal penaltyMoney;	//罚款

	@Text("是否已经处理")
	private boolean dealt;	//是否已经处理

	@Text("处理日期")
	private Date dealtDate;	//处理日期

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

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPenaltyPoint() {
		return penaltyPoint;
	}

	public void setPenaltyPoint(int penaltyPoint) {
		this.penaltyPoint = penaltyPoint;
	}

	public BigDecimal getPenaltyMoney() {
		return penaltyMoney;
	}

	public void setPenaltyMoney(BigDecimal penaltyMoney) {
		this.penaltyMoney = penaltyMoney;
	}

	public boolean isDealt() {
		return dealt;
	}

	public void setDealt(boolean dealt) {
		this.dealt = dealt;
	}

	public Date getDealtDate() {
		return dealtDate;
	}

	public void setDealtDate(Date dealtDate) {
		this.dealtDate = dealtDate;
	}
}
