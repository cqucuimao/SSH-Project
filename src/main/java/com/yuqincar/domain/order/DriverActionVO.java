package com.yuqincar.domain.order;

import java.util.Date;

public class DriverActionVO {
	private String id;
	private Date date;
	private OrderStatusEnum status;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public OrderStatusEnum getStatus() {
		return status;
	}
	public void setStatus(OrderStatusEnum status) {
		this.status = status;
	}
	public boolean equals(DriverActionVO davo){
		return id.equals(davo.getId());
	}
}
