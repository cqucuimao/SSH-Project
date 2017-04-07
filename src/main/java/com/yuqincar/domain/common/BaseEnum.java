package com.yuqincar.domain.common;

import java.io.Serializable;

public interface BaseEnum extends Serializable {
	public int getId();

	public String getLabel();
	
	public BaseEnum getById(int id);
}
