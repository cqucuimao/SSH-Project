package com.yuqincar.dao.car.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarInsuranceDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.CarInsurance;

@Repository
public class CarInsuranceDaoImpl extends BaseDaoImpl<CarInsurance> implements CarInsuranceDao{

	public BigDecimal statisticCarInsurance(Date fromDate,Date toDate){
		BigDecimal money= (BigDecimal)getSession().createQuery("select sum(ci.money) from CarInsurance as ci where TO_DAYS(?) <= TO_DAYS(ci.payDate) and TO_DAYS(ci.payDate) <= TO_DAYS(?)")
				.setParameter(0, fromDate).setParameter(1, toDate).uniqueResult();
		if(money==null)
			money=BigDecimal.ZERO;
		return money;
	}
}
