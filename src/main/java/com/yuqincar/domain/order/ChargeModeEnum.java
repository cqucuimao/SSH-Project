package com.yuqincar.domain.order;

import com.yuqincar.domain.car.PlateTypeEnum;
import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

public enum ChargeModeEnum implements BaseEnum {
	@Text("按里程计费")
	MILE(0,"按里程计费"),
	
	@Text("按天计费")
	DAY(1,"按天计费"),
	
	@Text("协议计费")
	PROTOCOL(2,"协议计费"),
	
	@Text("接送机")
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

	public static PlateTypeEnum[] getAllEnum() {
		return PlateTypeEnum.values();
	}

	public static PlateTypeEnum getById(int id) {
		for (PlateTypeEnum u : PlateTypeEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}

	public static PlateTypeEnum getByLabel(String label) {
		for (PlateTypeEnum u : PlateTypeEnum.values()) {
			if (u.getLabel().equals(label))
				return u;
		}
		return null;
	}
	
	public static ChargeModeEnum fromString(String str){
		if(str==null || str.length()==0)
			return null;
		if(str.equals("mile") || str.equals("按里程计费"))
			return MILE;
		else if(str.equals("day") || str.equals("按天计费"))
			return DAY;
		else if(str.equals("protocol") || str.equals("按协议计费"))
			return PROTOCOL;
		else if(str.equals("plane") || str.equals("接送机"))
			return PLANE;
		return null;
	} 
}
