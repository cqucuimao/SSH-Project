package com.yuqincar.timer;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.order.ReserveCarApplyOrder;
import com.yuqincar.domain.order.ReserveCarApplyOrderStatusEnum;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.ReserveCarApplyOrderService;
import com.yuqincar.utils.DateUtils;

@Component
public class StandingGarageCarCheck {
	@Autowired
	ReserveCarApplyOrderService reserveCarApplyOrderService;
	@Autowired
	CarService carService;
	
	
	@Scheduled(cron = "0 10 0 * * ?")
	@Transactional
	public void update(){
		for(ReserveCarApplyOrder rcao:reserveCarApplyOrderService.getNeedCheckReserveCarApplyOrders()){
			Date from=DateUtils.getMinDate(rcao.getFromDate());
			Date to=DateUtils.getMaxDate(rcao.getToDate());
			Date now=new Date();
			if(to.before(now)){
				for(Car car:rcao.getCars()){
					car.setTempStandingGarage(false);
					carService.updateCar(car);
				}
				rcao.setStatus(ReserveCarApplyOrderStatusEnum.EXPIRED);
				reserveCarApplyOrderService.update(rcao);
			}
		}
	}
}
