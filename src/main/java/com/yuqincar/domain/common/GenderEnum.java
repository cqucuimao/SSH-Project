package com.yuqincar.domain.common;


/**
 * 用户性别
 *
 */
public enum GenderEnum implements BaseEnum{

	
	MALE(0,"男"),	
	
	FEMALE(1,"女");

	private int id;
	private String label;
	
	GenderEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public GenderEnum getById(int id) {
		for (GenderEnum u : GenderEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
