package com.yuqincar.service.car.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.car.CarInsuranceDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarInsurance;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarInsuranceService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarInsuranceServiceImpl implements CarInsuranceService {
	
	@Autowired
	private CarInsuranceDao carInsuranceDao;
	@Autowired
	private CarDao carDao;

	@Transactional
	public void saveCarInsurance(CarInsurance carInsurance) {
		carInsuranceDao.save(carInsurance);

	}

	public CarInsurance getCarInsuranceById(long id) {
		return carInsuranceDao.getById(id);
	}

	public PageBean<CarInsuranceService> queryCarInsurance(int pageNum, QueryHelper helper) {
		return carInsuranceDao.getPageBean(pageNum, helper);
	}

	public List<Car> getAllNeedInsuranceCars() {
		return carDao.getAllNeedInsuranceCars();
	}
	
	public BigDecimal statisticCarInsurance(Date fromDate, Date toDate){
		return carInsuranceDao.statisticCarInsurance(fromDate, toDate);
	}

}
