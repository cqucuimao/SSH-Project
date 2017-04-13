package com.yuqincar.domain.car;

import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

public enum PlateTypeEnum implements BaseEnum {
	
	BLUE(0,"蓝牌"),
	
	YELLOW(1,"黄牌");
	
	private int id;
	private String label;
	
	PlateTypeEnum(int id, String label) {
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

	public PlateTypeEnum getById(int id) {
		for (PlateTypeEnum u : PlateTypeEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
