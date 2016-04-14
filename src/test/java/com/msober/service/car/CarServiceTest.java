package com.msober.service.car;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.msober.base.BaseTest;
import com.yuqincar.domain.car.Car;
import com.yuqincar.service.car.CarService;

import junit.framework.Assert;

public class CarServiceTest extends BaseTest{
	@Autowired
	private CarService carService;
	
	@Test
	public void testInit(){
		assertNotNull(carService);
	}
	
	@Test 
	public void testGetCarById() throws SQLException, Exception{

	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/cars_inserts.xml" })
	public void testisPlateNumberExist(){
		
		assertTrue("车牌号存在",carService.isPlateNumberExist("A1111"));
		assertFalse("车牌号不存在",carService.isPlateNumberExist("B1111"));
		
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/cars_inserts.xml" })
	public void testisisVINExist(){
		
		assertTrue("识别码存在",carService.isVINExist("1"));
		assertFalse("识别码不存在",carService.isVINExist("11"));
		
	}

	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/cars_inserts.xml" })
	public void testisEngineSNExist(){
	
		assertTrue("发动机号存在",carService.isEngineSNExist("2"));
		assertFalse("发动机号不存在",carService.isEngineSNExist("22"));
		
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/order_inserts.xml" })
	public void testcanDeleteCar(){
		assertFalse("有订单和车辆关联，不能删除",carService.canDeleteCar(1L));		
		assertTrue("没有订单和车辆关联，可以删除",carService.canDeleteCar(3L));	
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/car/cars_inserts.xml" })
	public void testcanDeleteServicePoint(){
		assertFalse("有车辆和服务点关联，不能删除",carService.canDeleteCar(1L));		
		assertTrue("没有车辆和服务点关联，可以删除",carService.canDeleteCar(3L));	
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/order_inserts.xml" })
	public void testcanDeleteCarServiceType(){
		assertFalse("有订单和车型关联，不能删除",carService.canDeleteCar(1L));		
		assertTrue("没有订单和车型关联，可以删除",carService.canDeleteCar(3L));	
	}
}
