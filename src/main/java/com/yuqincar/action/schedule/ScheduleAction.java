/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yuqincar.action.schedule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.domain.order.Address;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderSourceEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.order.OtherPassenger;
import com.yuqincar.domain.order.WatchKeeper;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.customer.CustomerService;
import com.yuqincar.service.order.AddressService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.order.WatchKeeperService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.Configuration;
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
	private AddressService addressService;
	@Autowired
	private CustomerOrganizationService customerOrganizationService;
	@Autowired
	private WatchKeeperService watchKeeperService;
	@Autowired
	private UserService userService;
	
	private List<CarServiceType> serviceTypes;
	private long driverId;
	private String driverName;
	private String carNumber;
	private String keyWord;
	private String customerOrganizationName;
	private String customerName;
	private String phone;
	private String chargeMode;
	private String planBeginDate;
	private String planEndDate;
	private String passengerNumber;
	private String serviceType;
	private List<Address> address;
	private String selectCarId;
	private String memo;
	private double lng0;
	private double lat0;
	private double lng1;
	private double lat1;
	private String needCopy;
	private int copyNumber;
	private String orderDate;
	private long orderCarId;
	private long cancelOrderId;
	private String scheduleMode;  //新建时为null，从队列调度为FROM_QUEUE,修改订单为FROM_UPDATE
	private long scheduleFromQueueOrderId;
	private long scheduleFromUpdateOrderId;
	private String selectCarDriverName;
	private String selectCarPlateNumber;
	private String selectCarDriverPhone;
	private boolean onDuty;
	private String callForOther;
	private String otherPassengerName;
	private String otherPhoneNumber;
	private String callForOtherSendSMS;
	private Integer componentId;
	
	public void getCar() {
		System.out.println("driverId="+driverId);
		System.out.println("carNumber="+carNumber);
		QueryHelper helper = new QueryHelper(Car.class, "c");
		if (driverId > 0 && carNumber != null && carNumber.length() > 0)
			helper.addWhereCondition("c.status=? and c.driver.id=? and c.plateNumber=?",
					CarStatusEnum.NORMAL,driverId, carNumber);
		else if (driverId > 0)
			helper.addWhereCondition("c.status=? and c.driver.id=?", CarStatusEnum.NORMAL,driverId);
		else
			helper.addWhereCondition("c.status=? and c.plateNumber=?", CarStatusEnum.NORMAL,carNumber);
		List<Car> cars = carService.queryCar(1, helper).getRecordList();
		System.out.println("cars.size="+cars.size());
		for(Car car:cars)
			System.out.println("car="+car.getPlateNumber());
		Date beginDate_ = DateUtils.getMinDate(DateUtils
				.getYMDHM(planBeginDate));
		Date endDate_;
		if (getChargeMode(chargeMode)!=ChargeModeEnum.MILE)
			endDate_ = DateUtils.getMaxDate(DateUtils.getYMDHM(planEndDate));
		else
			endDate_ = DateUtils.getMaxDate(DateUtils.getOffsetDate(beginDate_,
					4));
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
			System.out.println("0000");
			List<List<BaseEntity>> carUseInfo = orderService.getCarTask(car,
					beginDate_, endDate_);
			System.out.println("carUseInfo.size="+carUseInfo.size());
			int i = 0;
			for (List<BaseEntity> dayList : carUseInfo) {
				if(dayList!=null && dayList.size()>0){
					BaseEntity baseEntity=dayList.get(0);//用最前面的实体作为显示依据。
					if (baseEntity instanceof CarCare) {
						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
								.getOffsetDate(beginDate_, i++)), 0);
					} else if (baseEntity instanceof CarRepair) {
						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
								.getOffsetDate(beginDate_, i++)), 1);
					} else if (baseEntity instanceof CarExamine) {
						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
								.getOffsetDate(beginDate_, i++)), 2);
					} else if (baseEntity instanceof Order) {
						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
								.getOffsetDate(beginDate_, i++)), 3);
					} else {
						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
								.getOffsetDate(beginDate_, i++)), 4);
					}
				}else{
					carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
							.getOffsetDate(beginDate_, i++)), 4);
				}
			}
			myCar.setId(car.getId().toString());
			myCar.setCarNumber(car.getPlateNumber());
			myCar.setDriverName(car.getDriver().getName());
			myCar.setPhone(car.getDriver().getPhoneNumber());
			myCar.setCarInfo(carUseInfoNum);
			myCars.add(myCar);
			System.out.println("111");
		}
		writeJson(JSONArray.toJSONString(myCars));
	}

	public String map() {
		return "map";
	}
	
	public String historyAddress(){
		System.out.println("componentId="+componentId);
		if(phone!=null && phone.length()>0)
			ActionContext.getContext().getSession().put("phone", phone);
		if(componentId!=null)
			ActionContext.getContext().getSession().put("componentId", componentId);
		Customer customer=customerService.getCustomerByPhoneNumber((String)ActionContext.getContext().getSession().get("phone"));
		List<Address> addressList=new ArrayList<Address>();
		if(customer!=null)
			addressList.addAll(customer.getAddresses());
		int from,to;
		from=(pageNum-1)*Configuration.getPageSize();
		to=from+Configuration.getPageSize();
		if(to>=addressList.size())
			to=addressList.size();
		System.out.println("pageNum="+pageNum+",from="+from+",to="+to);
		PageBean<Address> pageBean=new PageBean(pageNum,Configuration.getPageSize(),addressList.size(),addressList.subList(from, to));
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().put("componentId", (Integer)ActionContext.getContext().getSession().get("componentId"));
		System.out.println("componentId="+componentId);
		return "historyAddress";
	}

	public void getCustomerOrganization() {		
		List<Object> list = new ArrayList<Object>();
		for (CustomerOrganization co : customerOrganizationService
				.queryCustomerOrganizationByKeyword(keyWord).getRecordList())
			list.add(co.getName());
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
	
	public String cancelOrder(){
		System.out.println("cancelOrder");
		System.out.println("cancelOrderId="+cancelOrderId);
		Order order=orderService.getOrderById(cancelOrderId);
		User user = (User) ActionContext.getContext().getSession().get("user");
		orderService.cancelOrder(order, user, "在队列中被取消");
		System.out.println("before return");
		return "toQueue";
	}

	public String getChargeModeString() {
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.getChargeModeString(order.getChargeMode());
	}

	public String inQueue() {
		System.out.println("in inQueue");
		Order order = new Order();
		CustomerOrganization customerOrganization = null;
		Customer customer = null;
		ChargeModeEnum chargeModeEnum = null;
		CarServiceType carServiceType_ = null;
		customerOrganization=new CustomerOrganization();
		customerOrganization.setName(customerOrganizationName);
		order.setCustomerOrganization(customerOrganization);	//到Service层去处理是否新建客户单位
		customer=new Customer();
		customer.setName(customerName);
		order.setCustomer(customer);							//到Service层去处理是否新建客户
		System.out.println("callForOther="+callForOther);
		System.out.println("otherPassengerName="+otherPassengerName);
		System.out.println("otherPhoneNumber="+otherPhoneNumber);
		System.out.println("callForOtherSendSMS="+callForOtherSendSMS);
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
		order.setPlanBeginDate(DateUtils.getYMDHM(planBeginDate));
		if (order.getChargeMode()!=ChargeModeEnum.MILE)
			order.setPlanEndDate(DateUtils.getYMDHM(planEndDate));
		carServiceType_ = carService.getCarServiceTypeById(Long
				.parseLong(serviceType));
		order.setServiceType(carServiceType_);
		order.setPassengerNumber(Integer.parseInt(passengerNumber));
		order.setPhone(phone);
		order.setOrderMoney(new BigDecimal(0));
		order.setStatus(OrderStatusEnum.INQUEUE);
		order.setFromAddress(address.get(0));
		if (order.getChargeMode()==ChargeModeEnum.MILE) {
			order.setToAddress(address.get(1));
		}
		order.setMemo(memo);
		order.setOrderMile(0L);
		order.setCreateTime(new Date());
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
		} else {
			return ChargeModeEnum.PROTOCOL;
		}
	}
	
	private String getChargeModeText(ChargeModeEnum chargeMode) {
		switch(chargeMode){
			case MILE:
				return "filter_mileage";
			case DAY:
				return "filter_days";
			case PROTOCOL:
				return "filter_protocol";
		}
		return "";
	}
	
	public void isCarAvailable(){
		System.out.println("in isCarAvailable");
		System.out.println("chargeMode="+chargeMode);
		System.out.println("planBeginDate="+planBeginDate);
		System.out.println("planEndDate="+planEndDate);
		System.out.println("selectCarId="+selectCarId);
		System.out.println("serviceType="+serviceType);
		System.out.println("passengerNumber="+passengerNumber);
		System.out.println("scheduleFromUpdateOrderId="+scheduleFromUpdateOrderId);
		Order order=new Order();
		if(scheduleFromUpdateOrderId>0)	//修改订单。
			order.setId(scheduleFromUpdateOrderId);   //修改订单时，设置id的目的是为了在查询数据库时，把本订单排除在外。
		order.setChargeMode(getChargeMode(chargeMode));
		if (planBeginDate != null && planBeginDate.length() > 0)
			order.setPlanBeginDate(DateUtils.getYMDHM(planBeginDate));
		if (order.getChargeMode()!=ChargeModeEnum.MILE && planEndDate != null && planEndDate.length() > 0)
			order.setPlanEndDate(DateUtils.getYMDHM(planEndDate));
		order.setPassengerNumber(Integer.parseInt(passengerNumber));
		order.setServiceType(carService.getCarServiceTypeById(Long
					.parseLong(serviceType)));
		Car car = carService.getCarById(Long.parseLong(selectCarId));
		int result=orderService.isCarAvailable(order, car);
		System.out.println("resutl="+result);
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
		if(order.getChargeMode()!=ChargeModeEnum.MILE)
			planEndDate=DateUtils.getYMDHMString(order.getPlanEndDate());
		passengerNumber=String.valueOf(order.getPassengerNumber());
		serviceType=String.valueOf(order.getServiceType().getId());
		address=new ArrayList<Address>();
		address.add(order.getFromAddress());
		if(order.getChargeMode()==ChargeModeEnum.MILE)
			address.add(order.getToAddress());
		memo=order.getMemo();
		serviceTypes = orderService.getAllCarServiceType();
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
		scheduleFromQueueOrderId=order.getId();
		scheduleMode=OrderService.SCHEDULE_FROM_QUEUE;
		initInputField(order);
		return "scheduling";
	}
	
	public String updateOrder(){
		System.out.println("in updateOrder");
		System.out.println("scheduleFromUpdateOrderId="+scheduleFromUpdateOrderId);
		Order order=orderService.getOrderById(scheduleFromUpdateOrderId);
		System.out.println(order);
		scheduleMode=OrderService.SCHEDULE_FROM_UPDATE;
		initInputField(order);
		selectCarDriverName=order.getCar().getDriver().getName();
		selectCarPlateNumber=order.getCar().getPlateNumber();
		selectCarDriverPhone=order.getCar().getDriver().getPhoneNumber();
		selectCarId=String.valueOf(order.getCar().getId());
		System.out.println("callForOther="+callForOther);
		System.out.println("otherPassengerName="+otherPassengerName);
		System.out.println("otherPhoneNumber="+otherPhoneNumber);
		System.out.println("callForOtherSendSMS="+callForOtherSendSMS);
		return "scheduling";
	}
	
	public void isOrderDeprived(){
		System.out.println("in isOrderDeprived");
		System.out.println("scheduleFromQueueOrderId="+scheduleFromQueueOrderId);
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
		System.out.println("startSchedule");
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
			toUpdateOrder.setPassengerNumber(order.getPassengerNumber());
			toUpdateOrder.setServiceType(order.getServiceType());
			toUpdateOrder.setFromAddress(order.getFromAddress());
			toUpdateOrder.setToAddress(order.getToAddress());
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
		CustomerOrganization customerOrganization = null;
		Customer customer = null;
		ChargeModeEnum chargeModeEnum = null;
		CarServiceType carServiceType_ = null;
		Date beginDate = null;
		Date endDate = null;
		Car car = null;
		car = carService.getCarById(Long.parseLong(selectCarId));
		chargeModeEnum = getChargeMode(chargeMode);
		order.setChargeMode(chargeModeEnum);
		if (planBeginDate != null && planBeginDate.length() > 0) {
			beginDate = DateUtils.getYMDHM(planBeginDate);
			order.setPlanBeginDate(beginDate);
		}
		if (order.getChargeMode()!=ChargeModeEnum.MILE && planEndDate != null && planEndDate.length() > 0) {
			endDate = DateUtils.getYMDHM(planEndDate);
			order.setPlanEndDate(endDate);
		}
		carServiceType_ = carService.getCarServiceTypeById(Long
				.parseLong(serviceType));
		order.setServiceType(carServiceType_);
		order.setPassengerNumber(Integer.parseInt(passengerNumber));
		order.setPhone(phone);
		
		if(order.getOrderMoney()==null)
			order.setOrderMoney(new BigDecimal(0));
		if(order.getCreateTime()==null)
			order.setCreateTime(new Date());
		order.setFromAddress(address.get(0));
		System.out.println("address.size()="+address.size());
		if (order.getChargeMode()==ChargeModeEnum.MILE) {
			order.setToAddress(address.get(1));
		}
		order.setMemo(memo);
		order.setOrderMile(0L);
		System.out.println("needCopy="+needCopy);
		if(needCopy==null || needCopy.isEmpty() || needCopy.equals("off"))
			copyNumber=0;
		
		//把设置单位、客户的代码放在这儿的原因是：如果放在前面，当从队列拿出订单来调度时，会报Customer未保存的错误。
		customerOrganization=new CustomerOrganization();
		customerOrganization.setName(customerOrganizationName);
		order.setCustomerOrganization(customerOrganization);	//到Service层去处理是否新建客户单位
		customer=new Customer();
		customer.setName(customerName);
		order.setCustomer(customer);							//到Service层去处理是否新建客户

		System.out.println("callForOther="+callForOther);
		System.out.println("otherPassengerName="+otherPassengerName);
		System.out.println("otherPhoneNumber="+otherPhoneNumber);
		System.out.println("callForOtherSendSMS="+callForOtherSendSMS);

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
		result = orderService.scheduleOrder(str, order, car,copyNumber,toUpdateOrder,user);
		System.out.println("result="+result);
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
			serviceTypes = orderService.getAllCarServiceType();
			return "scheduling";
		}
	}

	public String scheduling() {
		Order order=orderService.getOrderDistributed((User)ActionContext.getContext().getSession().get("user"));
		if(order!=null){   //说明还有未过期的队列订单需要调度。
			scheduleFromQueueOrderId=order.getId();
			scheduleMode=OrderService.SCHEDULE_FROM_QUEUE;
			initInputField(order);
		}else
			serviceTypes = orderService.getAllCarServiceType();
		return "scheduling";
	}

	public void estimate() {
		System.out.println("in estimate");
		System.out.println("selectCarId="+selectCarId);
		CarServiceType cst = carService.getCarServiceTypeById(Long
				.parseLong(serviceType));
		ChargeModeEnum cme = getChargeMode(chargeMode);
		double mileage = 0;
		int days = 0;
		if (cme == ChargeModeEnum.MILE){
			Location from=new Location();
			from.setLongitude(lng0);
			from.setLatitude(lat0);
			Location to=new Location();
			to.setLongitude(lng1);
			to.setLatitude(lat1);
			
			Car car=null;
			if(selectCarId!=null && selectCarId.length()>0)
				car=carService.getCarById(Long.valueOf(selectCarId));
				
			mileage=orderService.estimateMileage(car, from, to);
		}
		if (cme == ChargeModeEnum.DAY || cme == ChargeModeEnum.PROTOCOL)
			days = DateUtils.elapseDays(DateUtils.getYMDHM(planBeginDate),
					DateUtils.getYMDHM(planEndDate), true, true);
		BigDecimal money = orderService.calculateOrderMoney(cst, cme, mileage,
				days);

		StringBuffer sb = new StringBuffer();
		sb.append(money.setScale(1, BigDecimal.ROUND_HALF_UP));
		if (cme == ChargeModeEnum.MILE)
			sb.append("&nbsp;&nbsp;(")
					.append(new BigDecimal(mileage).setScale(1,
							BigDecimal.ROUND_HALF_UP)).append("km)");
		writeJson("{\"money\":\"" + sb.toString() + "\"}");
	}

	public void getRecommandDriver() {

		Location location = new Location();
		location.setLatitude(lat0);
		location.setLongitude(lng0);
		System.out.println("in getRecommendDriver");
		System.out.println("planBeginDate=" + planBeginDate);
		System.out.println("planEndDate=" + planEndDate);
		Date beginDate_ = DateUtils.getYMDHM(planBeginDate);
		Date endDate_;
		if (getChargeMode(chargeMode) == ChargeModeEnum.MILE) {
			endDate_ = DateUtils.getOffsetDate(beginDate_, 4);
		} else {
			endDate_ = DateUtils.getYMDHM(planEndDate);
		}
		List<Car> cars = orderService.getRecommandedCar(
				carService.getCarServiceTypeById(Long.parseLong(serviceType)),getChargeMode(chargeMode),
				location, beginDate_, endDate_, pageNum).getRecordList();

		List<LinkedHashMap<myCar, LinkedHashMap<String, Integer>>> carStatus = new ArrayList<LinkedHashMap<myCar, LinkedHashMap<String, Integer>>>();
		LinkedHashMap<myCar, LinkedHashMap<String, Integer>> teMap = null;
		LinkedHashMap<String, Integer> carUseInfoNum = null;
		List<myCar> myCars = new ArrayList<myCar>();
		myCar myCar = null;
		for (Car car : cars) {
			try {
				myCar = new myCar();
				teMap = new LinkedHashMap<myCar, LinkedHashMap<String, Integer>>();
				carUseInfoNum = new LinkedHashMap<String, Integer>();
				List<List<BaseEntity>> carUseInfo = orderService.getCarTask(car,
						beginDate_, endDate_);
				int i = 0;
				for (List<BaseEntity> dayList : carUseInfo) {
					System.out.println("in dayList");
					if(dayList!=null && dayList.size()>0){
						System.out.println("1");
						BaseEntity baseEntity=dayList.get(0);//用最前面的实体作为显示依据。
						if (baseEntity instanceof CarCare) {
							System.out.println("2");
							carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
									.getOffsetDate(beginDate_, i++)), 0);
						} else if (baseEntity instanceof CarRepair) {
							System.out.println("3");
							carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
									.getOffsetDate(beginDate_, i++)), 1);
						} else if (baseEntity instanceof CarExamine) {
							System.out.println("4");
							carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
									.getOffsetDate(beginDate_, i++)), 2);
						} else if (baseEntity instanceof Order) {
							System.out.println("5");
							carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
									.getOffsetDate(beginDate_, i++)), 3);
						} else {
							System.out.println("6");
							carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
									.getOffsetDate(beginDate_, i++)), 4);
						}
					}else{
						System.out.println("7");
						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
								.getOffsetDate(beginDate_, i++)), 4);
					}
				}
				System.out.println("8");
				myCar.setId(car.getId().toString());
				myCar.setCarNumber(car.getPlateNumber());
				myCar.setDriverName(car.getDriver().getName());
				myCar.setPhone(car.getDriver().getPhoneNumber());
				myCar.setCarInfo(carUseInfoNum);
				myCars.add(myCar);

			} catch (Exception e) {
				System.out.println("9");
				e.printStackTrace();
			}
		}
		writeJson(JSONArray.toJSONString(myCars));
	}

	public String queue() {
		System.out.println("in queue.");
		System.out.println("pageNum="+pageNum);
		QueryHelper helper = new QueryHelper("order_", "o");
		helper.addWhereCondition("o.status=?", OrderStatusEnum.INQUEUE);
		helper.addWhereCondition("o.scheduling=?", false);
		PageBean<Order> pageBean = orderService.queryOrder(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().put("queueSize", pageBean.getRecordCount());
		return "queue";
	}
	
	public String watchKeeper(){
		System.out.println("in watchKeeper");
		WatchKeeper watchKeeper=watchKeeperService.getWatchKeeper();
		onDuty=watchKeeper.isOnDuty();
		driverName=watchKeeper.getKeeper().getName();
		driverId=watchKeeper.getKeeper().getId();
		return "watchKeeper";
	}
	
	public String configWatchKeeper(){
		System.out.println("in configWatchKeeper");
		WatchKeeper watchKeeper=watchKeeperService.getWatchKeeper();
		watchKeeper.setOnDuty(onDuty);
		watchKeeper.setKeeper(userService.getById(driverId));
		watchKeeperService.updateWatchKeeper(watchKeeper);
		System.out.println("end configWatchKeeper");
		return queue();
	}
	
	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public long getDriverId() {
		return driverId;
	}

	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	public List<CarServiceType> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<CarServiceType> serviceTypes) {
		this.serviceTypes = serviceTypes;
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

	public String getPassengerNumber() {
		return passengerNumber;
	}

	public String getSelectCarId() {
		return selectCarId;
	}

	public void setSelectCarId(String selectCarId) {
		this.selectCarId = selectCarId;
	}

	public void setPassengerNumber(String passengerNumber) {
		this.passengerNumber = passengerNumber;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public double getLng0() {
		return lng0;
	}

	public void setLng0(double lng0) {
		this.lng0 = lng0;
	}

	public double getLat0() {
		return lat0;
	}

	public void setLat0(double lat0) {
		this.lat0 = lat0;
	}

	public double getLng1() {
		return lng1;
	}

	public void setLng1(double lng1) {
		this.lng1 = lng1;
	}

	public double getLat1() {
		return lat1;
	}

	public void setLat1(double lat1) {
		this.lat1 = lat1;
	}

	public void setCopyNumber(int copyNumber) {
		this.copyNumber = copyNumber;
	}

	public int getCopyNumber() {
		return copyNumber;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public long getOrderCarId() {
		return orderCarId;
	}

	public void setOrderCarId(long orderCarId) {
		this.orderCarId = orderCarId;
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

	public String getSelectCarDriverName() {
		return selectCarDriverName;
	}

	public void setSelectCarDriverName(String selectCarDriverName) {
		this.selectCarDriverName = selectCarDriverName;
	}

	public String getSelectCarPlateNumber() {
		return selectCarPlateNumber;
	}

	public void setSelectCarPlateNumber(String selectCarPlateNumber) {
		this.selectCarPlateNumber = selectCarPlateNumber;
	}

	public String getSelectCarDriverPhone() {
		return selectCarDriverPhone;
	}

	public void setSelectCarDriverPhone(String selectCarDriverPhone) {
		this.selectCarDriverPhone = selectCarDriverPhone;
	}	

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
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

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
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
