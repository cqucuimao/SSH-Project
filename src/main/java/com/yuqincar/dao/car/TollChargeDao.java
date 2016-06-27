package com.yuqincar.dao.car;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.TollCharge;

public interface TollChargeDao extends BaseDao<TollCharge> {
	public TollCharge getRecentTollCharge(Car car);
}
