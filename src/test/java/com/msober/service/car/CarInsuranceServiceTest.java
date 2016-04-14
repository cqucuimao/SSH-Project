package com.msober.service.car;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.msober.base.BaseTest;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarInsurance;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarInsuranceService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.utils.QueryHelper;


public class CarInsuranceServiceTest extends BaseTest{
	
	@Autowired
	private CarInsuranceService carInsuranceService;
	@Autowired
	private CarService carService;
	
	@Test
	public void testInit(){
		assertNotNull(carInsuranceService);
		assertNotNull(carService);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carinsurances_inserts.xml" })
	@Transactional
	public void testSaveCarInsurance(){
		Calendar cal=Calendar.getInstance();
		Calendar cal1=Calendar.getInstance();
		cal.setTime(new Date());
		cal1.setTime(new Date());
		cal.add(Calendar.YEAR, -15);
		cal1.add(Calendar.YEAR, 5);
		Date qishi=cal.getTime();
		Date guoqi=cal1.getTime();
		CarInsurance carInsurance = new CarInsurance();
		Car car = carService.getCarById(1L);
		carInsurance.setCar(car);
		carInsurance.setFromDate(qishi);
		carInsurance.setToDate(guoqi);
		carInsurance.setInsureCompany("重庆保险");
		carInsurance.setMoney(new BigDecimal(2300));
		
		carInsuranceService.saveCarInsurance(carInsurance);
		CarInsurance carInsuranceVerify=carInsuranceService.getCarInsuranceById(carInsurance.getId());
		assertEquals(carInsuranceVerify.getCar().getId().longValue(), 1L);
		assertEquals(carInsuranceVerify.getFromDate(), qishi);
		assertEquals(carInsuranceVerify.getToDate(), guoqi);
		assertEquals(carInsuranceVerify.getInsureCompany(), "重庆保险");
		assertEquals(carInsuranceVerify.getMoney().intValue(), 2300);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carinsurances_inserts.xml" })
	public void testQueryCarInsurance(){
		QueryHelper helper = new QueryHelper(CarInsurance.class, "ci");
		helper.addWhereCondition("ci.insureCompany=?", "平安保险");
		PageBean<CarInsuranceService> pageBean = carInsuranceService.queryCarInsurance(1, helper);
		assertEquals(pageBean.getRecordCount(),	1);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carinsurances_inserts.xml" })
	public void testGetAllNeedInsuranceCars(){
		List<Car> list = carInsuranceService.getAllNeedInsuranceCars();
		assertEquals(list.size(), 4);
	}

}
