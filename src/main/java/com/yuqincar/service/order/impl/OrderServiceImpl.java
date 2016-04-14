/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.yuqincar.service.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.yuqincar.dao.order.OrderDao;
import com.yuqincar.dao.order.OrderOperationRecordDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.domain.order.Address;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderOperationRecord;
import com.yuqincar.domain.order.OrderOperationTypeEnum;
import com.yuqincar.domain.order.OrderSourceEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.order.OtherPassenger;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.app.APPMessageService;
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
	private WatchKeeperService watchKeeperService;
	
	@Autowired
	private SMSService smsService;
	
	@Autowired
	private APPMessageService appMessageService;

	public List<CarServiceType> getAllCarServiceType() {
		return orderDao.getAllCarServiceType();
	}

	public BigDecimal calculateOrderMoney(CarServiceType serviceType,
			ChargeModeEnum chargeMode, double mile, int days) {
		System.out.println("in calculateOrderMoney");
		return orderDao
				.calculateOrderMoney(serviceType, chargeMode, mile, days);
	}

	@Transactional
	public void EnQueue(Order order,List<Address> addresses, String baseSN,int copyNumber) {
		//处理是否新建客户单位和客户		
		List<BaseEntity> cc=dealCCP(order.getCustomerOrganization().getName(),order.getCustomer().getName(),order.getPhone());
		order.setCustomerOrganization((CustomerOrganization)cc.get(0));
		order.setCustomer((Customer)cc.get(1));
		
		//保存地址
		generateAddress(order,addresses);
		
		orderDao.EnQueue(order,baseSN);
		
		if(copyNumber>0)
			copyOrderScheduled(order,copyNumber);
		
		if(watchKeeperService.getWatchKeeper().isOnDuty()){
			List<String> sList=new ArrayList<String>(10);
			sList.add(order.getSn());
			sList.add(order.getCustomerOrganization().getName());
			sList.add(order.getCustomer().getName());
			sList.add(order.getPhone());
			sList.add(getChargeModeString(order.getChargeMode()));
			sList.add(String.valueOf(order.getPassengerNumber()));
			sList.add(DateUtils.getYMDHMString(order.getPlanBeginDate()));
			if(order.getChargeMode()==ChargeModeEnum.MILE)
				sList.add("<空>");
			else
				sList.add(DateUtils.getYMDHMString(order.getPlanEndDate()));
			sList.add(order.getFromAddress().getDescription()+"（"+order.getFromAddress().getDetail()+"）");
			if(order.getChargeMode()==ChargeModeEnum.MILE)
				sList.add(order.getToAddress().getDescription()+"（"+order.getToAddress().getDetail()+"）");
			else
				sList.add("<空>");
			
			StringBuffer sb=new StringBuffer();
			sb.append("有新订单入队列。").append(order.getCustomer().getName()).append("，").append(order.getChargeMode().toString())
				.append("，上车时间：").append(DateUtils.getYMDHMString(order.getPlanBeginDate()));
			appMessageService.sendMessageToSchedulerAPP(watchKeeperService.getWatchKeeper().getKeeper(), sb.toString(),null);
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
			o.setPassengerNumber(order.getPassengerNumber());
			o.setPhone(order.getPhone());
			o.setOrderMoney(order.getOrderMoney());
			o.setStatus(order.getStatus());
			List<Address> addresses=new ArrayList<Address>(2);
			addresses.add(order.getFromAddress());
			addresses.add(order.getToAddress());
			o.setOrderMile(order.getOrderMile());
			o.setMemo(o.getMemo());
			o.setCreateTime(new Date());
			o.setCallForOther(order.isCallForOther());
			o.setOtherPassengerName(order.getOtherPassengerName());
			o.setOtherPhoneNumber(order.getOtherPhoneNumber());
			o.setCallForOtherSendSMS(order.isCallForOtherSendSMS());
			o.setOrderSource(order.getOrderSource());
			EnQueue(o,addresses,baseSN,0);
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
	
	private void saveCustomerAddress(Order order){
		if (order.getCustomer().getAddresses() == null
				|| order.getCustomer().getAddresses().size() == 0) {
			order.getCustomer().setAddresses(new ArrayList<Address>());
			order.getCustomer().getAddresses().add(order.getFromAddress());
			if(order.getChargeMode()==ChargeModeEnum.MILE)
				order.getCustomer().getAddresses().add(order.getToAddress());
			customerDao.update(order.getCustomer());
		} else {
			boolean exist=false;
			for (Address uadd : order.getCustomer().getAddresses()) {
				if(isAddressEquals(uadd,order.getFromAddress())){
					exist=true;
					break;
				}
			}
			if(!exist){
				order.getCustomer().getAddresses().add(order.getFromAddress());
				customerDao.update(order.getCustomer());
			}
			if(order.getChargeMode()==ChargeModeEnum.MILE){
				exist=false;
				for (Address uadd : order.getCustomer().getAddresses()) {
					if(isAddressEquals(uadd,order.getToAddress())){
						exist=true;
						break;
					}
				}
				if(!exist){
					order.getCustomer().getAddresses().add(order.getToAddress());
					customerDao.update(order.getCustomer());
				}
			}
		}
	}
		
	private void generateAddress(Order order,List<Address> addresses){
		Address ad=addressDao.getEqualAddress(addresses.get(0));
		if(ad!=null)
			order.setFromAddress(ad);
		else{
			locationDao.save(addresses.get(0).getLocation());
			addressDao.save(addresses.get(0));
			order.setFromAddress(addresses.get(0));
		}
		if(order.getChargeMode()==ChargeModeEnum.MILE){
			ad=addressDao.getEqualAddress(addresses.get(1));
			if(ad!=null)
				order.setToAddress(ad);
			else{
				locationDao.save(addresses.get(1).getLocation());
				addressDao.save(addresses.get(1));
				order.setToAddress(addresses.get(1));
			}
		}
	}
	
	@Transactional
	public int scheduleOrder(String scheduleMode,Order order, String organizationName, String customerName, List<Address> addresses, Car car,int copyNumber,Order toUpdateOrder,User user) {
		//处理是否新建客户单位和客户
		List<BaseEntity> cc=dealCCP(organizationName,customerName,order.getPhone());
		order.setCustomerOrganization((CustomerOrganization)cc.get(0));
		order.setCustomer((Customer)cc.get(1));

		//保存地址
		generateAddress(order,addresses);
			
		int result= orderDao.scheduleOrder(scheduleMode,order, car, user);
		if(result==0){
			//复制订单
			if(copyNumber>0)
				copyOrderScheduled(order,copyNumber);
			//保存客户常用地址
			saveCustomerAddress(order);
			if(order.isCallForOther())
				saveOtherPassenger(order);
			if(scheduleMode==OrderService.SCHEDULE_FROM_NEW || scheduleMode==OrderService.SCHEDULE_FROM_QUEUE)
				appMessageService.sendMessageToDriverAPP(order.getDriver(), "你有新的订单。上车时间："+DateUtils.getYMDHMString(order.getPlanBeginDate())
						+ "；上车地点："+order.getFromAddress().getDescription(),null);
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
				
		if(order.getPassengerNumber()!=toUpdateOrder.getPassengerNumber())
			sb.append("(").append(++n).append(")").append("乘车人数由：")
				.append(toUpdateOrder.getPassengerNumber())
				.append(" 改为 ").append(order.getPassengerNumber()).append("；");
		
		if(!order.getServiceType().equals(toUpdateOrder.getServiceType()))
			sb.append("(").append(++n).append(")").append("车型由：")
				.append(toUpdateOrder.getServiceType().getTitle())
				.append(" 改为 ").append(order.getServiceType().getTitle()).append("；");
		
		if(!isAddressEquals(order.getFromAddress(),toUpdateOrder.getFromAddress()))
			sb.append("(").append(++n).append(")").append("上车地点由：")
				.append(toUpdateOrder.getFromAddress().getDescription())
				.append("（").append(toUpdateOrder.getFromAddress().getDetail()).append("）")
				.append(" 改为 ").append(order.getFromAddress().getDescription())
				.append("（").append(order.getFromAddress().getDetail()).append("）").append("；");
		
		if(order.getToAddress()==null){
			if(toUpdateOrder.getToAddress()!=null)
				sb.append("(").append(++n).append(")").append("删除了下车地点").append("；");
		}else{
			if(toUpdateOrder.getToAddress()!=null && !isAddressEquals(order.getToAddress(),toUpdateOrder.getToAddress()))
				sb.append("(").append(++n).append(")").append("下车地点由：")
					.append(toUpdateOrder.getToAddress().getDescription())
					.append("（").append(toUpdateOrder.getToAddress().getDetail()).append("）")
					.append(" 改为 ").append(order.getToAddress().getDescription())
					.append("（").append(order.getToAddress().getDetail()).append("）").append("；");
			else if(toUpdateOrder.getToAddress()==null)
				sb.append("(").append(++n).append(")").append("新增下车地点：")
					.append(order.getToAddress().getDescription())
					.append("（").append(order.getToAddress().getDetail()).append("）").append("；");
		}
		
		if(!order.getCar().equals(toUpdateOrder.getCar()))
			sb.append("(").append(++n).append(")").append("车辆由：")
				.append(toUpdateOrder.getCar().getPlateNumber()).append("（")
				.append(toUpdateOrder.getCar().getDriver().getName()).append("）")
				.append(" 改为 ").append(order.getCar().getPlateNumber()).append("（")
				.append(order.getCar().getDriver().getName()).append("）").append("；");
		
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
						+ "；上车地点："+order.getFromAddress().getDescription(),null);
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

	@Transactional
	public void modifyOrderMile(Order order, float newMile, User user){
		order.setOrderMile(newMile);
		order.setOrderMoney(calculateOrderMoney(order.getServiceType(),order.getChargeMode(),newMile,0));
		orderDao.update(order);
		
		if(!new BigDecimal(order.getOrderMile()).setScale(1, BigDecimal.ROUND_HALF_UP)
				.equals(new BigDecimal(newMile).setScale(1, BigDecimal.ROUND_HALF_UP))){//如果没有改动值，那么就不添加操作记录。
			OrderOperationRecord oor=new OrderOperationRecord();
			oor.setOrder(order);
			oor.setType(OrderOperationTypeEnum.MODIFY_MILE);
			oor.setDate(new Date());
			oor.setUser(user);
			oor.setDescription("设置订单里程为："+new BigDecimal(newMile).setScale(1, BigDecimal.ROUND_HALF_UP).toString());
			orderOperationRecordDao.save(oor);
		}
	}

	@Transactional
	public void modifyOrderMoney(Order order, BigDecimal orderMoney, User user){
		order.setOrderMoney(orderMoney);
		orderDao.update(order);
		
		if(!order.getOrderMoney().setScale(1, BigDecimal.ROUND_HALF_UP)
				.equals(orderMoney.setScale(1, BigDecimal.ROUND_HALF_UP))){//如果没有改动值，那么就不添加操作记录。
			OrderOperationRecord oor=new OrderOperationRecord();
			oor.setOrder(order);
			oor.setType(OrderOperationTypeEnum.MODIFY_MONEY);
			oor.setDate(new Date());
			oor.setUser(user);
			oor.setDescription("设置订单金额为："+orderMoney.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
			orderOperationRecordDao.save(oor);
		}
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
		if(isCarAvailable(order,order.getCar())!=0){
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
	public int orderReschedule(Order order, Car car, User user, String description){
		if(!canOrderReschedule(order))
			return 2;
		Date temp=order.getPlanBeginDate();
		order.setPlanBeginDate(new Date());
		if(isCarAvailable(order,car)!=0){
			order.setPlanBeginDate(temp);
			return 1;
		}
		order.setPlanBeginDate(temp);
		Car tempCar=order.getCar();
		order.setCar(car);
		order.setDriver(car.getDriver());
		orderDao.update(order);
		
		OrderOperationRecord oor=new OrderOperationRecord();
		oor.setOrder(order);
		oor.setType(OrderOperationTypeEnum.RESCHEDULE);
		oor.setDate(new Date());
		oor.setUser(user);
		oor.setDescription("将车辆由 "+ tempCar.getPlateNumber()+"（"+tempCar.getDriver().getName()+"）" 
				+" 重新调度为 "+order.getCar().getPlateNumber()+"（"+order.getCar().getDriver().getName()
				+"） 原因："+description);
		orderOperationRecordDao.save(oor);
		
		appMessageService.sendMessageToDriverAPP(tempCar.getDriver(), "订单（"+order.getSn()+"）被重新调度给"+order.getCar().getDriver().getName(),null);
		appMessageService.sendMessageToDriverAPP(order.getDriver(), "订单（"+order.getSn()+"）从"+tempCar.getDriver().getName()+"重新调度给了你。",null);
		
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
			Location location, Date planBeginDate, Date planEndDate, int pageNum) {
		return orderDao.getRecommandedCar(serviceType, chargeMode,location, planBeginDate,
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
	 * 计算两个位置的驾车路线最短距离
	 * @param l1 起点
	 * @param l2 终点
	 * @return
	 */
	public double calculatePathDistance(Location l1, Location l2) {
		String origins=new Double(l1.getLatitude()).toString()+","+new Double(l1.getLongitude()).toString();
		String destinations=new Double(l2.getLatitude()).toString()+","+new Double(l2.getLongitude()).toString();
		
		StringBuffer url=new StringBuffer();
		url.append("http://api.map.baidu.com/direction/v1/routematrix?output=json&origins=");
		url.append(origins);
		url.append("&&destinations=");
		url.append(destinations);
		url.append("&ak=XNcVScWmj4gRZeSvzIyWQ5TZ");
		
		String json = HttpMethod.get(url.toString());
		JSONObject distanceObj=(JSONObject) JSON.parseObject(json).getJSONObject("result").getJSONArray("elements").get(0);
		String value=distanceObj.getJSONObject("distance").getString("value");
		
		double distance=Math.round(Double.parseDouble(value)/100)/10.0;
		
		return distance;
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
		    String address=JSON.parseObject(realAddressJson).getJSONObject("result").getString("sematic_description");
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
		case MODIFY_MILE:
			return TextResolve.getText("order.OrderOperationTypeEnum.MODIFY_MILE");
		case MODIFY_MONEY:
			return TextResolve.getText("order.OrderOperationTypeEnum.MODIFY_MONEY");
		}
		return null;
	}
	
	public int isCarAvailable(Order order, Car car){
		return orderDao.isCarAvailable(order, car);
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
	
	public double estimateMileage(Car car, Location from,Location to){
		double mileage=0;
		//业务点前往接客的距离
		ServicePoint servicePoint=null;
		if(car!=null)
			servicePoint=car.getServicePoint();
		else
			servicePoint=orderDao.getAscendingDirectNearestServicePoints(from).get(0);
		mileage+=calculatePathDistance(servicePoint.getPointAddress().getLocation(),from);
		
		//送客的距离
		mileage+=calculatePathDistance(from,to);
		
		//送客结束后回到业务点的距离
		mileage+=calculatePathDistance(to,servicePoint.getPointAddress().getLocation());
		
		return mileage;
	}
}
