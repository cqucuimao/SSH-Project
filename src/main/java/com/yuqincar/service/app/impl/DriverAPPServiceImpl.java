package com.yuqincar.service.app.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.dao.monitor.APPRemindMessageDao;
import com.yuqincar.dao.monitor.LocationDao;
import com.yuqincar.dao.order.DayOrderDetailDao;
import com.yuqincar.dao.order.OrderDao;
import com.yuqincar.dao.order.OrderOperationRecordDao;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.APPRemindMessage;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderOperationRecord;
import com.yuqincar.domain.order.OrderOperationTypeEnum;
import com.yuqincar.domain.order.OrderSourceEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.app.APPMessageService;
import com.yuqincar.service.app.DriverAPPService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Service
public class DriverAPPServiceImpl implements DriverAPPService{
	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private APPRemindMessageDao appRemindMessageDao;
	@Autowired
	private LBSDao lbsDao;
	@Autowired
	private LocationDao locationDao;
	@Autowired
	private DayOrderDetailDao dayOrderDetailDao;
	@Autowired
	private SMSService smsService;
	@Autowired
	private APPMessageService appMessageService;
	@Autowired
	private OrderOperationRecordDao orderOperationRecordDao;
	@Autowired
	private OrderService orderService;
	
	/**
	 * 获取某个待执行的订单
	 * @param user
	 * @return
	 */
	public Order getUndoOrder(User user, Long orderId) {
		return orderDao.getUndoOrder(user, orderId);
	}
	
	/**
	 * 得到司机还未执行的所有订单。按时间降序排列。所查询到的订单需满足如下要求：
	 * 1. order.car.driver==user
	 * 2. order.status==unAccepted
	 * @param user
	 * @return
	 */
	public List<Order> getAllUndoOrders(User user){
		return orderDao.getAllUndoOrders(user);
	}
	
	/**
	 * 得到司机还未执行的订单数量。
	 * @param user
	 * @return
	 */
	public int getNumberOfUndoOrders(User user) {
		return getAllUndoOrders(user).size();
	}
	
	/**
	 * 得到还没有获取的提醒消息。获取之后，不对sended属性做改变，因为有可能接收不成功。
	 * @param user
	 * @return
	 */
	public List<APPRemindMessage> getAllUnsendedRemindMessage(User user){
		return appRemindMessageDao.getAllUnsendedRemindMessage(user);
	}
	
	/**
	 * 将参数message中的sended设置为true，以表示成功发送到了APP端。
	 * @param message
	 */
	@Transactional
	public void setRemindMessageSended(APPRemindMessage message){
		appRemindMessageDao.setRemindMessageSended(message);
	}

	@Transactional
	public int orderAccept(Order order) {
		if(order.getStatus() == OrderStatusEnum.SCHEDULED) {
			order.setStatus(OrderStatusEnum.ACCEPTED);
			order.setAcceptedTime(new Date());
			orderDao.update(order);

			//给乘客发送短信
			Map<String,String> params=new HashMap<String,String>();
			params.put("driverName", order.getDriver().getName());
			params.put("plateNumber", order.getCar().getPlateNumber());
			params.put("driverPhoneNumber", order.getDriver().getPhoneNumber());
			params.put("fromAddress", order.getFromAddress());
			if(order.getChargeMode()==ChargeModeEnum.MILE){
				params.put("planBeginDate", DateUtils.getYMDHMString(order.getPlanBeginDate()));
				params.put("toAddress", order.getToAddress());
				if(order.getOrderSource()!=OrderSourceEnum.APP)
					smsService.sendTemplateSMS(order.getPhone(),SMSService.SMS_TEMPLATE_MILE_ORDER_ACCEPTED, params);
				if(order.isCallForOther() && order.isCallForOtherSendSMS())
					smsService.sendTemplateSMS(order.getOtherPhoneNumber(),SMSService.SMS_TEMPLATE_MILE_ORDER_ACCEPTED, params);
			}else{
				params.put("planDate", DateUtils.getYMDHMString(order.getPlanBeginDate())+" 至 "+DateUtils.getYMDString(order.getPlanEndDate()));
				if(order.getOrderSource()!=OrderSourceEnum.APP)
					smsService.sendTemplateSMS(order.getPhone(),SMSService.SMS_TEMPLATE_DAY_ORDER_ACCEPTED, params);
				if(order.isCallForOther() && order.isCallForOtherSendSMS())
					smsService.sendTemplateSMS(order.getOtherPhoneNumber(),SMSService.SMS_TEMPLATE_DAY_ORDER_ACCEPTED, params);
			}
			
			//给APP下单用户发送APP提醒消息
			if(order.getOrderSource()==OrderSourceEnum.APP){
				StringBuffer sb=new StringBuffer();
				sb.append("订单已派发。司机：").append(order.getDriver().getName()).append("（").append(order.getDriver().getPhoneNumber())
					.append("，").append(order.getCar().getPlateNumber()).append("）");
				Map<String,Object> map=new HashMap<String,Object>();
				//map.put("orderId", order.getId());
				map.put("orderStatus", "WAIT");
				map.put("id", order.getId());
				appMessageService.sendMessageToCustomerAPP(order.getCustomer(), sb.toString(),map);
			}
			return 1;
		} else {
			return 0;
		}
	}
	
	@Transactional
	public int orderBegin(Order order,User user,Date date){
		if(!canBeginOrder(order))
			return 1;
		
		order.setStatus(OrderStatusEnum.BEGIN);			
		if(user!=null && date!=null)
			order.setActualBeginDate(date);
		else
			order.setActualBeginDate(new Date());
		orderDao.update(order);
		
		if(user!=null && date!=null){
			OrderOperationRecord oor=new OrderOperationRecord();
			oor.setOrder(order);
			oor.setType(OrderOperationTypeEnum.EDIT_DRIVER_ACTION);
			oor.setDate(new Date());
			oor.setUser(user);
			oor.setDescription("增加了司机“开始”操作，指定的操作时间是："+DateUtils.getYMDHMSString(date));
			orderOperationRecordDao.save(oor);
		}
		
		return 0;
	}
	
	@Transactional
	public int orderEnd(Order order,User user, Date date){
		if(!canEndOrder(order))
			return 1;

		order.setStatus(OrderStatusEnum.END);		
		if(user!=null && date!=null)
			order.setActualEndDate(date);
		else
			order.setActualEndDate(new Date());
		orderDao.update(order);
		
		if(user!=null && date!=null){
			OrderOperationRecord oor=new OrderOperationRecord();
			oor.setOrder(order);
			oor.setType(OrderOperationTypeEnum.EDIT_DRIVER_ACTION);
			oor.setDate(new Date());
			oor.setUser(user);
			oor.setDescription("增加了司机“结束”操作，指定的操作时间是："+DateUtils.getYMDHMSString(date));
			orderOperationRecordDao.save(oor);
		}
		
		orderService.orderCheckout(order);
		
		return 0;
	}
	
	/**
	 * 保存用户签名
	 * @param order
	 * @param signature
	 */
	@Transactional
	public void saveSignature(Order order,DiskFile signature) {
		
	}
	
	/**
	 * 查询某个时间段内已经执行完成的订单。
	 * @param pageNum
	 * @param fromDate	可以为空，表示不指定起始时间
	 * @param toDate	可以为空，表示不指定截止时间
	 * @param user
	 * @return	key			value
	 * 			mile		float		查询结果的订单中，如果chargeMode为MILE，此值代表actualMile的和值。
	 * 			day			int			查询结果的订单中，如果chargeMode为DAY，此值代表actualDay的和值。
	 * 			pageBean	PageBean	分页对象。里面的recordCount是总数，可能要用到界面中。
	 */
	public Map<String,Object> queryEndOrder( int pageNum , Date fromDate, Date toDate, User user) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		QueryHelper queryHelper = new QueryHelper("order_","o");
		if(fromDate!=null && toDate!=null)
			queryHelper.addWhereCondition("o.actualEndDate between ? and ?", fromDate, toDate);
		else if(fromDate==null && toDate!=null)
			queryHelper.addWhereCondition("o.actualEndDate < ? ", toDate);
		else if(fromDate!=null && toDate == null)
			queryHelper.addWhereCondition("o.actualEndDate > ? ", fromDate);
		if(user!=null)
			queryHelper.addWhereCondition("o.driver=?", user);
		queryHelper.addWhereCondition("o.status=?", OrderStatusEnum.END);
		queryHelper.addOrderByProperty("planEndDate", false);
	
		PageBean<Order> pageBean = orderDao.getPageBean(pageNum, queryHelper);
		map.put("pageBean", pageBean);
		return map;
	}

	public Order getDoneOrderDetailById(Long orderId) {
		return orderDao.getDoneOrderDetailById(orderId);
	}

	public Order getBeginOrder(User user) {		
		return orderDao.getBeginOrder(user);
	}

	@Transactional
	public int customerGeton(Order order,User user, Date date){
		if(!canCustomerGeton(order))
			return 1;
		
		DayOrderDetail dayOrderDetail=new DayOrderDetail();
		dayOrderDetail.setOrder(order);
		if(user!=null && date!=null)
			dayOrderDetail.setGetonDate(date);
		else
			dayOrderDetail.setGetonDate(new Date());
		dayOrderDetailDao.save(dayOrderDetail);		
		order.setStatus(OrderStatusEnum.GETON);
		orderDao.update(order);
		
		if(user!=null && date!=null){
			OrderOperationRecord oor=new OrderOperationRecord();
			oor.setOrder(order);
			oor.setType(OrderOperationTypeEnum.EDIT_DRIVER_ACTION);
			oor.setDate(new Date());
			oor.setUser(user);
			oor.setDescription("增加了司机“客户上车”操作，指定的操作时间是："+DateUtils.getYMDHMSString(date));
			orderOperationRecordDao.save(oor);
		}
		
		return 0;
	}

	@Transactional
	public int customerGetoff(Order order,User user, Date date){
		if(!canCustomerGetoff(order))
			return 1;
		
		DayOrderDetail dayOrderDetail=dayOrderDetailDao.getUngetoffDayOrderDetail(order);
		if(user!=null && date!=null)
			dayOrderDetail.setGetoffDate(date);
		else
			dayOrderDetail.setGetoffDate(new Date());
		dayOrderDetailDao.update(dayOrderDetail);		
		order.setStatus(OrderStatusEnum.GETOFF);
		orderDao.update(order);
		
		if(user!=null && date!=null){
			OrderOperationRecord oor=new OrderOperationRecord();
			oor.setOrder(order);
			oor.setType(OrderOperationTypeEnum.EDIT_DRIVER_ACTION);
			oor.setDate(new Date());
			oor.setUser(user);
			oor.setDescription("增加了司机“客户下车”操作，指定的操作时间是："+DateUtils.getYMDHMSString(date));
			orderOperationRecordDao.save(oor);
		}
		
		return 0;
	}

	public boolean canBeginOrder(Order order){
		return order.getStatus()==OrderStatusEnum.ACCEPTED;
	}
	
	public boolean canEndOrder(Order order){
		return order.getStatus()==OrderStatusEnum.GETOFF;
	}
	
	public boolean canCustomerGeton(Order order){
		if(order.getChargeMode()==ChargeModeEnum.DAY || order.getChargeMode()==ChargeModeEnum.PROTOCOL)
			//TODO 需要判断订单是否已经没有可用天数。
			return order.getStatus()==OrderStatusEnum.BEGIN || order.getStatus()==OrderStatusEnum.GETOFF;
		else
			return order.getStatus()==OrderStatusEnum.BEGIN;
	}
	
	public boolean canCustomerGetoff(Order order){
		return order.getStatus()==OrderStatusEnum.GETON;
	}
}
