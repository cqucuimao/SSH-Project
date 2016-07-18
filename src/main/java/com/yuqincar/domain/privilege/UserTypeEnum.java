package com.yuqincar.domain.privilege;

import com.yuqincar.domain.car.PlateTypeEnum;
import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

/*
 * 用户类别
 */
public enum UserTypeEnum implements BaseEnum{
	@Text("办公室员工")
	OFFICE(0,"办公室员工"),	
	@Text("司机员工")
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

	public static UserTypeEnum[] getAllEnum() {
		return UserTypeEnum.values();
	}

	public static UserTypeEnum getById(int id) {
		for (UserTypeEnum u : UserTypeEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}

	public static UserTypeEnum getByLabel(String label) {
		for (UserTypeEnum u : UserTypeEnum.values()) {
			if (u.getLabel().equals(label))
				return u;
		}
		return null;
	}
}
