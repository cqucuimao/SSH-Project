package com.yuqincar.timer;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.utils.CapcareMessageUtils;

@Component
public class CapcareData {
	
	@Scheduled(cron = "0/10 * * * * ? ") 
	@Transactional
	public void update(){
		CapcareMessageUtils.getCapcareMessage();
	}
}
