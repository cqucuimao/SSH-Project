package com.yuqincar.domain.car;

import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

public enum PlateTypeEnum implements BaseEnum {

	@Text("蓝牌牌照")
	BLUE(0,"蓝牌牌照"),
	@Text("黄牌牌照")
	YELLOW(1,"黄牌牌照");
	
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
}
