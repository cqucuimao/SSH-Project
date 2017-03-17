package com.yuqincar.timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.service.sms.SMSService;

@Component
public class AsynchronizeSendSMS {

	@Autowired
	public SMSService sMSService;

	@Scheduled(cron = "0/5 * * * * ?") // 每5秒执行一次
	@Transactional
	public void send() {
		sMSService.sendSMSInQueue();
	}
}
