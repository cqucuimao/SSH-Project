package com.yuqincar.dao.car.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarRepairDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.car.CarRepair;

@Repository
public class CarRepairDaoImpl extends BaseDaoImpl<CarRepair> implements CarRepairDao {
	
	CarRepair carRepair = new CarRepair();

	public boolean canDeleteCarRefuel(CarRefuel carRefuel) {
		if(carRepair.isAppointment()==true){
			return true;
		}
		return false;
	}

	public boolean canUpdateCarRefuel(CarRefuel carRefuel) {
		if(carRepair.isAppointment()==true){
			return true;
		}
		return false;
	}
	
	public BigDecimal statisticCarRepair(Date fromDate,Date toDate){
		BigDecimal money= (BigDecimal)getSession().createQuery("select sum(cr.money) from CarRepair as cr where cr.appointment=? and TO_DAYS(?) <= TO_DAYS(cr.payDate) and TO_DAYS(cr.payDate) <= TO_DAYS(?)")
				.setParameter(0, false).setParameter(1, fromDate).setParameter(2, toDate).uniqueResult();
		if(money==null)
			money=BigDecimal.ZERO;
		return money;
	}
	
}
