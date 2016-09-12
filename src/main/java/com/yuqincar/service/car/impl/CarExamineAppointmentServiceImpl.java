package com.yuqincar.service.car.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarExamineAppointmentDao;
import com.yuqincar.domain.car.CarExamineAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarExamineAppointmentService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarExamineAppointmentServiceImpl implements CarExamineAppointmentService {
	@Autowired
	private CarExamineAppointmentDao carExamineAppointmentDao;

	@Transactional
	public void saveCarExamineAppointment(CarExamineAppointment carExamineAppointment) {
		carExamineAppointmentDao.save(carExamineAppointment);
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
}
