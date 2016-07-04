package com.yuqincar.dao.car;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.CarWashShop;

public interface CarWashShopDao extends BaseDao<CarWashShop> {
	public boolean canDeleteCarWashShop(Long id);
}
