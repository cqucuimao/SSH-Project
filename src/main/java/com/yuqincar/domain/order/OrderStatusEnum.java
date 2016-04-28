package com.yuqincar.domain.order;

import com.yuqincar.utils.Text;

public enum OrderStatusEnum {
	@Text("在队列")
	INQUEUE,	//在队列
	@Text("已调度")
	SCHEDULED,	//已调度，下一步是ACCEPTED
	@Text("已接受")
	ACCEPTED, //接受，下一步是BEGIN
	@Text("已开始")
	BEGIN,	//已开始
	@Text("已结束")
	END,	//已结束
	@Text("已付费")
	PAYED,	//已付费
	@Text("已取消")
	CANCELLED;	//已取消

	public String toString(){
		switch(this){
		case INQUEUE:	return "在队列";
		case SCHEDULED:	return "已调度";
		case ACCEPTED:	return "已接受";
		case BEGIN:	return "已开始";
		case END:	return "已结束";
		case PAYED:	return "已付费";
		case CANCELLED:	return "已取消";
		}
		return "";
	}
	
	public String getString(){
		return toString();
	}
}
