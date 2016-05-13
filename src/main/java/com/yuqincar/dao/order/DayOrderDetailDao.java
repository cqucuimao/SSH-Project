package com.yuqincar.dao.order;

import java.util.Date;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.Order;

public interface DayOrderDetailDao extends BaseDao<DayOrderDetail> {
	public DayOrderDetail getDayOrderDetailByDate(Order order, Date date);
	
	public DayOrderDetail getUngetoffDayOrderDetail(Order order);
}
