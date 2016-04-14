package com.yuqincar.dao.car;

import java.math.BigDecimal;
import java.util.Date;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.CarInsurance;

public interface CarInsuranceDao extends BaseDao<CarInsurance> {
	public BigDecimal statisticCarInsurance(Date fromDate,Date toDate);
}
