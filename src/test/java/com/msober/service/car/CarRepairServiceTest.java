package com.msober.service.car;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.msober.base.BaseTest;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarRepairService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

public class CarRepairServiceTest extends BaseTest {
	
	@Autowired
	private CarRepairService carRepairService;
	@Autowired
	private CarService carService;


	@Test
	public void testInit() {
		assertNotNull(carRepairService); 
		assertNotNull(carService);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carrepairs_inserts.xml" })
	@Transactional
	public void testSaveCarRepair(){
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow=cal.getTime();
		CarRepair carRepair = new CarRepair();
		Car car=carService.getCarById(1L);
		carRepair.setCar(car);
		carRepair.setFromDate(tomorrow);
		carRepair.setToDate(tomorrow);
		carRepair.setMoney(new BigDecimal(3000));
		carRepair.setMemo("常规保修");
		carRepair.setAppointment(true);
		carRepairService.saveCarRepair(carRepair);
		
		CarRepair carRepair2 = carRepairService.getCarRepairById(carRepair.getId());
		assertEquals(carRepair2.getCar().getId().longValue(), 1L);
		assertEquals(DateUtils.compareYMD(carRepair2.getFromDate(),tomorrow),true);
		assertEquals(DateUtils.compareYMD(carRepair2.getToDate(),tomorrow),true);
		assertEquals(carRepair2.getMoney().intValue(), 3000);
		assertEquals(carRepair2.getMemo(), "常规保修");
		assertEquals(carRepair2.isAppointment(),true);
		
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carrepairs_inserts.xml" })
	@Transactional
	public void testCarRepairAppointment() {
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow=cal.getTime();
		CarRepair carRepair = new CarRepair();
		carRepair.setCar(carService.getCarById(1));
		carRepair.setFromDate(tomorrow);
		carRepair.setToDate(tomorrow);
		carRepair.setAppointment(true);
		carRepairService.saveCarRepair(carRepair);
		
		CarRepair carRepair2=carRepairService.getCarRepairById(carRepair.getId());
		assertEquals(carRepair2.getCar().getId().longValue(),1L);
		assertEquals(DateUtils.compareYMD(carRepair2.getFromDate(),tomorrow),true);
		assertEquals(DateUtils.compareYMD(carRepair2.getToDate(),tomorrow),true);
		assertEquals(carRepair2.isAppointment(),true);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carrepairs_inserts.xml" })
	public void testQueryCarRepair(){
		QueryHelper helper = new QueryHelper(CarRepair.class, "cr");
		helper.addWhereCondition("cr.appointment=?", false);
		PageBean<CarRepairService> pageBean=carRepairService.queryCarRepair(1, helper);
		assertEquals(pageBean.getRecordCount(),2);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carrepairs_inserts.xml" })
	public void testCanDeleteCarRepair(){
		CarRepair carRepair = carRepairService.getCarRepairById(2);
		assertEquals(carRepairService.canDeleteCarRepair(carRepair), true);

		carRepair = carRepairService.getCarRepairById(1);
		assertEquals(carRepairService.canDeleteCarRepair(carRepair), false);
	}

	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carrepair.xml" })
	public void testImportData() {
		
	}
}
