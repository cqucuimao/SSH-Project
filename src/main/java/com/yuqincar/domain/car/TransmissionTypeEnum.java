package com.yuqincar.domain.car;

import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

public enum TransmissionTypeEnum implements BaseEnum {

	@Text("自动")
	AUTO(0,"自动"),
	@Text("手动")
	MANNUAL(1,"手动"),
	@Text("不确定")
	UNKNOWN(2,"不确定");
	
	private int id;
	private String label;
	
	TransmissionTypeEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public static TransmissionTypeEnum[] getAllEnum() {
		return TransmissionTypeEnum.values();
	}

	public static TransmissionTypeEnum getById(int id) {
		for (TransmissionTypeEnum u : TransmissionTypeEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}

	public static TransmissionTypeEnum getByLabel(String label) {
		for (TransmissionTypeEnum u : TransmissionTypeEnum.values()) {
			if (u.getLabel().equals(label))
				return u;
		}
		return null;
	}
}
