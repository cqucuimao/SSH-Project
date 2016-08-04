package com.yuqincar.timer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.service.car.CarService;

@Component
public class MileageUpdate {

	@Autowired
	public CarService carService;

	@Autowired
	public LBSDao lbsDao;

	@Scheduled(cron = "0 0 1 * * ?")
	@Transactional
	public void update() {
		List<Car> cars = carService.getAll();
		for(Car car : cars) {
			if(car.getStatus()==CarStatusEnum.SCRAPPED)
				continue;
			if(car.getDevice()==null || car.getDevice().getSN()==null)
				continue;
			
			int mile = (int) lbsDao.getCurrentMile(car.getDevice().getSN());
			car.setMileage(mile);
			
			//判断是否保养过期
			if(car.getMileage()>car.getNextCareMile())
				car.setCareExpired(true);
			carService.updateCar(car);
		}
	}
}
