package com.yuqincar.dao.orderStatement.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.orderStatement.OrderStatementDao;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.domain.order.OrderStatementStatusEnum;

@Repository
public class OrderStatementDaoImpl extends BaseDaoImpl<OrderStatement> implements OrderStatementDao {

	/**
     * 根据对账单名称查询相应的对账单
     * @param orderStatementName
     * @return
     */
	public OrderStatement getOrderStatementByName(String orderStatementName) {
		return (OrderStatement)(getSession().createQuery("from OrderStatement o where o.name=?")
				.setParameter(0, orderStatementName).uniqueResult());
	}

	/**
	 * 获取所有状态为新建的对账单列表
	 * @return
	 */
	public List<OrderStatement> getAllNewOrderStatement(){
		return getSession().createQuery("from OrderStatement o where o.status=?")
				.setParameter(0, OrderStatementStatusEnum.NEW).list();
	}
	
	/**
	 * 获取所有状态为已开票的对账单列表
	 * @return
	 */
	public List<OrderStatement> getAllInvoicedOrderStatement(){
		return getSession().createQuery("from OrderStatement o where o.status=?")
				.setParameter(0, OrderStatementStatusEnum.INVOICED).list();
	}
	
	/**
	 * 获取所有状态为已回款的对账单列表
	 * @return
	 */
	public List<OrderStatement> getAllPaidOrderStatement(){
		return getSession().createQuery("from OrderStatement o where o.status=?")
				.setParameter(0, OrderStatementStatusEnum.PAID).list();
	}
	
	public BigDecimal statisticOrderStatement(Date fromDate, Date toDate) {
		BigDecimal totalMoney= (BigDecimal)getSession().createQuery("select sum(os.totalMoney) from OrderStatement as os where TO_DAYS(?) <= TO_DAYS(os.date) and TO_DAYS(os.date) <= TO_DAYS(?)")
				.setParameter(0, fromDate).setParameter(1, toDate).uniqueResult();
		if(totalMoney==null)
			totalMoney=BigDecimal.ZERO;
		return totalMoney;
	}

}
