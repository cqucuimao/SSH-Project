package com.yuqincar.domain.privilege;

import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

public enum UserStatusEnum implements BaseEnum{
	NORMAL(0,"正常"),	
	LOCKED(1,"已锁定");
	
	private int id;
	private String label;
	
	UserStatusEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public UserStatusEnum getById(int id) {
		for (UserStatusEnum u : UserStatusEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
