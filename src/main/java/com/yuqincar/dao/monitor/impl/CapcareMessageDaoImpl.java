package com.yuqincar.dao.monitor.impl;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.monitor.CapcareMessageDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.monitor.CapcareMessage;
@Repository
public class CapcareMessageDaoImpl extends BaseDaoImpl<CapcareMessage> implements CapcareMessageDao{

	public CapcareMessage getCapcareMessageByPlateNumber(String plateNumber) {
		
		return (CapcareMessage) getSession().createQuery(
				"FROM CapcareMessage cm WHERE cm.plateNumber=?")
				.setParameter(0, plateNumber)
				.uniqueResult();
	}

}
