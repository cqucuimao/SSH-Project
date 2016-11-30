package com.yuqincar.service.order;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.utils.QueryHelper;

public interface ProtocolOrderPayOrderService {
	public void saveProtocolOrderPayOrder(ProtocolOrderPayOrder popo);
	
	public void updateProtocolOrderPayOrder(ProtocolOrderPayOrder popo);
	
	public ProtocolOrderPayOrder getProtocolOrderPayOrderById(Long id);
	
	public void deleteProtocolOrderPayOrder(Long id);
	
	public PageBean<ProtocolOrderPayOrder> queryProtocolOrderPayOrder(QueryHelper helper,int pageNum);
	
	public boolean canAddProtocolOrderPayOrder(Order order);
}
