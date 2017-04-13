package com.yuqincar.domain.common;

public enum BooleanEnum implements BaseEnum{

	
	TRUE(0,"是"),	
	
	FALSE(1,"否");

	private int id;
	private String label;
	
	BooleanEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public BooleanEnum getById(int id) {
		for (BooleanEnum u : BooleanEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
