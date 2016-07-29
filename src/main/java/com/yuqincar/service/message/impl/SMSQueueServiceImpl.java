package com.yuqincar.service.message.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuqincar.dao.message.SMSQueueDao;
import com.yuqincar.domain.message.SMSQueue;
import com.yuqincar.service.message.SMSQueueService;

@Service
public class SMSQueueServiceImpl implements SMSQueueService {

	@Autowired
	private SMSQueueDao sMSQueueDao;

	public void saveSMSQueue(SMSQueue sMSQueue) {
		sMSQueueDao.save(sMSQueue);
	}

	public List<SMSQueue> getAllSMSQueue() {
		return sMSQueueDao.getAll();
	}

	public void deleteSMSQueue(Long id) {
		sMSQueueDao.delete(id);
	}

	public void updateSMSQueue(SMSQueue sMSQueue) {
		sMSQueueDao.update(sMSQueue);
	}
}
