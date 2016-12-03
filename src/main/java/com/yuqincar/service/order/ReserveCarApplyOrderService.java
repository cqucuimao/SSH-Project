package com.yuqincar.service.order;

import java.util.List;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.ReserveCarApplyOrder;
import com.yuqincar.domain.order.ReserveCarApplyOrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.QueryHelper;

public interface ReserveCarApplyOrderService {
	public boolean canDelete(ReserveCarApplyOrder reserveCarApplyOrder,User user);
	public boolean canUpdate(ReserveCarApplyOrder reserveCarApplyOrder,User user);
	public boolean canApprove(ReserveCarApplyOrder reserveCarApplyOrder,User user);
	public boolean canConfigCar(ReserveCarApplyOrder reserveCarApplyOrder,User user);
	public boolean canConfigDriver(ReserveCarApplyOrder reserveCarApplyOrder,User user);
	public void save(ReserveCarApplyOrder reserveCarApplyOrder);
	public void update(ReserveCarApplyOrder reserveCarApplyOrder);
	public void saveAndSubmit(ReserveCarApplyOrder reserveCarApplyOrder);
	public void submit(ReserveCarApplyOrder reserveCarApplyOrder);
	public void approve(ReserveCarApplyOrder reserveCarApplyOrder);
	public void reject(ReserveCarApplyOrder reserveCarApplyOrder);
	public void configCar(ReserveCarApplyOrder reserveCarApplyOrder);
	public void configDriver(ReserveCarApplyOrder reserveCarApplyOrder);
	public void delete(Long id);
	public PageBean<ReserveCarApplyOrder> queryReserveCarApplyOrder(int pageNum, QueryHelper helper);
	public List<ReserveCarApplyOrder> getNeedCheckReserveCarApplyOrders();
	public ReserveCarApplyOrder getById(Long id);
	public List<User> sortUserByName(List<User> users);
	
	public List<ReserveCarApplyOrder> getRejects(User proposer);
	public List<ReserveCarApplyOrder> getNeedApprove(User approveUser);
	public List<ReserveCarApplyOrder> getNeedConfigureCar(User configureCarUser);
	public List<ReserveCarApplyOrder> getNeedConfigureDriver(User configureDriverUser);
}
