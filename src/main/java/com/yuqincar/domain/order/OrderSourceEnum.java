package com.yuqincar.domain.order;

import com.yuqincar.utils.Text;

public enum OrderSourceEnum {
	@Text("调度员")
	SCHEDULER,
	@Text("客户端")
	APP,
	@Text("网站")
	WEB
}
