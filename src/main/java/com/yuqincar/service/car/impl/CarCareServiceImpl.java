package com.yuqincar.service.car.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarCareService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.ExcelUtil;
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
    
    	Car car=carCare.getCar();
    	car.setNextCareMile(car.getMileage()+carCare.getMileInterval());
    	carDao.update(car);
    }

	@Transactional
	public void carCareAppointment(Car car,User driver, Date date) {
		CarCare carCare=new CarCare();
		carCare.setCar(car);
		carCare.setDriver(driver);
		carCare.setDate(date);
		carCare.setAppointment(true);
		carCareDao.save(carCare);
		
		//给司机发送短信
		Map<String,String> params=new HashMap<String,String>();
		params.put("carCareDate", DateUtils.getYMDString(date));
		smsService.sendTemplateSMS(driver.getPhoneNumber(), SMSService.SMS_TEMPLATE_CARCARE_APPOINTMENT, params);
	}

	public CarCare getCarCareById(long id) {
		return carCareDao.getById(id);
	}

	public PageBean<CarCare> queryCarCare(int pageNum, QueryHelper helper) {
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

	public PageBean<Car> getNeedCareCars(int pageNum) {
		QueryHelper helper = new QueryHelper(Car.class, "c");
		helper.addWhereCondition("mileNeedCare<300 and c.status=?", CarStatusEnum.NORMAL);
		helper.addOrderByProperty("mileNeedCare", true);
		return carDao.getPageBean(pageNum, helper);
	}

	public BigDecimal statisticCarCare(Date fromDate,Date toDate){
		return carCareDao.statisticCarCare(fromDate,toDate);
	}
	
	
		
	@Transactional
	public void importExcelFile(List<CarCare> carCares){
		
		for(int i=0;i<carCares.size();i++){
			carCareDao.save(carCares.get(i));
		}	
	}
}
