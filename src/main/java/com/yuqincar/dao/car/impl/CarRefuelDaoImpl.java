package com.yuqincar.dao.car.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarRefuelDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.CarRefuel;

@Repository
public class CarRefuelDaoImpl extends BaseDaoImpl<CarRefuel> implements CarRefuelDao {

	public BigDecimal statisticCarRefuel(Date fromDate,Date toDate){
		BigDecimal money= (BigDecimal)getSession().createQuery("select sum(cr.money) from CarRefuel as cr where TO_DAYS(?) <= TO_DAYS(cr.date) and TO_DAYS(cr.date) <= TO_DAYS(?)")
				.setParameter(0, fromDate).setParameter(1, toDate).uniqueResult();
		if(money==null)
			money=BigDecimal.ZERO;
		return money;
	}
}
