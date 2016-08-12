package com.yuqincar.domain.order;

import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

public enum OrderStatementStatusEnum implements BaseEnum {

	@Text("新建")
	NEW(0,"新建"),
	@Text("已开票")
	INVOICED(1,"已开票"),
	@Text("已回款")
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

	public static OrderStatementStatusEnum[] getAllEnum() {
		return OrderStatementStatusEnum.values();
	}

	public static OrderStatementStatusEnum getById(int id) {
		for (OrderStatementStatusEnum u : OrderStatementStatusEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}

	public static OrderStatementStatusEnum getByLabel(String label) {
		for (OrderStatementStatusEnum u : OrderStatementStatusEnum.values()) {
			if (u.getLabel().equals(label))
				return u;
		}
		return null;
	}
}
