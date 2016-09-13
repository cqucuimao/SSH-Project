package com.yuqincar.service.car.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarCareAppointmentDao;
import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCareAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarCareAppointmentService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarCareAppointmentServiceImpl implements CarCareAppointmentService {
    @Autowired
    private CarCareAppointmentDao carCareAppointmentDao;
    @Autowired
	private SMSService smsService;
    @Autowired
	private LBSDao lbsDao;
	@Autowired
	private CarService carService;
	@Transactional
    public void saveCarCareAppointment(CarCareAppointment carCareAppointment) {
    	carCareAppointmentDao.save(carCareAppointment);
    	Map<String,String> params=new HashMap<String,String>();
		params.put("plateNumber",carCareAppointment.getCar().getPlateNumber());
		params.put("date", DateUtils.getYMDString(carCareAppointment.getDate()));
		smsService.sendTemplateSMS(carCareAppointment.getDriver().getPhoneNumber(), SMSService.SMS_TEMPLATE_CARCARE_APPOINTMENT_GENERATED_FOR_DRIVER, params);
		
    }
	
	public CarCareAppointment getCarCareAppointmentById(Long id){
		return carCareAppointmentDao.getById(id);
	}

	public PageBean<CarCareAppointment> queryCarCareAppointment(int pageNum, QueryHelper helper) {
		return carCareAppointmentDao.getPageBean(pageNum, helper);
	}

	@Transactional
	public void updateCarCareAppointment(CarCareAppointment carCareAppointment,int mileInterval) {
		if(carCareAppointment.isDone()){
			carCareAppointment.getCar().setCareExpired(false);
			carCareAppointment.getCar().setNextCareMile( (int)lbsDao.getCurrentMile(carCareAppointment.getCar())+mileInterval);
			carService.updateCar(carCareAppointment.getCar());
		}
		carCareAppointmentDao.update(carCareAppointment);
	}

	public boolean isExistAppointment(long selfId,Car car) {
		return carCareAppointmentDao.isExistAppointment(selfId, car);
	}
	@Transactional
	public void deleteCarCareAppointmentById(long id) {
		CarCareAppointment carCareAppointment = carCareAppointmentDao.getById(id);
		Car car = carCareAppointment.getCar();
		car.setCareExpired(false);
		carService.updateCar(car);
		
		carCareAppointmentDao.delete(id);
	}
}
