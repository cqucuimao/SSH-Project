package com.yuqincar.dao.car.impl;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarExamineDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.CarExamine;

@Repository
public class CarExamineDaoImpl extends BaseDaoImpl<CarExamine> implements CarExamineDao{
	
	CarExamine examine = new CarExamine();

	public boolean canDeleteCarExamine(CarExamine carExamine) {
		if(examine.isAppointment()==true){
			 return true;
		 }
		return false;
	}

	public boolean canUpdateCarExamine(CarExamine carExamine) {
		if(examine.isAppointment()==true){
			return true;
		}
		return false;
	}
	

}
