package com.yuqincar.dao.car.impl;

import java.util.List;

import javax.persistence.Entity;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarServiceTypeDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.order.Order;

@Repository
public class CarServiceTypeDaoImpl extends BaseDaoImpl<CarServiceType> implements CarServiceTypeDao{

	public boolean canDeleteCarServiceType(long id) {		
			
			List<Order> orders = getSession().createQuery("from order_ where serviceType_id=?").
					setParameter(0, id).list();
			List<Car> cars = getSession().createQuery("from Car where serviceType.id=?").
					setParameter(0, id).list();
			if(orders.size()!=0 || cars.size()!=0) 
				return false;
			return true;
		}
	
	public CarServiceType getCarServiceTypeByTitle(String title){
		return (CarServiceType)getSession().createQuery("from CarServiceType where title=?").
				setParameter(0, title).uniqueResult();
	}
}
