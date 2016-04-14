package com.msober.service.car;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.msober.base.BaseTest;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarRefuelService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.utils.QueryHelper;

public class CarRefuelServiceTest extends BaseTest {
	
	@Autowired
	private CarRefuelService carRefuelService;
	@Autowired
	private CarService carService;
	
	@Test
	public void testInit(){
		assertNotNull(carRefuelService);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carrefuels_inserts.xml" })
	@Transactional
	public void testSaveCarRefuel(){
		Date date = new Date();
		CarRefuel carRefuel = new CarRefuel();
		carRefuel.setCar(carService.getCarById(2L));
		carRefuel.setDate(date);
		carRefuel.setMemo("今天你加油了吗？");
		carRefuel.setMoney(new BigDecimal(800));
		carRefuelService.saveCarRefuel(carRefuel);
		
		CarRefuel carRefuel2 = carRefuelService.getCarRefuelById(carRefuel.getId());
		assertEquals(carRefuel2.getCar().getId().longValue(), 2L);
		assertEquals(carRefuel2.getDate(), date);
		assertEquals(carRefuel2.getMemo(), "今天你加油了吗？");
		assertEquals(carRefuel2.getMoney().intValue(), 800);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/car/carrefuels_inserts.xml" })
	public void testQueryCarCare(){
		QueryHelper helper = new QueryHelper(CarRefuel.class, "cr");
		helper.addWhereCondition("cr.memo=?", "在磁器口加油");
		PageBean<CarRefuelService> pageBean=carRefuelService.queryCarRefuel(1, helper);
		assertEquals(pageBean.getRecordCount(),2);
	}

}
