package com.yuqincar.service.car.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.car.CarInsuranceDao;
import com.yuqincar.dao.car.CommercialInsuranceDao;
import com.yuqincar.dao.car.CommercialInsuranceTypeDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarInsurance;
import com.yuqincar.domain.car.CommercialInsurance;
import com.yuqincar.domain.car.CommercialInsuranceType;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarInsuranceService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarInsuranceServiceImpl implements CarInsuranceService {
	
	@Autowired
	private CarInsuranceDao carInsuranceDao;
	@Autowired
	private CommercialInsuranceTypeDao commercialInsuranceTypeDao;
	@Autowired
	private CommercialInsuranceDao commercialInsuranceDao;
	@Autowired
	private CarDao carDao;

	@Transactional
	public void saveCarInsurance(CarInsurance carInsurance) {
		carInsuranceDao.save(carInsurance);
		
		carInsurance.getCar().setInsuranceExpiredDate(carInsurance.getToDate());
		if(carInsurance.getCar().getInsuranceExpiredDate().after(new Date()))
			carInsurance.getCar().setInsuranceExpired(false);
		else
			carInsurance.getCar().setInsuranceExpired(true);
		carDao.update(carInsurance.getCar());
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
	@Transactional	
	public void saveCommercialInsuranceType(CommercialInsuranceType commercialInsuranceType){
		commercialInsuranceTypeDao.save(commercialInsuranceType);
	}
	
	public boolean canDeleteCommercialInsuranceType(Long id){
		return commercialInsuranceTypeDao.canDeleteCommercialInsuranceType(id);
	}
	@Transactional
	public void deleteCommercialInsuranceType(Long id){
		commercialInsuranceTypeDao.delete(id);
	}
	
	public CommercialInsuranceType getCommercialInsuranceTypeById(Long id){
		return commercialInsuranceTypeDao.getById(id);
	}
	@Transactional
	public void updateCommercialInsuranceType(CommercialInsuranceType commercialInsuranceType){
		commercialInsuranceTypeDao.update(commercialInsuranceType);
	}
	
	public List<CommercialInsuranceType> getAllCommercialInsuranceType(){
		return commercialInsuranceTypeDao.getAll();
	}
	@Transactional
	public void saveCommercialInsurance(CommercialInsurance commercialInsurance){
		commercialInsuranceDao.save(commercialInsurance);
	}
	@Transactional
	public void deleteCommercialInsurance(Long id){
		commercialInsuranceDao.delete(id);
	}

}
