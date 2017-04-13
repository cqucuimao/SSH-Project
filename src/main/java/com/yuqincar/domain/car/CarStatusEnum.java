package com.yuqincar.domain.car;

import com.yuqincar.domain.common.BaseEnum;

public enum CarStatusEnum implements BaseEnum{
	
	NORMAL(0,"正常"),
	
	SCRAPPED(1,"已报废");
	
	CarStatusEnum(int id,String label){
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
	
	public CarStatusEnum getById(int id) {
		for (CarStatusEnum u : CarStatusEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
