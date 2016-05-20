package com.yuqincar.service.app;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.order.APPRemindMessage;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.privilege.User;

	
public interface DriverAPPService {
	
	public Order getUndoOrder(User user, Long orderId) ;
	/**
	 * 得到司机还未执行的所有订单。按时间降序排列。所查询到的订单需满足如下要求：
	 * 1. order.car.driver==user
	 * 2. order.status==SCHEDULED
	 * @param user
	 * @return
	 */
	public List<Order> getAllUndoOrders(User user);
	
	/**
	 * 得到司机还未执行的订单数量。
	 * @param user
	 * @return
	 */
	public int getNumberOfUndoOrders(User user);
	
	/**
	 * 得到还没有获取的提醒消息。获取之后，不对sended属性做改变，因为有可能接收不成功。
	 * @param user
	 * @return
	 */
	public List<APPRemindMessage> getAllUnsendedRemindMessage(User user);
	
	/**
	 * 将参数message中的sended设置为true，以表示成功发送到了APP端。
	 * @param message
	 */
	public void setRemindMessageSended(APPRemindMessage message);
	
	public int orderBegin(Order order, User user, Date date);
	
	public int orderEnd(Order order, User user, Date date);
	
	/**
	 * 保存用户签名
	 * @param order
	 * @param signature
	 */
	public void saveSignature(Order order,DiskFile signature);
	
	/**
	 * 查询某个时间段内已经执行的订单。
	 * @param pageNum
	 * @param fromDate	可以为空，表示不指定起始时间
	 * @param toDate	可以为空，表示不指定截止时间
	 * @param user
	 * @return	key			value
	 * 			mile		float		查询结果的订单中，如果chargeMode为MILE，此值代表actualMile的和值。
	 * 			day			int			查询结果的订单中，如果chargeMode为DAY，此值代表actualDay的和值。
	 * 			pageBean	PageBean	分页对象。里面的recordCount是总数，可能要用到界面中。
	 */
	public Map<String,Object> queryEndOrder( int pageNum , Date fromDate,Date toDate,User user);
	
	
	public Order getDoneOrderDetailById(Long orderId);
	
	/**
	 * 获取某个用户执行中的订单
	 * 应该只有一条记录返回
	 */
	public Order getBeginOrder(User user);
	
	/**
	 * 接收订单
	 * @param order
	 * @return
	 */
	public int orderAccept(Order order);
	
	public int customerGeton(Order order, User user, Date date);
	
	public int customerGetoff(Order order, User user, Date date);

	public boolean canBeginOrder(Order order);
	
	public boolean canEndOrder(Order order);
	
	public boolean canCustomerGeton(Order order);
	
	public boolean canCustomerGetoff(Order order);
}
