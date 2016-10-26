/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yuqincar.action.schedule;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.dao.order.CarServiceSuperTypeDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderSourceEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.order.OtherPassenger;
import com.yuqincar.domain.order.Price;
import com.yuqincar.domain.order.PriceTable;
import com.yuqincar.domain.order.WatchKeeper;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.customer.CustomerService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.order.PriceService;
import com.yuqincar.service.order.WatchKeeperService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class ScheduleAction extends BaseAction {
	@Autowired
	private OrderService orderService;
	@Autowired
	private CarService carService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerOrganizationService customerOrganizationService;
	@Autowired
	private WatchKeeperService watchKeeperService;
	@Autowired
	private UserService userService;
	@Autowired
	CarServiceSuperTypeDao carServiceSuperTypeDao;
	@Autowired
	PriceService priceSerivce;
	
	private User keeper;
	private String keyWord;
	private String customerOrganizationName;
	private String customerName;
	private String phone;
	private String chargeMode;
	private String planBeginDate;
	private String planEndDate;
	private CarServiceType serviceType;
	private String fromAddress;
	private String toAddress;
	private String memo;
	private String needCopy;
	private int copyNumber;
	private long cancelOrderId;
	private String scheduleMode;  //新建时为null，从队列调度为FROM_QUEUE,修改订单为FROM_UPDATE
	private long scheduleFromQueueOrderId;
	private long scheduleFromUpdateOrderId;
	private Car queryCar;
	private Car selectedCar;
	private User selectedDriver;
	private boolean onDuty;
	private String callForOther;
	private String otherPassengerName;
	private String otherPhoneNumber;
	private String callForOtherSendSMS;
	private User saler;
	private BigDecimal protocolMoney;
	
	
	public void getCar() {
		QueryHelper helper = new QueryHelper(Car.class, "c");
		helper.addWhereCondition("c.status=? and c.plateNumber=?", CarStatusEnum.NORMAL,queryCar.getPlateNumber());
		List<Car> cars = carService.queryCar(1, helper).getRecordList();
		System.out.println("cars.size="+cars.size());
		for(Car car:cars)
			System.out.println("car="+car.getPlateNumber());
		Date beginDate_,endDate_;
		if(getChargeMode(chargeMode)==ChargeModeEnum.DAY || getChargeMode(chargeMode)==ChargeModeEnum.PROTOCOL){
			beginDate_=DateUtils.getMinDate(DateUtils.getYMD(planBeginDate));
			endDate_ = DateUtils.getMaxDate(DateUtils.getYMD(planEndDate));
		}else{
			beginDate_=DateUtils.getMinDate(DateUtils.getYMDHM(planBeginDate));
			endDate_ = DateUtils.getMaxDate(DateUtils.getOffsetDate(beginDate_,
					4));
		}
		System.out.println("beginDate="+beginDate_);
		System.out.println("endDate="+endDate_);
		// 每一个car保存对应5天的状态信息
		List<LinkedHashMap<myCar, LinkedHashMap<String, Integer>>> carStatus = new ArrayList<LinkedHashMap<myCar, LinkedHashMap<String, Integer>>>();
		LinkedHashMap<myCar, LinkedHashMap<String, Integer>> teMap = null;
		LinkedHashMap<String, Integer> carUseInfoNum = null;
		List<myCar> myCars = new ArrayList<myCar>();
		myCar myCar = null;
		for (Car car : cars) {
			myCar = new myCar();
			teMap = new LinkedHashMap<myCar, LinkedHashMap<String, Integer>>();
			carUseInfoNum = new LinkedHashMap<String, Integer>();
			List<List<Order>> carUseInfo = orderService.getCarTask(car,
					beginDate_, endDate_);
			int i = 0;
			for (List<Order> dayList : carUseInfo) {
				if(dayList!=null && dayList.size()>0){
					if(dayList.get(0)!=null){
						boolean alldone=true;
						for(Order order : dayList){
							if(order!=null && (order.getStatus()==OrderStatusEnum.SCHEDULED ||
									order.getStatus()==OrderStatusEnum.ACCEPTED ||
									order.getStatus()==OrderStatusEnum.BEGIN ||
									order.getStatus()==OrderStatusEnum.GETON ||
									order.getStatus()==OrderStatusEnum.GETOFF)){
								alldone=false;
								break;
							}
								
						}
						//如果有任务，且存在没有完成和订单，那么就应该在界面上显示“有任务”（0），否则应该显示空闲（非0）。
						if(!alldone)							
							carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate_, i++)), 0);
						else
							carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate_, i++)), 5);
					}else
						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate_, i++)), 1);
				}else{
					carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
							.getOffsetDate(beginDate_, i++)), 4);
				}
			}
			myCar.setId(car.getId().toString());
			myCar.setCarNumber(car.getPlateNumber());
			if(car.getDriver()!=null){
				myCar.setDriverName(car.getDriver().getName());
				myCar.setDriverId(String.valueOf(car.getDriver().getId()));
				myCar.setPhone(car.getDriver().getPhoneNumber());
			}
			myCar.setCarInfo(carUseInfoNum);
			myCars.add(myCar);
		}
		writeJson(JSONArray.toJSONString(myCars));
	}

	public void getCustomerOrganization() {
		System.out.println("in getCustomerOrganization");
		System.out.println("keyWord="+keyWord);
		
		List<Object> list = new ArrayList<Object>();
		for (CustomerOrganization co : customerOrganizationService
				.queryCustomerOrganizationByKeyword(keyWord).getRecordList())
			list.add(co.getName());
		System.out.println("list.size()="+list.size());
		JSONArray jsonArray = new JSONArray(list);
		writeJson(jsonArray.toJSONString());
	}

	public void getCustomer() {
		System.out.println("in getCustomer");
		System.out.println("customerOrganization="+customerOrganizationName);
		System.out.println("keyWord="+keyWord);
		QueryHelper helper = new QueryHelper(Customer.class, "c");
		helper.addWhereCondition(
				"c.customerOrganization.name=? and c.name like ?",
				customerOrganizationName, "%" + keyWord + "%");
		List<Object> list = new ArrayList<Object>();
		for (Customer customer : customerService.queryCustomer(1, helper)
				.getRecordList()) {
			list.add(customer.getName());
		}
		JSONArray jsonArray = new JSONArray(list);
		writeJson(jsonArray.toJSONString());
	}

	public void getPhoneNumber() {
		QueryHelper helper = new QueryHelper(Customer.class, "c");
		helper.addWhereCondition("c.customerOrganization.name=? and c.name=?",
				customerOrganizationName, customerName);
		List<Customer> customerList = (List<Customer>) customerService
				.queryCustomer(1, helper).getRecordList();
		List<Object> list = new ArrayList<Object>();
		for (Customer customer : customerList) {
			for (String phoneNumber : customer.getPhones()) {
				list.add(phoneNumber);
			}
		}
		JSONArray jsonArray = new JSONArray(list);
		writeJson(jsonArray.toJSONString());
	}
	
	public void getOtherPassengers(){
		System.out.println("in getOtherPassengers");
		System.out.println("phone="+phone);
		Customer customer=customerService.getCustomerByPhoneNumber(phone);
		if(customer!=null){
			List<Object> list = new ArrayList<Object>();
			for(OtherPassenger op:customer.getOtherPassengers())
				list.add(op.getName());
			JSONArray jsonArray = new JSONArray(list);
			writeJson(jsonArray.toJSONString());
		}
	}
	
	public void getOtherPhoneNumbers(){
		System.out.println("in getOtherPhoneNumbers");
		System.out.println("phone="+phone);
		Customer customer=customerService.getCustomerByPhoneNumber(phone);
		if(customer!=null){
			List<Object> list = new ArrayList<Object>();
			for(OtherPassenger op:customer.getOtherPassengers())
				list.add(op.getPhoneNumber());
			JSONArray jsonArray = new JSONArray(list);
			writeJson(jsonArray.toJSONString());
		}
	}

	public String getChargeModeString() {
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.getChargeModeString(order.getChargeMode());
	}
	
	public String getPlanBeginDateString(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		if(order.getChargeMode()==ChargeModeEnum.DAY || order.getChargeMode()==ChargeModeEnum.PROTOCOL)
			return DateUtils.getYMDString(order.getPlanBeginDate());
		else
			return DateUtils.getYMDHMString(order.getPlanBeginDate());
	}

	public String inQueue() {
		System.out.println("in inQueue");
		System.out.println("i am coming here");
		Order order = new Order();
		CustomerOrganization customerOrganization = null;
		Customer customer = null;
		ChargeModeEnum chargeModeEnum = null;
		customerOrganization=new CustomerOrganization();
		customerOrganization.setName(customerOrganizationName);
		order.setCustomerOrganization(customerOrganization);	//到Service层去处理是否新建客户单位
		customer=new Customer();
		customer.setName(customerName);
		order.setCustomer(customer);							//到Service层去处理是否新建客户
		if(callForOther!=null && callForOther.equals("on")){
			order.setCallForOther(true);
			order.setOtherPassengerName(otherPassengerName);
			order.setOtherPhoneNumber(otherPhoneNumber);
			if(callForOtherSendSMS!=null && callForOtherSendSMS.equals("on"))
				order.setCallForOtherSendSMS(true);
			else
				order.setCallForOtherSendSMS(false);	
		}else
			order.setCallForOther(false);
		
		chargeModeEnum = getChargeMode(chargeMode);
		order.setChargeMode(chargeModeEnum);
		
		if(order.getChargeMode()==ChargeModeEnum.DAY || order.getChargeMode()==ChargeModeEnum.PROTOCOL){
			order.setPlanBeginDate(DateUtils.getYMD(planBeginDate));
			order.setPlanEndDate(DateUtils.getYMD(planEndDate));
		}else
			order.setPlanBeginDate(DateUtils.getYMDHM(planBeginDate));
		
		order.setFromAddress(fromAddress);
		if(order.getChargeMode()==ChargeModeEnum.MILE || order.getChargeMode()==ChargeModeEnum.PLANE)
			order.setToAddress(toAddress);
		if(saler!=null)
			order.setSaler(saler);
		if(order.getChargeMode()==ChargeModeEnum.PROTOCOL)
			order.setOrderMoney(protocolMoney);
		else
			order.setOrderMoney(new BigDecimal(0));
		order.setServiceType(serviceType);
		order.setPhone(phone);
		order.setStatus(OrderStatusEnum.INQUEUE);
		order.setMemo(memo);
		order.setOrderSource(OrderSourceEnum.SCHEDULER);
		System.out.println("needCopy="+needCopy);
		if(needCopy==null || needCopy.equals("off"))
			copyNumber=0;
		orderService.EnQueue(order,null,copyNumber);
		return "success";
	}

	private ChargeModeEnum getChargeMode(String chargeMode) {
		if (chargeMode.equals("filter_mileage")) {
			return ChargeModeEnum.MILE;
		} else if (chargeMode.equals("filter_days")) {
			return ChargeModeEnum.DAY;
		} else if(chargeMode.equals("filter_protocol")) {
			return ChargeModeEnum.PROTOCOL;
		}else if(chargeMode.equals("filter_plane")) {
			return ChargeModeEnum.PLANE;
		}
		return null;
	}
	
	private String getChargeModeText(ChargeModeEnum chargeMode) {
		switch(chargeMode){
			case MILE:
				return "filter_mileage";
			case DAY:
				return "filter_days";
			case PROTOCOL:
				return "filter_protocol";
			case PLANE:
				return "filter_plane";
		}
		return "";
	}
	
	public void isCarAndDriverAvailable(){
		Order order=new Order();
		if(scheduleFromUpdateOrderId>0)	//修改订单。
			order.setId(scheduleFromUpdateOrderId);   //修改订单时，设置id的目的是为了在查询数据库时，把本订单排除在外。
		order.setChargeMode(getChargeMode(chargeMode));
		if(order.getChargeMode()==ChargeModeEnum.DAY || order.getChargeMode()==ChargeModeEnum.PROTOCOL){
			order.setPlanBeginDate(DateUtils.getYMD(planBeginDate));
			order.setPlanEndDate(DateUtils.getYMD(planEndDate));
		}else
			order.setPlanBeginDate(DateUtils.getYMDHM(planBeginDate));
		order.setServiceType(serviceType);
		int result=orderService.isCarAndDriverAvailable(order, selectedCar,selectedDriver);
		//System.out.println("resutl="+result);
		writeJson("{\"result\":" + result + "}");
	}
	
	public void initInputField(Order order){
		customerOrganizationName=order.getCustomerOrganization().getName();
		customerName=order.getCustomer().getName();
		phone=order.getPhone();
		callForOther=order.isCallForOther() ? "on" : "off";
		otherPassengerName=order.getOtherPassengerName();
		otherPhoneNumber=order.getOtherPhoneNumber();
		callForOtherSendSMS=order.isCallForOtherSendSMS() ? "on" : "off";
		chargeMode=getChargeModeText(order.getChargeMode());
		planBeginDate=DateUtils.getYMDHMString(order.getPlanBeginDate());
		if(order.getChargeMode()==ChargeModeEnum.DAY || order.getChargeMode()==ChargeModeEnum.PROTOCOL)
			planEndDate=DateUtils.getYMDHMString(order.getPlanEndDate());
		fromAddress=order.getFromAddress();
		if(order.getChargeMode()==ChargeModeEnum.MILE || order.getChargeMode()==ChargeModeEnum.PLANE)
			toAddress=order.getToAddress();
		if(order.getSaler()!=null)
			saler=order.getSaler();
		if(order.getChargeMode()==ChargeModeEnum.PROTOCOL)
			protocolMoney=order.getOrderMoney();
		serviceType=order.getServiceType();
		memo=order.getMemo();
	}
	
	public String scheduleFromQueue(){
		System.out.println("scheduleFromQueueOrderId="+scheduleFromQueueOrderId);
		Order order=orderService.getOrderById(scheduleFromQueueOrderId);
		order=orderService.distributeOrder((User)ActionContext.getContext().getSession().get("user"), order);
		scheduleFromQueueOrderId=order.getId();
		scheduleMode=OrderService.SCHEDULE_FROM_QUEUE;
		initInputField(order);
		return "scheduling";
	}

	public boolean isCanDistributeOrderToUser(){
		return orderService.canDistributeOrderToUser((User)ActionContext.getContext().getSession().get("user"));
	}

	public String dispatchOrder(){
		System.out.println("in dispatchOrder");
		Order order=orderService.distributeOrder((User)ActionContext.getContext().getSession().get("user"),null);
		if(order!=null){
			scheduleFromQueueOrderId=order.getId();
			scheduleMode=OrderService.SCHEDULE_FROM_QUEUE;
			initInputField(order);
			return "scheduling";
		}else{
			return queue();
		}
	}
	
	public String updateOrder(){
		System.out.println("in updateOrder");
		System.out.println("scheduleFromUpdateOrderId="+scheduleFromUpdateOrderId);
		Order order=orderService.getOrderById(scheduleFromUpdateOrderId);
		System.out.println(order);
		scheduleMode=OrderService.SCHEDULE_FROM_UPDATE;
		initInputField(order);	
		if(!(order.getChargeMode()==ChargeModeEnum.PROTOCOL && order.getCar()==null))
			selectedCar=order.getCar();
		if(!(order.getChargeMode()==ChargeModeEnum.PROTOCOL && order.getDriver()==null))
			selectedDriver=order.getDriver();
		return "scheduling";
	}
	
	public void isOrderDeprived(){
		Order order=orderService.getOrderById(scheduleFromQueueOrderId);
		if(order.getStatus()==OrderStatusEnum.INQUEUE && order.isScheduling() 
				&& order.getScheduler().equals((User)ActionContext.getContext().getSession().get("user")))
			writeJson("{\"result\":\"false\"}");
		else
			writeJson("{\"result\":\"true\"}");
	}
	
	public String showDeprived(){
		return "deprived";
	}
	
	public boolean isFromUpdate(){
		return OrderService.SCHEDULE_FROM_UPDATE.equals(scheduleMode);
	}

	public String startSchedule() {
		Order order=null;
		Order toUpdateOrder=null;	//为了记录改动数据项而把修改之前的Order记录下来。
		if(scheduleMode==null || scheduleMode.isEmpty())
			order=new Order();
		else if(scheduleMode.equals(OrderService.SCHEDULE_FROM_QUEUE)){
			System.out.println("scheduleMode="+scheduleMode);
			System.out.println("scheduleFromQueueOrderId="+scheduleFromQueueOrderId);
			order=orderService.getOrderById(scheduleFromQueueOrderId);
		}else if(scheduleMode.equals(OrderService.SCHEDULE_FROM_UPDATE)){
			System.out.println("scheduleMode="+scheduleMode);
			System.out.println("scheduleFromUpdateOrderId="+scheduleFromUpdateOrderId);
			order=orderService.getOrderById(scheduleFromUpdateOrderId);
			toUpdateOrder=new Order();
			toUpdateOrder.setCustomerOrganization(order.getCustomerOrganization());
			toUpdateOrder.setCustomer(order.getCustomer());
			toUpdateOrder.setPhone(order.getPhone());
			toUpdateOrder.setChargeMode(order.getChargeMode());
			toUpdateOrder.setPlanBeginDate(order.getPlanBeginDate());
			toUpdateOrder.setPlanEndDate(order.getPlanEndDate());
			toUpdateOrder.setServiceType(order.getServiceType());
			toUpdateOrder.setFromAddress(order.getFromAddress());
			toUpdateOrder.setToAddress(order.getToAddress());
			toUpdateOrder.setSaler(order.getSaler());
			toUpdateOrder.setCar(order.getCar());
			toUpdateOrder.setDriver(order.getDriver());
			if(order.isCallForOther()){
				toUpdateOrder.setCallForOther(true);
				toUpdateOrder.setOtherPassengerName(order.getOtherPassengerName());
				toUpdateOrder.setOtherPhoneNumber(order.getOtherPhoneNumber());
				toUpdateOrder.setCallForOtherSendSMS(order.isCallForOtherSendSMS());
			}else
				toUpdateOrder.setCallForOther(false);
		}
		ChargeModeEnum chargeModeEnum = null;
		chargeModeEnum = getChargeMode(chargeMode);
		order.setChargeMode(chargeModeEnum);
		
		order.setFromAddress(fromAddress);
		if(order.getChargeMode()==ChargeModeEnum.DAY || order.getChargeMode()==ChargeModeEnum.PROTOCOL){
			order.setPlanBeginDate(DateUtils.getYMD(planBeginDate));
			order.setPlanEndDate(DateUtils.getYMD(planEndDate));
		}else{
			order.setToAddress(toAddress);
			order.setPlanBeginDate(DateUtils.getYMDHM(planBeginDate));
		}
		if(saler!=null)
			order.setSaler(saler);
		
		if(order.getChargeMode()==ChargeModeEnum.PROTOCOL){
			order.setOrderMoney(protocolMoney);
			order.setActualMoney(protocolMoney);
		}else{
			if(order.getOrderMoney()==null)
				order.setOrderMoney(new BigDecimal(0));
			if(order.getActualMoney()==null)
				order.setActualMoney(new BigDecimal(0));
		}
		
		order.setServiceType(serviceType);
		order.setPhone(phone);
		
		order.setMemo(memo);
		if(needCopy==null || needCopy.isEmpty() || needCopy.equals("off"))
			copyNumber=0;
		
		if(callForOther!=null && callForOther.equals("on")){
			order.setCallForOther(true);
			order.setOtherPassengerName(otherPassengerName);
			order.setOtherPhoneNumber(otherPhoneNumber);
			if(callForOtherSendSMS!=null && callForOtherSendSMS.equals("on"))
				order.setCallForOtherSendSMS(true);
			else
				order.setCallForOtherSendSMS(false);	
		}else{
			order.setCallForOther(false);
			order.setOtherPassengerName(null);
			order.setOtherPhoneNumber(null);
			order.setCallForOtherSendSMS(false);
		}

		int result = 0;
		String str=null;
		if(scheduleMode==null || scheduleMode.isEmpty()){
			str=OrderService.SCHEDULE_FROM_NEW;
			order.setOrderSource(OrderSourceEnum.SCHEDULER);
		}else if(scheduleMode.equals(OrderService.SCHEDULE_FROM_QUEUE))
			str=OrderService.SCHEDULE_FROM_QUEUE;
		else if(scheduleMode.equals(OrderService.SCHEDULE_FROM_UPDATE))
			str=OrderService.SCHEDULE_FROM_UPDATE;
		User user=(User)ActionContext.getContext().getSession().get("user");
		result = orderService.scheduleOrder(str, order,customerOrganizationName, customerName, selectedCar, selectedDriver, copyNumber,toUpdateOrder,user);
		if (result == 0){
			if(scheduleMode.equals(OrderService.SCHEDULE_FROM_UPDATE)){	
				return "orderView";
			}
			return "success";
		}
		else{
			if(result==1)
				addFieldError("scheduleError", "订单已经被调度 ");
			else if(result==2)
				addFieldError("scheduleError", "车辆已经被调度");
			else if(result==3)
				addFieldError("scheduleError", "车辆已报废");
			else if(result==4)
				addFieldError("scheduleError", "车辆在维修 ");
			else if(result==5)
				addFieldError("scheduleError", "车辆在年审");
			else if(result==6)
				addFieldError("scheduleError", "车辆在保养");
			else if(result==7)
				addFieldError("scheduleError", "车型不匹配");
			else if(result==8)
				addFieldError("scheduleError", "乘车人数超过限定");
			else if(result==9)
				addFieldError("scheduleError", "超过调度时间，被剥夺调度权");
			return "scheduling";
		}
	}

	public String scheduling() {
		Order order=orderService.getOrderDistributed((User)ActionContext.getContext().getSession().get("user"));
		if(order!=null){   //说明还有未过期的队列订单需要调度。
			scheduleFromQueueOrderId=order.getId();
			scheduleMode=OrderService.SCHEDULE_FROM_QUEUE;
			initInputField(order);
		}
		
		return "scheduling";
	}

	public void getRecommandDriver() {
		Date beginDate_=null;
		Date endDate_=null;
		if (getChargeMode(chargeMode) == ChargeModeEnum.MILE || getChargeMode(chargeMode) == ChargeModeEnum.PLANE) {
			beginDate_ = DateUtils.getYMDHM(planBeginDate);
			endDate_ = DateUtils.getOffsetDate(beginDate_, 4);
		} else {
			beginDate_ = DateUtils.getYMD(planBeginDate);
			endDate_ = DateUtils.getYMD(planEndDate);
		}
		List<Car> cars = orderService.getRecommandedCar(serviceType,getChargeMode(chargeMode),
				beginDate_, endDate_, pageNum).getRecordList();

		LinkedHashMap<String, Integer> carUseInfoNum = null;
		List<myCar> myCars = new ArrayList<myCar>();
		myCar myCar = null;
		for (Car car : cars) {
			try {
				myCar = new myCar();
				carUseInfoNum = new LinkedHashMap<String, Integer>();
				List<List<Order>> carUseInfo = orderService.getCarTask(car,
						beginDate_, endDate_);
				int i = 0;
				for (List<Order> dayList : carUseInfo) {
					if(dayList!=null && dayList.size()>0){
						if(dayList.get(0)!=null){
							boolean alldone=true;
							for(Order order : dayList){
								if(order!=null && (order.getStatus()==OrderStatusEnum.SCHEDULED ||
										order.getStatus()==OrderStatusEnum.ACCEPTED ||
										order.getStatus()==OrderStatusEnum.BEGIN ||
										order.getStatus()==OrderStatusEnum.GETON ||
										order.getStatus()==OrderStatusEnum.GETOFF)){
									alldone=false;
									break;
								}
									
							}
							//如果有任务，且存在没有完成和订单，那么就应该在界面上显示“有任务”（0），否则应该显示空闲（非0）。
							if(!alldone)							
								carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate_, i++)), 0);
							else
								carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate_, i++)), 5);
						}else
							carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate_, i++)), 1);
					}else{
						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
								.getOffsetDate(beginDate_, i++)), 4);
					}
				}
				myCar.setId(car.getId().toString());
				myCar.setCarNumber(car.getPlateNumber());
				if(car.getDriver()!=null){
					myCar.setDriverName(car.getDriver().getName());
					myCar.setDriverId(String.valueOf(car.getDriver().getId()));
					myCar.setPhone(car.getDriver().getPhoneNumber());
				}
				myCar.setCarInfo(carUseInfoNum);
				myCars.add(myCar);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		writeJson(JSONArray.toJSONString(myCars));
	}

	public String queue() {
		QueryHelper helper = new QueryHelper("order_", "o");
		helper.addWhereCondition("o.status=?", OrderStatusEnum.INQUEUE);
		helper.addWhereCondition("o.scheduling=?", false);
		PageBean<Order> pageBean = orderService.queryOrder(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().put("queueSize", pageBean.getRecordCount());
		return "queue";
	}
	
	public String watchKeeper(){
		WatchKeeper watchKeeper=watchKeeperService.getWatchKeeper();
		onDuty=watchKeeper.isOnDuty();
		keeper=watchKeeper.getKeeper();
		return "watchKeeper";
	}
	
	public String configWatchKeeper(){
		WatchKeeper watchKeeper=watchKeeperService.getWatchKeeper();
		watchKeeper.setOnDuty(onDuty);
		watchKeeper.setKeeper(keeper);
		watchKeeperService.updateWatchKeeper(watchKeeper);
		return queue();
	}
	
	public String popup() {
		DecimalFormat df = new DecimalFormat ("#.0");
		String tmp;
		int index=0;
    	List<CarServiceSuperType> listSuper=new ArrayList<CarServiceSuperType>();
    	Map<CarServiceType, Price> priceMap=new HashMap<CarServiceType, Price>();
    	Map<String, List<List<String>>> mapListResults=new LinkedHashMap<String, List<List<String>>>();
    	listSuper=carServiceSuperTypeDao.getAll();
    	PriceTable priceTable=new PriceTable();
    	priceTable=priceSerivce.getDefaultPriceTable();
    	priceMap=priceTable.getCarServiceType();
    	for (int i = 0; i < listSuper.size(); i++) 
    		mapListResults.put(listSuper.get(i).getTitle(), new ArrayList<List<String>>());
        for (CarServiceType key : priceMap.keySet()) 
    	 {
    	        //System.out.println("key= " + key.getId() + " and value= " + priceMap.get(key).getId());
    	        List<String> list=new ArrayList<String>();
	    		list.add(key.getId().toString());
    	        list.add(key.getTitle());
	      		list.add(priceMap.get(key).getPerDay().setScale(0,BigDecimal.ROUND_HALF_UP).toString());
	      		list.add(priceMap.get(key).getPerHalfDay().setScale(0,BigDecimal.ROUND_HALF_UP).toString());
	      		tmp=df.format(priceMap.get(key).getPerMileAfterLimit());
    	        index=tmp.indexOf('.');
    	        if (Integer.parseInt(tmp.substring(index+1, index+2))>0) {
    	        	BigDecimal bigDecimal=new BigDecimal(tmp);
    	        	tmp=df.format(bigDecimal);
    	        	list.add(tmp);
				}else {
					list.add(priceMap.get(key).getPerMileAfterLimit().setScale(0,BigDecimal.ROUND_HALF_UP).toString());
				}
	      		list.add(priceMap.get(key).getPerHourAfterLimit().setScale(0,BigDecimal.ROUND_HALF_UP).toString());
	      		list.add(priceMap.get(key).getPerPlaneTime().setScale(0,BigDecimal.ROUND_HALF_UP).toString());
	      		mapListResults.get(key.getSuperType().getTitle()).add(list);
    	    }
     	ActionContext.getContext().getSession().put("mapList", mapListResults);
     	return "popup";
	}
	

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public Car getQueryCar() {
		return queryCar;
	}

	public void setQueryCar(Car queryCar) {
		this.queryCar = queryCar;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}	

	public String getCustomerOrganizationName() {
		return customerOrganizationName;
	}

	public void setCustomerOrganizationName(String customerOrganizationName) {
		this.customerOrganizationName = customerOrganizationName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getChargeMode() {
		return chargeMode;
	}

	public void setChargeMode(String chargeMode) {
		this.chargeMode = chargeMode;
	}

	public String getPlanBeginDate() {
		return planBeginDate;
	}

	public void setPlanBeginDate(String planBeginDate) {
		this.planBeginDate = planBeginDate;
	}

	public String getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}	
	
	public CarServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(CarServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setCopyNumber(int copyNumber) {
		this.copyNumber = copyNumber;
	}

	public int getCopyNumber() {
		return copyNumber;
	}

	public String getNeedCopy() {
		return needCopy;
	}

	public void setNeedCopy(String needCopy) {
		this.needCopy = needCopy;
	}

	public long getCancelOrderId() {
		return cancelOrderId;
	}

	public void setCancelOrderId(long cancelOrderId) {
		this.cancelOrderId = cancelOrderId;
	}

	public long getScheduleFromQueueOrderId() {
		return scheduleFromQueueOrderId;
	}

	public void setScheduleFromQueueOrderId(long scheduleFromQueueOrderId) {
		this.scheduleFromQueueOrderId = scheduleFromQueueOrderId;
	}

	public String getScheduleMode() {
		return scheduleMode;
	}

	public void setScheduleMode(String scheduleMode) {
		this.scheduleMode = scheduleMode;
	}

	public long getScheduleFromUpdateOrderId() {
		return scheduleFromUpdateOrderId;
	}

	public void setScheduleFromUpdateOrderId(long scheduleFromUpdateOrderId) {
		this.scheduleFromUpdateOrderId = scheduleFromUpdateOrderId;
	}

	public Car getSelectedCar() {
		return selectedCar;
	}

	public void setSelectedCar(Car selectedCar) {
		this.selectedCar = selectedCar;
	}

	public User getSelectedDriver() {
		return selectedDriver;
	}

	public void setSelectedDriver(User selectedDriver) {
		this.selectedDriver = selectedDriver;
	}

	public User getSaler() {
		return saler;
	}

	public void setSaler(User saler) {
		this.saler = saler;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public User getKeeper() {
		return keeper;
	}

	public void setKeeper(User keeper) {
		this.keeper = keeper;
	}

	public boolean isOnDuty() {
		return onDuty;
	}

	public void setOnDuty(boolean onDuty) {
		this.onDuty = onDuty;
	}

	public String getOtherPassengerName() {
		return otherPassengerName;
	}

	public void setOtherPassengerName(String otherPassengerName) {
		this.otherPassengerName = otherPassengerName;
	}

	public String getOtherPhoneNumber() {
		return otherPhoneNumber;
	}

	public void setOtherPhoneNumber(String otherPhoneNumber) {
		this.otherPhoneNumber = otherPhoneNumber;
	}

	public String getCallForOther() {
		return callForOther;
	}

	public void setCallForOther(String callForOther) {
		this.callForOther = callForOther;
	}

	public String getCallForOtherSendSMS() {
		return callForOtherSendSMS;
	}

	public void setCallForOtherSendSMS(String callForOtherSendSMS) {
		this.callForOtherSendSMS = callForOtherSendSMS;
	}

	public BigDecimal getProtocolMoney() {
		return protocolMoney;
	}

	public void setProtocolMoney(BigDecimal protocolMoney) {
		this.protocolMoney = protocolMoney;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	private boolean isStringEmpty(String str) {
		if (str == null || str.trim().length() == 0
				|| str.trim().equalsIgnoreCase("null"))
			return true;
		return false;
	}
}

/**
 * 这是为了构造json数据
 * 
 * @author wanglei
 * @version $Id: ScheduleAction.java, v 0.1 2016年1月8日 下午4:00:15 wanglei Exp $
 */
class myCar {
	String id;
	String driverName;
	String driverId;
	String carNumber;
	String phone;
	Map<String, Integer> carInfo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Map<String, Integer> getCarInfo() {
		return carInfo;
	}

	public void setCarInfo(Map<String, Integer> carInfo) {
		this.carInfo = carInfo;
	}
	
}
