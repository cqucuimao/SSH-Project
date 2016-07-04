package com.yuqincar.dao.car.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CommercialInsuranceTypeDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.CommercialInsuranceType;

@Repository
public class CommercialInsuranceTypeDaoImpl extends BaseDaoImpl<CommercialInsuranceType> implements CommercialInsuranceTypeDao{

	public boolean canDeleteCommercialInsuranceType(long id) {
			List<CommercialInsuranceType> cars = getSession().createQuery("from CommercialInsurance where type.id=?").
					setParameter(0, id).list();
			if(cars.size()!=0) 
				return false;
			return true;
		}
	
}
