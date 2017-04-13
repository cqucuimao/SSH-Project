/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.yuqincar.service.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuqincar.dao.CustomerOrganization.CustomerOrganizationDao;
import com.yuqincar.dao.customer.CustomerDao;
import com.yuqincar.dao.customer.OtherPassengerDao;
import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.dao.monitor.LocationDao;
import com.yuqincar.dao.order.DayOrderDetailDao;
import com.yuqincar.dao.order.OrderDao;
import com.yuqincar.dao.order.OrderOperationRecordDao;
import com.yuqincar.dao.order.PriceTableDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.GenderEnum;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.DriverActionVO;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderOperationRecord;
import com.yuqincar.domain.order.OrderOperationTypeEnum;
import com.yuqincar.domain.order.OrderSourceEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.order.OtherPassenger;
import com.yuqincar.domain.order.Price;
import com.yuqincar.domain.order.ProtocolOrderPayPeriodEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.app.APPMessageService;
import com.yuqincar.service.app.DriverAPPService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.order.WatchKeeperService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.CommonUtils;
import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.HttpMethod;
import com.yuqincar.utils.QueryHelper;
import com.yuqincar.utils.TextResolve;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private LocationDao locationDao;
	
	@Autowired
	private LBSDao lbsDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private OtherPassengerDao otherPassengerDao;
	
	@Autowired
	private CustomerOrganizationDao customerOrganizationDao;
	
	@Autowired
	private OrderOperationRecordDao orderOperationRecordDao;
	
	@Autowired
	private PriceTableDao priceTableDao;
	
	@Autowired
	private DayOrderDetailDao dayOrderDetailDao;
	
	@Autowired
	private WatchKeeperService watchKeeperService;
	
	@Autowired
	private SMSService smsService;
	
	@Autowired
	private APPMessageService appMessageService;
	
	@Autowired
	private DriverAPPService driverAPPService;	

	public List<CarServiceType> getAllCarServiceType() {
		return orderDao.getAllCarServiceType();
	}
	
	@Transactional
	public void EnQueueAgain(Order order,String organizationName, String customerName){
		order.setScheduling(false);
		order.setScheduler(null);
		order.setQueueTime(new Date());
		List<BaseEntity> cc=dealCCP(organizationName,customerName,order.getPhone());
		order.setCustomerOrganization((CustomerOrganization)cc.get(0));
		order.setCustomer((Customer)cc.get(1));
		orderDao.update(order);
	}

	@Transactional
	public void EnQueue(Order order,int copyNumber) {
		//处理是否新建客户单位和客户		
		List<BaseEntity> cc=dealCCP(order.getCustomerOrganization().getName(),order.getCustomer().getName(),order.getPhone());
		order.setCustomerOrganization((CustomerOrganization)cc.get(0));
		order.setCustomer((Customer)cc.get(1));
				
		orderDao.EnQueue(order);
		
		if(copyNumber>0)
			copyOrderScheduled(order,copyNumber);
		
		if(watchKeeperService.getWatchKeeper().isOnDuty()){
			Map<String,String> param=new HashMap<String,String>();
			param.put("customer", order.getCustomer().getName());
			param.put("customerOrganization",order.getCustomerOrganization().getName());
			param.put("phoneNumber", order.getPhone());
			param.put("chargeMode", order.getChargeMode().getLabel());
			if(order.getChargeMode()==ChargeModeEnum.MILE || order.getChargeMode()==ChargeModeEnum.PLANE){
				param.put("time", DateUtils.getYMDHMString(order.getPlanBeginDate()));
				param.put("address", order.getFromAddress()+" 到 "+order.getToAddress());
			}else{
				param.put("time", DateUtils.getYMDHMString(order.getPlanBeginDate())+" 到 "+DateUtils.getYMDHMString(order.getPlanEndDate()));
				param.put("address", order.getFromAddress());
			}
			
			//给值班员发短信
			smsService.sendTemplateSMS(watchKeeperService.getWatchKeeper().getKeeper().getPhoneNumber(), SMSService.SMS_TEMPLATE_ORDER_ENQUEUE, param);
			
			//给值班员发APP推送消息
			//TODO 目前APP没有投入使用，先注释掉，以后使用。
//			StringBuffer sb=new StringBuffer();
//			sb.append("有新订单入队列。").append(order.getCustomer().getName()).append("，").append(order.getChargeMode().toString())
//				.append("，上车时间：").append(DateUtils.getYMDHMString(order.getPlanBeginDate()));
//			appMessageService.sendMessageToSchedulerAPP(watchKeeperService.getWatchKeeper().getKeeper(), sb.toString(),null);
		}
	}

	public List<List<Order>> getCarTask(Car car, Date fromDate, Date toDate) {
		return orderDao.getCarTask(car, fromDate, toDate);
	}
	
	public List<List<Order>> getDriverTask(User driver, Date fromDate, Date toDate) {
		return orderDao.getDriverTask(driver, fromDate, toDate);
	}

	public boolean canScheduleOrder(Order order) {
		return order.getStatus()==OrderStatusEnum.INQUEUE;
	}
	
	private void copyOrderScheduled(Order order, int n) {
		for (int i = 0; i < n; i++) {
			Order o = new Order();
			o.setCustomerOrganization(order.getCustomerOrganization());
			o.setCustomer(order.getCustomer());
			o.setChargeMode(order.getChargeMode());
			o.setPlanBeginDate(order.getPlanBeginDate());
			o.setPlanEndDate(order.getPlanEndDate());
			o.setServiceType(order.getServiceType());
			o.setPhone(order.getPhone());
			o.setOrderMoney(order.getOrderMoney());
			o.setStatus(order.getStatus());
			o.setMemo(order.getMemo());
			o.setCallForOther(order.isCallForOther());
			o.setOtherPassengerName(order.getOtherPassengerName());
			o.setOtherPhoneNumber(order.getOtherPhoneNumber());
			o.setCallForOtherSendSMS(order.isCallForOtherSendSMS());
			o.setOrderSource(order.getOrderSource());
			o.setFromAddress(order.getFromAddress());
			o.setToAddress(order.getToAddress());
			o.setCustomerMemo(order.getCustomerMemo());
			o.setDestination(order.getDestination());
			o.setSaler(order.getSaler());
			o.setOrderMoney(order.getOrderMoney());
			o.setActualMoney(order.getActualMoney());
			EnQueue(o,0);
		}
	}
	
	private void saveOtherPassenger(Order order){
		if(!order.isCallForOther())
			return;
		boolean exist=false;
		if(order.getCustomer().getOtherPassengers()!=null){
			for(OtherPassenger op:order.getCustomer().getOtherPassengers())
				if(op.getPhoneNumber().equals(order.getOtherPhoneNumber())){
					exist=true;
					//如果姓名不相同，就修改姓名。
					if(!op.getName().equals(order.getOtherPassengerName())){
						op.setName(order.getOtherPassengerName());
						otherPassengerDao.update(op);
					}
					//如果曾经被删除，就取消删除
					if(op.isDeleted()){
						op.setDeleted(false);
						otherPassengerDao.update(op);
					}
				}
		}
		if(!exist){
			OtherPassenger otherPassenger=new OtherPassenger();
			otherPassenger.setCustomer(order.getCustomer());
			otherPassenger.setName(order.getOtherPassengerName());
			otherPassenger.setPhoneNumber(order.getOtherPhoneNumber());
			otherPassenger.setDeleted(false);
			otherPassengerDao.save(otherPassenger);
		}
	}
	
	private void saveCustomerAddress(Order order){
		boolean exist=false;
		List<String> addresses=null;
		if(order.getCustomer().getAddresses()!=null && order.getCustomer().getAddresses().size()>0){
			addresses=order.getCustomer().getAddresses();
			
			for(String address:order.getCustomer().getAddresses()){
				if(address.equals(order.getFromAddress())){
					exist=true;
					break;
				}
			}
			if(!exist)
				addresses.add(order.getFromAddress());
			
			if(order.getToAddress()!=null && order.getToAddress().length()>0){
				for(String address:order.getCustomer().getAddresses()){
					if(address.equals(order.getToAddress())){
						exist=true;
						break;
					}
				}
				if(!exist)
					addresses.add(order.getToAddress());
			}
		}else{
			addresses=new ArrayList<String>();
			addresses.add(order.getFromAddress());
			if(order.getToAddress()!=null && order.getToAddress().length()>0)
				addresses.add(order.getToAddress());
		}
		
		for(int i=addresses.size()-1;i>=0;i--)
			if(addresses.get(i).equals("电话联系"))
				addresses.remove(i);
		order.getCustomer().setAddresses(addresses);
		customerDao.save(order.getCustomer());
	}
	
	@Transactional
	public void sendOrderInfoSMSToDriver(Order order){
		if(order.isSmsForDriver()){
			Map<String,String> param=new HashMap<String,String>();
			param.put("customerOrganization", order.getCustomerOrganization().getName());
			param.put("customerName", order.getCustomer().getName());
			param.put("phoneNumber",order.getPhone());
			if(order.getChargeMode()==ChargeModeEnum.DAY)
				param.put("time", DateUtils.getYMDString(order.getPlanBeginDate())+" 到 "+DateUtils.getYMDString(order.getPlanEndDate()));
			else
				param.put("time", DateUtils.getYMDString(order.getPlanBeginDate()));
			param.put("destination", (order.getDestination()==null || StringUtils.isEmpty(order.getDestination())) ? "无" : order.getDestination());
			param.put("customerMemo",(order.getCustomerMemo()==null || StringUtils.isEmpty(order.getCustomerMemo())) ? "无" : order.getCustomerMemo());
			
			if(!order.isCallForOther()){
				smsService.sendTemplateSMS(order.getDriver().getPhoneNumber(), SMSService.SMS_TEMPLATE_NEW_ORDER, param);
			}else{
				param.put("otherPassengerName", order.getOtherPassengerName());
				param.put("otherPhoneNumber", order.getOtherPhoneNumber());
				smsService.sendTemplateSMS(order.getDriver().getPhoneNumber(), SMSService.SMS_TEMPLATE_NEW_ORDER_INCLUDE_OTHER_PASSENGER, param);
			}
		}
	}
		
	@Transactional
	public void sendOrderInfoSMSToCustomer(Order order){
		Map<String,String> params=new HashMap<String,String>();
		params.put("driverName", order.getDriver().getName());
		params.put("plateNumber", order.getCar().getPlateNumber());
		params.put("driverPhoneNumber", order.getDriver().getPhoneNumber());
		params.put("customerSurname", CommonUtils.getSurname(order.getCustomer().getName()));
		params.put("schedulerName", order.getScheduler().getName());
		//params.put("fromAddress", order.getFromAddress());
		if(order.getChargeMode()==ChargeModeEnum.MILE || order.getChargeMode()==ChargeModeEnum.PLANE){
			params.put("planBeginDate", DateUtils.getYMDHMString(order.getPlanBeginDate()));
			//params.put("toAddress", order.getToAddress());
			if(order.getOrderSource()!=OrderSourceEnum.APP && order.isSmsForCustomer())
				smsService.sendTemplateSMS(order.getPhone(),SMSService.SMS_TEMPLATE_MILE_ORDER_ACCEPTED, params);
			if(order.isCallForOther() && order.isCallForOtherSendSMS())
				smsService.sendTemplateSMS(order.getOtherPhoneNumber(),SMSService.SMS_TEMPLATE_MILE_ORDER_ACCEPTED, params);
		}else{
			params.put("planBeginDate", DateUtils.getYMDString(order.getPlanBeginDate()));
			if(order.getOrderSource()!=OrderSourceEnum.APP && order.isSmsForCustomer())
				smsService.sendTemplateSMS(order.getPhone(),SMSService.SMS_TEMPLATE_DAY_ORDER_ACCEPTED, params);
			if(order.isCallForOther() && order.isCallForOtherSendSMS())
				smsService.sendTemplateSMS(order.getOtherPhoneNumber(),SMSService.SMS_TEMPLATE_DAY_ORDER_ACCEPTED, params);
		}
	}
		
	@Transactional
	public String scheduleOrder(String scheduleMode,Order order, String organizationName, String customerName, 
			Car car, User driver, int copyNumber,Order toUpdateOrder,User user) {
		//处理是否新建客户单位和客户
		List<BaseEntity> cc=dealCCP(organizationName,customerName,order.getPhone());
		order.setCustomerOrganization((CustomerOrganization)cc.get(0));
		order.setCustomer((Customer)cc.get(1));
		String result= orderDao.scheduleOrder(scheduleMode, order, car, driver, user);
		if("OK".equals(result)){
			//复制订单
			if(copyNumber>0)
				copyOrderScheduled(order,copyNumber);

			saveCustomerAddress(order);

			if(order.isCallForOther())
				saveOtherPassenger(order);
			if(scheduleMode==OrderService.SCHEDULE_FROM_NEW || scheduleMode==OrderService.SCHEDULE_FROM_QUEUE){
				if(order.getDriver()!=null){  //协议用车有可能不指定司机。当然就不发送APP消息和短信。
					String timeString=null;
					if(order.getChargeMode()==ChargeModeEnum.DAY)
						timeString=DateUtils.getYMDString(order.getPlanBeginDate())+" 到 "+DateUtils.getYMDString(order.getPlanEndDate());
					else
						timeString=DateUtils.getYMDHMString(order.getPlanBeginDate());
					appMessageService.sendMessageToDriverAPP(order.getDriver(), "你有新的订单。用车时间："+timeString+ "；上车地点："+order.getFromAddress(),null);

					sendOrderInfoSMSToDriver(order);
				}
				
				//TODO 临时措施 目前设备有问题，暂时不需要司机做动作，所以自动操作司机接受。
				driverAPPService.orderAccept(order);
				order.setActualBeginDate(DateUtils.getMinDate(order.getPlanBeginDate()));
				if(order.getChargeMode()!=ChargeModeEnum.PROTOCOL){
					int days=1;
					if(order.getChargeMode()==ChargeModeEnum.DAY)
						days=DateUtils.elapseDays(order.getPlanBeginDate(), order.getPlanEndDate(),true,true);
					for(int n=0;n<days;n++){
						Date date=DateUtils.getMinDate(DateUtils.getOffsetDate(order.getPlanBeginDate(), n));
						DayOrderDetail dod=new DayOrderDetail();
						dod.setOrder(order);
						dod.setGetonDate(date);
						dod.setGetoffDate(date);
						dayOrderDetailDao.save(dod);
					}
				}
				if(order.getChargeMode()==ChargeModeEnum.DAY || order.getChargeMode()==ChargeModeEnum.PROTOCOL)
					order.setActualEndDate(DateUtils.getMinDate(order.getPlanEndDate()));
				else
					order.setActualEndDate(DateUtils.getMinDate(order.getPlanBeginDate()));
				orderDao.update(order);
				//////////////////////////////////
			}else{
				checkUpdateData(order,toUpdateOrder,user);
				if(toUpdateOrder.getChargeMode()==ChargeModeEnum.PROTOCOL && order.getChargeMode()!=ChargeModeEnum.PROTOCOL){
					order.setPayPeriod(null);
					order.setFirstPayDate(null);
					order.setMoneyForPeriodPay(null);
					order.setLastPayDate(null);
					order.setNextPayDate(null);
					orderDao.update(order);
				}
			}
			//针对协议订单，设置定期收款相关属性
			if(order.getChargeMode()==ChargeModeEnum.PROTOCOL){		//无论是新建还是修改的order，都用相同的方法设置下次收款时间。
				if(order.getPayPeriod()==ProtocolOrderPayPeriodEnum.ONCE)
					order.setNextPayDate(order.getFirstPayDate());
				else{
					Date now=DateUtils.getMinDate(new Date());
					Date nextPayDate=order.getFirstPayDate();
					while(nextPayDate.before(now)){
						int period=0;
						switch(order.getPayPeriod()){
						case MONTH:
							period=DateUtils.PERIOD_MONTH;
							break;
						case QUARTER:
							period=DateUtils.PERIOD_QUARTER;
							break;
						case YEAR:
							period=DateUtils.PERIOD_YEAR;
							break;
						}
						nextPayDate=DateUtils.getPeriodDate(nextPayDate, period);
					}
					order.setNextPayDate(nextPayDate);
				}
				orderDao.update(order);
			}
		}
		return result;
	}
	
	private void checkUpdateData(Order order,Order toUpdateOrder,User user){
		StringBuffer sb=new StringBuffer();
		int n=0;
		if(!order.getCustomerOrganization().equals(toUpdateOrder.getCustomerOrganization()))
			sb.append("(").append(++n).append(")").append("客户单位由：")
				.append(toUpdateOrder.getCustomerOrganization().getName())
				.append(" 改为 ").append(order.getCustomerOrganization().getName()).append("；");
		
		if(!order.getCustomer().getName().equals(toUpdateOrder.getCustomer().getName()))
			sb.append("(").append(++n).append(")").append("联系人由：")
				.append(toUpdateOrder.getCustomer().getName())
				.append(" 改为 ").append(order.getCustomer().getName()).append("；");
		
		if(!order.getPhone().equals(toUpdateOrder.getPhone()))
			sb.append("(").append(++n).append(")").append("联系电话由：")
				.append(toUpdateOrder.getPhone())
				.append(" 改为 ").append(order.getPhone()).append("；");
		
		if(toUpdateOrder.isCallForOther()){
			if(!order.isCallForOther())
				sb.append("(").append(++n).append(")").append("取消为他人叫车；");
			else{
				if(!order.getOtherPassengerName().equals(toUpdateOrder.getOtherPassengerName()))
					sb.append("(").append(++n).append(")").append("乘车人姓名由：").append(toUpdateOrder.getOtherPassengerName())
						.append(" 改为 ").append(order.getOtherPassengerName()).append("；");
				else if(!order.getOtherPhoneNumber().equals(toUpdateOrder.getOtherPhoneNumber()))
					sb.append("(").append(++n).append(")").append("乘车人手机号码由：").append(toUpdateOrder.getOtherPhoneNumber())
						.append(" 改为 ").append(order.getOtherPhoneNumber()).append("；");
				else if(order.isCallForOtherSendSMS()!=toUpdateOrder.isCallForOtherSendSMS())
					sb.append("(").append(++n).append(")").append("是否给乘车人发短信由：").append(toUpdateOrder.isCallForOtherSendSMS() ? "是" : "否")
						.append(" 改为 ").append(order.isCallForOtherSendSMS() ? "是" : "否").append("；");
			}
		}else{
			if(order.isCallForOther()){
				sb.append("(").append(++n).append(")").append("设置了为他人叫车：").append(order.getOtherPassengerName()).append(",").append(order.getOtherPhoneNumber())
					.append("(").append(order.isCallForOtherSendSMS() ? "发送短信" : "不发送短信").append(")；");
			}
		}
		
		if(order.getChargeMode()!=toUpdateOrder.getChargeMode())
			sb.append("(").append(++n).append(")").append("计价方式由：")
				.append(toUpdateOrder.getChargeMode().getLabel())
				.append(" 改为 ").append(order.getChargeMode().getLabel()).append("；");
		
		if(!order.getPlanBeginDate().equals(toUpdateOrder.getPlanBeginDate()))
			sb.append("(").append(++n).append(")").append("计划开始时间由：")
				.append(DateUtils.getYMDHMString(toUpdateOrder.getPlanBeginDate()))
				.append(" 改为 ").append(DateUtils.getYMDHMString(order.getPlanBeginDate())).append("；");
		
		if(order.getPlanEndDate()==null){
			if(toUpdateOrder.getPlanEndDate()!=null)
				sb.append("(").append(++n).append(")").append("删除了计划结束时间").append("；");
		}else
			if(toUpdateOrder.getPlanEndDate()!=null && !order.getPlanEndDate().equals(toUpdateOrder.getPlanEndDate()))
				sb.append("(").append(++n).append(")").append("计划结束时间由：")
					.append(DateUtils.getYMDHMString(toUpdateOrder.getPlanEndDate()))
					.append(" 改为 ").append(DateUtils.getYMDHMString(order.getPlanEndDate())).append("；");
			else if(toUpdateOrder.getPlanEndDate()==null)
				sb.append("(").append(++n).append(")").append("新增计划结束时间：")
					.append(DateUtils.getYMDHMString(order.getPlanEndDate())).append("；");
				
		if(!order.getServiceType().equals(toUpdateOrder.getServiceType()))
			sb.append("(").append(++n).append(")").append("车型由：")
				.append(toUpdateOrder.getServiceType().getTitle())
				.append(" 改为 ").append(order.getServiceType().getTitle()).append("；");
		
		if(!order.getFromAddress().equals(toUpdateOrder.getFromAddress()))
			sb.append("(").append(++n).append(")").append("上车地点由：")
				.append(toUpdateOrder.getFromAddress())
				.append(" 改为 ").append(order.getFromAddress()).append("；");
		
		if(order.getToAddress()==null){
			if(toUpdateOrder.getToAddress()!=null)
				sb.append("(").append(++n).append(")").append("删除了下车地点").append("；");
		}else{
			if(toUpdateOrder.getToAddress()!=null && !order.getToAddress().equals(toUpdateOrder.getToAddress()))
				sb.append("(").append(++n).append(")").append("下车地点由：")
					.append(toUpdateOrder.getToAddress()).append(" 改为 ")
					.append(order.getToAddress()).append("；");
			else if(toUpdateOrder.getToAddress()==null)
				sb.append("(").append(++n).append(")").append("新增下车地点：")
					.append(order.getToAddress()).append("；");
		}
		
		if(order.getCustomerMemo()==null || StringUtils.isEmpty(order.getCustomerMemo())){
			if(toUpdateOrder.getCustomerMemo()!=null && !StringUtils.isEmpty(toUpdateOrder.getCustomerMemo()))
				sb.append("(").append(++n).append(")").append("删除了客户要求").append("；");
		}else{
			if(toUpdateOrder.getCustomerMemo()!=null && !order.getCustomerMemo().equals(toUpdateOrder.getCustomerMemo()))
				sb.append("(").append(++n).append(")").append("客户要求由：")
					.append(toUpdateOrder.getCustomerMemo()).append(" 改为 ")
					.append(order.getCustomerMemo()).append("；");
			else if(toUpdateOrder.getCustomerMemo()==null)
				sb.append("(").append(++n).append(")").append("新增客户要求：")
					.append(order.getCustomerMemo()).append("；");
		}
		
		if(order.getDestination()==null || StringUtils.isEmpty(order.getDestination())){
			if(toUpdateOrder.getDestination()!=null && !StringUtils.isEmpty(toUpdateOrder.getDestination()))
				sb.append("(").append(++n).append(")").append("删除了目的地").append("；");
		}else{
			if(toUpdateOrder.getDestination()!=null && !order.getDestination().equals(toUpdateOrder.getDestination()))
				sb.append("(").append(++n).append(")").append("目的地由：")
					.append(toUpdateOrder.getDestination()).append(" 改为 ")
					.append(order.getDestination()).append("；");
			else if(toUpdateOrder.getDestination()==null)
				sb.append("(").append(++n).append(")").append("新增目的地：")
					.append(order.getDestination()).append("；");
		}
		
		boolean carChanged=false;
		boolean driverChanged=false;
		if((order.getCar()!=null && !order.getCar().equals(toUpdateOrder.getCar())) ||
				(order.getCar()==null && toUpdateOrder.getCar()!=null)){
			sb.append("(").append(++n).append(")").append("车辆由：")
				.append(toUpdateOrder.getCar()!=null ? toUpdateOrder.getCar().getPlateNumber() : "<空>")
				.append(" 改为 ").append(order.getCar()!=null ? order.getCar().getPlateNumber() : "<空>").append("；");
			carChanged=true;
		}
				
		if((order.getDriver()!=null && !order.getDriver().equals(toUpdateOrder.getDriver())) || 
				(order.getDriver()==null && toUpdateOrder.getDriver()!=null)){
			sb.append("(").append(++n).append(")").append("司机由：")
				.append(toUpdateOrder.getDriver()!=null ? toUpdateOrder.getDriver().getName() : "<空>")
				.append(" 改为 ").append(order.getDriver()!=null ? order.getDriver().getName() : "<空>").append("；");
			driverChanged=true;
		}
		if(carChanged || driverChanged){
			Map<String,String> param=new HashMap<String,String>();
			param.put("plateNumber", order.getCar().getPlateNumber());
			param.put("driverName", order.getDriver().getName());
			param.put("phoneNumber", order.getDriver().getPhoneNumber());
			smsService.sendTemplateSMS(order.getPhone(), SMSService.SMS_TEMPLATE_RESCHEDULE, param);
			if(order.isCallForOther() && order.isCallForOtherSendSMS())
				smsService.sendTemplateSMS(order.getOtherPhoneNumber(), SMSService.SMS_TEMPLATE_RESCHEDULE, param);
				
			sendOrderInfoSMSToDriver(order);
		}
		
		if((order.getPayPeriod()!=null && order.getPayPeriod()!=toUpdateOrder.getPayPeriod()) || 
				(order.getPayPeriod()==null && toUpdateOrder.getPayPeriod()!=null))
			sb.append("(").append(++n).append(")").append("协议订单付款周期由：")
				.append(toUpdateOrder.getPayPeriod()!=null ? toUpdateOrder.getPayPeriod().getLabel() : "<空>")
				.append(" 改为 ").append(order.getPayPeriod()!=null ? order.getPayPeriod().getLabel() : "<空>").append("；");
		
		if((order.getFirstPayDate()!=null && !order.getFirstPayDate().equals(toUpdateOrder.getFirstPayDate())) || 
				(order.getFirstPayDate()==null && toUpdateOrder.getFirstPayDate()!=null))
			sb.append("(").append(++n).append(")").append("协议订单首次付款日期由：")
				.append(toUpdateOrder.getFirstPayDate()!=null ? DateUtils.getYMDString(toUpdateOrder.getFirstPayDate()) : "<空>")
				.append(" 改为 ").append(order.getFirstPayDate()!=null ? DateUtils.getYMDString(order.getFirstPayDate()) : "<空>").append("；");
		
		if((order.getMoneyForPeriodPay()!=null && !order.getMoneyForPeriodPay().equals(toUpdateOrder.getMoneyForPeriodPay())) || 
				(order.getMoneyForPeriodPay()==null && toUpdateOrder.getMoneyForPeriodPay()!=null))
			sb.append("(").append(++n).append(")").append("协议订单每次付款金额由：")
				.append(toUpdateOrder.getMoneyForPeriodPay()!=null ? toUpdateOrder.getMoneyForPeriodPay().toString() : "<空>")
				.append(" 改为 ").append(order.getMoneyForPeriodPay()!=null ? order.getMoneyForPeriodPay().toString() : "<空>").append("；");
		
		
		if(n>0){
			OrderOperationRecord orderOperation = new OrderOperationRecord();
			orderOperation.setOrder(order);
			orderOperation.setDescription(sb.toString());
			orderOperation.setDate(new Date());
			orderOperation.setType(OrderOperationTypeEnum.MODIFY);
			orderOperation.setUser(user);
			orderOperationRecordDao.save(orderOperation);
			
			if(toUpdateOrder.getDriver()!=null)
				appMessageService.sendMessageToDriverAPP(toUpdateOrder.getDriver(), "有订单（"+order.getSn()+"）发生了修改："+sb.toString(),null);
			if(order.getDriver()!=null && !order.getDriver().equals(toUpdateOrder.getDriver()))
				appMessageService.sendMessageToDriverAPP(order.getDriver(), "你有新的订单。上车时间："+DateUtils.getYMDHMString(order.getPlanBeginDate())
						+ "；上车地点："+order.getFromAddress(),null);
		}
	}

	public List<Order> getOrderQueue() {
		return orderDao.getOrderQueue();
	}

	public boolean canCancelOrder(Order order) {
		return order.getStatus()==OrderStatusEnum.INQUEUE || 
				order.getStatus()==OrderStatusEnum.SCHEDULED || 
				order.getStatus()==OrderStatusEnum.ACCEPTED ||
				order.getStatus()==OrderStatusEnum.BEGIN ||
				order.getStatus()==OrderStatusEnum.GETON ||
				order.getStatus()==OrderStatusEnum.GETOFF;
	}

	@Transactional
	public int cancelOrder(Order order, User user, String description){
		if (canCancelOrder(order)) {
			order.setStatus(OrderStatusEnum.CANCELLED);
			order.setSn(order.getSn()+"X"+order.getId());
			orderDao.update(order);
			
			OrderOperationRecord oor=new OrderOperationRecord();
			oor.setOrder(order);
			oor.setType(OrderOperationTypeEnum.CANCEL);
			oor.setDate(new Date());
			oor.setUser(user);
			oor.setDescription(description);
			orderOperationRecordDao.save(oor);

			String message="订单（"+order.getSn()+"）已经被取消。";
			Map<String,String> params=new HashMap<String,String>();
			params.put("sn", order.getSn());
			params.put("reason", description);
			if(order.getDriver()!=null)
				//给司机发消息
				appMessageService.sendMessageToDriverAPP(order.getDriver(), message,null);
			
			if(order.getOrderSource()!=OrderSourceEnum.APP)
				//给非APP用户发短信
				smsService.sendTemplateSMS(order.getPhone(), SMSService.SMS_TEMPLATE_ORDER_CANCELLED, params);
			else{
				//给APP用户发消息
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("orderId", order.getId());
				appMessageService.sendMessageToCustomerAPP(order.getCustomer(), message,map);
			}
			
			//给实际乘车人发短信
			if(order.isCallForOther() && order.isCallForOtherSendSMS())
				smsService.sendTemplateSMS(order.getOtherPhoneNumber(), SMSService.SMS_TEMPLATE_ORDER_CANCELLED, params);
			
			
			return 0;
		}
		return 1;
	}
	
	public boolean canUpdate(Order order) {
		return order.getStatus()==OrderStatusEnum.SCHEDULED 
				|| order.getStatus()==OrderStatusEnum.ACCEPTED;
	}

	public PageBean getPageBean(int pageNum, QueryHelper helper) {
		return orderDao.getPageBean(pageNum, helper);
	}

	public boolean canOrderEndPostpone(Order order) {
		return (order.getChargeMode()==ChargeModeEnum.DAY || order.getChargeMode()==ChargeModeEnum.PROTOCOL)
				&& (order.getStatus()==OrderStatusEnum.BEGIN || order.getStatus()==OrderStatusEnum.GETON
					|| order.getStatus()==OrderStatusEnum.GETOFF);
	}
	
	public boolean canAddProtocolOrderPayOrder(Order order) {
		if(order.getChargeMode()!=ChargeModeEnum.PROTOCOL)
			return false;
		if(order.getStatus()==OrderStatusEnum.INQUEUE || order.getStatus()==OrderStatusEnum.END || 
				order.getStatus()==OrderStatusEnum.PAID || order.getStatus()==OrderStatusEnum.CANCELLED)
			return false;
		return true;
	}

	@Transactional
	public String orderEndPostpone(Order order, Date endDate, String description,
			User user){
		StringBuffer result=new StringBuffer();
		if(!canOrderEndPostpone(order))
			result.append("订单的当前状态不支持延后操作；");
		Date temp=order.getPlanEndDate();	//如果在不成功的情况，不使用temp来补救，不知为什么：不调用update的情况下，planEndDate还是会反应在数据库中。
		order.setPlanEndDate(endDate);
		String str=isCarAndDriverAvailable(order,order.getCar(),order.getDriver());
		if(!str.equals("OK")){
			order.setPlanEndDate(temp);
			result.append(str);
		}
				
		orderDao.update(order);
		OrderOperationRecord oor=new OrderOperationRecord();
		oor.setOrder(order);
		oor.setType(OrderOperationTypeEnum.END_POSTPONE);
		oor.setDate(new Date());
		oor.setUser(user);
		oor.setDescription("将计划结束日期由 "+DateUtils.getYMDString(temp)+" 延后到了 "+
				DateUtils.getYMDString(order.getPlanEndDate())+" 原因："+description);
		orderOperationRecordDao.save(oor);
		
		String message="订单（"+order.getSn()+"）被延后。从"+DateUtils.getYMDString(temp)+" 延后到了 "+
				DateUtils.getYMDString(order.getPlanEndDate());
		Map<String,String> params=new HashMap<String,String>();
		params.put("sn", order.getSn());
		params.put("oriDate", DateUtils.getYMDString(temp));
		params.put("newDate", DateUtils.getYMDString(order.getPlanEndDate()));
		//给司机发消息
		appMessageService.sendMessageToDriverAPP(order.getDriver(), message,null);
		
		if(order.getOrderSource()!=OrderSourceEnum.APP)
			//给非APP用户发短信
			smsService.sendTemplateSMS(order.getPhone(), SMSService.SMS_TEMPLATE_ORDER_POSTPONE, params);
		else{
			//给APP用户发消息
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("orderId", order.getId());
			appMessageService.sendMessageToCustomerAPP(order.getCustomer(), message,map);
		}
		
		//给实际乘车人发短信
		if(order.isCallForOther() && order.isCallForOtherSendSMS())
			smsService.sendTemplateSMS(order.getOtherPhoneNumber(), SMSService.SMS_TEMPLATE_ORDER_POSTPONE, params);
		
		if(result.length()>0)
			return result.toString();
		else
			return "OK";
	}

	public boolean canOrderReschedule(Order order) {
		return order.getStatus()==OrderStatusEnum.BEGIN 
				|| order.getStatus()==OrderStatusEnum.GETON 
				|| order.getStatus()==OrderStatusEnum.GETOFF;
	}

	@Transactional
	public String orderReschedule(Order order, Car car, User driver, User user, String description){
		StringBuffer result=new StringBuffer();
		if(!canOrderReschedule(order))
			result.append("订单的当前状态不支持重新调度操作；");
		Date temp=order.getPlanBeginDate();
		order.setPlanBeginDate(new Date());
		String str=isCarAndDriverAvailable(order,car,driver);
		if(!str.equals("OK")){
			order.setPlanBeginDate(temp);
			result.append(str);
		}
		order.setPlanBeginDate(temp);
		Car tempCar=order.getCar();
		User tempDriver=order.getDriver();
		order.setCar(car);
		order.setDriver(driver);
		orderDao.update(order);
		
		OrderOperationRecord oor=new OrderOperationRecord();
		oor.setOrder(order);
		oor.setType(OrderOperationTypeEnum.RESCHEDULE);
		oor.setDate(new Date());
		oor.setUser(user);
		
		StringBuffer sb=new StringBuffer();
		if(!tempCar.equals(order.getCar()))
			sb.append("将车辆由").append(tempCar.getPlateNumber()).append("改为").append(order.getCar().getPlateNumber()).append(";");
		if(!tempDriver.equals(order.getDriver()))
			sb.append("将司机由").append(tempDriver.getName()).append("改为").append(order.getDriver().getName()).append(";");
		if(sb.length()>0){
			Map<String,String> param=new HashMap<String,String>();
			param.put("plateNumber", order.getCar().getPlateNumber());
			param.put("driverName", order.getDriver().getName());
			param.put("phoneNumber", order.getDriver().getPhoneNumber());
			smsService.sendTemplateSMS(order.getPhone(), SMSService.SMS_TEMPLATE_RESCHEDULE, param);
		}
		oor.setDescription(sb.toString()+"原因："+description);
		orderOperationRecordDao.save(oor);
		
		String message="订单（"+order.getSn()+"）"+sb.toString();
		if(!tempDriver.equals(order.getDriver())){
			appMessageService.sendMessageToDriverAPP(tempDriver, message, null);
			appMessageService.sendMessageToDriverAPP(order.getDriver(), message, null);
		}
		if(result.length()>0)
			return result.toString();
		else
			return "OK";
	}

	public Order getOrderBySN(String sn) {
		return orderDao.getOrderBySN(sn);
	}

	public PageBean<Order> queryOrder(int pageNum, QueryHelper helper) {
		return orderDao.getPageBean(pageNum, helper);
	}
	
	public List<Order> queryAllOrder(QueryHelper helper){
		return orderDao.getAllQuerry(helper);
	}

	public Order getOrderById(long id) {
		return orderDao.getOrderById(id);
	}

	public PageBean getRecommandedCar(CarServiceType serviceType,ChargeModeEnum chargeMode,
			Date planBeginDate, Date planEndDate, int pageNum) {
		return orderDao.getRecommandedCar(serviceType, chargeMode,planBeginDate,
				planEndDate, pageNum);
	}

	@Transactional
	public void save(Order order) {
		orderDao.save(order);
	}

	public List<Car> getAllCar() {
		return orderDao.getAllCar();
	}

	@Transactional
	public void update(Order order) {
		orderDao.update(order);
	}

	public Order getCurrentOrderByCarId(Long id) {
		return orderDao.getCurrentOrderByCarId(id);
	}

	/**
	 * 根据单位名称，开始时间，结束时间查询相应的未收款订单
	 * 未收款订单的条件是，当前订单状态OrderStatusEnum为END,所属orderStatement为null
	 * 
	 * @param orgName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Order> getUnpaidOrderByOrgNameAndTime(String orgName,
			Date beginDate, Date endDate) {
		return orderDao.getUnpaidOrderByOrgNameAndTime(orgName, beginDate,
				endDate);
	}

	/**
	 * 获取执行订单车辆的轨迹，最多9个地点段
	 * @param order
	 * @return
	 */
	public String getOrderTrackAbstract(String sn, Date beginDate, Date endDate){    	
    	//轨迹提取的长度，即轨迹中序列的最大点数
    	int TRACK_LENGTH=9;
    	//百度地图服务的ak账号
    	String baiduMapAk="wzq3sn49ZUQuOFRvdoS4HaQnpZLBFBMd";
		//获取开始时间和结束时间的时间戳
		String begin=new Long(beginDate.getTime()).toString();
		String end=new Long(endDate.getTime()).toString();
		//拼接获取轨迹序列的url请求
		StringBuffer rawTrackUrl=new StringBuffer();
		rawTrackUrl.append("http://api.capcare.com.cn:1045/api/get.track.do?device_sn=");
		rawTrackUrl.append(sn);
		rawTrackUrl.append("&begin=");
		rawTrackUrl.append(begin);
		rawTrackUrl.append("&end=");
		rawTrackUrl.append(end);
		rawTrackUrl.append("&page_number=-1&page_size=-1&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD");
		//得到的json格式字符串
		String rawTrackJson = HttpMethod.get(rawTrackUrl.toString());
		
		//存储轨迹序列的所有位置信息   经度,纬度
		List<String> allLocations=new ArrayList<String>();
		//存放选中的时间序列和相应的location序列，大于trackLenth个则均匀选取，否则直接全选
		List<String> selectedLocations=new ArrayList<String>();
		
        //解析轨迹的json数据
		JSONObject trackJsonObj=JSON.parseObject(rawTrackJson);
		String ret=trackJsonObj.getString("ret");
		//如果返回结果正确，防止服务器端发生错误
		if("1".equals(ret)){
			JSONArray track=trackJsonObj.getJSONArray("track");
			//由于轨迹序列是倒序的 所以进行倒序处理
			for(int i=track.size()-1;i>0;i--){
				JSONObject obj=(JSONObject) track.get(i);
				String lng=obj.getString("lng");
				String lat=obj.getString("lat");
				String location=lng+","+lat;
				allLocations.add(location);
			}
			//获取轨迹的点数
			int num=allLocations.size();
			//存放均匀分布的trackLenth个点，并保证起点和终点对应是一一对应的
			int[] indexArray=new int[TRACK_LENGTH];
			if(num>TRACK_LENGTH){
				//计算均匀分布的点的索引
				float rate=num/TRACK_LENGTH;
				for(int i=1;i<=TRACK_LENGTH;i++){
					if(i==1){
						indexArray[i-1]=0;
					}else if(i==TRACK_LENGTH){
						indexArray[TRACK_LENGTH-1]=num-1;
					}else{
						indexArray[i-1]=Math.round(rate*i)-1;   //这里没有直接取到最后一个值，不确定原因，先直接手动设置
					}
				}
				for(int i=0;i<TRACK_LENGTH;i++){
					selectedLocations.add(allLocations.get(indexArray[i]));
				}
			}else if(num>=1&&num<=TRACK_LENGTH){
				for(int i=0;i<num;i++){
					selectedLocations.add(allLocations.get(i));
				}
			}else{
				return null;
			}
		}else{
			return null;
		}
		//将gps坐标解析成百度坐标时的坐标序列格式
		StringBuffer locationsBuf=new StringBuffer();
		for(int i=0;i<selectedLocations.size();i++){
			locationsBuf.append(selectedLocations.get(i).toString()+";");
		}
		//去掉最后一个";"
		String locationsStr=locationsBuf.toString().substring(0,locationsBuf.length()-1);
		//拼接成请求序列
		StringBuffer baiduUrl=new StringBuffer();
		baiduUrl.append("http://api.map.baidu.com/geoconv/v1/?coords=");
		baiduUrl.append(locationsStr);
		baiduUrl.append("&from=1&to=5&ak="+baiduMapAk);
		//获得解析的百度json数据
		String baiduJson = HttpMethod.get(baiduUrl.toString());
		
		//解析json数据
		JSONObject baiduJsonObj=JSON.parseObject(baiduJson);
		JSONArray baiduResult=baiduJsonObj.getJSONArray("result");
		
		List<String> addresses=new ArrayList<String>();
		List<String> baiduLocations=new ArrayList<String>();
		//获取坐标点，并拼接成地址解析需要的格式
		for(int i=0;i<baiduResult.size();i++){
			JSONObject locationObj=baiduResult.getJSONObject(i);
			baiduLocations.add(locationObj.getString("y")+","+locationObj.getString("x"));
		}
		//地址解析的url
		StringBuffer addressUrl=new StringBuffer();
		//百度接口每次只能解析一个点，循环解析
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<baiduLocations.size();i++){
			addressUrl.append("http://api.map.baidu.com/geocoder/v2/?ak="+baiduMapAk+"&callback=renderReverse&location=");
		    addressUrl.append(baiduLocations.get(i));
		    addressUrl.append("&output=json");
		    //返回的数据不是标准的json格式,外部有一层renderReverse&&renderReverse()，需要手动去除
		    String addressJson=HttpMethod.get(addressUrl.toString());
		    //计算前缀的长度
		    int prefix="renderReverse&&renderReverse(".length();
		    String realAddressJson=addressJson.substring(prefix, addressJson.length()-1);
		    JSONObject result=JSON.parseObject(realAddressJson).getJSONObject("result");
		    String address=result.getString("formatted_address") + "（" + result.getString("sematic_description")+ "）";
		    sb.append(address).append(" - ");
		}
		if(sb.length()>0)
			return sb.substring(0, sb.length()-3);
		else
			return null;
	}

	public String getChargeModeString(ChargeModeEnum chargeMode) {
		switch(chargeMode){
		case MILE:
			return TextResolve.getText("order.ChargeModeEnum.MILE");
		case DAY:
			return TextResolve.getText("order.ChargeModeEnum.DAY");
		case PROTOCOL:
			return TextResolve.getText("order.ChargeModeEnum.PROTOCOL");
		case PLANE:
			return TextResolve.getText("order.ChargeModeEnum.PLANE");
		}
		return null;
	}

	public String getOperationRecordTypeString(OrderOperationTypeEnum type){
		switch(type){
		case MODIFY:
			return TextResolve.getText("order.OrderOperationTypeEnum.MODIFY");
		case END_POSTPONE:
			return TextResolve.getText("order.OrderOperationTypeEnum.END_POSTPONE");
		case RESCHEDULE:
			return TextResolve.getText("order.OrderOperationTypeEnum.RESCHEDULE");
		case CANCEL:
			return TextResolve.getText("order.OrderOperationTypeEnum.CANCEL");
		case MODIFY_ORDER_CHECK:
			return TextResolve.getText("order.OrderOperationTypeEnum.MODIFY_SCHEDULE_FORM");
		case EDIT_DRIVER_ACTION:
			return TextResolve.getText("order.OrderOperationTypeEnum.EDIT_DRIVER_ACTION");
		}
		return null;
	}
	
	public String isCarAndDriverAvailable(Order order, Car car, User driver){
		return orderDao.isCarAndDriverAvailable(order, car, driver);
	}
	
	public List<Order> getNeedRemindProtocolOrder(){
		return orderDao.getNeedRemindProtocolOrder();
	}

	@Transactional
	public Order distributeOrder(User scheduler,Order order){
		if(order==null){
			order=orderDao.getEarliestOrderInQueue();
			if(order==null)
				return null;
		}
		order.setScheduling(true);
		order.setScheduler(scheduler);
		order.setSchedulingBeginTime(new Date());
		orderDao.update(order);
		return order;
	}
	
	public boolean canDistributeOrderToUser(User user){
		return orderDao.canDistributeOrderToUser(user);
	}
	
	public Order getOrderDistributed(User user){
		return orderDao.getOrderDistributed(user);
	}

	@Transactional
	public List<BaseEntity> dealCCP(String customerOrganizationName,String customerName,String phoneNumber){
		boolean customerOrganizationExist=false,customerExist=false,phoneNumberExist=false;
		List<BaseEntity> cc=new ArrayList<BaseEntity>(2);
		customerOrganizationExist=customerOrganizationDao.isNameExist(0, customerOrganizationName);
		
		if(customerOrganizationExist){
			QueryHelper helper = new QueryHelper(Customer.class, "c");
			helper.addWhereCondition("c.customerOrganization.name=? and c.name=?",
					customerOrganizationName, customerName);
			List<Customer> customerList = customerDao.getPageBean(1, helper).getRecordList();
			if (customerList!= null && customerList.size() > 0)
				customerExist = true;
		}
		
		QueryHelper helper = new QueryHelper(Customer.class, "c");
		helper.addWhereCondition("? in elements(c.phones)", phoneNumber);
		List<Customer> customerList = customerDao.getPageBean(1, helper).getRecordList();
		if (customerList!= null && customerList.size() > 0)
			phoneNumberExist = true;
		
		if(phoneNumberExist){
			if(customerOrganizationExist){
				if(customerExist){
					//三者都存在，什么都不增加或修改，直接获取对象。
					cc.add(customerOrganizationDao.getByName(customerOrganizationName));
					cc.add(customerDao.getCustomerByNameAndOrganization(customerName, customerOrganizationName));
				}else{
					//修改客户姓名。
					helper = new QueryHelper(Customer.class, "c");
					helper.addWhereCondition("? in elements(c.phones)", phoneNumber);
					customerList = customerDao.getPageBean(1, helper).getRecordList();
					Customer customer=customerList.get(0);
					customer.setName(customerName);
					customerDao.update(customer);
					
					cc.add(customerOrganizationDao.getByName(customerOrganizationName));
					cc.add(customer);
				}
			}else{
				if(customerExist){
					;//这种情况不会出现。客户单位都不存在，此单位下的客户肯定不存在。
				}else{
					//新建客户单位，修改客户姓名，修改客户所属单位。
					CustomerOrganization customerOrganization=new CustomerOrganization();
					customerOrganization.setName(customerOrganizationName);
					customerOrganization.setPriceTable(priceTableDao.getDefaultPriceTable());
					customerOrganizationDao.save(customerOrganization);
					
					helper = new QueryHelper(Customer.class, "c");
					helper.addWhereCondition("? in elements(c.phones)", phoneNumber);
					customerList = customerDao.getPageBean(1, helper).getRecordList();
					Customer customer=customerList.get(0);
					customer.setName(customerName);
					customer.setCustomerOrganization(customerOrganization);
					customerDao.update(customer);
					
					cc.add(customerOrganization);
					cc.add(customer);
				}
			}
		}else{
			if(customerOrganizationExist){
				if(customerExist){
					//客户新建一个电话号码。
					Customer customer=customerDao.getCustomerByNameAndOrganization(customerName, customerOrganizationName);
					customer.getPhones().add(phoneNumber);
					customerDao.update(customer);
					
					cc.add(customerOrganizationDao.getByName(customerOrganizationName));
					cc.add(customer);
				}else{
					//新建客户、电话号码。将客户所属单位设置为已有的单位。
					CustomerOrganization customerOrganization=customerOrganizationDao.getByName(customerOrganizationName);
					
					Customer customer=new Customer();
					customer.setName(customerName);
					customer.setGender(GenderEnum.MALE);
					customer.setCustomerOrganization(customerOrganization);
					List<String> phones=new ArrayList<String>(1);
					phones.add(phoneNumber);
					customer.setPhones(phones);
					customerDao.save(customer);
					
					cc.add(customerOrganization);
					cc.add(customer);
				}
			}else{
				if(customerExist){
					;//这种情况不会出现。客户单位都不存在，此单位下的客户肯定不存在。
				}else{
					//三个数据都不存在，全部新建。
					CustomerOrganization customerOrganization=new CustomerOrganization();
					customerOrganization.setName(customerOrganizationName);
					customerOrganization.setPriceTable(priceTableDao.getDefaultPriceTable());
					customerOrganizationDao.save(customerOrganization);
					
					Customer customer=new Customer();
					customer.setName(customerName);
					customer.setGender(GenderEnum.MALE);
					customer.setCustomerOrganization(customerOrganization);
					List<String> phones=new ArrayList<String>(1);
					phones.add(phoneNumber);
					customer.setPhones(phones);
					customerDao.save(customer);
					
					cc.add(customerOrganization);
					cc.add(customer);
				}
			}
		}
		
		return cc;
	}
	

	public DayOrderDetail getDayOrderDetailByDate(Order order,Date date){
		return orderDao.getDayOrderDetailByDate(order, date);
	}
	
	private DayOrderDetail getDayOrderDetailById(long id){
		return dayOrderDetailDao.getById(id);
	}
	
	@Transactional
	public void orderCheckout(Order order){
		if(order.getStatus()!=OrderStatusEnum.END)
			return;
		float mile=0,chargeMile=0,totalChargeMile=0,chargeMoney=0,totalChargeMoney=0;
		float hours=0;
		float getonDistance=0,getoffDistance=0;
		boolean inMiddleDay;
		Price price;
		//TODO 临时措施 目前设备有问题
		if(true){
		//if(order.getCar().getDevice()!=null && !StringUtils.isEmpty(order.getCar().getDevice().getSN())){
			//TODO 临时措施 目前设备有问题，暂时不需要司机做动作，采用调度填写的数据。
			getonDistance=order.getCustomerGetonMile()-order.getBeginMile();
			getoffDistance=order.getEndMile()-order.getCustomerGetoffMile();
			//getonDistance=lbsDao.getStepMile(order.getCar(), order.getActualBeginDate(), order.getDayDetails().get(0).getGetonDate());
			//getoffDistance=lbsDao.getStepMile(order.getCar(), order.getDayDetails().get(order.getDayDetails().size()-1).getGetoffDate(), order.getActualEndDate());
			
			totalChargeMile=0;
			totalChargeMoney=0;
			price=order.getCustomerOrganization().getPriceTable().getCarServiceType().get(order.getServiceType());
			
			if(order.getChargeMode()==ChargeModeEnum.DAY){
				for(int i=0;i<order.getDayDetails().size();i++){
					DayOrderDetail dod=order.getDayDetails().get(i);
					hours=DateUtils.elapseHours(dod.getGetonDate(), dod.getGetoffDate());
					//TODO 临时措施 目前设备有问题，暂时不需要司机做动作，采用调度填写的数据。
					mile=dod.getActualMile();
					//mile=lbsDao.getStepMile(order.getCar(), dod.getGetonDate(), dod.getGetoffDate());
					//dod.setActualMile(mile);
					if(i==0 && getonDistance>10)
						mile=mile+(getonDistance-10);   //如果上车地点超过10公里远，超出部分列入行驶公里数
					if(i==order.getDayDetails().size()-1 && getoffDistance>10)
						mile=mile+(getoffDistance-10);	//如果下车地点超过10公里远，超出部分列入行驶公里数
					inMiddleDay=(i>0) && (i<order.getDayDetails().size()-1);
					
					chargeMile=0;
					chargeMoney=0;
					if(mile<=10 && inMiddleDay){
						chargeMile=100;
						chargeMoney=price.getPerDay().floatValue()*0.8f;
					}else if(mile<=50 && !inMiddleDay){
						chargeMile=50;
						if(hours<=4)
							chargeMoney=price.getPerHalfDay().floatValue();
						else
							chargeMoney=price.getPerDay().floatValue();
					}else if(mile<=100){
						if(hours<=4 && !inMiddleDay){
							chargeMile=mile;
							chargeMoney=price.getPerHalfDay().floatValue()+(mile-50)*price.getPerMileAfterLimit().floatValue();
						}else if(hours<=8){
							chargeMile=100;
							chargeMoney=price.getPerDay().floatValue();
						}else if(hours<=11){
							chargeMile=100;
							chargeMoney=price.getPerDay().floatValue()+(hours-8)*price.getPerHourAfterLimit().floatValue();
						}else{
							chargeMile=100;
							chargeMoney=price.getPerDay().floatValue()+price.getPerHalfDay().floatValue();
						}
					}else{
						chargeMile=mile;
						chargeMoney =price.getPerDay().floatValue()+(mile-100)*price.getPerMileAfterLimit().floatValue();
					}
					
					dod.setChargeMile(chargeMile);
					dod.setChargeMoney(new BigDecimal(chargeMoney));
					//TODO 临时措施 目前设备有问题，暂时不需要司机做动作，采用调度填写的数据。
					//dod.setPathAbstract(getOrderTrackAbstract(order.getCar().getDevice().getSN(),dod.getGetonDate(),dod.getGetoffDate()));
					dayOrderDetailDao.update(dod);
		
					totalChargeMile+=chargeMile;
					totalChargeMoney+=chargeMoney;
				}
			}else if(order.getChargeMode()==ChargeModeEnum.PLANE){
				DayOrderDetail dod=order.getDayDetails().get(0);
				hours=DateUtils.elapseHours(dod.getGetonDate(), dod.getGetoffDate());
				//TODO 临时措施 目前设备有问题，暂时不需要司机做动作，采用调度填写的数据。
				mile=dod.getActualMile();
				//mile=lbsDao.getStepMile(order.getCar(), dod.getGetonDate(), dod.getGetoffDate());
				//dod.setActualMile(mile);
				if(getonDistance>10)
					mile=mile+(getonDistance-10);   //如果上车地点超过10公里远，超出部分列入行驶公里数
				if(getoffDistance>10)
					mile=mile+(getoffDistance-10);	//如果下车地点超过10公里远，超出部分列入行驶公里数
				if(mile<=50 && hours<=2){
					chargeMile=50;
					chargeMoney=price.getPerPlaneTime().floatValue();
				}else{
					if(mile>50){
						if(mile<100){
							chargeMoney=price.getPerPlaneTime().floatValue()+(price.getPerMileAfterLimit().floatValue()*(mile-50));
						}else{
							chargeMoney=price.getPerDay().floatValue()+(price.getPerMileAfterLimit().floatValue()*(mile-100));
						}
					}else if(hours>2){
						if(hours<4){
							chargeMoney=price.getPerHalfDay().floatValue();
						}else if(hours<8){
							chargeMoney=price.getPerHalfDay().floatValue()+(price.getPerHourAfterLimit().floatValue()*(hours-4));
						}else{
							chargeMoney=price.getPerDay().floatValue()+(price.getPerHourAfterLimit().floatValue()*(hours-8));
						}
					}					
				}
				
				dod.setChargeMile(chargeMile);
				dod.setChargeMoney(new BigDecimal(chargeMoney));
				//TODO 临时措施 目前设备有问题，暂时不需要司机做动作，采用调度填写的数据。
				//dod.setPathAbstract(getOrderTrackAbstract(order.getCar().getDevice().getSN(),dod.getGetonDate(),dod.getGetoffDate()));
				dayOrderDetailDao.update(dod);
	
				totalChargeMile=chargeMile;
				totalChargeMoney=chargeMoney;
			}
			//TODO 临时措施 目前设备有问题，暂时不需要司机做动作，采用调度填写的数据。
			//order.setCustomerGetonMile(order.getDayDetails().get(0).getGetonMile());
			//order.setCustomerGetoffMile(order.getDayDetails().get(order.getDayDetails().size()-1).getGetoffMile());
			order.setTotalChargeMile(totalChargeMile);			
			order.setOrderMoney(new BigDecimal(totalChargeMoney));
		}else{
			order.setBeginMile(0);
			order.setEndMile(0);
			order.setCustomerGetonMile(0);
			order.setCustomerGetoffMile(0);
			order.setTotalChargeMile(0);
			order.setOrderMoney(BigDecimal.ZERO);
		}
		

		BigDecimal agentMoney=BigDecimal.ZERO;
		if(order.getToll()!=null)
			agentMoney=agentMoney.add(order.getToll());
		if(order.getRoomAndBoardFee()!=null)
			agentMoney=agentMoney.add(order.getRoomAndBoardFee());
		if(order.isOtherFeeAccount() && order.getOtherFee()!=null)
			agentMoney=agentMoney.add(order.getOtherFee());
		if(order.isRefuelMoneyAccount() && order.getRefuelMoney()!=null)
			agentMoney=agentMoney.add(order.getRefuelMoney());
		if(order.isWashingFeeAccount() && order.getWashingFee()!=null)
			agentMoney=agentMoney.add(order.getWashingFee());
		if(order.isParkingFeeAccount() && order.getParkingFee()!=null)
			agentMoney=agentMoney.add(order.getParkingFee());
		order.setTax(agentMoney.multiply(new BigDecimal(Configuration.getAgentMoneyTaxRatio())));
		order.setOrderMoney(order.getOrderMoney().add(agentMoney).add(order.getTax()));
		order.setActualMoney(order.getOrderMoney());
			
		orderDao.update(order);
	}
	
	public Order getProtocolOrderByCar(Car car){
		QueryHelper queryHelper=new QueryHelper("order_","o");
		queryHelper.addWhereCondition("(o.status=? or o.status=? or o.status=? or o.status=? or o.status=?)", 
				OrderStatusEnum.SCHEDULED,OrderStatusEnum.ACCEPTED,OrderStatusEnum.BEGIN,OrderStatusEnum.GETON,OrderStatusEnum.GETOFF);
		queryHelper.addWhereCondition("o.car=?",car);
		queryHelper.addWhereCondition("o.chargeMode=?", ChargeModeEnum.PROTOCOL);
		List<Order> list=orderDao.getAllQuerry(queryHelper);
		if(list!=null && list.size()>0)
			return list.get(0);
		else
			return null;
	}
	
	public List<Order> getToBeDeprivedSchedulingOrder(){
		return orderDao.getToBeDeprivedSchedulingOrder();
	}
	
	public boolean canEditDriverAction(Order order){
		return order.getStatus()==OrderStatusEnum.SCHEDULED || order.getStatus()==OrderStatusEnum.ACCEPTED
				|| order.getStatus()==OrderStatusEnum.BEGIN || order.getStatus()==OrderStatusEnum.GETON
				|| order.getStatus()==OrderStatusEnum.GETOFF || order.getStatus()==OrderStatusEnum.END;
	}
	
	private String getDriverActionVOId(Order order,OrderStatusEnum status, DayOrderDetail dayOrderDetail){
		StringBuffer sb=new StringBuffer();
		sb.append(order.getId()).append("-");
		sb.append(status.getId()).append("-");
		if(dayOrderDetail!=null)
			sb.append(dayOrderDetail.getId());
		else
			sb.append("0");
		return sb.toString();
	}
	
	private Order getOrderFromActionVOId(String id){
		return getOrderById(Long.valueOf(id.split("-")[0]));
	}
	
	private OrderStatusEnum getStatusFromActionVOId(String id){
		return OrderStatusEnum.ACCEPTED.getById(Integer.valueOf(id.split("-")[1]));
	}
	
	private DayOrderDetail getDayOrderDetailFromActionVOId(String id){
		long dodId=Long.valueOf(id.split("-")[2]);
		if(dodId==0)
			return null;
		else
			return getDayOrderDetailById(dodId);
	}
	
	public List<DriverActionVO> getDriverActions(Order order){
		List<DriverActionVO> actionList=new LinkedList<DriverActionVO>();
		switch(order.getStatus()){
		case CANCELLED:
		case PAID:
			break;
		case END:
			DriverActionVO davo=new DriverActionVO();
			davo.setId(getDriverActionVOId(order,OrderStatusEnum.END,null));
			davo.setStatus(OrderStatusEnum.END);
			davo.setDate(order.getActualEndDate());
			davo.setStatus(OrderStatusEnum.END);
			actionList.add(0,davo);
		case GETOFF:
		case GETON:
			for(int n=order.getDayDetails().size()-1;n>=0;n--){
				DayOrderDetail dod=order.getDayDetails().get(n);
				if(dod.getGetoffDate()!=null){
					davo=new DriverActionVO();
					davo.setId(getDriverActionVOId(order,OrderStatusEnum.GETOFF,dod));
					davo.setStatus(OrderStatusEnum.GETOFF);
					davo.setDate(dod.getGetoffDate());
					davo.setStatus(OrderStatusEnum.GETOFF);
					actionList.add(0,davo);
				}
				if(dod.getGetonDate()!=null){
					davo=new DriverActionVO();
					davo.setId(getDriverActionVOId(order,OrderStatusEnum.GETON,dod));
					davo.setStatus(OrderStatusEnum.GETON);
					davo.setDate(dod.getGetonDate());
					davo.setStatus(OrderStatusEnum.GETON);
					actionList.add(0,davo);
				}
			}
		case BEGIN:
			davo=new DriverActionVO();
			davo.setId(getDriverActionVOId(order,OrderStatusEnum.BEGIN,null));
			davo.setStatus(OrderStatusEnum.BEGIN);
			davo.setDate(order.getActualBeginDate());
			davo.setStatus(OrderStatusEnum.BEGIN);
			actionList.add(0,davo);
		case ACCEPTED:
			davo=new DriverActionVO();
			davo.setId(getDriverActionVOId(order,OrderStatusEnum.ACCEPTED,null));
			davo.setStatus(OrderStatusEnum.ACCEPTED);
			davo.setDate(order.getAcceptedTime());
			davo.setStatus(OrderStatusEnum.ACCEPTED);
			actionList.add(0,davo);
		case SCHEDULED:
		case INQUEUE:
			break;
		}
		return actionList;
	}
	
	private boolean isDateValidForDriverAction(Date date, Order order, OrderStatusEnum status,DayOrderDetail dod){
		switch(status){
		case ACCEPTED:
			if(date.before(order.getScheduleTime()))
				return false;
			if(order.getActualBeginDate()!=null && date.after(order.getActualBeginDate()))
				return false;
			break;
		case BEGIN:
			if(date.before(order.getAcceptedTime()))
				return false;
			if(order.getDayDetails().size()>0 && date.after(order.getDayDetails().get(0).getGetonDate()))
				return false;
			break;
		case GETON:
			Date date1=null,date2=null;
			if(dod!=null){//编辑动作时间
				int index=order.getDayDetails().indexOf(dod);
				if(index==0)
					date1=order.getActualBeginDate();
				else
					date1=order.getDayDetails().get(index-1).getGetoffDate();
				if(dod.getGetoffDate()!=null)
					date2=dod.getGetoffDate();
			}else{//新建动作
				if(order.getDayDetails()==null || order.getDayDetails().size()==0)
					date1=order.getActualBeginDate();
				else
					date1=order.getDayDetails().get(order.getDayDetails().size()-1).getGetoffDate();
			}
			if(date.before(date1))
				return false;
			if(date2!=null && date.after(date2))
				return false;
			break;
		case GETOFF:
			date1=null;
			date2=null;
			date1=dod.getGetonDate();
			int index=order.getDayDetails().indexOf(dod);
			if(index==order.getDayDetails().size()-1){
				if(order.getActualEndDate()!=null)
					date2=order.getActualEndDate();
			}else
				date2=order.getDayDetails().get(index+1).getGetonDate();
			if(date.before(date1))
				return false;
			if(date2!=null && date.after(date2))
				return false;
			break;
		case END:
			//TODO 应该考虑结束时间不应该晚于收款时间
			return date.after(order.getDayDetails().get(order.getDayDetails().size()-1).getGetoffDate());
		}
		return true;
	}
	
	@Transactional
	public void EditDriverAction(String actionId, Date date, User user){
		Order order=getOrderFromActionVOId(actionId);
		OrderStatusEnum status=getStatusFromActionVOId(actionId);
		DayOrderDetail dod=getDayOrderDetailFromActionVOId(actionId);
		String description=null;		

		if(!isDateValidForDriverAction(date,order,status,dod))
			return;
		
		if(status==OrderStatusEnum.ACCEPTED){
			description="将订单接受时间由 "+DateUtils.getYMDHMSString(order.getAcceptedTime())+" 改为了 "+DateUtils.getYMDHMSString(date);
			order.setAcceptedTime(date);
			orderDao.update(order);
		}else if(status==OrderStatusEnum.BEGIN){
			description="将订单开始时间由 "+DateUtils.getYMDHMSString(order.getActualBeginDate())+" 改为了 "+DateUtils.getYMDHMSString(date);
			order.setActualBeginDate(date);
			orderDao.update(order);
		}else if(status==OrderStatusEnum.GETON){
			description="将订单上车时间由 "+DateUtils.getYMDHMSString(dod.getGetonDate())+" 改为了 "+DateUtils.getYMDHMSString(date);
			dod.setGetonDate(date);
			dayOrderDetailDao.update(dod);
		}else if(status==OrderStatusEnum.GETOFF){
			description="将订单下车时间由 "+DateUtils.getYMDHMSString(dod.getGetoffDate())+" 改为了 "+DateUtils.getYMDHMSString(date);
			dod.setGetoffDate(date);
			dayOrderDetailDao.update(dod);
		}else if(status==OrderStatusEnum.END){
			description="将订单完成时间由 "+DateUtils.getYMDHMSString(order.getActualEndDate())+" 改为了 "+DateUtils.getYMDHMSString(date);
			order.setActualEndDate(date);
			orderDao.update(order);
		}
		
		OrderOperationRecord oor=new OrderOperationRecord();
		oor.setOrder(order);
		oor.setType(OrderOperationTypeEnum.EDIT_DRIVER_ACTION);
		oor.setDate(new Date());
		oor.setUser(user);
		oor.setDescription(description);
		orderOperationRecordDao.save(oor);
	}
	
	private boolean canDeleteDriverAction(String actionId){
		Order order=getOrderFromActionVOId(actionId);
		List<DriverActionVO> voList=getDriverActions(order);
		return voList.get(voList.size()-1).getId().equals(actionId);
	}
	
	@Transactional
	public void deleteDriverAction(String actionId,User user){
		if(!canDeleteDriverAction(actionId))
			return;
		
		Order order=getOrderFromActionVOId(actionId);
		OrderStatusEnum status=getStatusFromActionVOId(actionId);
		DayOrderDetail dod=getDayOrderDetailFromActionVOId(actionId);
		String description=null;
		
		if(status==OrderStatusEnum.ACCEPTED){
			description="删除“接受”动作，原动作时间： "+DateUtils.getYMDHMSString(order.getAcceptedTime());
			order.setStatus(OrderStatusEnum.SCHEDULED);
			order.setAcceptedTime(null);
			orderDao.update(order);
		}else if(status==OrderStatusEnum.BEGIN){
			description="删除“开始”动作，原动作时间： "+DateUtils.getYMDHMSString(order.getActualBeginDate());
			order.setStatus(OrderStatusEnum.ACCEPTED);
			order.setActualBeginDate(null);
			orderDao.update(order);
		}else if(status==OrderStatusEnum.GETON){
			description="删除“上车”动作，原动作时间： "+DateUtils.getYMDHMSString(dod.getGetonDate());
			if(order.getDayDetails().size()==1)
				order.setStatus(OrderStatusEnum.BEGIN);
			else
				order.setStatus(OrderStatusEnum.GETOFF);
			orderDao.update(order);
			
			dayOrderDetailDao.delete(dod.getId());
		}else if(status==OrderStatusEnum.GETOFF){
			description="删除“下车”动作，原动作时间： "+DateUtils.getYMDHMSString(dod.getGetoffDate());
			order.setStatus(OrderStatusEnum.GETON);
			orderDao.update(order);
			
			dod.setGetoffDate(null);
			dayOrderDetailDao.update(dod);
		}else if(status==OrderStatusEnum.END){
			description="删除“结束”动作，原动作时间： "+DateUtils.getYMDHMSString(order.getActualEndDate());
			order.setStatus(OrderStatusEnum.GETOFF);
			order.setActualEndDate(null);
			orderDao.update(order);
		}
			
		OrderOperationRecord oor=new OrderOperationRecord();
		oor.setOrder(order);
		oor.setType(OrderOperationTypeEnum.EDIT_DRIVER_ACTION);
		oor.setDate(new Date());
		oor.setUser(user);
		oor.setDescription(description);
		orderOperationRecordDao.save(oor);
	}
	
	public boolean canAddAcceptAction(Order order){
		return order.getStatus()==OrderStatusEnum.SCHEDULED;
	}
	
	public boolean canAddBeginAction(Order order){
		return driverAPPService.canBeginOrder(order);
	}
	
	public boolean canAddGetonAction(Order order){
		return driverAPPService.canCustomerGeton(order);
	}
	
	public boolean canAddGetoffAction(Order order){
		return driverAPPService.canCustomerGetoff(order);
	}
	
	public boolean canAddEndAction(Order order){
		return driverAPPService.canEndOrder(order);
	}
	
	@Transactional
	public void addAcceptAction(Order order,User user,Date date){
		if(order.getStatus() == OrderStatusEnum.SCHEDULED) {
			order.setStatus(OrderStatusEnum.ACCEPTED);
			order.setAcceptedTime(date);
			orderDao.update(order);
			
			OrderOperationRecord oor=new OrderOperationRecord();
			oor.setOrder(order);
			oor.setType(OrderOperationTypeEnum.EDIT_DRIVER_ACTION);
			oor.setDate(new Date());
			oor.setUser(user);
			oor.setDescription("增加了司机“接受”操作，指定的操作时间是："+DateUtils.getYMDHMSString(date));
			orderOperationRecordDao.save(oor);
		}
	}
	
	@Transactional
	public void addBeginAction(Order order,User user,Date date){
		driverAPPService.orderBegin(order, user, date);
	}
	
	@Transactional
	public void addGetonAction(Order order,User user,Date date){
		driverAPPService.customerGeton(order, user, date);
	}
	
	@Transactional
	public void addGetoffAction(Order order,User user,Date date){
		driverAPPService.customerGetoff(order, user, date);
	}

	@Transactional
	public void addEndAction(Order order,User user,Date date){
		driverAPPService.orderEnd(order, user, date);
	}
	
	/********************************************
	 * 编辑派车单
	 ********************************************/
	public boolean canEditOrderBill(Order order){
		//TODO 临时措施 目前设备有问题
		return order.getStatus()==OrderStatusEnum.ACCEPTED ||
				order.getStatus()==OrderStatusEnum.BEGIN ||
						order.getStatus()==OrderStatusEnum.GETON ||
								order.getStatus()==OrderStatusEnum.GETOFF ||
									order.getStatus()==OrderStatusEnum.END;
		////////////////////////
		//return order.getStatus()==OrderStatusEnum.END;
	}
	
	@Transactional
	public void editOrderBill(Order order, User user){
		Order oldOrder=orderDao.getById(order.getId());
		StringBuffer sb=new StringBuffer();
		if((order.getDestination()==null && oldOrder.getDestination()!=null) 
				|| (order.getDestination()!=null && oldOrder.getDestination()!=null && !order.getDestination().equals(oldOrder.getDestination()))
				|| (order.getDestination()!=null && oldOrder.getDestination()==null))
			sb.append("将目的地由： ").append(oldOrder.getDestination()!=null ? oldOrder.getDestination() : "<空>").append(" 改为了： ").append(order.getDestination()!=null ? order.getDestination() : "<空>").append("；");
		for(int i=0;i<oldOrder.getDayDetails().size();i++){
			DayOrderDetail dod=order.getDayDetails().get(i);
			DayOrderDetail oldDod=oldOrder.getDayDetails().get(i);
			if(!DateUtils.getHMString(dod.getGetonDate()).equals(DateUtils.getHMString(oldDod.getGetonDate())))
				sb.append("将").append(DateUtils.getYMDString(dod.getGetonDate())).append("的上车时间由： ")
					.append(DateUtils.getHMString(oldDod.getGetonDate())).append(" 改为了： ")
					.append(DateUtils.getHMString(dod.getGetonDate())).append("；");
			if(!DateUtils.getHMString(dod.getGetoffDate()).equals(DateUtils.getHMString(oldDod.getGetoffDate())))
				sb.append("将").append(DateUtils.getYMDString(dod.getGetonDate())).append("的下车时间由： ")
					.append(DateUtils.getHMString(oldDod.getGetoffDate())).append(" 改为了： ")
					.append(DateUtils.getHMString(dod.getGetoffDate())).append("；");
			if(!dod.getPathAbstract().equals(oldDod.getPathAbstract()))
				sb.append("将").append(DateUtils.getYMDString(dod.getGetonDate())).append("的经过地点摘要由： ")
					.append(oldDod.getPathAbstract()!=null ? oldDod.getPathAbstract() : "<空>").append(" 改为了： ").append(dod.getPathAbstract()!=null ? dod.getPathAbstract() : "<空>").append("；");
			if(dod.getActualMile()!=oldDod.getActualMile())
				sb.append("将").append(DateUtils.getYMDString(dod.getGetonDate())).append("的实际公里由： ")
					.append(oldDod.getActualMile()).append(" 改为了： ").append(dod.getActualMile()).append("；");
			if(dod.getChargeMile()!=oldDod.getChargeMile())
				sb.append("将").append(DateUtils.getYMDString(dod.getGetonDate())).append("的收费公里由： ")
					.append(oldDod.getChargeMile()).append(" 改为了： ").append(dod.getChargeMile()).append("；");
		}
		
		if(order.getBeginMile()!=oldOrder.getBeginMile())
			sb.append("将出库路码由： ").append(oldOrder.getBeginMile()).append(" 改为了： ").append(order.getBeginMile()).append("；");
		if(order.getCustomerGetonMile()!=oldOrder.getCustomerGetonMile())
			sb.append("将客户上车路码由： ").append(oldOrder.getCustomerGetonMile()).append(" 改为了： ").append(order.getCustomerGetonMile()).append("；");
		if(order.getCustomerGetoffMile()!=oldOrder.getCustomerGetoffMile())
			sb.append("将客户下车路码由： ").append(oldOrder.getCustomerGetoffMile()).append(" 改为了： ").append(order.getCustomerGetoffMile()).append("；");
		if(order.getEndMile()!=oldOrder.getEndMile())
			sb.append("将回库路码由： ").append(oldOrder.getEndMile()).append(" 改为了： ").append(order.getEndMile()).append("；");
		if((order.getRefuelMoney()==null && oldOrder.getRefuelMoney()!=null) 
				|| (order.getRefuelMoney()!=null && oldOrder.getRefuelMoney()!=null && !order.getRefuelMoney().equals(oldOrder.getRefuelMoney()))
				|| (order.getRefuelMoney()!=null && oldOrder.getRefuelMoney()==null))
			sb.append("将油费由： ").append(oldOrder.getRefuelMoney()!=null ? oldOrder.getRefuelMoney() : "<空>").append(" 改为了： ")
				.append(order.getRefuelMoney()!=null ? order.getRefuelMoney() : "<空>").append("；");
		if((order.getWashingFee()==null && oldOrder.getWashingFee()!=null) 
				|| (order.getWashingFee()!=null && oldOrder.getWashingFee()!=null && !order.getWashingFee().equals(oldOrder.getWashingFee()))
				|| (order.getWashingFee()!=null && oldOrder.getWashingFee()==null))
			sb.append("将洗车费由： ").append(oldOrder.getWashingFee()!=null ? oldOrder.getWashingFee() : "<空>").append(" 改为了： ")
				.append(order.getWashingFee()!=null ? order.getWashingFee() : "<空>").append("；");
		if((order.getParkingFee()==null && oldOrder.getParkingFee()!=null) 
				|| (order.getParkingFee()!=null && oldOrder.getParkingFee()!=null && !order.getParkingFee().equals(oldOrder.getParkingFee()))
				|| (order.getParkingFee()!=null && oldOrder.getParkingFee()==null))
			sb.append("将停车费由： ").append(oldOrder.getParkingFee()!=null ? oldOrder.getParkingFee() : "<空>").append(" 改为了： ")
				.append(order.getParkingFee()!=null ? order.getParkingFee() : "<空>").append("；");
		if(order.getTotalChargeMile()!=oldOrder.getTotalChargeMile())
			sb.append("将计费路码由： ").append(oldOrder.getTotalChargeMile()).append(" 改为了： ").append(order.getTotalChargeMile()).append("；");
		if((order.getToll()==null && oldOrder.getToll()!=null) 
				|| (order.getToll()!=null && oldOrder.getToll()!=null && !order.getToll().equals(oldOrder.getToll()))
				|| (order.getToll()!=null && oldOrder.getToll()==null))
			sb.append("将过路费由： ").append(oldOrder.getToll()!=null ? oldOrder.getToll() : "<空>").append(" 改为了： ")
				.append(order.getToll()!=null ? order.getToll() : "<空>").append("；");
		if((order.getRoomAndBoardFee()==null && oldOrder.getRoomAndBoardFee()!=null) 
				|| (order.getRoomAndBoardFee()!=null && oldOrder.getRoomAndBoardFee()!=null && !order.getRoomAndBoardFee().equals(oldOrder.getRoomAndBoardFee()))
				|| (order.getRoomAndBoardFee()!=null && oldOrder.getRoomAndBoardFee()==null))
			sb.append("将食宿费由： ").append(oldOrder.getRoomAndBoardFee()!=null ? oldOrder.getRoomAndBoardFee() : "<空>").append(" 改为了： ")
				.append(order.getRoomAndBoardFee()!=null ? order.getRoomAndBoardFee() : "<空>").append("；");
		if((order.getOtherFee()==null && oldOrder.getOtherFee()!=null) 
				|| (order.getOtherFee()!=null && oldOrder.getOtherFee()!=null && !order.getOtherFee().equals(oldOrder.getOtherFee()))
				|| (order.getOtherFee()!=null && oldOrder.getOtherFee()==null))
			sb.append("将其它费用由： ").append(oldOrder.getOtherFee()!=null ? oldOrder.getOtherFee() : "<空>").append(" 改为了： ")
				.append(order.getOtherFee()!=null ? order.getOtherFee() : "<空>").append("；");
		if((order.getTax()==null && oldOrder.getTax()!=null) 
				|| (order.getTax()!=null && oldOrder.getTax()!=null && !order.getTax().equals(oldOrder.getTax()))
				|| (order.getTax()!=null && oldOrder.getTax()==null))
			sb.append("将税费由： ").append(oldOrder.getTax()!=null ? oldOrder.getTax() : "<空>").append(" 改为了： ")
				.append(order.getTax()!=null ? order.getTax() : "<空>").append("；");
		if((order.getOrderMoney()==null && oldOrder.getOrderMoney()!=null) 
				|| (order.getOrderMoney()!=null && oldOrder.getOrderMoney()!=null && !order.getOrderMoney().equals(oldOrder.getOrderMoney()))
				|| (order.getOrderMoney()!=null && oldOrder.getOrderMoney()==null))
			sb.append("将核算金额由： ").append(oldOrder.getOrderMoney()!=null ? oldOrder.getOrderMoney() : "<空>").append(" 改为了： ")
				.append(order.getOrderMoney()!=null ? order.getOrderMoney() : "<空>").append("；");
		if((order.getActualMoney()==null && oldOrder.getActualMoney()!=null) 
				|| (order.getActualMoney()!=null && oldOrder.getActualMoney()!=null && !order.getActualMoney().equals(oldOrder.getActualMoney()))
				|| (order.getActualMoney()!=null && oldOrder.getActualMoney()==null))
			sb.append("将实收金额由： ").append(oldOrder.getActualMoney()!=null ? oldOrder.getActualMoney() : "<空>").append(" 改为了： ")
				.append(order.getActualMoney()!=null ? order.getActualMoney() : "<空>").append("；");
		if(order.getGrade()!=oldOrder.getGrade())
			sb.append("将服务评价由： ").append(oldOrder.getGrade()).append(" 改为了： ").append(order.getGrade()).append("；");
		if((order.getOptions()==null && oldOrder.getOptions()!=null) 
				|| (order.getOptions()!=null && oldOrder.getOptions()!=null && !order.getOptions().equals(oldOrder.getOptions()))
				|| (order.getOptions()!=null && oldOrder.getOptions()==null))
			sb.append("将服务评价由： ").append(oldOrder.getOptions()!=null ? oldOrder.getOptions() : "<空>").append(" 改为了： ").append(order.getOptions()!=null ? order.getOptions() : "<空>").append("；");
		
		if(sb.length()>0){
			OrderOperationRecord oor=new OrderOperationRecord();
			oor.setOrder(order);
			oor.setType(OrderOperationTypeEnum.MODIFY_ORDER_CHECK);
			oor.setDate(new Date());
			oor.setUser(user);
			oor.setDescription(sb.toString());
			orderOperationRecordDao.save(oor);
		}
				
		orderDao.update(order);
	}
	
	@Transactional
	public void deleteDayOrderDetail(long id){
		dayOrderDetailDao.delete(id);
	}

	@Transactional
	public void saveDayOrderDetail(DayOrderDetail dod){
		dayOrderDetailDao.save(dod);
	}
}
