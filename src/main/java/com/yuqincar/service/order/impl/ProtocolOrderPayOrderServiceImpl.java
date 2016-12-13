package com.yuqincar.service.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.order.ProtocolOrderPayOrderDao;
import com.yuqincar.dao.orderStatement.OrderStatementDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.domain.order.OrderStatementStatusEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.service.order.ProtocolOrderPayOrderService;
import com.yuqincar.utils.QueryHelper;


@Service
public class ProtocolOrderPayOrderServiceImpl implements
		ProtocolOrderPayOrderService {
	@Autowired
	private ProtocolOrderPayOrderDao popoDao;
	@Autowired
	private OrderStatementDao orderStatementDao;
	
	@Transactional
	public void saveProtocolOrderPayOrder(ProtocolOrderPayOrder popo) {
		popoDao.save(popo);
	}

	@Transactional
	public void updateProtocolOrderPayOrder(ProtocolOrderPayOrder popo) {
		popoDao.update(popo);
	}

	public ProtocolOrderPayOrder getProtocolOrderPayOrderById(Long id) {
		return popoDao.getById(id);
	}
	@Transactional
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
	
	/**
	 * 根据对账单名称和相应popo的id数组，生成新对账单
	 * 新对账单的生成包含两个步骤，(1)将相应订单加到新建的对账单中;(2)改变这些加入到对账单中的订单的orderStatement状态
	 * @param ids
	 */
	@Transactional
	public void newOrderStatementByPopoIds(String orderStatementName,Long[] ids) {
		OrderStatement orderStatement=new OrderStatement();
		//对账单名称
		orderStatement.setName(orderStatementName);
		//设置订单生成时间
		orderStatement.setDate(new Date());
		//生成所有订单的集合
		List<ProtocolOrderPayOrder> popos=new ArrayList<ProtocolOrderPayOrder>();
		for(int i=0;i<ids.length;i++){
			ProtocolOrderPayOrder popo= new ProtocolOrderPayOrder();
			popo = popoDao.getById(ids[i]);
			System.out.println("popo="+popo);
			popos.add(popo);
		}
		//对账单所属单位  默认使用第一个订单的单位
		CustomerOrganization customerOrganization=popos.get(0).getOrder().getCustomerOrganization();
		//设置对账单中的单位名称
		orderStatement.setCustomerOrganization(customerOrganization);
		//将对账单的起止时间初始化为第一个订单的起止时间
		Date fromDate=popos.get(0).getFromDate();
		Date toDate=popos.get(0).getToDate();
		//找到所有订单的最早开始时间
		for(int i=0;i<popos.size();i++){
		    if(popos.get(i).getFromDate().before(fromDate))
		       fromDate=popos.get(i).getFromDate();
		}
		//找到所有订单的最晚截止时间
	    for(int i=0;i<popos.size();i++){
		    if(popos.get(i).getToDate().after(toDate))
		       toDate=popos.get(i).getToDate();
		}
	    //设置最早日期
	    orderStatement.setFromDate(fromDate);
	    //设置最晚日期
	    orderStatement.setToDate(toDate);
	    //设置对账单中的订单数量
	    orderStatement.setOrderNum(popos.size());
	    //所包含的订单总金额
	    BigDecimal totalMoney=new BigDecimal(0);
	    for(int i=0;i<popos.size();i++){
	    	totalMoney=totalMoney.add(popos.get(i).getMoney());
	    }
	    //设置总金额
	    orderStatement.setTotalMoney(totalMoney);
		//对账单中的协议订单收款单序列
		orderStatement.setPopos(popos);
		//设置对账单状态,新建对账单都为 UNPAYED状态
		orderStatement.setStatus(OrderStatementStatusEnum.NEW);
		orderStatement.setActualTotalMoney(BigDecimal.ZERO);
		//保存orderStatement对象
		orderStatementDao.save(orderStatement);
		//改变对应的所有popo的状态
		for(int i=0;i<popos.size();i++){
			ProtocolOrderPayOrder popo=popos.get(i);
			popo.setOrderStatement(orderStatement);
		}
	}

	/**
	 * 根据对账单名称和相应order的id数组，将order的id数组对应的对账单加入到相应的对账单名称的对账单中
	 * 添加对账单包括如下步骤，(1)将相应的订单添加到相应对账单名称对应的对账单中;
	 *                 (2)改变这些加入到对账单中的订单的orderStatement状态
	 *                 (3)改变对账单的总金额、订单数、起止时间
	 * @param orderStatementName
	 * @param ids
	 */
	@Transactional
	public void addOrderStatementByPopoIds(String orderStatementName, Long[] ids) {	
		//查询orderStatementName对应的对账单
		OrderStatement orderStatement=orderStatementDao.getOrderStatementByName(orderStatementName);
		if(orderStatement==null)
			return;
		//获取对账单中的popo列表
		List<ProtocolOrderPayOrder> popos=orderStatement.getPopos();
		BigDecimal ordersMoney=new BigDecimal(0);
		//将ids对应的order加入到对账单对应的popos中,并改变相应订单的orderStatement状态,同时计算这些订单的总金额
		for(int i=0;i<ids.length;i++){
			ProtocolOrderPayOrder popo=popoDao.getById(ids[i]);
			popos.add(popo);
			popo.setOrderStatement(orderStatement);
			ordersMoney=ordersMoney.add(popo.getMoney());
		}
		//设置对账单的popo集合
		orderStatement.setPopos(popos);
		//设置对账单的总金额
		orderStatement.setTotalMoney(orderStatement.getTotalMoney().add(ordersMoney));
		//设置对账单的订单数
		orderStatement.setOrderNum(orderStatement.getOrderNum()+ids.length);
		//获得原对账单的起止时间
		Date fromDate=orderStatement.getFromDate();
		Date toDate=orderStatement.getToDate();
		//找到所有订单的最早开始时间
		for(int i=0;i<popos.size();i++){
			if(popos.get(i).getFromDate().before(fromDate))
			   fromDate=popos.get(i).getToDate();
		}
	    //找到所有订单的最晚截止时间
	    for(int i=0;i<popos.size();i++){
			if(popos.get(i).getToDate().after(toDate))
			   toDate=popos.get(i).getToDate();
		}
	    //设置最早日期
	    orderStatement.setFromDate(fromDate);
	    //设置最晚日期
	    orderStatement.setToDate(toDate);
	}

	public List<ProtocolOrderPayOrder> getAllPopos() {
		return popoDao.getAll();
	}
}
