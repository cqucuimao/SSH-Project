package com.yuqincar.dao.car.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarRepairAppointmentDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCareAppointment;
import com.yuqincar.domain.car.CarRepairAppointment;

@Repository
public class CarRepairAppointmentDaoImpl extends BaseDaoImpl<CarRepairAppointment> implements CarRepairAppointmentDao {

	public boolean isExistAppointment(long selfId,Car car) {
		if(car == null){
			return false;
		}	
		
		List<CarRepairAppointment> carRepairAppointments = getSession().createQuery("from CarRepairAppointment cra where cra.car=? and cra.id<>? and cra.done=false")
				.setParameter(0, car)
				.setParameter(1, selfId)
				.list();
		if(carRepairAppointments.size()!=0) 
			return true;
		return false;		
	}
}
