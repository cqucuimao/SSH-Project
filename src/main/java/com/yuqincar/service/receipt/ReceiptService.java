package com.yuqincar.service.receipt;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.MoneyGatherInfo;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.utils.QueryHelper;

public interface ReceiptService {
	
	/**
	 * 查询出所有状态为新建的对账单
	 * @return 
	 */
	List<OrderStatement> getAllNewOrderStatement();
	
	/**
	 * 查询出所有状态为已开票的对账单
	 * @return 
	 */
	List<OrderStatement> getAllInvoicedOrderStatement();
	
	/**
	 * 获取所有状态为已回款的对账单列表
	 * @return
	 */
	List<OrderStatement> getAllPaidOrderStatement();
	
	/**
	 * 获取一条对账单
	 * @return 
	 */
	OrderStatement getOrderStatementById(Long id);
	
	/**
	 * 保存对账单信息
	 * @param orderStatement
	 */
	void saveOrderStatement(OrderStatement orderStatement);
	

	/**
	 * 保存对账单信息
	 * @param orderStatement
	 */
	void updateOrderStatement(OrderStatement orderStatement);

	/**
	 * 根据对账单名称和相应order的id数组，生成新对账单
	 * 新对账单的生成包含两个步骤，(1)将相应订单加到新建的对账单中;(2)改变这些加入到对账单中的订单的orderStatement状态
	 * @param orderStatementName 
	 * @param ids
	 */
	void newOrderStatementByOrderIds(String orderStatementName, Long[] ids);

	/**
	 * 根据对账单名称和相应order的id数组，将order的id数组对应的对账单加入到相应的对账单名称的对账单中
	 * 添加对账单包括两个步骤，(1)将相应的订单添加到相应对账单名称对应的对账单中;(2)改变这些加入到对账单中的订单的orderStatement状态
	 * @param orderStatementName
	 * @param ids
	 */
	void addOrderStatementByOrderIds(String orderStatementName, Long[] ids);

	/**
	 * 分页公共类
	 * @param pageNum
	 * @param helper
	 * @return
	 */
	PageBean<OrderStatement> getPageBean(int pageNum, QueryHelper helper);

	/**
	 * 判断名称为 orderStatementName 的对账单是否存在
	 * @param orderStatementName
	 * @return
	 */
	boolean isOrderStatementExist(String orderStatementName);

	/**
	 * 根据对账单名称查询相应的对账单
	 * @param orderStatementName
	 * @return
	 */
	OrderStatement getOrderStatementByName(String orderStatementName);

	/**
	 * 根据对账单名称和订单id，从对账单中除去相应的订单
	 * @param orderStatementName
	 * @param ids
	 */
	
	void excludeOrdersAndPoposFromOrderStatement(Long orderStatementId, Long[] ids,Long[] idsOfPopo);
	
	/**
	 * 根据对账单的名称取消相应的对账单
	 * @param orderStatementName
	 */
	public void cancelOrderStatement(Long orderStatementId);

	/**
	 * 根据对账单名称对相应对账单确认收款
	 * @param orderStatementName
	 */
	void confirmReceipt(String orderStatementName);
	
	public BigDecimal statisticOrderStatement(Date fromDate, Date toDate);
	
	public void saveMoneyGatherInfo(MoneyGatherInfo gatherMoneyInfo);
	
	public void moneyGatherComplete(OrderStatement orderStatement);

}
