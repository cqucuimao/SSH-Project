package com.yuqincar.domain.order;

import com.yuqincar.domain.car.PlateTypeEnum;
import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

public enum ReserveCarApplyOrderStatusEnum implements BaseEnum {
	NEW(0,"新建"),
	
	SUBMITTED(1,"已提交审核"),
	
	REJECTED(2,"被驳回"),
	
	APPROVED(3,"审核通过"),
	
	EXPIRED(4,"过期");
	
	private int id;
	private String label;
	
	ReserveCarApplyOrderStatusEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public static ReserveCarApplyOrderStatusEnum[] getAllEnum() {
		return ReserveCarApplyOrderStatusEnum.values();
	}

	public static ReserveCarApplyOrderStatusEnum getById(int id) {
		for (ReserveCarApplyOrderStatusEnum u : ReserveCarApplyOrderStatusEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}

	public static ReserveCarApplyOrderStatusEnum getByLabel(String label) {
		for (ReserveCarApplyOrderStatusEnum u : ReserveCarApplyOrderStatusEnum.values()) {
			if (u.getLabel().equals(label))
				return u;
		}
		return null;
	}
}
