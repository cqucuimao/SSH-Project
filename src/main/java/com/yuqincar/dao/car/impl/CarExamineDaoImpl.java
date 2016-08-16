package com.yuqincar.dao.car.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarExamineDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.QueryHelper;

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
	
	public CarExamine getRecentCarExamine(Car car){
		List<CarExamine> list =(List<CarExamine>) getSession().createQuery("from CarExamine ce where ce.car=? and ce.appointment=? order by ce.nextExamineDate desc")
				.setParameter(0, car).setParameter(1, false)
				.list();
		
		if(list!=null && list.size()>0)
			return list.get(0);
		else
			return null;
	}

}
