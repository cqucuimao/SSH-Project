package com.yuqincar.domain.monitor;

import com.yuqincar.domain.common.BaseEnum;

public enum WarningMessageTypeEnum implements BaseEnum{
	PULLEDOUT(0,"设备拔出"),
	UNPLANNED_RUNNING(1,"非计划形式");
	
	private int id;
	private String label;
	
	WarningMessageTypeEnum(int id, String label){
		this.id=id;
		this.label=label;
	}
	
	public int getId(){
		return id;
	}
	
	public String getLabel(){
		return label;
	}
	
	public WarningMessageTypeEnum getById(int id) {
		for (WarningMessageTypeEnum u : WarningMessageTypeEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
