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

@Component
public class PullViolation {
	
	@Autowired
	CarViolationService carViolationService;

	@Scheduled(cron = "0 30 02 5 * ? ") 
	@Transactional
	public void update() throws UnsupportedEncodingException, ParseException {
		carViolationService.pullViolationFromCQJG();
		
	}
}
