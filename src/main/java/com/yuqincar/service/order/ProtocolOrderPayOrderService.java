package com.yuqincar.service.order;

import java.util.Date;
import java.util.List;

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
	
	/**
	 * 根据对账单名称和相应order的id数组，生成新对账单
	 * 新对账单的生成包含两个步骤，(1)将相应订单加到新建的对账单中;(2)改变这些加入到对账单中的订单的orderStatement状态
	 * @param orderStatementName 
	 * @param ids
	 */
	void newOrderStatementByPopoIds(String orderStatementName, Long[] ids);

	/**
	 * 根据对账单名称和相应order的id数组，将order的id数组对应的对账单加入到相应的对账单名称的对账单中
	 * 添加对账单包括两个步骤，(1)将相应的订单添加到相应对账单名称对应的对账单中;(2)改变这些加入到对账单中的订单的orderStatement状态
	 * @param orderStatementName
	 * @param ids
	 */
	void addOrderStatementByPopoIds(String orderStatementName, Long[] ids);
}
