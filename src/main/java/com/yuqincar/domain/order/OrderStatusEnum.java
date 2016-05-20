package com.yuqincar.domain.order;

import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

public enum OrderStatusEnum implements BaseEnum {

	@Text("在队列")
	INQUEUE(0,"在队列"),	//在队列
	@Text("已调度")
	SCHEDULED(1,"已调度"),	//已调度，下一步是ACCEPTED
	@Text("已接受")
	ACCEPTED(2,"已接受"), //接受，下一步是BEGIN
	@Text("已开始")
	BEGIN(3,"已开始"),	//已开始
	@Text("客户已上车")
	GETON(4,"客户已上车"),
	@Text("客户已下车")
	GETOFF(5,"客户已下车"),
	@Text("已结束")
	END(6,"已结束"),	//已结束
	@Text("已付费")
	PAYED(7,"已付费"),	//已付费
	@Text("已取消")
	CANCELLED(8,"已取消");	//已取消
	
	private int id;
	private String label;
	
	OrderStatusEnum(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public static OrderStatusEnum[] getAllEnum() {
		return OrderStatusEnum.values();
	}

	public static OrderStatusEnum getById(int id) {
		for (OrderStatusEnum u : OrderStatusEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}

	public static OrderStatusEnum getByLabel(String label) {
		for (OrderStatusEnum u : OrderStatusEnum.values()) {
			if (u.getLabel().equals(label))
				return u;
		}
		return null;
	}


	public String toString(){
		return getLabel();
	}
	
	public String getString(){
		return getLabel();
	}
}
