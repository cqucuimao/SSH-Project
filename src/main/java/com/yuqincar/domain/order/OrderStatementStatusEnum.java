package com.yuqincar.domain.order;

import com.yuqincar.domain.common.BaseEnum;

public enum OrderStatementStatusEnum implements BaseEnum {

	NEW(0,"新建"),
	
	INVOICED(1,"已开票"),
	
	PAID(2,"已回款");
	
	private int id;
	private String label;
	
	OrderStatementStatusEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public OrderStatementStatusEnum getById(int id) {
		for (OrderStatementStatusEnum u : OrderStatementStatusEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
