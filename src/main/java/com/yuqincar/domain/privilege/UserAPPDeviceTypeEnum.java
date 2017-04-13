package com.yuqincar.domain.privilege;

import com.yuqincar.domain.common.BaseEnum;

public enum UserAPPDeviceTypeEnum implements BaseEnum{
	IOS(0,"IOS"),
	ANDROID(1,"Android");
	
	private int id;
	private String label;
	
	UserAPPDeviceTypeEnum(int id,String label){
		this.id=id;
		this.label=label;
	}
	
	public int getId(){
		return id;
	}
	
	public String getLabel(){
		return label;
	}
	
	public UserAPPDeviceTypeEnum getById(int id) {
		for (UserAPPDeviceTypeEnum u : UserAPPDeviceTypeEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
