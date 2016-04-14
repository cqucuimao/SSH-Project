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
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarExamineService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

public class CarExamineServiceTest extends BaseTest{
	
	@Autowired
	private CarExamineService carExamineService;
	@Autowired
	private CarService carService;
	
	@Test
	public void testInit(){
		assertNotNull(carExamineService);
		assertNotNull(carService);
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/carexamines_inserts.xml" })
	@Transactional
	public void testSaveCarExamine(){
		Calendar cal=Calendar.getInstance();
		Calendar cal1=Calendar.getInstance();
		cal.setTime(new Date());
		cal1.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal1.add(Calendar.YEAR, 10);
		Date tomorrow=cal.getTime();
		Date next=cal1.getTime();
		CarExamine carExamine = new CarExamine();
		Car car=carService.getCarById(1L);
		carExamine.setCar(car);
		carExamine.setDate(tomorrow);
		carExamine.setExamineIntervalYear(next);
		carExamine.setMoney(new BigDecimal(800));
		carExamine.setMemo("保存年审记录");		
		carExamine.setAppointment(true);
		carExamineService.saveCarExamine(carExamine);
		
		CarExamine carExamineVerify=carExamineService.getCarExamineById(carExamine.getId());
		assertEquals(carExamineVerify.getCar().getId().longValue(), 1L);
		assertEquals(DateUtils.compareYMD(carExamineVerify.getDate(),tomorrow),true);
		assertEquals(DateUtils.compareYMD(carExamineVerify.getExamineIntervalYear(), next), true);
		assertEquals(carExamineVerify.getMoney().intValue(), 800);
		assertEquals(carExamineVerify.getMemo(), "保存年审记录");
		assertEquals(carExamineVerify.isAppointment(),true);
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/carexamines_inserts.xml" })
	@Transactional
	public void testCarExamineAppointment(){
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow=cal.getTime();
		CarExamine carExamine = new CarExamine();
		Car car=carService.getCarById(1L);
		carExamine.setCar(car);
		carExamine.setDate(tomorrow);
		carExamine.setAppointment(true);
		carExamineService.saveCarExamine(carExamine);
		
		CarExamine carExamineVerify=carExamineService.getCarExamineById(carExamine.getId());
		assertEquals(carExamineVerify.getCar().getId().longValue(), 1L);
		assertEquals(DateUtils.compareYMD(carExamineVerify.getDate(),tomorrow),true);
		assertEquals(carExamineVerify.isAppointment(),true);
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/carexamines_inserts.xml" })
	public void testQueryCarExamine(){
		QueryHelper helper = new QueryHelper(CarExamine.class, "ce");
		helper.addWhereCondition("ce.memo=?", "车辆年审记录");
		PageBean<CarExamineService> pageBean=carExamineService.queryCarExamine(1, helper);
		assertEquals(pageBean.getRecordCount(),1);
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/carexamines_inserts.xml" })
	public void testCanDeleteCarExamine(){
		CarExamine carExamine = carExamineService.getCarExamineById(1L);
		assertEquals(carExamineService.canDeleteCarExamine(carExamine), false);
		
		CarExamine carExamine2 = carExamineService.getCarExamineById(2L);
		assertEquals(carExamineService.canDeleteCarExamine(carExamine2), true);
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/carexamines_inserts.xml" })
	public void testDeleteCarExamineById(){
		carExamineService.deleteCarExamineById(2L);
		CarExamine carExamine=carExamineService.getCarExamineById(2L);
		assertNull(carExamine);
		
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/carexamines_inserts.xml" })
	public void testCanUpdateCarExamine(){
		CarExamine carExamine = carExamineService.getCarExamineById(3L);
		assertEquals(carExamineService.canUpdateCarExamine(carExamine), false);
		
		CarExamine ce = carExamineService.getCarExamineById(4L);
		assertEquals(carExamineService.canUpdateCarExamine(ce), true);
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/carexamines_inserts.xml" })
	public void testUpdateCarExamine(){
		CarExamine carExamine = carExamineService.getCarExamineById(1);
		carExamine.setCar(carService.getCarById(2));
		carExamine.setMoney(new BigDecimal(1500));
		carExamine.setMemo("年检更新情况");
		carExamine.setAppointment(true);
		carExamineService.updateCarExamine(carExamine);
		
		CarExamine ce = carExamineService.getCarExamineById(1);
		assertEquals(ce.getCar().getId().longValue(), 2);
		assertEquals(ce.getMoney().intValue(), 1500);
		assertEquals(ce.getMemo(), "年检更新情况");
		assertEquals(ce.isAppointment(), true);
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/carexamines_inserts.xml" })
	public void testGetAllNeedExamineCars(){
		List<Car> l = carExamineService.getAllNeedExamineCars();
		assertEquals(l.size(), 3);

	}

}
