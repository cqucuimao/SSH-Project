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

	public CarRefuel getCarRefuelById(long id) {
		return carRefuelDao.getById(id);
	}

	public PageBean<CarRefuelService> queryCarRefuel(int pageNum, QueryHelper helper) {
		return carRefuelDao.getPageBean(pageNum, helper);
	}
	
	public BigDecimal statisticCarRefuel(Date fromDate, Date toDate){
		return carRefuelDao.statisticCarRefuel(fromDate,toDate);
	}

	@Transactional
	public int importExcelFile(InputStream is,int rowFrom,int colFrom,int colTo,int sheetIndex){
		ExcelUtil eu = new ExcelUtil();
		List<List<String>> excelLines = eu.getLinesFromExcel(is, rowFrom, colFrom, colTo, sheetIndex);
		if(excelLines.size()>0){
			for(int i=1;i<excelLines.size();i++){
				try {
						CarRefuel cr = new CarRefuel();
						//流水号
						System.out.println("流水号="+excelLines.get(i).get(0));
						cr.setSn(excelLines.get(i).get(0));
						//金额
						System.out.println("金额="+excelLines.get(i).get(1));
						BigDecimal bd = new BigDecimal(excelLines.get(i).get(1));
						cr.setMoney(bd);
						//油量
						System.out.println("油量="+excelLines.get(i).get(2));
						float volume = Float.parseFloat(excelLines.get(i).get(2));
						cr.setVolume(volume);
						//车辆
						System.out.println("车牌号="+excelLines.get(i).get(3));
						Car car = carService.getCarByPlateNumber(excelLines.get(i).get(3));
						cr.setCar(car);
						//司机
						System.out.println("司机="+excelLines.get(i).get(4));
						User driver = userService.getByLoginName(excelLines.get(i).get(4));
						cr.setDriver(driver);
						//交易时间
						System.out.println("交易时间="+excelLines.get(i).get(5));
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
					    Date date;				
						date = sdf.parse(excelLines.get(i).get(5));
						cr.setDate(date);
						
						carRefuelDao.save(cr);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
		return excelLines.size()-1;
	}


}
