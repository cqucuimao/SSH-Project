package com.yuqincar.dao.order;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.CarServiceSuperType;

public interface CarServiceSuperTypeDao extends BaseDao<CarServiceSuperType> {
	public CarServiceSuperType getCarServiceSuperTypeByTitle(String title);
	public boolean canDeleteCarServiceSuperType(long id);
}
