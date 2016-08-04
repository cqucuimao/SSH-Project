package com.yuqincar.dao.car;

import java.math.BigDecimal;
import java.util.Date;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;

public interface CarCareDao extends BaseDao<CarCare>{
	public boolean canDeleteCarCare(CarCare carCare);
	public boolean canUpdateCarCare(CarCare carCare);
	public CarCare getRecentCarCare(Car car);
	public BigDecimal statisticCarCare(Date fromDate,Date toDate);
}
