package com.yuqincar.service.order.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuqincar.dao.order.ReserveCarApplyOrderDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ReserveCarApplyOrder;
import com.yuqincar.domain.order.ReserveCarApplyOrderStatusEnum;
import com.yuqincar.service.order.ReserveCarApplyOrderService;
import com.yuqincar.utils.QueryHelper;

@Service
public class ReserveCarApplyOrderServiceImpl implements ReserveCarApplyOrderService {
	@Autowired
	private ReserveCarApplyOrderDao reserveCarApplyOrderDao;
	
	public void saveReserveCarApplyOrder(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrderDao.save(reserveCarApplyOrder);
	}
	
	public void updateReserveCarApplyOrder(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrderDao.update(reserveCarApplyOrder);
	}
	
	public void deleteReserveCarApplyOrder(Long id){
		reserveCarApplyOrderDao.delete(id);
	}
	
	public PageBean<ReserveCarApplyOrder> queryReserveCarApplyOrder(int pageNum, QueryHelper helper){
		return reserveCarApplyOrderDao.getPageBean(pageNum, helper);
	}
	
	public List<ReserveCarApplyOrder> queryAllReserveCarApplyOrder(QueryHelper helper){
		return reserveCarApplyOrderDao.getAllQuerry(helper);
	}

	public List<ReserveCarApplyOrder> getNeedCheckReserveCarApplyOrders() {
		QueryHelper queryHelper=new QueryHelper(ReserveCarApplyOrder.class,"rcao");
		queryHelper.addWhereCondition("rcao.status=?", ReserveCarApplyOrderStatusEnum.APPROVED);
		return reserveCarApplyOrderDao.getAllQuerry(queryHelper);
	}
	
}
