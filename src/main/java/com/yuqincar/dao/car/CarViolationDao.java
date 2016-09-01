package com.yuqincar.dao.car;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarViolation;

public interface CarViolationDao extends BaseDao<CarViolation> {
	
	public void pullViolationFromCQJG();
	
	public boolean canDealCarViolation(long carViolationId);
	
	public void dealCarViolation(long carViolationId, int penaltyPoint, BigDecimal penaltyMoney, Date dealtDate);
	
	public List<CarViolation> getCarViolationByCar(Car car);

}
