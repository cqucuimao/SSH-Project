package com.yuqincar.domain.order;

import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.utils.Text;

public enum OrderStatusEnum implements BaseEnum {

	INQUEUE(0,"在队列"),	//在队列
	
	SCHEDULED(1,"已调度"),	//已调度，下一步是ACCEPTED
	
	ACCEPTED(2,"已接受"), //接受，下一步是BEGIN
	
	BEGIN(3,"已开始"),	//已开始
	
	GETON(4,"客户已上车"),
	
	GETOFF(5,"客户已下车"),
	
	END(6,"已结束"),	//已结束
	
	PAID(7,"已付费"),	//已付费
	
	CANCELLED(8,"已取消"),	//已取消
	
	UNDONE(9,"未完成");	//未完成 没有订单属于这个状态，只是为了查询时使用。  等价于  SCHEDULED || ACCEPTED || BEGIN || GETON || GETOFF
	
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

	public OrderStatusEnum getById(int id) {
		for (OrderStatusEnum u : OrderStatusEnum.values()) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
}
