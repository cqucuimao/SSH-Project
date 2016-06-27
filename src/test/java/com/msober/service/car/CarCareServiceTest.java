package com.msober.service.car;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarCareService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;
import com.yuqincar.utils.TestUtils;

public class CarCareServiceTest extends BaseTest{
	
	@Autowired
	private CarCareService carCareService;
	@Autowired
	private CarService carService;


	@Test
	public void testInit() {
		assertNotNull(carCareService); 
		assertNotNull(carService);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carcares_inserts.xml" })
	@Transactional
	public void testSaveCarCare(){
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow=cal.getTime();
		CarCare carCare = new CarCare();
		Car car=carService.getCarById(1L);
		carCare.setDate(tomorrow);
		carCare.setMileInterval(8000);
		carCare.setMoney(new BigDecimal(1000));
		carCare.setMemo("常规保养");		
		carCare.setAppointment(false);
		carCare.setCar(car);
		carCareService.saveCarCare(carCare);
		
		CarCare carCareVerify=carCareService.getCarCareById(carCare.getId());
		assertEquals(carCareVerify.getCar().getId().longValue(), 1L);
		assertEquals(DateUtils.compareYMD(carCareVerify.getDate(),tomorrow),true);
		assertEquals(carCareVerify.getMileInterval(), 8000);
		assertEquals(carCareVerify.getMoney().intValue(), 1000);
		assertEquals(carCareVerify.getMemo(), "常规保养");
		assertEquals(carCareVerify.isAppointment(),false);
	}
	

	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carcares_inserts.xml" })
	@Transactional
	public void testCarCareAppointment() {
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow=cal.getTime();
		CarCare carCare = new CarCare();
		carCare.setCar(carService.getCarById(1));
		carCare.setDate(tomorrow);
		carCare.setAppointment(true);
		carCareService.saveCarCare(carCare);
		
		CarCare carCareVerify=carCareService.getCarCareById(carCare.getId());
		assertEquals(carCareVerify.getCar().getId().longValue(),1L);
		assertEquals(DateUtils.compareYMD(carCareVerify.getDate(),tomorrow),true);
		assertEquals(carCareVerify.isAppointment(),true);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carcares_inserts.xml" })
	public void testQueryCarCare(){
		QueryHelper helper = new QueryHelper(CarCare.class, "cc");
		helper.addWhereCondition("cc.appointment=?", false);
		//PageBean<CarCareService> pageBean=carCareService.queryCarCare(1, helper);
		//assertEquals(pageBean.getRecordCount(),2);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carcares_inserts.xml" })
	public void testCanDeleteCarCare(){
		CarCare carCare = carCareService.getCarCareById(2);
		assertEquals(carCareService.canDeleteCarCare(carCare), true);

		carCare = carCareService.getCarCareById(1);
		assertEquals(carCareService.canDeleteCarCare(carCare), false);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carcares_inserts.xml" })
	public void testDeleteCarCareById(){
		
        carCareService.deleteCarCareById(2L);
        CarCare carCare = carCareService.getCarCareById(2L);
		assertNull(carCare);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carcares_inserts.xml" })
	public void testCanUpdateCarCare(){
		CarCare carCare = carCareService.getCarCareById(2);
		assertEquals(carCareService.canUpdateCarCare(carCare), true);
		
		CarCare carCare2 = carCareService.getCarCareById(1);
		assertEquals(carCareService.canUpdateCarCare(carCare2), false);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carcares_inserts.xml" })
	@Transactional
	public void testUpdateCarCare(){
		CarCare carCare = carCareService.getCarCareById(1);
		carCare.setCar(carService.getCarById(2));
		carCare.setMileInterval(8000);
		carCare.setMoney(new BigDecimal(2000));
		carCare.setMemo("更新情况");
		carCare.setAppointment(true);
		carCareService.updateCarCare(carCare);
		
		CarCare cc = carCareService.getCarCareById(1);
		assertEquals(cc.getCar().getId().longValue(), 2);
		assertEquals(cc.getMileInterval(), 8000);
		assertEquals(cc.getMoney().intValue(), 2000);
		assertEquals(cc.getMemo(), "更新情况");
		assertEquals(cc.isAppointment(), true);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carcares_inserts.xml" })
	public void testGetAllNeedCareCars(){
		
		List<Car> l = carCareService.getAllNeedCareCars();
		assertEquals(l.size(), 2);
	}
}
