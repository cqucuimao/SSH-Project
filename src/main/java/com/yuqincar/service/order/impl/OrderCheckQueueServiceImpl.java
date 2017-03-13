package com.yuqincar.service.order.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.order.OrderCheckQueueDao;
import com.yuqincar.domain.order.OrderCheckQueue;
import com.yuqincar.service.order.OrderCheckQueueService;

@Service
public class OrderCheckQueueServiceImpl implements OrderCheckQueueService {
	@Autowired
	private OrderCheckQueueDao orderCheckQueueDao;
	
	@Transactional
	public void save(OrderCheckQueue orderCheckQueue) {
		orderCheckQueueDao.save(orderCheckQueue);
	}

	@Transactional
	public void delete(long id) {
		orderCheckQueueDao.delete(id);
	}

	@Transactional
	public void update(OrderCheckQueue orderCheckQueue) {
		orderCheckQueueDao.update(orderCheckQueue);
	}
	
	public List<OrderCheckQueue> getAll() {
		return orderCheckQueueDao.getAll();
	}

}
