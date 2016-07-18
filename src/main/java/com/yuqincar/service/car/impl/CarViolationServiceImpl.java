package com.yuqincar.service.car.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarViolationDao;
import com.yuqincar.domain.car.CarViolation;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarViolationService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarViolationServiceImpl implements CarViolationService {

	@Autowired
	private CarViolationDao carViolationDao;

	@Transactional
	public void pullViolationFromCQJG() {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	public void saveCarViolation(CarViolation carViolation){
		carViolationDao.save(carViolation);
	}
	
	public boolean canUpdateCarViolation(Long id){
		return !carViolationDao.getById(id).isDealt();
	}

	@Transactional
	public void updateCarViolation(CarViolation carViolation){
		carViolationDao.update(carViolation);
	}

	@Transactional
	public void deleteCarViolation(Long id){
		carViolationDao.delete(id);
	}
	
	public PageBean<CarViolation> queryCarViolation(int pageNum, QueryHelper helper) {
		return carViolationDao.getPageBean(pageNum, helper);
	}
	
	public CarViolation getCarViolationById(long id) {
	
		return carViolationDao.getById(id);
	}
	
	


}
