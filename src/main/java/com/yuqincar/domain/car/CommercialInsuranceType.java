package com.yuqincar.domain.car;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

/*
 * 商业保险种类
 */
@Entity
public class CommercialInsuranceType extends BaseEntity {
	@Text("名称")
	@Column(nullable=false)
	private String name;	//名称

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
