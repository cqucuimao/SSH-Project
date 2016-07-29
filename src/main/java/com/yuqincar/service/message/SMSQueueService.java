package com.yuqincar.service.message;

import java.util.List;

import com.yuqincar.domain.message.SMSQueue;

public interface SMSQueueService {
	public void saveSMSQueue(SMSQueue sMSQueue);

	public List<SMSQueue> getAllSMSQueue();

	public void deleteSMSQueue(Long id);

	public void updateSMSQueue(SMSQueue sMSQueue);
}
