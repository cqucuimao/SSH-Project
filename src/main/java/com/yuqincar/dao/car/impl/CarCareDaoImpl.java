package com.yuqincar.dao.car.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarCareDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.CarCare;

@Repository
public class CarCareDaoImpl extends BaseDaoImpl<CarCare> implements CarCareDao{
	
	CarCare care = new CarCare();

	public boolean canDeleteCarCare(CarCare carCare) {
		 if(care.isAppointment()==true){
			 return true;
		 }
		return false;
	}

	public boolean canUpdateCarCare(CarCare carCare) {
		if(care.isAppointment()==true){
			return true;
		}
		return false;
	}

	public BigDecimal statisticCarCare(Date fromDate,Date toDate){
		BigDecimal money= (BigDecimal)getSession().createQuery("select sum(cc.money) from CarCare as cc where cc.appointment=? and TO_DAYS(?) <= TO_DAYS(cc.date) and TO_DAYS(cc.date) <= TO_DAYS(?)")
				.setParameter(0, false).setParameter(1, fromDate).setParameter(2, toDate).uniqueResult();
		if(money==null)
			money=BigDecimal.ZERO;
		return money;
	}
}
