package com.yuqincar.dao.order.impl;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.order.CarServiceSuperTypeDao;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.privilege.User;

@Repository
public class CarServiceSuperTypeDaoImpl extends BaseDaoImpl<CarServiceSuperType> implements CarServiceSuperTypeDao {
	public CarServiceSuperType getCarServiceSuperTypeByTitle(String title){
		return (CarServiceSuperType) getSession().createQuery(
				"FROM CarServiceSuperType csst WHERE csst.title=?")
				.setParameter(0, title)
				.uniqueResult();
	}

}
