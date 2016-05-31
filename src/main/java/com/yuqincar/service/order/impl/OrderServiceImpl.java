/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.yuqincar.service.order.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuqincar.dao.CustomerOrganization.CustomerOrganizationDao;
import com.yuqincar.dao.customer.CustomerDao;
import com.yuqincar.dao.customer.OtherPassengerDao;
import com.yuqincar.dao.monitor.LocationDao;
import com.yuqincar.dao.order.AddressDao;
import com.yuqincar.dao.order.DayOrderDetailDao;
import com.yuqincar.dao.order.OrderDao;
import com.yuqincar.dao.order.OrderOperationRecordDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Address;
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
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.app.APPMessageService;
import com.yuqincar.service.app.DriverAPPService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.order.WatchKeeperService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.HttpMethod;
import com.yuqincar.utils.QueryHelper;
import com.yuqincar.utils.TextResolve;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private AddressDao addressDao;
	
	@Autowired
	private LocationDao locationDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private OtherPassengerDao otherPassengerDao;
	
	@Autowired
	private CustomerOrganizationDao customerOrganizationDao;
	
	@Autowired
	private OrderOperationRecordDao orderOperationRecordDao;
	
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
	public void EnQueue(Order order,String baseSN,int copyNumber) {
		System.out.println("in EnQueue");
		//处理是否新建客户单位和客户		
		List<BaseEntity> cc=dealCCP(order.getCustomerOrganization().getName(),order.getCustomer().getName(),order.getPhone());
		order.setCustomerOrganization((CustomerOrganization)cc.get(0));
		order.setCustomer((Customer)cc.get(1));
				
		orderDao.EnQueue(order,baseSN);
		
		if(copyNumber>0)
			copyOrderScheduled(order,copyNumber);
		
		if(watchKeeperService.getWatchKeeper().isOnDuty()){
			Map<String,String> param=new HashMap<String,String>();
			param.put("customer", order.getCustomer().getName());
			param.put("customerOrganization",order.getCustomerOrganization().getName());
			param.put("phoneNumber", order.getPhone());
			param.put("chargeMode", order.getChargeMode().toString());
			if(order.getChargeMode()==ChargeModeEnum.MILE || order.getChargeMode()==ChargeModeEnum.PLANE){
				param.put("time", DateUtils.getYMDHMString(order.getPlanBeginDate()));
				param.put("address", order.getFromAddress()+" 到 "+order.getToAddress());
			}else{
				param.put("time", DateUtils.getYMDHMString(order.getPlanBeginDate())+" 到 "+DateUtils.getYMDHMString(order.getPlanEndDate()));
				param.put("address", order.getFromAddress());
			}
			
			//给值班员发短信
			System.out.println("keeper="+watchKeeperService.getWatchKeeper().getKeeper().getName());
			System.out.println("keeper="+watchKeeperService.getWatchKeeper().getKeeper().getPhoneNumber());
			smsService.sendTemplateSMS(watchKeeperService.getWatchKeeper().getKeeper().getPhoneNumber(), SMSService.SMS_TEMPLATE_ORDER_ENQUEUE, param);
			
			//给值班员发APP推送消息
			//TODO 目前APP没有投入使用，先注释掉，以后使用。
//			StringBuffer sb=new StringBuffer();
//			sb.append("有新订单入队列。").append(order.getCustomer().getName()).append("，").append(order.getChargeMode().toString())
//				.append("，上车时间：").append(DateUtils.getYMDHMString(order.getPlanBeginDate()));
//			appMessageService.sendMessageToSchedulerAPP(watchKeeperService.getWatchKeeper().getKeeper(), sb.toString(),null);
		}
	}

	public List<List<BaseEntity>> getCarTask(Car car, Date fromDate, Date toDate) {
		return orderDao.getCarTask(car, fromDate, toDate);
	}

	public boolean canScheduleOrder(Order order) {
		return order.getStatus()==OrderStatusEnum.INQUEUE;
	}

	private CustomerOrganization generateCustomerOrganization(
			String customerOrganizationName) {
		if (customerOrganizationDao
				.isNameExist(0, customerOrganizationName))
			return customerOrganizationDao.getByName(customerOrganizationName);
		else {
			CustomerOrganization customerOrganization = new CustomerOrganization();
			customerOrganization.setName(customerOrganizationName);
			customerOrganizationDao.save(customerOrganization);
			return customerOrganization;
		}
	}

	private Customer generateCustomer(
			CustomerOrganization customerOrganization, String customerName,
			String phone) {
		boolean customerExist, phoneExist;
		QueryHelper helper = new QueryHelper(Customer.class, "c");
		helper.addWhereCondition("c.customerOrganization=? and c.name=?",
				customerOrganization, customerName);
		List<Customer> customerList = (List<Customer>) customerDao
				.getPageBean(1, helper).getRecordList();
		if (customerList == null || customerList.size() == 0)
			customerExist = false;
		else
			customerExist = true;

		helper = new QueryHelper(Customer.class, "c");
		helper.addWhereCondition(
				"? in elements(c.phones) and c.customerOrganization=?", phone,
				customerOrganization);
		List<Customer> cList = (List<Customer>) customerDao.getPageBean(
				1, helper).getRecordList();
		if (cList == null || cList.size() == 0)
			phoneExist = false;
		else
			phoneExist = true;

		if (!customerExist) {
			Customer customer = new Customer();
			customer.setName(customerName);
			customer.setCustomerOrganization(customerOrganization);
			List<String> phones = new ArrayList<String>();
			phones.add(phone);
			customer.setPhones(phones);
			customerDao.save(customer);
			return customer;
		} else {
			if (!phoneExist) {
				customerList.get(0).getPhones().add(phone);
				customerDao.update(customerList.get(0));
			}
			return customerList.get(0);
		}
	}

	private boolean isAddressEquals(Address address1, Address address2) {
		boolean description = (address1.getDescription() == null && address2
				.getDescription() == null)
				|| (address1.getDescription() != null && address1
						.getDescription().equals(address2.getDescription()));
		boolean detail = (address1.getDetail() == null && address2.getDetail() == null)
				|| (address1.getDetail() != null && address1.getDetail()
						.equals(address2.getDetail()));
		return description && detail;
	}
	
	private void copyOrderScheduled(Order order, int n) {
		String baseSN=order.getSn();
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
			List<Address> addresses=new ArrayList<Address>(2);
			o.setMemo(o.getMemo());
			o.setCallForOther(order.isCallForOther());
			o.setOtherPassengerName(order.getOtherPassengerName());
			o.setOtherPhoneNumber(order.getOtherPhoneNumber());
			o.setCallForOtherSendSMS(order.isCallForOtherSendSMS());
			o.setOrderSource(order.getOrderSource());
			EnQueue(o,baseSN,0);
			baseSN=o.getSn();
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
		
	@Transactional
	public int scheduleOrder(String scheduleMode,Order order, String organizationName, String customerName, Car car, User driver, int copyNumber,Order toUpdateOrder,User user) {
		//处理是否新建客户单位和客户
		List<BaseEntity> cc=dealCCP(organizationName,customerName,order.getPhone());
		order.setCustomerOrganization((CustomerOrganization)cc.get(0));
		order.setCustomer((Customer)cc.get(1));
			
		int result= orderDao.scheduleOrder(scheduleMode, order, car, driver, user);
		if(result==0){
			//复制订单
			if(copyNumber>0)
				copyOrderScheduled(order,copyNumber);
			if(order.isCallForOther())
				saveOtherPassenger(order);
			if(scheduleMode==OrderService.SCHEDULE_FROM_NEW || scheduleMode==OrderService.SCHEDULE_FROM_QUEUE)
				appMessageService.sendMessageToDriverAPP(order.getDriver(), "你有新的订单。上车时间："+DateUtils.getYMDHMString(order.getPlanBeginDate())
						+ "；上车地点："+order.getFromAddress(),null);
			else
				checkUpdateData(order,toUpdateOrder,user);
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
				.append(getChargeModeString(toUpdateOrder.getChargeMode()))
				.append(" 改为 ").append(getChargeModeString(order.getChargeMode())).append("；");
		
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
		
		if(!order.getCar().equals(toUpdateOrder.getCar()))
			sb.append("(").append(++n).append(")").append("车辆由：")
				.append(toUpdateOrder.getCar().getPlateNumber())
				.append(" 改为 ").append(order.getCar().getPlateNumber()).append("；");
		
		if(n>0){
			OrderOperationRecord orderOperation = new OrderOperationRecord();
			orderOperation.setOrder(order);
			orderOperation.setDescription(sb.toString());
			orderOperation.setDate(new Date());
			orderOperation.setType(OrderOperationTypeEnum.MODIFY);
			orderOperation.setUser(user);
			orderOperationRecordDao.save(orderOperation);
			
			appMessageService.sendMessageToDriverAPP(toUpdateOrder.getDriver(), "有订单（"+order.getSn()+"）发生了修改："+sb.toString(),null);
			if(!toUpdateOrder.getDriver().equals(order.getDriver()))
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
				order.getStatus()==OrderStatusEnum.BEGIN ;
	}

	@Transactional
	public int cancelOrder(Order order, User user, String description){
		if (canCancelOrder(order)) {
			order.setStatus(OrderStatusEnum.CANCELLED);
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
		return order.getStatus()==OrderStatusEnum.SCHEDULED || order.getStatus()==OrderStatusEnum.ACCEPTED;
	}

	public PageBean getPageBean(int pageNum, QueryHelper helper) {
		return orderDao.getPageBean(pageNum, helper);
	}

	public boolean canOrderEndPostpone(Order order) {
		return order.getChargeMode()!=ChargeModeEnum.MILE && order.getStatus()==OrderStatusEnum.BEGIN;
	}

	@Transactional
	public int orderEndPostpone(Order order, Date endDate, String description,
			User user){
		if(!canOrderEndPostpone(order))
			return 2;
		Date temp=order.getPlanEndDate();	//如果在不成功的情况，不使用temp来补救，不知为什么：不调用update的情况下，planEndDate还是会反应在数据库中。
		order.setPlanEndDate(endDate);
		if(isCarAndDriverAvailable(order,order.getCar(),order.getDriver())!=0){
			order.setPlanEndDate(temp);
			return 1;
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
		
		return 0;
	}

	public boolean canOrderReschedule(Order order) {
		return order.getStatus()==OrderStatusEnum.BEGIN;
	}

	@Transactional
	public int orderReschedule(Order order, Car car, User driver, User user, String description){
		if(!canOrderReschedule(order))
			return 2;
		Date temp=order.getPlanBeginDate();
		order.setPlanBeginDate(new Date());
		if(isCarAndDriverAvailable(order,car,driver)!=0){
			order.setPlanBeginDate(temp);
			return 1;
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
		oor.setDescription(sb.toString()+"原因："+description);
		orderOperationRecordDao.save(oor);
		
		String message="订单（"+order.getSn()+"）"+sb.toString();
		if(!tempDriver.equals(order.getDriver())){
			appMessageService.sendMessageToDriverAPP(tempDriver, message, null);
			appMessageService.sendMessageToDriverAPP(order.getDriver(), message, null);
		}
		return 0;
	}

	public Order getOrderBySN(String sn) {
		return orderDao.getOrderBySN(sn);
	}

	public PageBean<Order> queryOrder(int pageNum, QueryHelper helper) {
		return orderDao.getPageBean(pageNum, helper);
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

	public List<Car> getCarsWithoutOrderNow() {
		return orderDao.getCarsWithoutOrderNow();
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
	public TreeMap<Date, String> getOrderTrackAbstract(Order order) {
		//如果订单未结束，返回null
    	if(order.getStatus()!=OrderStatusEnum.END&&order.getStatus()!=OrderStatusEnum.PAYED)
    		return null;
    	
    	//轨迹提取的长度，即轨迹中序列的最大点数
    	int trackLenth=9;
    	//百度地图服务的ak账号
    	String baiduMapAk="XNcVScWmj4gRZeSvzIyWQ5TZ";
		//获取执行订单车辆的设备sn
		String device_sn=order.getCar().getDevice().getSN();
		//获取订单执行的实际开始时间和结束时间
		Date beginDate=order.getActualBeginDate();
		Date endDate=order.getActualEndDate();
		//获取开始时间和结束时间的时间戳
		String begin=new Long(beginDate.getTime()).toString();
		String end=new Long(endDate.getTime()).toString();
		//拼接获取轨迹序列的url请求
		StringBuffer rawTrackUrl=new StringBuffer();
		rawTrackUrl.append("http://api.capcare.com.cn:1045/api/get.track.do?device_sn=");
		rawTrackUrl.append(device_sn);
		rawTrackUrl.append("&begin=");
		rawTrackUrl.append(begin);
		rawTrackUrl.append("&end=");
		rawTrackUrl.append(end);
		rawTrackUrl.append("&page_number=-1&page_size=-1&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD");
		//得到的json格式字符串
		String rawTrackJson = HttpMethod.get(rawTrackUrl.toString());
		
		//存储轨迹序列的所有时间点
		List<Date> allDates=new ArrayList<Date>();
		//存储轨迹序列的所有位置信息   经度,纬度
		List<String> allLocations=new ArrayList<String>();
		//存放选中的时间序列和相应的location序列，大于trackLenth个则均匀选取，否则直接全选
		List<Date> selectedDates=new ArrayList<Date>();
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
				String dateLong=obj.getString("receive");
				Date date=new Date(Long.parseLong(dateLong));
				String lng=obj.getString("lng");
				String lat=obj.getString("lat");
				String location=lng+","+lat;
				allDates.add(date);
				allLocations.add(location);
			}
			//获取轨迹的点数
			int num=allDates.size();
			//存放均匀分布的trackLenth个点，并保证起点和终点对应是一一对应的
			int[] indexArray=new int[trackLenth];
			if(num>9){
				//计算均匀分布的点的索引
				float rate=num/trackLenth;
				for(int i=1;i<=trackLenth;i++){
					if(i==1){
						indexArray[i-1]=0;
					}else if(i==trackLenth){
						indexArray[trackLenth-1]=num-1;
					}else{
						indexArray[i-1]=Math.round(rate*i)-1;   //这里没有直接取到最后一个值，不确定原因，先直接手动设置
					}
				}
				for(int i=0;i<trackLenth;i++){
					selectedDates.add(allDates.get(indexArray[i]));
					selectedLocations.add(allLocations.get(indexArray[i]));
				}
			}else if(num>=1&&num<=trackLenth){
				for(int i=0;i<num;i++){
					selectedDates.add(allDates.get(i));
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
		for(int i=0;i<baiduLocations.size();i++){
			addressUrl.append("http://api.map.baidu.com/geocoder/v2/?ak="+baiduMapAk+"&callback=renderReverse&location=");
		    addressUrl.append(baiduLocations.get(i));
		    addressUrl.append("&output=json");
		    //返回的数据不是标准的json格式,外部有一层renderReverse&&renderReverse()，需要手动去除
		    String addressJson=HttpMethod.get(addressUrl.toString());
		    //计算前缀的长度
		    int prefix="renderReverse&&renderReverse(".length();
		    String realAddressJson=addressJson.substring(prefix, addressJson.length()-1);
//		    System.out.println("result=");
//		    System.out.println(JSON.parseObject(realAddressJson).getJSONObject("result"));
		    JSONObject result=JSON.parseObject(realAddressJson).getJSONObject("result");
		    String address=result.getString("formatted_address") + "（" + result.getString("sematic_description")+ "）";
		    //生成address的list对象
		    addresses.add(address);
		}
		//存储轨迹序列的有序集合
		TreeMap<Date,String> trackAbstract=new TreeMap<Date,String>();
		//在解析addresses的过程中可能发生服务器错误,增加这个条件，在地址解析发生错误时，返回null
		if(selectedDates.size()==addresses.size()){
			for(int i=0;i<selectedDates.size();i++){
				trackAbstract.put(selectedDates.get(i), addresses.get(i));
			}
		}else{
			return null;
		}
		return trackAbstract;
	}

	public String getChargeModeString(ChargeModeEnum chargeMode) {
		System.out.println("in getChargeModeString in serviceImpl");
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
		System.out.println("in getOperationRecordTypeString in serviceImpl");
		switch(type){
		case MODIFY:
			return TextResolve.getText("order.OrderOperationTypeEnum.MODIFY");
		case END_POSTPONE:
			return TextResolve.getText("order.OrderOperationTypeEnum.END_POSTPONE");
		case RESCHEDULE:
			return TextResolve.getText("order.OrderOperationTypeEnum.RESCHEDULE");
		case CANCEL:
			return TextResolve.getText("order.OrderOperationTypeEnum.CANCEL");
		case MODIFY_SCHEDULE_FORM:
			return TextResolve.getText("order.OrderOperationTypeEnum.MODIFY_SCHEDULE_FORM");
		case EDIT_DRIVER_ACTION:
			return TextResolve.getText("order.OrderOperationTypeEnum.EDIT_DRIVER_ACTION");
		}
		return null;
	}
	
	public String getDriverActionStatusLabel(OrderStatusEnum status){
		System.out.println("in getDriverActionStatusLabel in serviceImpl");
		switch(status){
		case INQUEUE:
			return TextResolve.getText("order.OrderStatusEnum.INQUEUE");
		case SCHEDULED:
			return TextResolve.getText("order.OrderStatusEnum.SCHEDULED");
		case ACCEPTED:
			return TextResolve.getText("order.OrderStatusEnum.ACCEPTED");
		case BEGIN:
			return TextResolve.getText("order.OrderStatusEnum.BEGIN");
		case GETON:
			return TextResolve.getText("order.OrderStatusEnum.GETON");
		case GETOFF:
			return TextResolve.getText("order.OrderStatusEnum.GETOFF");
		case END:
			return TextResolve.getText("order.OrderStatusEnum.END");
		case PAYED:
			return TextResolve.getText("order.OrderStatusEnum.PAYED");
		case CANCELLED:
			return TextResolve.getText("order.OrderStatusEnum.CANCELLED");
		}
		return null;
	}
	
	public int isCarAndDriverAvailable(Order order, Car car, User driver){
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
					customer.setGender(true);
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
					customerOrganizationDao.save(customerOrganization);
					
					Customer customer=new Customer();
					customer.setName(customerName);
					customer.setGender(true);
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
		return OrderStatusEnum.getById(Integer.valueOf(id.split("-")[1]));
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
		case PAYED:
			break;
		case END:
			DriverActionVO davo=new DriverActionVO();
			davo.setId(getDriverActionVOId(order,OrderStatusEnum.END,null));
			davo.setDate(order.getActualEndDate());
			actionList.add(0,davo);
		case GETOFF:
		case GETON:
			for(int n=order.getDayDetails().size()-1;n>=0;n--){
				DayOrderDetail dod=order.getDayDetails().get(n);
				if(dod.getGetoffDate()!=null){
					davo=new DriverActionVO();
					davo.setId(getDriverActionVOId(order,OrderStatusEnum.GETOFF,dod));
					davo.setDate(dod.getGetoffDate());
					actionList.add(0,davo);
				}
				if(dod.getGetonDate()!=null){
					davo=new DriverActionVO();
					davo.setId(getDriverActionVOId(order,OrderStatusEnum.GETON,dod));
					davo.setDate(dod.getGetonDate());
					actionList.add(0,davo);
				}
			}
		case BEGIN:
			davo=new DriverActionVO();
			davo.setId(getDriverActionVOId(order,OrderStatusEnum.BEGIN,null));
			davo.setDate(order.getActualBeginDate());
			actionList.add(0,davo);
		case ACCEPTED:
			davo=new DriverActionVO();
			davo.setId(getDriverActionVOId(order,OrderStatusEnum.ACCEPTED,null));
			davo.setDate(order.getAcceptedTime());
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
	@Transactional
	public boolean canEditOrderBill(Order order){
		return order.getStatus()==OrderStatusEnum.END;
	}
	
	@Transactional
	public void editOrderBill(Order order){
		
	}
}
