package com.yuqincar.domain.car;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

/*
 * 洗车点
 */
@Entity
public class CarWashShop extends BaseEntity {

	@Text("洗车点名称")
	@Column(nullable=false)
	private String name;	//名称

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
