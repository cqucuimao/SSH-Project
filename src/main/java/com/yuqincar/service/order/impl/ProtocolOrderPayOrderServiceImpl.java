package com.yuqincar.service.order.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.order.ProtocolOrderPayOrderDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.service.order.ProtocolOrderPayOrderService;
import com.yuqincar.utils.QueryHelper;

@Service
public class ProtocolOrderPayOrderServiceImpl implements
		ProtocolOrderPayOrderService {
	@Autowired
	private ProtocolOrderPayOrderDao popoDao;
	
	@Transactional
	public void saveProtocolOrderPayOrder(ProtocolOrderPayOrder popo) {
		popoDao.save(popo);
	}

	public void updateProtocolOrderPayOrder(ProtocolOrderPayOrder popo) {
		popoDao.update(popo);
	}

	public ProtocolOrderPayOrder getProtocolOrderPayOrderById(Long id) {
		return popoDao.getById(id);
	}

	public void deleteProtocolOrderPayOrder(Long id) {
		popoDao.delete(id);
	}

	public PageBean<ProtocolOrderPayOrder> queryProtocolOrderPayOrder(
			QueryHelper helper, int pageNum) {
		return popoDao.getPageBean(pageNum, helper);
	}
	
	public boolean canAddProtocolOrderPayOrder(Order order){
		if(order.getChargeMode()!=ChargeModeEnum.PROTOCOL)
			return false;
		if(order.getStatus()==OrderStatusEnum.INQUEUE || order.getStatus()==OrderStatusEnum.END || 
				order.getStatus()==OrderStatusEnum.PAID || order.getStatus()==OrderStatusEnum.CANCELLED)
			return false;
		return true;
	}

}
