package com.yuqincar.service.order;

import java.util.List;

import com.yuqincar.domain.order.OrderCheckQueue;

public interface OrderCheckQueueService {
	public void save(OrderCheckQueue orderCheckQueue);
	
	public void delete(long id);
	
	public void update(OrderCheckQueue orderCheckQueue);
	
	public List<OrderCheckQueue> getAll();
}
