package com.yuqincar.domain.privilege;

import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

/**
 * 用户性别
 *
 */
public enum UserGenderEnum implements BaseEnum{

	@Text("男")
	MALE(0,"男"),	
	@Text("女")
	FEMALE(1,"女");

	private int id;
	private String label;
	
	UserGenderEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public static UserGenderEnum[] getAllEnum() {
		return UserGenderEnum.values();
	}

	public static UserGenderEnum getById(int id) {
		for (UserGenderEnum u : UserGenderEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}

	public static UserGenderEnum getByLabel(String label) {
		for (UserGenderEnum u : UserGenderEnum.values()) {
			if (u.getLabel().equals(label))
				return u;
		}
		return null;
	}
}
