package com.yuqincar.dao.car.impl;

import java.util.List;

import com.yuqincar.dao.car.CarWashShopDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.CarWash;
import com.yuqincar.domain.car.CarWashShop;

public class CarWashShopDaoImpl extends BaseDaoImpl<CarWashShop> implements CarWashShopDao {
	public boolean canDeleteCarWashShop(Long id){
		List<CarWash> carWashes = getSession().createQuery("from CarWash where shop.id=?").
				setParameter(0,id).list();
		if(carWashes.size()!=0) 
			return false;
		return true;
	}
}
