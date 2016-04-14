package com.yuqincar.service.car.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarCareDao;
import com.yuqincar.dao.car.CarDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarCareService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarCareServiceImpl implements CarCareService {
    @Autowired
    private CarCareDao carCareDao;
    @Autowired
    private CarDao carDao;
    @Autowired
    private SMSService smsService;

	@Transactional
    public void saveCarCare(CarCare carCare) {
    	carCareDao.save(carCare);
    
    }

	@Transactional
	public void carCareAppointment(Car car, Date date) {
		CarCare carCare=new CarCare();
		carCare.setCar(car);
		carCare.setDate(date);
		carCare.setAppointment(true);
		carCareDao.save(carCare);
		
		//给司机发送短信
		Map<String,String> params=new HashMap<String,String>();
		params.put("carCareDate", DateUtils.getYMDString(date));
		smsService.sendTemplateSMS(car.getDriver().getPhoneNumber(), SMSService.SMS_TEMPLATE_CARCARE_APPOINTMENT, params);
	}

	public CarCare getCarCareById(long id) {
		return carCareDao.getById(id);
	}

	public PageBean<CarCareService> queryCarCare(int pageNum, QueryHelper helper) {
		return carCareDao.getPageBean(pageNum, helper);
	}

	public boolean canDeleteCarCare(CarCare carCare) {
		return carCare.isAppointment()==true;
	}

	@Transactional
	public void deleteCarCareById(long id) {
		carCareDao.delete(id);
	}

	public boolean canUpdateCarCare(CarCare carCare) {
		return carCare.isAppointment()==true;
	}

	@Transactional
	public void updateCarCare(CarCare carCare) {
		carCareDao.update(carCare);

	}

	public List<Car> getAllNeedCareCars() {
		return carDao.getAllNeedCareCars();
	}

	public BigDecimal statisticCarCare(Date fromDate,Date toDate){
		return carCareDao.statisticCarCare(fromDate,toDate);
	}
}
