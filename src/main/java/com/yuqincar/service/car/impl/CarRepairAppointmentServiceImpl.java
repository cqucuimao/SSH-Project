package com.yuqincar.service.car.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarRepairAppointmentDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarRepairAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarRepairAppointmentService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarRepairAppointmentServiceImpl implements CarRepairAppointmentService {
	@Autowired
	private CarRepairAppointmentDao carRepairAppointmentDao;
	@Autowired
	private SMSService smsService;
	@Transactional
	public void saveCarRepairAppointment(CarRepairAppointment carRepairAppointment) {
		carRepairAppointmentDao.save(carRepairAppointment);
    	Map<String,String> params=new HashMap<String,String>();
		params.put("carRepairDate", DateUtils.getYMDString(carRepairAppointment.getFromDate()));
		smsService.sendTemplateSMS(carRepairAppointment.getDriver().getPhoneNumber(), SMSService.SMS_TEMPLATE_REPAIR_APPOINTMENT, params);
		
	}

	public CarRepairAppointment getCarRepairAppointmentById(long id) {
		return carRepairAppointmentDao.getById(id);
	}

	public PageBean<CarRepairAppointment> queryCarRepairAppointment(int pageNum, QueryHelper helper) {
		return carRepairAppointmentDao.getPageBean(pageNum, helper);
	}

	@Transactional
	public void updateCarRepairAppointment(CarRepairAppointment carRepairAppointment) {
		carRepairAppointmentDao.update(carRepairAppointment);
	}

	public boolean isExistAppointment(long selfId, Car car) {
		return carRepairAppointmentDao.isExistAppointment(selfId,car);
	}
	@Transactional
	public void deleteCarRepairAppointmentById(long id) {
		carRepairAppointmentDao.delete(id);
		
	}
}
