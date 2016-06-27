package com.yuqincar.service.car.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.car.CarExamineDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarExamineService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarExamineServiceImpl implements CarExamineService {
	@Autowired
	private CarExamineDao carExamineDao;
	@Autowired
	private CarDao carDao;
	@Autowired
	private SMSService smsService;

	@Transactional
	public void saveCarExamine(CarExamine carExamine) {
		carExamineDao.save(carExamine);

	}

	@Transactional
	public void carExamineAppointment(Car car, User driver, Date date) {
		CarExamine carExamine = new CarExamine();
		carExamine.setCar(car);
		carExamine.setDriver(driver);
		carExamine.setDate(date);
		carExamine.setAppointment(true);
		carExamineDao.save(carExamine);

		//给司机发送短信
		Map<String,String> params=new HashMap<String,String>();
		params.put("carExamineDate", DateUtils.getYMDString(date));
		smsService.sendTemplateSMS(driver.getPhoneNumber(), SMSService.SMS_TEMPLATE_EXAMINE_APPOINTMENT, params);
	}

	public CarExamine getCarExamineById(long id) {
		return carExamineDao.getById(id);
	}

	public PageBean<CarExamineService> queryCarExamine(int pageNum, QueryHelper helper) {
		return carExamineDao.getPageBean(pageNum, helper);
	}

	public boolean canDeleteCarExamine(CarExamine carExamine) {
		return carExamine.isAppointment()==true;
	}

	@Transactional
	public void deleteCarExamineById(Long id) {
		carExamineDao.delete(id);

	}

	public boolean canUpdateCarExamine(CarExamine carExamine) {
		return carExamine.isAppointment()==true;
	}

	@Transactional
	public void updateCarExamine(CarExamine carExamine) {
		carExamineDao.update(carExamine);

	}

	public List<Car> getAllNeedExamineCars() {
		return carDao.getAllNeedExamineCars();
	}

}
