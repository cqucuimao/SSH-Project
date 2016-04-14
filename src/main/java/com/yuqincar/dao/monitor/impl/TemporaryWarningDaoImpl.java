package com.yuqincar.dao.monitor.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.monitor.TemporaryWarningDao;
import com.yuqincar.domain.monitor.TemporaryWarning;
import com.yuqincar.domain.monitor.WarningMessageTypeEnum;

@Repository
public class TemporaryWarningDaoImpl extends BaseDaoImpl<TemporaryWarning> implements TemporaryWarningDao {
	
	@Autowired
	private CarDao carDao;

	public boolean isTempMessageExist(Long carId) {
		TemporaryWarning temp=(TemporaryWarning)getSession().createQuery("from TemporaryWarning t where t.car.id=?")//
				.setParameter(0, carId).uniqueResult();
		if(temp==null)
		   return false;
		else
		   return true;
	}

	public void deleteTempMessage(Long carId) {
		TemporaryWarning temp=(TemporaryWarning)getSession().createQuery("from TemporaryWarning t where t.car.id=?")//
				.setParameter(0, carId).uniqueResult();
		getSession().delete(temp);
	}

	public void addTempWarningMessage(Long carId) {
		TemporaryWarning temp=new TemporaryWarning();
		temp.setCar(carDao.getById(carId));
		temp.setType(WarningMessageTypeEnum.UNPLANNED_RUNNING); //临时记录默认都是异常行驶
		getSession().save(temp);
	}

}
