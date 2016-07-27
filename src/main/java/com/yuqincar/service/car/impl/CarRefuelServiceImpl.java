package com.yuqincar.service.car.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarRefuelDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarRefuelService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.ExcelUtil;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarRefuelServiceImpl implements CarRefuelService {
	@Autowired
	private CarRefuelDao carRefuelDao;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private UserService userService;
	
	@Transactional
	public void saveCarRefuel(CarRefuel carRefuel) {
		carRefuelDao.save(carRefuel);

	}
	public List<CarRefuel> getAllCarRefuel(QueryHelper queryHelper){
		return carRefuelDao.getAllQuerry(queryHelper);
	} 
	
	public CarRefuel getCarRefuelById(long id) {
		return carRefuelDao.getById(id);
	}

	public PageBean<CarRefuel> queryCarRefuel(int pageNum, QueryHelper helper) {
		return carRefuelDao.getPageBean(pageNum, helper);
	}
	
	public BigDecimal statisticCarRefuel(Date fromDate, Date toDate){
		return carRefuelDao.statisticCarRefuel(fromDate,toDate);
	}

	@Transactional
	public void importExcelFile(List<CarRefuel> carRefuels){
		
		for(int i=0;i<carRefuels.size();i++){
			carRefuelDao.save(carRefuels.get(i));
		}	
	}


}
