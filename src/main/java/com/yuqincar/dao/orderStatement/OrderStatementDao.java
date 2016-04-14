package com.yuqincar.dao.orderStatement;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.order.OrderStatement;

public interface OrderStatementDao extends BaseDao<OrderStatement> {
	
    /**
     * 根据对账单名称查询相应的对账单
     * @param orderStatementName
     * @return
     */
	OrderStatement getOrderStatementByName(String orderStatementName);

	/**
	 * 获取所有未支付的对账单列表
	 * @return
	 */
	List<OrderStatement> getAllUnpaidOrderStatement();

	/**
	 * 获取所有已支付的对账单列表
	 * @return
	 */
	List<OrderStatement> getAllPaidOrderStatement();

	public BigDecimal statisticOrderStatement(Date fromDate, Date toDate);

}
