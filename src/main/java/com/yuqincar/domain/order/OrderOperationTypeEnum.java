package com.yuqincar.domain.order;

import com.yuqincar.domain.common.BaseEnum;

/*
 * 订单操作类型
 */
public enum OrderOperationTypeEnum implements BaseEnum{
	
	MODIFY(0,"常规修改"),
	
	END_POSTPONE(1,"延后结束"),	//针对已开始的日租车
	
	RESCHEDULE(2,"重新调度"),
	
	CANCEL(3,"取消"),
	
	MODIFY_ORDER_CHECK(4,"修改派车单"),
	
	EDIT_DRIVER_ACTION(5,"编辑司机动作");
	
	private int id;
	private String label;
	
	OrderOperationTypeEnum(int id,String label){
		this.id=id;
		this.label=label;
	}
	
	public int getId(){
		return id;
	}
	
	public String getLabel(){
		return label;
	}

	public BaseEnum getById(int id) {
		for (ChargeModeEnum u : ChargeModeEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}	
}
