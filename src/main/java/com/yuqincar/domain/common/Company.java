package com.yuqincar.domain.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.yuqincar.utils.Text;

@Entity
public class Company
{
	@Id
	@GeneratedValue
	@Text("编号")
	private Long id;
	
	@Text("公司名称")
	@Column
	private String name;
	
	@Text("订单前缀")
	@Column
	private String orderPrefix;
	
	@Text("协议订单前缀")
	@Column
	private String cooperationOrderPrefix;
	
	@Text("公司logo的文件名")
	@Column
	private String logoName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public String getOrderPrefix() {
		return orderPrefix;
	}

	public void setOrderPrefix(String orderPrefix) {
		this.orderPrefix = orderPrefix;
	}

	public String getCooperationOrderPrefix() {
		return cooperationOrderPrefix;
	}

	public void setCooperationOrderPrefix(String cooperationOrderPrefix) {
		this.cooperationOrderPrefix = cooperationOrderPrefix;
	}
	
}
