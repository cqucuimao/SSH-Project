package com.yuqincar.dao.car;

import java.math.BigDecimal;
import java.util.Date;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.CarRefuel;

public interface CarRefuelDao extends BaseDao<CarRefuel> {
	public BigDecimal statisticCarRefuel(Date fromDate,Date toDate);
}
