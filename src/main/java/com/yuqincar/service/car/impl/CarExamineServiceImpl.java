package com.yuqincar.service.car.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.car.CarExamineDao;
import com.yuqincar.dao.car.TollChargeDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.PlateTypeEnum;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarExamineService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarExamineServiceImpl implements CarExamineService {
	@Autowired
	private CarExamineDao carExamineDao;
	@Autowired
	private CarDao carDao;
	@Autowired
	private SMSService smsService;
	@Autowired
	private TollChargeDao tollChargeDao;

	@Transactional
	public void saveCarExamine(CarExamine carExamine,TollCharge tollCharge) {
		tollChargeDao.save(tollCharge);
		carExamineDao.save(carExamine);
		
		Car car=carExamine.getCar();
		car.setNextExaminateDate(carExamine.getNextExamineDate());
		if(car.getNextExaminateDate().after(new Date()))
			car.setExamineExpired(false);
		else
			car.setExamineExpired(true);
		carDao.update(car);
	}

	public CarExamine getCarExamineById(long id) {
		return carExamineDao.getById(id);
	}

	public PageBean<CarExamine> queryCarExamine(int pageNum, QueryHelper helper) {
		return carExamineDao.getPageBean(pageNum, helper);
	}

	@Transactional
	public void deleteCarExamineById(Long id) {
		CarExamine carExamine=carExamineDao.getById(id);
		carExamineDao.delete(id);
	}

	@Transactional
	public void updateCarExamine(CarExamine carExamine) {
		carExamineDao.update(carExamine);
		
		Car car=carExamine.getCar();
		car.setNextExaminateDate(carExamine.getNextExamineDate());
		if(car.getNextExaminateDate().after(new Date()))
			car.setExamineExpired(false);
		else
			car.setExamineExpired(true);
		carDao.update(car);
	}
	
	public PageBean<Car> getNeedExamineCars(int pageNum,QueryHelper helper) {
		return carDao.getPageBean(pageNum, helper);
	}

	public Date getNextExamineDate(Car car, Date recentExamineDate) {
		Date nextExamineDate = null;
		Date registDate = car.getRegistDate();
		List<Date> dates = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(registDate);
		//蓝牌车且座位数小于7
		if(car.getPlateType() == PlateTypeEnum.BLUE && car.getSeatNumber() < 7){
			//前六年，免审
			for(int i=0;i<1;i++){					
				calendar.add(Calendar.YEAR, +6);
				dates.add(calendar.getTime());
				calendar.setTime(calendar.getTime());
				System.out.println("time="+calendar.getTime());
			}
			//六年以上十年以下，一年一审
			calendar.setTime(dates.get(0));
			for(int i=1;i<5;i++){
				calendar.add(Calendar.YEAR, +1);
				dates.add(calendar.getTime());
				calendar.setTime(calendar.getTime());
			}
			//十年以上，一年两审
			calendar.setTime(dates.get(4));
			for(int i=5;i<20;i++){
				calendar.add(Calendar.MONTH, +6);
				dates.add(calendar.getTime());
				calendar.setTime(calendar.getTime());
			}
			for(int i=0;i<dates.size();i++){
				if(recentExamineDate.getTime()<dates.get(i).getTime()){
					nextExamineDate = dates.get(i);
					break;
				}
				
			}
		}
		//黄牌车，或者座位数大于7(假设报废期限30年)
		if(car.getPlateType() == PlateTypeEnum.YELLOW || car.getSeatNumber() >= 7){
			//前十年，一年一审
			for(int i=0;i<10;i++){					
				calendar.add(Calendar.YEAR, +1);
				dates.add(calendar.getTime());
				calendar.setTime(calendar.getTime());
				
			}
			//十年以上，一年两审
			calendar.setTime(dates.get(9));
			for(int i=10;i<30;i++){
				calendar.add(Calendar.MONTH, +6);
				dates.add(calendar.getTime());
				calendar.setTime(calendar.getTime());
			}
			for(int i=0;i<dates.size();i++){
				if(recentExamineDate.getTime()<dates.get(i).getTime()){
					nextExamineDate = dates.get(i);
					break;
				}
				
			}
		}
		return nextExamineDate;
	}

	public List<Car> getAllNeedExamineCars(QueryHelper helper) {
		return carDao.getAllQuerry(helper);
	}
}
