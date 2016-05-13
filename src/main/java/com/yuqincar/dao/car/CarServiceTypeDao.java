package com.yuqincar.dao.car;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.CarServiceType;

public interface CarServiceTypeDao extends BaseDao<CarServiceType>{

	boolean canDeleteCarServiceType(long id);
	
	CarServiceType getCarServiceTypeByTitle(String title);

}
