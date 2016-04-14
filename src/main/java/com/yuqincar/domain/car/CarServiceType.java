package com.yuqincar.domain.car;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.utils.Text;

/*
 * 车型
 * 不同的车型有不同的价格
 */
@Entity
public class CarServiceType extends BaseEntity {

	@Text("车型描述")
	private String title;	//车型描述

	@Text("每公里价格")
	@Column(nullable=false)
	private BigDecimal pricePerKM;	//每公里的价格

	@Text("每天的价格")
	@Column(nullable=false)
	private BigDecimal pricePerDay;	//每天的价格

	@Text("价格描述")
	private String priceDescription;	//价格描述

	@Text("载客人数")
	private int personLimit;	//载客人数

	@Text("车型描述图片")
	@OneToOne(fetch=FetchType.LAZY)
	private DiskFile picture;	//车型描述图片

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPricePerKM() {
		return pricePerKM;
	}

	public void setPricePerKM(BigDecimal pricePerKM) {
		this.pricePerKM = pricePerKM;
	}

	public BigDecimal getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(BigDecimal pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	public String getPriceDescription() {
		return priceDescription;
	}

	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}

	public int getPersonLimit() {
		return personLimit;
	}

	public void setPersonLimit(int personLimit) {
		this.personLimit = personLimit;
	}

	public DiskFile getPicture() {
		return picture;
	}

	public void setPicture(DiskFile picture) {
		this.picture = picture;
	}
	
	
}
