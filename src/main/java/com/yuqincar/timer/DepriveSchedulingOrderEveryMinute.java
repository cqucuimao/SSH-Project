package com.yuqincar.timer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.order.OrderDao;
import com.yuqincar.domain.order.Order;

@Component
public class DepriveSchedulingOrderEveryMinute {

	@Autowired
	public OrderDao orderDao;

	@Scheduled(cron = "0 * * * * ?") // 每分钟执行一次
	@Transactional
	public void deprive() {
		System.out.println("in deprive");
		System.out.println(new Date());
		List<Order> orderList=orderDao.getToBeDeprivedSchedulingOrder();
		for(Order order:orderList){
			System.out.println("order.sn="+order.getSn());
			order.setScheduling(false);
			order.setScheduler(null);
			order.setSchedulingBeginTime(null);
			orderDao.update(order);
		}
	}
}
