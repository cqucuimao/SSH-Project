package com.yuqincar.dao.car.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarCareDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.QueryHelper;

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
	

	public CarCare getRecentCarCare(Car car){
		List<CarCare> list =(List<CarCare>) getSession().createQuery("from CarCare cc where cc.car=? and cc.appointment=? order by cc.id desc")
				.setParameter(0, car).setParameter(1, false).list();
		
		if(list!=null && list.size()>0)
			return list.get(0);
		else
			return null;
	}

	public BigDecimal statisticCarCare(Date fromDate,Date toDate){
		BigDecimal money= (BigDecimal)getSession().createQuery("select sum(cc.money) from CarCare as cc where cc.appointment=? and TO_DAYS(?) <= TO_DAYS(cc.date) and TO_DAYS(cc.date) <= TO_DAYS(?)")
				.setParameter(0, false).setParameter(1, fromDate).setParameter(2, toDate).uniqueResult();
		if(money==null)
			money=BigDecimal.ZERO;
		return money;
	}
}
