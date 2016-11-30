package com.yuqincar.service.receipt.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.orderStatement.MoneyGatherInfoDao;
import com.yuqincar.dao.orderStatement.OrderStatementDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.MoneyGatherInfo;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.domain.order.OrderStatementStatusEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.receipt.ReceiptService;
import com.yuqincar.utils.QueryHelper;

@Service
public class ReceiptServiceImpl implements ReceiptService {

	@Autowired
	private OrderStatementDao orderStatementDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CustomerOrganizationService customerOrgnizationService;
	@Autowired
	private MoneyGatherInfoDao moneyGatherInfoDao;
	
	/**
	 * 查询出所有状态为新建的对账单
	 * @return 
	 */
	public List<OrderStatement> getAllNewOrderStatement(){
		return orderStatementDao.getAllNewOrderStatement();
	}
	
	/**
	 * 查询出所有状态为已开票的对账单
	 * @return 
	 */
	public List<OrderStatement> getAllInvoicedOrderStatement(){
		return orderStatementDao.getAllInvoicedOrderStatement();
	}
	
	/**
	 * 获取所有状态为已回款的对账单列表
	 * @return
	 */
	public List<OrderStatement> getAllPaidOrderStatement(){
		return orderStatementDao.getAllPaidOrderStatement();
	}
	/**
	 * 获取一条账单
	 * @return 
	 */
	public OrderStatement getOrderStatementById(Long id) {
		return orderStatementDao.getById(id);
		
	}

	/**
	 * 保存对账单信息
	 */
	@Transactional
	public void saveOrderStatement(OrderStatement orderStatement) {
		orderStatement.setActualTotalMoney(BigDecimal.ZERO);
		orderStatementDao.save(orderStatement);
	}
	
	@Transactional
	public void updateOrderStatement(OrderStatement orderStatement) {
		orderStatementDao.update(orderStatement);
	}

	/**
	 * 根据对账单名称和相应order的id数组，生成新对账单
	 * 新对账单的生成包含两个步骤，(1)将相应订单加到新建的对账单中;(2)改变这些加入到对账单中的订单的orderStatement状态
	 * @param ids
	 */
	@Transactional
	public void newOrderStatementByOrderIds(String orderStatementName,Long[] ids) {
		OrderStatement orderStatement=new OrderStatement();
		//对账单名称
		orderStatement.setName(orderStatementName);
		//设置订单生成时间
		orderStatement.setDate(new Date());
		//生成所有订单的集合
		List<Order> orders=new ArrayList<Order>();
		for(int i=0;i<ids.length;i++){
			Order order=orderService.getOrderById(ids[i]);
			orders.add(order);
		}
		//对账单所属单位  默认使用第一个订单的单位
		CustomerOrganization customerOrganization=orders.get(0).getCustomerOrganization();
		//设置对账单中的单位名称
		orderStatement.setCustomerOrganization(customerOrganization);
		//将对账单的起止时间初始化为第一个订单的起止时间
		Date fromDate=orders.get(0).getActualBeginDate();
		Date toDate=orders.get(0).getActualEndDate();
		//找到所有订单的最早开始时间
		for(int i=0;i<orders.size();i++){
		    if(orders.get(i).getActualBeginDate().before(fromDate))
		       fromDate=orders.get(i).getActualBeginDate();
		}
		//找到所有订单的最晚截止时间
	    for(int i=0;i<orders.size();i++){
		    if(orders.get(i).getActualEndDate().after(toDate))
		       toDate=orders.get(i).getActualEndDate();
		}
	    //设置最早日期
	    orderStatement.setFromDate(fromDate);
	    //设置最晚日期
	    orderStatement.setToDate(toDate);
	    //设置对账单中的订单数量
	    orderStatement.setOrderNum(orders.size());
	    //所包含的订单总金额
	    BigDecimal totalMoney=new BigDecimal(0);
	    for(int i=0;i<orders.size();i++){
	    	totalMoney=totalMoney.add(orders.get(i).getActualMoney());
	    }
	    //设置总金额
	    orderStatement.setTotalMoney(totalMoney);
		//对账单中的订单序列
		orderStatement.setOrders(orders);
		//设置对账单状态,新建对账单都为 UNPAYED状态
		orderStatement.setStatus(OrderStatementStatusEnum.NEW);
		orderStatement.setActualTotalMoney(BigDecimal.ZERO);
		//保存orderStatement对象
		orderStatementDao.save(orderStatement);
		//改变对应的所有订单的状态
		for(int i=0;i<orders.size();i++){
			Order order=orders.get(i);
			order.setOrderStatement(orderStatement);
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
	public void addOrderStatementByOrderIds(String orderStatementName, Long[] ids) {	
		//查询orderStatementName对应的对账单
		OrderStatement orderStatement=orderStatementDao.getOrderStatementByName(orderStatementName);
		if(orderStatement==null)
			return;
		//获取对账单中的订单列表
		List<Order> orders=orderStatement.getOrders();
		BigDecimal ordersMoney=new BigDecimal(0);
		//将ids对应的order加入到对账单对应的orders中,并改变相应订单的orderStatement状态,同时计算这些订单的总金额
		for(int i=0;i<ids.length;i++){
			Order order=orderService.getOrderById(ids[i]);
			orders.add(order);
			order.setOrderStatement(orderStatement);
			ordersMoney=ordersMoney.add(order.getActualMoney());
		}
		//设置对账单的订单集合
		orderStatement.setOrders(orders);
		//设置对账单的总金额
		orderStatement.setTotalMoney(orderStatement.getTotalMoney().add(ordersMoney));
		//设置对账单的订单数
		orderStatement.setOrderNum(orderStatement.getOrderNum()+ids.length);
		//获得原对账单的起止时间
		Date fromDate=orderStatement.getFromDate();
		Date toDate=orderStatement.getToDate();
		//找到所有订单的最早开始时间
		for(int i=0;i<orders.size();i++){
			if(orders.get(i).getActualBeginDate().before(fromDate))
			   fromDate=orders.get(i).getActualBeginDate();
		}
	    //找到所有订单的最晚截止时间
	    for(int i=0;i<orders.size();i++){
			if(orders.get(i).getActualEndDate().after(toDate))
			   toDate=orders.get(i).getActualEndDate();
		}
	    //设置最早日期
	    orderStatement.setFromDate(fromDate);
	    //设置最晚日期
	    orderStatement.setToDate(toDate);
	}

	/**
	 * 分页公共类
	 */
	public PageBean<OrderStatement> getPageBean(int pageNum, QueryHelper helper) {
		return orderStatementDao.getPageBean(pageNum, helper);
	}

	/**
	 * 判断名称为 orderStatementName 的对账单是否存在
	 * @param orderStatementName
	 * @return
	 */
	public boolean isOrderStatementExist(String orderStatementName) {
		OrderStatement orderStatement=orderStatementDao.getOrderStatementByName(orderStatementName);
		if(orderStatement==null)
			return false;
		else 
			return true;
			
	}

	/**
	 * 根据对账单名称查询相应的对账单
	 * @param orderStatementName
	 * @return
	 */
	public OrderStatement getOrderStatementByName(String orderStatementName) {
		return orderStatementDao.getOrderStatementByName(orderStatementName);
	}

	/**
	 * 根据对账单名称和订单id，从对账单中除去相应的订单
	 * @param orderStatementName
	 * @param ids
	 */
	@Transactional
	public void excludeOrdersFromOrderStatement(Long orderStatementId, Long[] ids) {
		//查找相应的对账单
		OrderStatement orderStatement=orderStatementDao.getById(orderStatementId);
		List<Order> orders=orderStatement.getOrders();
		List<Long> idList=Arrays.asList(ids);
		//判断取消的订单数是否等于订单总数，如果等于订单总数，则删除该对账单
		if(orders.size()==ids.length){
		   cancelOrderStatement(orderStatementId);
		}else{
		   //根据订单id修改相应订单的orderStatement状态
		   for(Order order:orders){
			   if(idList.contains(order.getId())){
				   order.setOrderStatement(null);
			   }
		   }
		   //获得排除相应订单之后orderStatement的order集合
		   for(int i=0;i<ids.length;i++){
			   for(int j=0;j<orders.size();j++){
				   if(orders.get(j).getId().longValue()==ids[i].longValue())
					  orders.remove(j);
			   }
		   }
		   //重置对账单的订单集合
		   orderStatement.setOrders(orders);
		   //将对账单的起止时间初始化为第一个订单的起止时间
		   Date fromDate=orders.get(0).getActualBeginDate();
		   Date toDate=orders.get(0).getActualEndDate();
		   //找到所有订单的最早开始时间
		   for(int i=0;i<orders.size();i++){
		       if(orders.get(i).getActualBeginDate().before(fromDate))
		          fromDate=orders.get(i).getActualBeginDate();
		   }
		   //找到所有订单的最晚截止时间
		   for(int i=0;i<orders.size();i++){
		       if(orders.get(i).getActualEndDate().after(toDate))
		          toDate=orders.get(i).getActualEndDate();
		   }
		   //设置最早日期
		   orderStatement.setFromDate(fromDate);
		   //设置最晚日期
	       orderStatement.setToDate(toDate);
	       //设置对账单中的订单数量
	       orderStatement.setOrderNum(orders.size());
		   //所包含的订单总金额
		   BigDecimal totalMoney=new BigDecimal(0);
		   for(int i=0;i<orders.size();i++){
		       totalMoney=totalMoney.add(orders.get(i).getActualMoney());
		   }
		   //设置总金额
		   orderStatement.setTotalMoney(totalMoney);
		   System.out.println("======执行排除操作了=====");
		}
	}

    /** 
	 * 根据对账单的名称取消相应的对账单
     * 包括两个步骤:(1)重置该对账单对应的order的orderStatement为null,(2)删除该对账单
	 * @param orderStatementName
	 */
	@Transactional
	public void cancelOrderStatement(Long orderStatementId) {
	     OrderStatement orderStatement=orderStatementDao.getById(orderStatementId);
	     List<Order> orders=orderStatement.getOrders();
	     for(Order order:orders){
	         order.setOrderStatement(null);
	     }
	     orderStatementDao.delete(orderStatement.getId());
	}

	/**
	 * 根据对账单名称对相应对账单确认收款
	 * 包括两个步骤:(1)对所有相应订单确认收款,(2)对该对账单确认收款
	 * @param orderStatementName
	 */
	@Transactional
	public void confirmReceipt(String orderStatementName) {
		 OrderStatement orderStatement=orderStatementDao.getOrderStatementByName(orderStatementName);
	     List<Order> orders=orderStatement.getOrders();
	     for(Order order:orders){
	         order.setStatus(OrderStatusEnum.PAID);
	     }
	     orderStatement.setStatus(OrderStatementStatusEnum.PAID);
	}
	
	public BigDecimal statisticOrderStatement(Date fromDate, Date toDate) {
		return orderStatementDao.statisticOrderStatement(fromDate, toDate);
	}
	
	@Transactional
	public void saveMoneyGatherInfo(MoneyGatherInfo moneyGatherInfo){
		moneyGatherInfoDao.save(moneyGatherInfo);
		
		OrderStatement orderStatement=moneyGatherInfo.getOrderStatement();
		if(orderStatement.getActualTotalMoney()==null)
			orderStatement.setActualTotalMoney(BigDecimal.ZERO);
		orderStatement.setActualTotalMoney(orderStatement.getActualTotalMoney().add(moneyGatherInfo.getMoney()));
		orderStatementDao.update(orderStatement);
	}
	
	@Transactional	
	public void moneyGatherComplete(OrderStatement orderStatement){
		orderStatement.setStatus(OrderStatementStatusEnum.PAID);
		orderStatementDao.update(orderStatement);
	}
	
}
