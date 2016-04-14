package com.yuqincar.domain.order;

import com.yuqincar.utils.Text;

/*
 * 订单操作类型
 */
public enum OrderOperationTypeEnum {
	@Text("常规修改")
	MODIFY,	//常规修改
	@Text("延后结束")
	END_POSTPONE,	//延后。针对已开始的日租车
	@Text("重新调度")
	RESCHEDULE,	//重新调度。
	@Text("取消")
	CANCEL,	//取消。
	@Text("修改订单里程")
	MODIFY_MILE,	//修改订单里程
	@Text("修改订单金额")
	MODIFY_MONEY	//修改订单里程
}
