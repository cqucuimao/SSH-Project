package com.yuqincar.domain.order;

import com.yuqincar.domain.common.BaseEnum;

public enum ProtocolOrderPayPeriodEnum implements BaseEnum {
	MONTH(0,"每月"),
	QUARTER(1,"每季度"),
	YEAR(2,"每年"),
	ONCE(3,"一次性");
	
	private int id;
	private String label;
	
	ProtocolOrderPayPeriodEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public static ProtocolOrderPayPeriodEnum[] getAllEnum() {
		return ProtocolOrderPayPeriodEnum.values();
	}

	public static ProtocolOrderPayPeriodEnum getById(int id) {
		for (ProtocolOrderPayPeriodEnum u : ProtocolOrderPayPeriodEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}

	public static ProtocolOrderPayPeriodEnum getByLabel(String label) {
		for (ProtocolOrderPayPeriodEnum u : ProtocolOrderPayPeriodEnum.values()) {
			if (u.getLabel().equals(label))
				return u;
		}
		return null;
	}
}
