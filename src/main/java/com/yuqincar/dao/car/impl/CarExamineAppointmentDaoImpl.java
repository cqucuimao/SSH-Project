package com.yuqincar.dao.car.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarExamineAppointmentDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCareAppointment;
import com.yuqincar.domain.car.CarExamineAppointment;

@Repository
public class CarExamineAppointmentDaoImpl extends BaseDaoImpl<CarExamineAppointment> implements CarExamineAppointmentDao{
	public boolean isExistAppointment(long selfId,Car car) {
		if(car == null){
			return false;
		}	
		
		List<CarExamineAppointment> carExamineAppointments = getSession().createQuery("from CarExamineAppointment cea where cea.car=? and cea.id<>? and cea.done=false")
				.setParameter(0, car)
				.setParameter(1, selfId)
				.list();
		if(carExamineAppointments.size()!=0) 
			return true;
		return false;		
	}
}
