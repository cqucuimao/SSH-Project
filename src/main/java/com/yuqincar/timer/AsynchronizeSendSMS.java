package com.yuqincar.timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yuqincar.service.sms.SMSService;

@Component
public class AsynchronizeSendSMS {

	@Autowired
	public SMSService sMSService;

	@Scheduled(cron = "1 * * * * ?") // 每分钟（第1秒）执行一次
	public void send() {
		sMSService.sendSMSInQueue();
	}
}
