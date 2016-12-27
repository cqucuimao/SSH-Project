package com.yuqincar.timer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.domain.order.OrderCheckQueue;
import com.yuqincar.service.order.OrderCheckQueueService;
import com.yuqincar.service.order.OrderService;

@Component
public class CheckOrder {
	@Autowired
	private OrderCheckQueueService orderCheckQueueService;
	@Autowired
	private OrderService orderService;
	
	@Scheduled(cron = "10 * * * * ? ") 
	@Transactional
	public void check(){
		List<OrderCheckQueue> ocqList=orderCheckQueueService.getAll();
		for(OrderCheckQueue ocq:ocqList){
			try{
				orderService.orderCheckout(ocq.getOrder());
				orderCheckQueueService.delete(ocq.getId());
			}catch(Exception e){
				e.printStackTrace();
				ocq.setCounter(ocq.getCounter()+1);
				orderCheckQueueService.update(ocq);
			}
		}
	}
	
}
