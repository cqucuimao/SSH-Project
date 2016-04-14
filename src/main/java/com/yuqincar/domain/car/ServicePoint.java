package com.yuqincar.domain.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.order.Address;
import com.yuqincar.utils.Text;

/*
 * 业务点
 */
@Entity
public class ServicePoint extends BaseEntity {

	@Text("业务点名称")
	@Column(nullable=false)
	private String name;	//名称

	@Text("位置")
	@OneToOne(fetch=FetchType.LAZY)
	private Address pointAddress;	//位置

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getPointAddress() {
		return pointAddress;
	}

	public void setPointAddress(Address pointAddress) {
		this.pointAddress = pointAddress;
	}
	
}
