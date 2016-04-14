package com.yuqincar.dao.car.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.ServicePointDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.ServicePoint;

@Repository
public class ServicePointDaoImpl extends BaseDaoImpl<ServicePoint> implements ServicePointDao{

	public boolean canDeleteServicePoint(long id) {
			List<Car> cars = getSession().createQuery("from Car where servicePoint.id=?").
					setParameter(0, id).list();
			if(cars.size()!=0) 
				return false;
			return true;
		}
	
}
