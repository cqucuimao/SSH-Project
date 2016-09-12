package com.yuqincar.service.car.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarRepairAppointmentDao;
import com.yuqincar.domain.car.CarRepairAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarRepairAppointmentService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarRepairAppointmentServiceImpl implements CarRepairAppointmentService {
	@Autowired
	private CarRepairAppointmentDao carRepairAppointmentDao;

	@Transactional
	public void saveCarRepairAppointment(CarRepairAppointment carRepairAppointment) {
		carRepairAppointmentDao.save(carRepairAppointment);
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
}
