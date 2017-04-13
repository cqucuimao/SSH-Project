package com.yuqincar.domain.order;

import com.yuqincar.domain.common.BaseEnum;

public enum OrderSourceEnum implements BaseEnum{
	
	SCHEDULER(0,"调度员"),
	
	APP(1,"客户端"),
	
	WEB(2,"网站");
	
	OrderSourceEnum(int id,String label){
		this.id = id;
		this.label = label;
	}
	
	private int id;
	private String label;

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}
	
	public OrderSourceEnum getById(int id) {
		for (OrderSourceEnum u : OrderSourceEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
