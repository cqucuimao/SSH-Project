package com.yuqincar.dao.orderStatement.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.orderStatement.OrderStatementDao;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.domain.order.OrderStatementStatusEnum;
import com.yuqincar.domain.order.OrderStatusEnum;

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
	 * 获取所有未支付的对账单列表
	 * @return
	 */
	public List<OrderStatement> getAllUnpaidOrderStatement() {
		return getSession().createQuery("from OrderStatement o where o.status=?")//
				.setParameter(0, OrderStatementStatusEnum.UNPAYED).list();
	}

	/**
	 * 获取所有已支付的对账单列表
	 * @return
	 */
	public List<OrderStatement> getAllPaidOrderStatement() {
		return getSession().createQuery("from OrderStatement o where o.status=?")//
				.setParameter(0, OrderStatementStatusEnum.PAYED).list();
	}

	public BigDecimal statisticOrderStatement(Date fromDate, Date toDate) {
		BigDecimal totalMoney= (BigDecimal)getSession().createQuery("select sum(os.totalMoney) from OrderStatement as os where TO_DAYS(?) <= TO_DAYS(os.date) and TO_DAYS(os.date) <= TO_DAYS(?)")
				.setParameter(0, fromDate).setParameter(1, toDate).uniqueResult();
		if(totalMoney==null)
			totalMoney=BigDecimal.ZERO;
		return totalMoney;
	}

}
