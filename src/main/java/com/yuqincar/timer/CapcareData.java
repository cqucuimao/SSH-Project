package com.yuqincar.timer;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.service.monitor.CapcareMessageService;

@Component
public class CapcareData {
	@Autowired
	private CapcareMessageService capcareMessageService;
	
	@Scheduled(cron = "0/10 * * * * ? ") 
	@Transactional
	public void update(){
		capcareMessageService.getCapcareMessagePerTenSecondFromCapcare();
		//CapcareMessageUtils.getCapcareMessage();
	}
}
