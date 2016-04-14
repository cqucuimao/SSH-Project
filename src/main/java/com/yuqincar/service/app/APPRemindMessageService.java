package com.yuqincar.service.app;

import com.yuqincar.domain.order.APPRemindMessage;

public interface APPRemindMessageService {
	public void save(APPRemindMessage message);

	public APPRemindMessage getById(Long id) ;
	public void update(APPRemindMessage message);


}
