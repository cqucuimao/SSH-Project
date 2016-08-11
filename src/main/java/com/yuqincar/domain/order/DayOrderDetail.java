package com.yuqincar.domain.order;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

/*
 * 订单执行过程中的某一天执行过程
 */
@Entity
public class DayOrderDetail extends BaseEntity {

	@Text("所属订单")
	@ManyToOne(fetch=FetchType.LAZY )
	private Order order;
	
	@Text("上车时间")
	private Date getonDate;

	@Text("下车时间")
	private Date getoffDate;
	
	@Text("经过地点摘要")
	@Column(length=1024)
	private String pathAbstract;

	@Text("实际公里")
	private float actualMile;

	@Text("收费公里")
	private float chargeMile;

	@Text("收费")
	private BigDecimal chargeMoney;

	public Date getGetonDate() {
		return getonDate;
	}

	public void setGetonDate(Date getonDate) {
		this.getonDate = getonDate;
	}

	public Date getGetoffDate() {
		return getoffDate;
	}

	public void setGetoffDate(Date getoffDate) {
		this.getoffDate = getoffDate;
	}

	public String getPathAbstract() {
		return pathAbstract;
	}

	public void setPathAbstract(String pathAbstract) {
		if(pathAbstract!=null && pathAbstract.length()>1024)
			pathAbstract=pathAbstract.substring(0, 1024);
		this.pathAbstract = pathAbstract;
	}

	public float getActualMile() {
		return actualMile;
	}

	public void setActualMile(float actualMile) {
		this.actualMile = actualMile;
	}

	public float getChargeMile() {
		return chargeMile;
	}

	public void setChargeMile(float chargeMile) {
		this.chargeMile = chargeMile;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public BigDecimal getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(BigDecimal chargeMoney) {
		this.chargeMoney = chargeMoney;
	}		
}
