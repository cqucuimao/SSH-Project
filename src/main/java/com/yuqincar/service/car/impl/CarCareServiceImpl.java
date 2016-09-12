package com.yuqincar.service.car.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarCareDao;
import com.yuqincar.dao.car.CarDao;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarCareService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarCareServiceImpl implements CarCareService {
    @Autowired
    private CarCareDao carCareDao;
    @Autowired
    private CarDao carDao;
    @Autowired
    private SMSService smsService;
    
    @Autowired
	private CarService carService;
	
	@Autowired
	private UserService userService;

	@Transactional
    public void saveCarCare(CarCare carCare) {
    	carCareDao.save(carCare);
    }

	public CarCare getCarCareById(long id) {
		return carCareDao.getById(id);
	}

	public PageBean<CarCare> queryCarCare(int pageNum, QueryHelper helper) {
		return carCareDao.getPageBean(pageNum, helper);
	}

	@Transactional
	public void deleteCarCareById(long id) {
		carCareDao.delete(id);
	}

	@Transactional
	public void updateCarCare(CarCare carCare) {
		carCareDao.update(carCare);
	}
			
	@Transactional
	public void importExcelFile(List<CarCare> carCares){
		
		for(int i=0;i<carCares.size();i++){
			carCareDao.save(carCares.get(i));
		}	
	}	
}
