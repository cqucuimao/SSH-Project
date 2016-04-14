package com.yuqincar.service.app.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.monitor.APPRemindMessageDao;
import com.yuqincar.domain.order.APPRemindMessage;
import com.yuqincar.service.app.APPRemindMessageService;

@Service
public class APPRemindMessageServiceImpl implements APPRemindMessageService {

	@Autowired
	private APPRemindMessageDao appRemindMessageDao;
	
	@Transactional
	public void save(APPRemindMessage message) {
		appRemindMessageDao.save(message);
	}
	
	public APPRemindMessage getById(Long id) {
		return appRemindMessageDao.getById(id);
	}
	
	@Transactional
	public void update(APPRemindMessage message) {
		appRemindMessageDao.update(message);
	}
}
