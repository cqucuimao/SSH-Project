package com.yuqincar.domain.privilege;

import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

/*
 * 用户类别
 */
public enum UserTypeEnum implements BaseEnum{
	
	OFFICE(0,"办公室员工"),	
	
	DRIVER(1,"司机员工");
	

	private int id;
	private String label;
	
	UserTypeEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public UserTypeEnum getById(int id) {
		for (UserTypeEnum u : UserTypeEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
