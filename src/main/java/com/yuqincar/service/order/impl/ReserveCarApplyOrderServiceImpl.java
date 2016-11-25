package com.yuqincar.service.order.impl;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.order.ReserveCarApplyOrderDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ReserveCarApplyOrder;
import com.yuqincar.domain.order.ReserveCarApplyOrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.order.ReserveCarApplyOrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.QueryHelper;

@Service
public class ReserveCarApplyOrderServiceImpl implements ReserveCarApplyOrderService {
	@Autowired
	private ReserveCarApplyOrderDao reserveCarApplyOrderDao;
	@Autowired
	private UserService userService;
	@Transactional
	public void saveReserveCarApplyOrder(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrderDao.save(reserveCarApplyOrder);
	}
	@Transactional
	public void updateReserveCarApplyOrder(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrderDao.update(reserveCarApplyOrder);
	}
	@Transactional
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
		queryHelper.addWhereCondition("rcao.status=?", ReserveCarApplyOrderStatusEnum.CONFIGURED);
		return reserveCarApplyOrderDao.getAllQuerry(queryHelper);
	}

	public ReserveCarApplyOrder getById(Long id) {
		return reserveCarApplyOrderDao.getById(id);
	}
	public List<User> sortUserByName(List<User> users) {

        Comparator<Object> com=Collator.getInstance(java.util.Locale.CHINA);
		
		//排序
		List<User> sortList = new ArrayList<User>();
		String[] names = new String[users.size()];
		for(int i=0;i<users.size();i++){
			names[i] = users.get(i).getName();
		}
		Arrays.sort(names,com);
		for(String name:names){
			sortList.add(userService.getByName(name));
		}
		return sortList;
	}
	public List<ReserveCarApplyOrder> getReserveCarApplyOrderByStatus(ReserveCarApplyOrderStatusEnum status) {
		QueryHelper queryHelper=new QueryHelper(ReserveCarApplyOrder.class,"rcao");
		queryHelper.addWhereCondition("rcao.status=?", status);
		return reserveCarApplyOrderDao.getAllQuerry(queryHelper);
	}
	
}
