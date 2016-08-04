package com.yuqincar.service.car.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.car.TollChargeDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.TollChargeService;
import com.yuqincar.utils.QueryHelper;

@Service
public class TollChargeServiceImpl implements TollChargeService {
	@Autowired
	private TollChargeDao tollChargeDao;
	@Autowired
	private CarDao carDao;
	
	@Transactional
	public void saveTollCharge(TollCharge tollCharge) {
		tollChargeDao.save(tollCharge);
		
		tollCharge.getCar().setNextTollChargeDate(tollCharge.getNextPayDate());
		if(tollCharge.getCar().getNextTollChargeDate().after(new Date()))
			tollCharge.getCar().setTollChargeExpired(false);
		else
			tollCharge.getCar().setTollChargeExpired(true);
		carDao.update(tollCharge.getCar());
	}

	@Transactional
	public void updateTollCharge(TollCharge tollCharge) {
		tollChargeDao.update(tollCharge);
		
		tollCharge.getCar().setNextTollChargeDate(tollCharge.getNextPayDate());
		if(tollCharge.getCar().getNextTollChargeDate().after(new Date()))
			tollCharge.getCar().setTollChargeExpired(false);
		else
			tollCharge.getCar().setTollChargeExpired(true);
		carDao.update(tollCharge.getCar());
	}

	@Transactional
	public void deleteTollCharge(Long id) {
		TollCharge tollCharge=tollChargeDao.getById(id);
		Car car=tollCharge.getCar();
		tollChargeDao.delete(id);
		
		TollCharge tc=tollChargeDao.getRecentTollCharge(car);
		if(tc!=null){
			car.setNextTollChargeDate(tc.getNextPayDate());
			if(car.getNextTollChargeDate().after(new Date()))
				car.setTollChargeExpired(false);
			else
				car.setTollChargeExpired(true);
			carDao.update(car);
		}
	}

	public PageBean<TollCharge> queryTollCharge(int pageNum, QueryHelper helper) {
		return tollChargeDao.getPageBean(pageNum, helper);
	}

	public TollCharge getTollChargeById(Long id) {
		return tollChargeDao.getById(id);
	}
	
	public TollCharge getRecentTollCharge(Car car){
		return tollChargeDao.getRecentTollCharge(car);
	}
}
