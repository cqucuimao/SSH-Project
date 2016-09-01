package com.yuqincar.dao.car.impl;


import java.util.List;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.car.CarViolationDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.CarViolation;
import com.yuqincar.domain.order.Order;

import antlr.StringUtils;
import freemarker.template.utility.StringUtil;

@Repository
public class CarViolationDaoImpl extends BaseDaoImpl<CarViolation> implements CarViolationDao {

	public void pullViolationFromCQJG() {
		// TODO Auto-generated method stub
		
	}

	public boolean canDealCarViolation(long carViolationId) {
		// TODO Auto-generated method stub
		return false;
	}

	public void dealCarViolation(long carViolationId, int penaltyPoint, BigDecimal penaltyMoney, Date dealtDate) {
		// TODO Auto-generated method stub
		
	}

	public List<CarViolation> getCarViolationByCar(Car car) {
		List<CarViolation> carViolations = getSession().createQuery("from CarViolation cv where cv.car=? and cv.dealt=?")
				.setParameter(0, car)
				.setParameter(1, false)
				.list();
		return carViolations;
	}

	
	
}
