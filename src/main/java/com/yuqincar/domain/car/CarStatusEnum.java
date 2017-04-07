package com.yuqincar.domain.car;

import com.yuqincar.domain.common.BaseEnum;

public enum CarStatusEnum implements BaseEnum {
	NORMAL(0,"正常"),
	
	SCRAPPED(1,"已报废");
	
	private int id;
	private String label;
	
	CarStatusEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public static CarStatusEnum[] getAllEnum() {
		return CarStatusEnum.values();
	}

	public CarStatusEnum getById(int id) {
		for (CarStatusEnum u : CarStatusEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}

	public static CarStatusEnum getByLabel(String label) {
		for (CarStatusEnum u : CarStatusEnum.values()) {
			if (u.getLabel().equals(label))
				return u;
		}
		return null;
	}


	public String toString(){
		return getLabel();
	}
	
	public String getString(){
		return getLabel();
	}
}
