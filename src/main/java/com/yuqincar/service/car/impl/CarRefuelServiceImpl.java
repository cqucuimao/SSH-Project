package com.yuqincar.service.car.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarRefuelDao;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarRefuelService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarRefuelServiceImpl implements CarRefuelService {
	@Autowired
	private CarRefuelDao carRefuelDao;

	@Transactional
	public void saveCarRefuel(CarRefuel carRefuel) {
		carRefuelDao.save(carRefuel);

	}

	public CarRefuel getCarRefuelById(long id) {
		return carRefuelDao.getById(id);
	}

	public PageBean<CarRefuelService> queryCarRefuel(int pageNum, QueryHelper helper) {
		return carRefuelDao.getPageBean(pageNum, helper);
	}
	
	public BigDecimal statisticCarRefuel(Date fromDate, Date toDate){
		return carRefuelDao.statisticCarRefuel(fromDate,toDate);
	}

}
