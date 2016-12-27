package com.yuqincar.timer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.domain.order.Order;
import com.yuqincar.service.order.OrderService;

@Component
public class DepriveSchedulingOrder {

	@Autowired
	public OrderService orderService;

	@Scheduled(cron = "15 * * * * ?") // 每分钟（第0秒）执行一次
	@Transactional
	public void deprive() {
		System.out.println("in deprive");
		List<Order> orderList=orderService.getToBeDeprivedSchedulingOrder();
		for(Order order:orderList){
			order.setScheduling(false);
			order.setScheduler(null);
			order.setSchedulingBeginTime(null);
			orderService.update(order);
		}
	}
}
