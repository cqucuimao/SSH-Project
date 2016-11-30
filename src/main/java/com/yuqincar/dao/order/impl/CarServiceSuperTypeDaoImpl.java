package com.yuqincar.dao.order.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.order.CarServiceSuperTypeDao;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.privilege.User;

@Repository
public class CarServiceSuperTypeDaoImpl extends BaseDaoImpl<CarServiceSuperType> implements CarServiceSuperTypeDao {
	public CarServiceSuperType getCarServiceSuperTypeByTitle(String title){
		return (CarServiceSuperType) getSession().createQuery(
				"FROM CarServiceSuperType csst WHERE csst.superTitle=?")
				.setParameter(0, title)
				.uniqueResult();
	}

	public boolean canDeleteCarServiceSuperType(long id) {

		List<CarServiceType> carServiceTypes = getSession().createQuery("from CarServiceType cst where cst.superType_id=?")
				.setParameter(0, id)
				.list();
		System.out.println("size="+carServiceTypes.size() );
		if(carServiceTypes.size() == 0)
			return true;
		return false;
	}

}
