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

	@Scheduled(cron = "20 * * * * ?") // 每分钟（第20秒）执行一次
	@Transactional
	public void send() {
		System.out.println("in AsynchronizeSendSMS");
		sMSService.sendSMSInQueue();
	}
}
