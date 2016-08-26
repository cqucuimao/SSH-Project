package com.yuqincar.timer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.service.car.CarService;

@Component
public class InsuranceExpired {

	@Autowired
	public CarService carService;

	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void update() {
		List<Car> cars = carService.getAll();
		Date now=new Date();
		for(Car car : cars) {
			if(car.getStatus()==CarStatusEnum.SCRAPPED || car.isBorrowed())
				continue;				
			if(car.getInsuranceExpiredDate()==null ||
				car.getInsuranceExpiredDate().before(now)){
				car.setInsuranceExpired(true);
				carService.updateCar(car);
			}
		}
	}
}
