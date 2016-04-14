package com.yuqincar.dao.car;

import java.math.BigDecimal;
import java.util.Date;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.car.CarRepair;

public interface CarRepairDao extends BaseDao<CarRepair> {
	public boolean canDeleteCarRefuel(CarRefuel carRefuel);
	public boolean canUpdateCarRefuel(CarRefuel carRefuel);
	public BigDecimal statisticCarRepair(Date fromDate,Date toDate);

}
