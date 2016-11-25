package com.yuqincar.service.order;

import java.util.List;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.ReserveCarApplyOrder;
import com.yuqincar.domain.order.ReserveCarApplyOrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.QueryHelper;

public interface ReserveCarApplyOrderService {
	public void saveReserveCarApplyOrder(ReserveCarApplyOrder reserveCarApplyOrder);
	public void updateReserveCarApplyOrder(ReserveCarApplyOrder reserveCarApplyOrder);
	public void deleteReserveCarApplyOrder(Long id);
	public PageBean<ReserveCarApplyOrder> queryReserveCarApplyOrder(int pageNum, QueryHelper helper);
	public List<ReserveCarApplyOrder> queryAllReserveCarApplyOrder(QueryHelper helper);
	public List<ReserveCarApplyOrder> getNeedCheckReserveCarApplyOrders();
	public ReserveCarApplyOrder getById(Long id);
	public List<User> sortUserByName(List<User> users);
	public List<ReserveCarApplyOrder> getReserveCarApplyOrderByStatus(ReserveCarApplyOrderStatusEnum status);
}