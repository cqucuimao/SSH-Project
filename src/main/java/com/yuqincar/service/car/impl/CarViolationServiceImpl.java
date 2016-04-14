package com.yuqincar.service.car.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
	public PageBean<CarViolation> queryCarViolation(int pageNum, QueryHelper helper) {
		
		return carViolationDao.getPageBean(pageNum, helper);
	}
	public boolean canDealCarViolation(long carViolationId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Transactional
	public void dealCarViolation(long carViolationId, int penaltyPoint, BigDecimal penaltyMoney, Date dealtDate) {
		// TODO Auto-generated method stub
		
	}
	public CarViolation getCarViolationById(long id) {
	
		return carViolationDao.getById(id);
	}
	
	


}
