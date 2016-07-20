package com.yuqincar.service.car.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarRepairDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarRepairService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarRepairServiceImpl implements CarRepairService {
	@Autowired
	private CarRepairDao carRepairDao;
	@Autowired
	private SMSService smsService;

	@Transactional
	public void saveCarRepair(CarRepair carRepair) {
		carRepairDao.save(carRepair);

	}

	@Transactional
	public void carRepairAppointment(Car car, User driver, Date fromDate, Date toDate) {
		CarRepair carRepair = new CarRepair();
		carRepair.setCar(car);
		carRepair.setDriver(driver);
		carRepair.setFromDate(fromDate);
		carRepair.setToDate(toDate);
		carRepair.setAppointment(true);
		carRepairDao.save(carRepair);

		//给司机发送短信
		Map<String,String> params=new HashMap<String,String>();
		params.put("carRepairDate", DateUtils.getYMDString(fromDate)+" 至 "+DateUtils.getYMDString(toDate));
		smsService.sendTemplateSMS(driver.getPhoneNumber(), SMSService.SMS_TEMPLATE_REPAIR_APPOINTMENT, params);
	}

	public CarRepair getCarRepairById(long id) {
		return carRepairDao.getById(id);
	}

	public PageBean<CarRepair> queryCarRepair(int pageNum, QueryHelper helper) {
		return carRepairDao.getPageBean(pageNum, helper);
	}

	public boolean canDeleteCarRepair(CarRepair carRepair) {
		return carRepair.isAppointment()==true;
	}

	@Transactional
	public void deleteCarRepairById(Long id) {
		carRepairDao.delete(id);

	}

	public boolean canUpdateCarRepair(CarRepair carRepair) {
		return carRepair.isAppointment()==true;
	}

	@Transactional
	public void updateCarRepair(CarRepair carRepair) {
		carRepairDao.update(carRepair);

	}
	
	public BigDecimal statisticCarRepair(Date fromDate, Date toDate){
		return carRepairDao.statisticCarRepair(fromDate, toDate);
	}

}
