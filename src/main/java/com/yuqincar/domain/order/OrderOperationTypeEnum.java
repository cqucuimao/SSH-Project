package com.yuqincar.domain.order;

import com.yuqincar.utils.Text;

/*
 * 订单操作类型
 */
public enum OrderOperationTypeEnum {
	@Text("常规修改")
	MODIFY,
	@Text("延后结束")
	END_POSTPONE,	//针对已开始的日租车
	@Text("重新调度")
	RESCHEDULE,
	@Text("取消")
	CANCEL,
	@Text("修改派车单")
	MODIFY_SCHEDULE_FORM
}
