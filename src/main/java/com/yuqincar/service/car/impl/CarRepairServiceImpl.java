package com.yuqincar.service.car.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarRepairDao;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarRepairService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarRepairServiceImpl implements CarRepairService {
	@Autowired
	private CarRepairDao carRepairDao;
	@Autowired
	private SMSService smsService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private UserService userService;

	@Transactional
	public void saveCarRepair(CarRepair carRepair) {
		carRepairDao.save(carRepair);

	}
	
	public CarRepair getCarRepairById(long id) {
		return carRepairDao.getById(id);
	}

	public PageBean<CarRepair> queryCarRepair(int pageNum, QueryHelper helper) {
		return carRepairDao.getPageBean(pageNum, helper);
	}

	@Transactional
	public void deleteCarRepairById(Long id) {
		carRepairDao.delete(id);

	}

	@Transactional
	public void updateCarRepair(CarRepair carRepair) {
		carRepairDao.update(carRepair);

	}
	
	public BigDecimal statisticCarRepair(Date fromDate, Date toDate){
		return null;
	}
	
	@Transactional
	public void importExcelFile(List<CarRepair> carRepairs){
		
		for(int i=0;i<carRepairs.size();i++){
			carRepairDao.save(carRepairs.get(i));
		}	
	}
}
