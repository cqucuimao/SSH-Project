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

import com.yuqincar.dao.car.CarRepairDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarRepairService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateRange;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.ExcelUtil;
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

	@Transactional
	public void saveAppointment(CarRepair carRepair) {
		carRepair.setAppointment(true);
		carRepairDao.save(carRepair);
	}
	
	@Transactional
	public void updateAppointment(CarRepair carRepair) {
		carRepairDao.update(carRepair);
	}

	public CarRepair getCarRepairById(long id) {
		return carRepairDao.getById(id);
	}

	public PageBean<CarRepair> queryCarRepair(int pageNum, QueryHelper helper) {
		return carRepairDao.getPageBean(pageNum, helper);
	}

	public boolean canDeleteCarRepair(CarRepair carRepair) {
		return carRepair.isAppointment()==true;
	}

	@Transactional
	public void deleteCarRepairById(Long id) {
		carRepairDao.delete(id);

	}

	public boolean canUpdateCarRepair(CarRepair carRepair) {
		return carRepair.isAppointment()==true;
	}

	@Transactional
	public void updateCarRepair(CarRepair carRepair) {
		carRepairDao.update(carRepair);

	}
	
	public BigDecimal statisticCarRepair(Date fromDate, Date toDate){
		return carRepairDao.statisticCarRepair(fromDate, toDate);
	}
	
	@Transactional
	public void importExcelFile(List<CarRepair> carRepairs){
		
		for(int i=0;i<carRepairs.size();i++){
			carRepairDao.save(carRepairs.get(i));
		}	
	}
	
	public CarRepair getUnDoneAppointRepair(Car car){
		QueryHelper helper=new QueryHelper(CarRepair.class,"cr");
		helper.addWhereCondition("cr.car=?", car);
		helper.addWhereCondition("cr.appointment=?", true);
		helper.addWhereCondition("cr.done=?", false);
		helper.addOrderByProperty("cr.fromDate", false);
		List<CarRepair> list=carRepairDao.getPageBean(1, helper).getRecordList();
		if(list!=null && list.size()>0){
			return list.get(0);
		}else
			return null;
	}
}
