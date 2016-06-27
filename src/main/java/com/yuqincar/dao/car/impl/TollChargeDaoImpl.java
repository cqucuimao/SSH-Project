package com.yuqincar.dao.car.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.TollChargeDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.TollCharge;

@Repository
public class TollChargeDaoImpl extends BaseDaoImpl<TollCharge> implements TollChargeDao {
	public TollCharge getRecentTollCharge(Car car){
		List<TollCharge> tollCharge = getSession().createQuery("from TollCharge tc where tc.car=? order by tc.payDate desc").
				setParameter(0, car).list();
		return tollCharge.get(0);
	}
}
