package com.yuqincar.domain.order;

import com.yuqincar.domain.common.BaseEnum;

public enum ChargeModeEnum implements BaseEnum {
	MILE(0,"按里程计费"),
	
	DAY(1,"按天计费"),
	
	PROTOCOL(2,"协议计费"),
	
	PLANE(3,"接送机");
	
	private int id;
	private String label;
	
	ChargeModeEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public ChargeModeEnum getById(int id) {
		for (ChargeModeEnum u : ChargeModeEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}

	
	private static ChargeModeEnum getByLabel(String label) {
		for (ChargeModeEnum u : ChargeModeEnum.values()) {
			if (u.getLabel().equals(label))
				return u;
		}
		return null;
	}
	
	public static ChargeModeEnum fromString(String str){
		if(str==null || str.length()==0)
			return null;
		if(str.equals("mile"))
			return MILE;
		else if(str.equals("day"))
			return DAY;
		else if(str.equals("protocol"))
			return PROTOCOL;
		else if(str.equals("plane"))
			return PLANE;
		
		return getByLabel(str);
	} 
}
