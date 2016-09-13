package com.yuqincar.service.car.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarExamineAppointmentDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarExamineAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarExamineAppointmentService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarExamineAppointmentServiceImpl implements CarExamineAppointmentService {
	@Autowired
	private CarExamineAppointmentDao carExamineAppointmentDao;
	@Autowired
	private SMSService smsService;
	@Transactional
	public void saveCarExamineAppointment(CarExamineAppointment carExamineAppointment) {
		carExamineAppointmentDao.save(carExamineAppointment);
		Map<String,String> params=new HashMap<String,String>();
		params.put("carExamineDate", DateUtils.getYMDString(carExamineAppointment.getDate()));
		smsService.sendTemplateSMS(carExamineAppointment.getDriver().getPhoneNumber(), SMSService.SMS_TEMPLATE_EXAMINE_APPOINTMENT, params);
		
	}

	public CarExamineAppointment getCarExamineAppointmentById(long id) {
		return carExamineAppointmentDao.getById(id);
	}

	public PageBean<CarExamineAppointment> queryCarExamineAppointment(int pageNum, QueryHelper helper) {
		return carExamineAppointmentDao.getPageBean(pageNum, helper);
	}

	@Transactional
	public void updateCarExamineAppointment(CarExamineAppointment carExamineAppointment) {
		carExamineAppointmentDao.update(carExamineAppointment);
	}

	public boolean isExistAppointment(long selfId, Car car) {
		return carExamineAppointmentDao.isExistAppointment(selfId,car);
	}
	@Transactional
	public void deleteCarExamineAppointmentById(long id) {
		carExamineAppointmentDao.delete(id);
	}
}
