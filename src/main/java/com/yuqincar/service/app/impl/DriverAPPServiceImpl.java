package com.yuqincar.service.app.impl;

import java.math.BigDecimal;
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
import com.yuqincar.dao.order.OrderDao;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.domain.order.APPRemindMessage;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderSourceEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.app.APPMessageService;
import com.yuqincar.service.app.DriverAPPService;
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
	private SMSService smsService;
	@Autowired
	private APPMessageService appMessageService;
	
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
			params.put("fromAddress", order.getFromAddress().getDescription()+"（"+order.getFromAddress().getDetail()+"）");
			if(order.getChargeMode()==ChargeModeEnum.MILE){
				params.put("planBeginDate", DateUtils.getYMDHMString(order.getPlanBeginDate()));
				params.put("toAddress", order.getToAddress().getDescription()+"（"+order.getToAddress().getDetail()+"）");
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
				map.put("orderId", order.getId());
				appMessageService.sendMessageToCustomerAPP(order.getCustomer(), sb.toString(),map);
			}
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * 司机点击“开始”按钮。
	 * 1. 设置属性 //这在Action中实现
	 * 	actualBeginDate
	 *	actualBeginLocation
	 *	actualBeginMile
	 * 2. 设置status为BEGIN
	 * 3. 需要断言：order.status==SCHEDULED
	 * @param order
	 * @return  0	成功
	 * 			1	失败
	 */
	@Transactional
	public int orderBegin(Order order){
		if(order.getStatus() == OrderStatusEnum.ACCEPTED)
			order.setStatus(OrderStatusEnum.BEGIN);
		else {
			return 1;
		}
		order.setActualBeginDate(new Date());
		
		String sn =  order.getCar().getDevice().getSN();
		
		Location actualBeginLocation = lbsDao.getCurrentLocation(sn);
		locationDao.save(actualBeginLocation);

		order.setActualBeginLocation(actualBeginLocation);
		order.setActualBeginMile(lbsDao.getCurrentMile(sn));
		
		orderDao.update(order);
		return 0;
	}
	
	/**
	 * 司机点击“结束”按钮。
	 * 1. 设置属性
	 * 	actualEndDate
	 *	actualEndLocation
	 *	actualEndMile
	 *	actualMile(如果chargeMode==MILE)
	 *	RctualDay(如果chargeMode==DAY)
	 * 2. 置status为END
	 * 3. 需要断言：order.status==BEGIN
	 * @param order
	 * @return  0	成功
	 * 			1	失败
	 */
	@Transactional
	public int orderEnd(Order order){
		if(order.getStatus() == OrderStatusEnum.BEGIN)
			order.setStatus(OrderStatusEnum.END);
		else {
			return 1;
		}
		
		String sn =  order.getCar().getDevice().getSN();		//设备sn
		Location endLocation = lbsDao.getCurrentLocation(sn);
		locationDao.save(endLocation);
		
		order.setActualEndLocation(endLocation);
		order.setActualEndMile(lbsDao.getCurrentMile(sn));
		//设置结束时间
		Date now = new Date();
		order.setActualEndDate(now);
		
		order.setActualMile(lbsDao.getStepMile(sn, order.getActualBeginDate().getTime()+"", now.getTime()+""));
		int actualDay = DateUtils.elapseDays(order.getActualBeginDate(), order.getActualEndDate(),true,true);
		order.setActualDay(actualDay);
		order.setActualMoney(orderDao.calculateOrderMoney(order.getServiceType(), order.getChargeMode(), order.getActualMile(), order.getActualDay()));
		
		if(order.getChargeMode().equals(ChargeModeEnum.MILE)) {
			if(order.getOrderMile()==0){
				order.setOrderMile(order.getActualMile());
				order.setOrderMoney(order.getActualMoney());
			}				
		} else if(order.getChargeMode().equals(ChargeModeEnum.DAY) 
				|| order.getChargeMode().equals(ChargeModeEnum.PROTOCOL)) {	//设置实际天数
			order.setOrderMile(order.getActualMile());
			if(order.getOrderMoney()==null || 
					order.getOrderMoney().compareTo(new BigDecimal(0))==0)
				order.setOrderMoney(order.getActualMoney());
		}

		orderDao.update(order);
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
		
		List<Order> recordList = pageBean.getRecordList();
		int sumActualMile = 0;
		int sumActualDay = 0;
		for(Order o : recordList) {
			sumActualMile += o.getActualMile();
			sumActualDay += o.getActualDay();
		}
		
		map.put("sumActualMile", sumActualMile);
		map.put("sumActualDay", sumActualDay);
		map.put("pageBean", pageBean);
		return map;
	}
	
	public static void main(String[] args) {
		
		System.out.println(new DriverAPPServiceImpl().queryEndOrder(1, null, null, null));
	}

	public Order getDoneOrderDetailById(Long orderId) {
		return orderDao.getDoneOrderDetailById(orderId);
	}

	public Order getBeginOrder(User user) {
		
		return orderDao.getBeginOrder(user);
	}
	
	
}
