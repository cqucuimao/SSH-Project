package com.yuqincar.domain.car;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.privilege.Department;
import com.yuqincar.utils.Text;

/*
 * 车型
 * 不同的车型有不同的价格
 */
@Entity
public class CarServiceType extends BaseEntity {

	@Text("车型描述")
	private String title;	//车型描述
	
	@Text("车型大类")
	@ManyToOne(fetch=FetchType.LAZY )
	private CarServiceSuperType superType;

	@Text("车型描述图片")
	@OneToOne(fetch=FetchType.LAZY)
	private DiskFile picture;	//车型描述图片

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DiskFile getPicture() {
		return picture;
	}

	public void setPicture(DiskFile picture) {
		this.picture = picture;
	}

	public CarServiceSuperType getSuperType() {
		return superType;
	}

	public void setSuperType(CarServiceSuperType superType) {
		this.superType = superType;
	}
}
