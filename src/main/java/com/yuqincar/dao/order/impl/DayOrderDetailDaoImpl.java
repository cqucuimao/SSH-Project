package com.yuqincar.dao.order.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.order.DayOrderDetailDao;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.Order;

@Repository
public class DayOrderDetailDaoImpl extends BaseDaoImpl<DayOrderDetail> implements DayOrderDetailDao {
	public DayOrderDetail getDayOrderDetailByDate(Order order, Date date){
		String hql = "from DayOrderDetail as dod where dod.order=? and TO_DAYS(dod.getonDate)=TO_DAYS(?)";
		return (DayOrderDetail)getSession().createQuery(hql).
				setParameter(0, order).setParameter(1, date).uniqueResult();
	}
	
	public DayOrderDetail getUngetoffDayOrderDetail(Order order){
		String hql = "from DayOrderDetail as dod where dod.order=? and dod.getoffDate is null";
		return (DayOrderDetail)getSession().createQuery(hql).setParameter(0, order).uniqueResult();
	}
}
