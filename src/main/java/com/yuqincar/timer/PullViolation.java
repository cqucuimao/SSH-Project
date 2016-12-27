package com.yuqincar.timer;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.car.CarViolationService;
import com.yuqincar.utils.Configuration;

@Component
public class PullViolation {
	
	@Autowired
	CarViolationService carViolationService;

	@Scheduled(cron = "0 10 0 5 * ? ") 
	@Transactional
	public void update() throws UnsupportedEncodingException, ParseException {
		if("on".equals(Configuration.getPullViolationSwitch())){
			carViolationService.pullViolationFromCQJG();
		}
	}
}
