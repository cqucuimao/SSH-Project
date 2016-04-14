package com.yuqincar.timer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.service.car.CarService;

@Component
public class MileageUpdateEveryday {

	@Autowired
	public CarService carService;

	@Autowired
	public LBSDao lbsDao;

	@Scheduled(cron = "0 0 1 * * ?") // 每天凌晨3点执行一次
	@Transactional
	public void update() {
		// 每天凌晨3点执行一次
		List<Car> cars = carService.getAll();
		for(Car car : cars) {
			int mile = (int) lbsDao.getCurrentMile(car.getDevice().getSN());
			car.setMileage(mile);
			carService.updateCar(car);
		}
	}
}
