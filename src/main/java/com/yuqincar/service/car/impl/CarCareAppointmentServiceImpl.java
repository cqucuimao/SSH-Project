package com.yuqincar.service.car.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarCareAppointmentDao;
import com.yuqincar.domain.car.CarCareAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarCareAppointmentService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarCareAppointmentServiceImpl implements CarCareAppointmentService {
    @Autowired
    private CarCareAppointmentDao carCareAppointmentDao;

	@Transactional
    public void saveCarCareAppointment(CarCareAppointment carCareAppointment) {
    	carCareAppointmentDao.save(carCareAppointment);
    }
	
	public CarCareAppointment getCarCareAppointmentById(Long id){
		return carCareAppointmentDao.getById(id);
	}

	public PageBean<CarCareAppointment> queryCarCareAppointment(int pageNum, QueryHelper helper) {
		return carCareAppointmentDao.getPageBean(pageNum, helper);
	}

	@Transactional
	public void updateCarCareAppointment(CarCareAppointment carCareAppointment) {
		carCareAppointmentDao.update(carCareAppointment);
	}
}
