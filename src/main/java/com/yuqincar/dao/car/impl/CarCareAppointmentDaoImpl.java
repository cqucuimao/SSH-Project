package com.yuqincar.dao.car.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarCareAppointmentDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCareAppointment;

@Repository
public class CarCareAppointmentDaoImpl extends BaseDaoImpl<CarCareAppointment> implements CarCareAppointmentDao{

	public boolean isExistAppointment(long selfId,Car car) {
		if(car == null){
			return false;
		}	
		
		List<CarCareAppointment> carCareAppointments = getSession().createQuery("from CarCareAppointment cca where cca.car=? and cca.id<>? and cca.done=false")
				.setParameter(0, car)
				.setParameter(1, selfId)
				.list();
		if(carCareAppointments.size()!=0) 
			return true;
		return false;		
	}
	
}
