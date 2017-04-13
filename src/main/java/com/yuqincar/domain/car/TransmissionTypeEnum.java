package com.yuqincar.domain.car;

import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

public enum TransmissionTypeEnum implements BaseEnum {

	AUTO(0,"自动"),
	
	MANNUAL(1,"手动"),
	
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

	public TransmissionTypeEnum getById(int id) {
		for (TransmissionTypeEnum u : TransmissionTypeEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
