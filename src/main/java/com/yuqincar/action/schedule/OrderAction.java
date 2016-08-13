/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yuqincar.action.schedule;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts2.views.jsp.IteratorStatus;
import org.dbunit.dataset.stream.StreamingDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONArray;
import com.mysql.fabric.xmlrpc.base.Struct;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.DriverActionVO;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderOperationRecord;
import com.yuqincar.domain.order.OrderSourceEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.common.DiskFileService;
import com.yuqincar.service.customer.CustomerService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class OrderAction extends BaseAction {

	@Autowired
	private OrderService orderService;
	@Autowired
	private CarService carService;
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private DiskFileService diskFileService;

	private String customerOrganizationName;
	private String customerName;
	private String phone;
	private String chargeMode;
	private Date planBeginDate1;
	private Date planBeginDate2;
	private Date planEndDate1;
	private Date planEndDate2;
	private String serviceType;
	private Date actualBeginDate1;
	private Date actualBeginDate2;
	private Date actualEndDate1;
	private Date actualEndDate2;
	private String fromAddress;
	private String toAddress;
	private BigDecimal orderMoney1;
	private BigDecimal orderMoney2;
	private BigDecimal actualMoney1;
	private BigDecimal actualMoney2;
	private Date queueTime1;
	private Date queueTime2;
	private Date scheduleTime1;
	private Date scheduleTime2;
	private String memo;
	private long schedulerId;
	private String orderSource;
	private String callForOtherLabel;
	private String otherPassengerName;
	private String otherPhoneNumber;
	private String callForOtherSendSMSLabel;
	private long imageId;
	private long driverId;
	private long salerId;
	private String driverName;
	private Date planBeginDate;
	private Date planEndDate;
	private String status;
	private long orderId;
	private String actionId;
	private Date actionTime;
	private String time;
	private String beforeTime;
	private String afterTime;
	private String reason;
	private String actualEndDate;
	private String keyWord;
	private long carId;
	private String orderDate;
	private String orderMileString;
	private String orderMoneyString;
	private String cancelReason;
	private Date postponeDate;
	private String postponeReason;
	private String sn;
	private String plateNumber;
	private String rescheduleReason;
	private float beginMile; 
	private float customerGetonMile;
	private float customerGetoffMile;
	private float endMile;
	private BigDecimal refuelMoney;
	private BigDecimal refuelMoney1;
	private BigDecimal refuelMoney2;
	private BigDecimal washingFee;
	private BigDecimal washingFee1;
	private BigDecimal washingFee2;
	private BigDecimal parkingFee;
	private BigDecimal parkingFee1;
	private BigDecimal parkingFee2;
	private BigDecimal toll;
	private BigDecimal toll1;
	private BigDecimal toll2;
	private float totalChargeMile;
	private BigDecimal roomAndBoardFee;
	private BigDecimal roomAndBoardFee1;
	private BigDecimal roomAndBoardFee2;
	private BigDecimal otherFee;
	private BigDecimal otherFee1;
	private BigDecimal otherFee2;
	private BigDecimal orderMoney;
	private BigDecimal actualMoney;
	private int grade;
	private String options;
	private IteratorStatus iteratorStatus;
	
	private List<DayOrderDetail> dayDetails = new ArrayList<DayOrderDetail>();
	
	public boolean isCanUpdateOrder(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.canUpdate(order);
	}
	
	public boolean isCanPushBackOrder(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.canOrderEndPostpone(order);
	}
	
	public boolean isCanRescheduleOrder(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.canOrderReschedule(order);
	}
	
	public boolean isCanCancelOrder(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.canCancelOrder(order);
	}
	
	public boolean isCanEditDriverAction(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.canEditDriverAction(order);
	}
	
	public boolean isCanAddAcceptAction(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.canAddAcceptAction(order);
	}
	
	public boolean isCanAddBeginAction(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.canAddBeginAction(order);
	}
	
	public boolean isCanAddGetonAction(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.canAddGetonAction(order);
	}
	
	public boolean isCanAddGetoffAction(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.canAddGetoffAction(order);
	}
	
	public boolean isCanAddEndAction(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.canAddEndAction(order);
	}
	public boolean isCanEditOrderBill(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return orderService.canEditOrderBill(order);
	}
	public void getDriverJson() {

		QueryHelper helper = new QueryHelper(User.class, "u");
		if (keyWord != null && keyWord.trim().length() > 0) {
			helper.addWhereCondition("u.name like ?", "%" + keyWord + "%");
		}

		List<Object> drivers = new ArrayList<Object>();
		drivers.addAll(customerService.queryCustomer(1, helper).getRecordList());
		this.writeJson(JSONArray.toJSONString(drivers));

	}

	public void getPlateNumberJson() {

		QueryHelper helper = new QueryHelper(Car.class, "c");
		if (keyWord != null && keyWord.trim().length() > 0) {
			helper.addWhereCondition("c.plateNumber like ?", "%" + keyWord
					+ "%");
		}

		List<Car> plateNumbers = carService.queryCar(1, helper).getRecordList();
		List<carJson> carJsons = new ArrayList<carJson>();
		for (Car car : plateNumbers) {
			carJson json = new carJson();
			json.setId(car.getId().toString());
			json.setName(car.getPlateNumber());
			carJsons.add(json);
		}
		this.writeJson(JSONArray.toJSONString(carJsons));
	}

	/*
	 * 订单打印页,非弹出框
	 */
	public String print() {
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);

			dayDetails=order.getDayDetails();
			List<DayOrderDetail> nullDayDetails = new ArrayList<DayOrderDetail>();
			System.out.println("list.size="+dayDetails.size());
			if(dayDetails.size()<8){
				int n=8-dayDetails.size();
				for(int i=1;i<=n;i++){				
					nullDayDetails.add(null);
				}
			}
			ActionContext.getContext().put("abstractTrackList", dayDetails);
			ActionContext.getContext().put("nullAbstractTrackList", nullDayDetails);
		}
		return "print";
	}
	/**
	 * 编辑派车单--显示页面
	 * @return
	 */
	public String editOrderBillUI() {
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(orderId);
			ActionContext.getContext().getValueStack().push(order);

			dayDetails=order.getDayDetails();
			List<DayOrderDetail> nullDayDetails = new ArrayList<DayOrderDetail>();
			System.out.println("list.size="+dayDetails.size());
			if(dayDetails.size()<8){
				int n=8-dayDetails.size();
				for(int i=1;i<=n;i++){			
					nullDayDetails.add(null);
				}
			}
			ActionContext.getContext().put("abstractTrackList", dayDetails);
			ActionContext.getContext().put("nullAbstractTrackList", nullDayDetails);
		}
		return "editOrderBillUI";
	}
	
	/*
	 * 编辑派车单
	 */
	public String editOrderBill(){
		User user = (User)ActionContext.getContext().getSession().get("user");
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			
			for(int i=0;i<order.getDayDetails().size();i++){
				order.getDayDetails().get(i).setGetonDate(dayDetails.get(i).getGetonDate());
				order.getDayDetails().get(i).setGetoffDate(dayDetails.get(i).getGetoffDate());
				order.getDayDetails().get(i).setPathAbstract(dayDetails.get(i).getPathAbstract());
				order.getDayDetails().get(i).setActualMile(dayDetails.get(i).getActualMile());
				order.getDayDetails().get(i).setChargeMile(dayDetails.get(i).getChargeMile());
			}
			order.setBeginMile(beginMile);
			order.setCustomerGetonMile(customerGetonMile);
			order.setCustomerGetoffMile(customerGetoffMile);
			order.setEndMile(endMile);
			order.setRefuelMoney(refuelMoney);
			order.setWashingFee(washingFee);
			order.setParkingFee(parkingFee);
			order.setTotalChargeMile(totalChargeMile);
			order.setToll(toll);
			order.setRoomAndBoardFee(roomAndBoardFee);
			order.setOtherFee(otherFee);
			order.setActualMoney(actualMoney);
			order.setOrderMoney(orderMoney);
			order.setGrade(grade);
			order.setOptions(options);
			
			orderService.editOrderBill(order, user);
		}

		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("orderManagerHelper");
		PageBean<Order> pageBean = orderService.queryOrder(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		
		return "list";
	}
	/*
	 * 显示用车单位签名（图片）
	 */
	public void getSignature(){
		diskFileService.downloadDiskFile(diskFileService.getDiskFileById(imageId), response);
	}
	/*
	 * 订单详情页,非弹出框
	 */
	public String view() {
		System.out.println("in view of OrderAction");
		System.out.println("orderId="+orderId);
		System.out.println(ActionContext.getContext().getValueStack().peek().getClass().toString());
		System.out.println("orderForView="+ActionContext.getContext().get("orderForView"));
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);
		}
		return "view";
	}
	
	/*
	 * 编辑司机动作
	 */
	public String editDriverAction() {
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);
			
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			System.out.println("List="+driverActionVOList);
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		return "operate";
	}
	
	//添加  接收订单操作
	public String addAcceptAction(){

		User user = (User)ActionContext.getContext().getSession().get("user");
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);		
			orderService.addAcceptAction(order, user, actionTime);	
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		
		return "operate";
	}
	
	//添加  开始订单操作
	public String addBeginAction(){

		User user = (User)ActionContext.getContext().getSession().get("user");
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);	
			orderService.addBeginAction(order, user, actionTime);		
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		
		return "operate";
	}
	
	//添加  客户上车操作
	public String addGetonAction(){

		User user = (User)ActionContext.getContext().getSession().get("user");
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);
			orderService.addGetonAction(order, user, actionTime);
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		
		return "operate";
	}
	
	//添加  客户下车操作
	public String addGetoffAction(){

		User user = (User)ActionContext.getContext().getSession().get("user");
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);		
			orderService.addGetoffAction(order, user, actionTime);
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		
		return "operate";
	}
	
	//添加  结束订单操作
	public String addEndAction(){

		User user = (User)ActionContext.getContext().getSession().get("user");
		System.out.println("user="+user);
		System.out.println("actionTime="+actionTime);
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);		
			orderService.addEndAction(order, user, actionTime);	
			System.out.println("order="+order);
			
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		
		return "operate";
	}
	
	//删除司机操作
	public String deleteDriverAction() throws Exception{
		
		User user = (User)ActionContext.getContext().getSession().get("user");
		orderService.deleteDriverAction(actionId, user);
		orderId = Long.parseLong(actionId.substring(0, 1));
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);		
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}

		return "operateList";
	}
	
	//修改时间 弹出框
	public String popupModify(){
		
		ActionContext.getContext().getValueStack().push(actionId);
		ActionContext.getContext().put("time",time);
		ActionContext.getContext().getValueStack().push(beforeTime);
		ActionContext.getContext().getValueStack().push(afterTime);
		return "popupModify";
	}
	
	//修改时间
	public void modifyDriverActionTime() throws ParseException{

		User user = (User)ActionContext.getContext().getSession().get("user");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(time);
		orderService.EditDriverAction(actionId, date, user);
		orderId = Long.parseLong(actionId.substring(0, 1));
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);		
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
	}
	
	public String cancel(){
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);
		}
		return "cancel";
	}
	
	public String cancelDo(){
		System.out.println("cancelDo");
		System.out.println("orderId="+orderId);
		Order order=orderService.getOrderById(orderId);
		int result=orderService.cancelOrder(order, (User)ActionContext.getContext().getSession().get("user"), cancelReason);
		ActionContext.getContext().getValueStack().push(order);
		if(result==0)
			return "view";
		else{
			addFieldError("cancelError", "当前状态不允许取消");
			return "cancel";
		}
	}
	
	public String reschedule(){
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			plateNumber=order.getCar().getPlateNumber();
			driverName=order.getDriver().getName();
			driverId=order.getDriver().getId();
			ActionContext.getContext().getValueStack().push(order);
		}
		return "reschedule";
	}
	
	public String rescheduleDo(){
		Order order=orderService.getOrderById(orderId);
		Car car=carService.getCarByPlateNumber(plateNumber);
		User driver=userService.getById(driverId);
		
		int result=0;
		result=orderService.orderReschedule(order, car, driver,(User)ActionContext.getContext().getSession().get("user"), rescheduleReason);
		if(result==1){
			addFieldError("rescheduleError", "车辆或司机不可用");
			return "reschedule";
		}else if(result==2){
			addFieldError("rescheduleError", "此订单当前状态不支持重新调度操作");
			return "reschedule";
		}
		ActionContext.getContext().getValueStack().push(order);
		return "view";
	}
	
	public String postpone(){
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);
		}
		return "postpone";
	}
	
	public String postponeDo(){
		System.out.println("postponeDo");
		System.out.println("orderId="+orderId);
		System.out.println("postponeDate="+postponeDate);
		Order order=orderService.getOrderById(orderId);
		ActionContext.getContext().getValueStack().push(order);
		if(postponeDate.before(order.getPlanEndDate())){
			addFieldError("postponeDateError", "延后的日期比计划结束日期还要早");
			postponeDate=null;
			return "postpone";
		}
		int result=orderService.orderEndPostpone(order, postponeDate, postponeReason, (User)ActionContext.getContext().getSession().get("user"));
		if(result==1){
			addFieldError("postponeError", "车辆在此时间段不可用");
			postponeDate=null;
			return "postpone";
		}else if(result==2){
			addFieldError("postponeError", "此订单当前计费方式或状态不支持延后操作");
			postponeDate=null;
			return "postpone";
		}
		return "view";
	}
	
	public String getTypeString(){
		OrderOperationRecord record=(OrderOperationRecord)ActionContext.getContext().getValueStack().peek();
		return orderService.getOperationRecordTypeString(record.getType());
	}
	
	/*
	 * 弹出订单详情页面
	 */
	public String info() {
		Date date = DateUtils.getYMD2(orderDate);
		if(carId>0 && driverId==0)
			ActionContext.getContext().put("orderList",orderService.getCarTask(carService.getCarById(carId), date,date).get(0));
		else if(driverId>0 && carId==0)
			ActionContext.getContext().put("orderList",orderService.getDriverTask(userService.getById(driverId), date,date).get(0));
		return "info";
	}
	
	public String getPlanDateString(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		StringBuffer sb=new StringBuffer();
		if(order.getChargeMode()==ChargeModeEnum.DAY || order.getChargeMode()==ChargeModeEnum.PROTOCOL){
			sb.append(DateUtils.getYMDString(order.getPlanBeginDate()));
			sb.append(" 到 ");
			sb.append(DateUtils.getYMDString(order.getPlanEndDate()));
		}
		if(order.getChargeMode()==ChargeModeEnum.MILE || order.getChargeMode()==ChargeModeEnum.PLANE){
			sb.append(DateUtils.getYMDHMString(order.getPlanBeginDate()));
		}
		return sb.toString();
	}
	
	public String getActualDateString(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		StringBuffer sb=new StringBuffer();
		if(order.getActualBeginDate()!=null)
			sb.append(DateUtils.getYMDHMString(order.getActualBeginDate()));
		if(order.getActualEndDate()!=null){
			sb.append(" 到 ");
			sb.append(DateUtils.getYMDHMString(order.getActualEndDate()));
		}
		return sb.toString();
	}
	
	public String getOrderMoneyString(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		if(order.getOrderMoney()==null)
			return "0.0";
		else
			return order.getOrderMoney().setScale(1, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	public String getActualMoneyString(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		if(order.getActualMoney()==null)
			return "0.0";
		else
			return order.getActualMoney().setScale(1, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	private OrderStatusEnum getOrderStatus(String status){
		if(status==null || status.isEmpty())
			return null;
		if (status.equals("在队列")) {
			return OrderStatusEnum.INQUEUE;
		} else if (status.equals("已调度")) {
			return OrderStatusEnum.SCHEDULED;
		} else if (status.equals("已接受")) {
			return OrderStatusEnum.ACCEPTED;
		} else if (status.equals("已开始")) {
			return OrderStatusEnum.BEGIN;
		} else if (status.equals("已上车")) {
			return OrderStatusEnum.GETON;
		} else if (status.equals("已下车")) {
			return OrderStatusEnum.GETOFF;
		} else if (status.equals("已结束")) {
			return OrderStatusEnum.END;
		} else if (status.equals("已付费")) {
			return OrderStatusEnum.PAYED;
		} else if (status.equals("已取消")) {
			return OrderStatusEnum.CANCELLED;
		}
		return null;
	}
	
	private String getOrderStatusString(OrderStatusEnum status){
		return status.toString();
	}
	
	public String protocolOrderRemind(){
		System.out.println("in protocolOrderRemind");
		ActionContext.getContext().put("recordList", orderService.getNeedRemindProtocolOrder());
		return "protocolOrderRemind";
	}
	
	public String unAcceptedOrderRemind(){
		System.out.println("in protocolOrderRemind");
		QueryHelper helper = new QueryHelper("order_", "o");
		helper.addWhereCondition("o.status=?", OrderStatusEnum.SCHEDULED);
		helper.addWhereCondition("o.chargeMode<>?",ChargeModeEnum.PROTOCOL);
		helper.addOrderByProperty("o.scheduleTime", true);
		List<Order> orderList=orderService.queryOrder(pageNum, helper).getRecordList();
		ActionContext.getContext().put("recordList", orderList);
		return "unAcceptedOrderRemind";
	}
	
	public String getWaiteMinutes(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		Date scheduleTime=order.getScheduleTime();
		long ms=new Date().getTime()-scheduleTime.getTime();
		int minutes=(int)(ms/60000);
		return String.valueOf(minutes);
	}
	
	private QueryHelper getInitHelper(){
		QueryHelper helper = new QueryHelper("order_", "o");
		helper.addOrderByProperty("o.id", false);
		return helper;
	}
	
	public String orderManagerQueryForm(){
		QueryHelper helper = new QueryHelper("order_", "o");
		if(sn!=null && !sn.isEmpty())
			helper.addWhereCondition("o.sn like ?", "%"+sn+"%");
		if(driverId>0)
			helper.addWhereCondition("o.driver.id=?", driverId);
		if(customerOrganizationName!=null && !customerOrganizationName.isEmpty())
			helper.addWhereCondition("o.customerOrganization.name=?", customerOrganizationName);
		if(planBeginDate!=null)
			helper.addWhereCondition("TO_DAYS(?)<=TO_DAYS(o.planBeginDate)", planBeginDate);
		if(planEndDate!=null)
			helper.addWhereCondition("TO_DAYS(o.planBeginDate)<=TO_DAYS(?)", planEndDate);
		OrderStatusEnum statusEnum=getOrderStatus(status);
		if(statusEnum!=null)
			helper.addWhereCondition("o.status=?", statusEnum);
		helper.addOrderByProperty("o.id", false);
		PageBean<Order> pageBean = orderService.queryOrder(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("orderManagerHelper", helper);
		driverId=0;
		customerOrganizationName=null;
		planBeginDate=null;
		planEndDate=null;
		status=null;
		return "list";
	}

	public String orderManager() {
		System.out.println("orderManager");
		QueryHelper helper=getInitHelper();
		PageBean<Order> pageBean = orderService.queryOrder(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("orderManagerHelper", helper);
		return "list";
	}
	
	public String list(){
		System.out.println("in list");
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("orderManagerHelper");
		PageBean<Order> pageBean = orderService.queryOrder(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}

	public String queryUI(){
		// 准备数据：carServiceTypeList
		ActionContext.getContext().put("carServiceTypeList", carService.getAllCarServiceType());
		return "queryUI";
	}
	
	private ChargeModeEnum getChargeMode(String chargeMode){
		if(chargeMode==null || chargeMode.isEmpty())
			return null;
		if (chargeMode.equals("按天计费")) {
			return ChargeModeEnum.DAY;
		} else if (chargeMode.equals("按里程计费")) {
			return ChargeModeEnum.MILE;
		} else if (chargeMode.equals("协议计费")) {
			return ChargeModeEnum.PROTOCOL;
		} else if (chargeMode.equals("接送机")) {
			return ChargeModeEnum.PLANE;
		} 
		return null;
	}
	private OrderSourceEnum getOrderSource(String orderSource){
		if(orderSource==null || orderSource.isEmpty())
			return null;
		if (orderSource.equals("调度员")) {
			return OrderSourceEnum.SCHEDULER;
		} else if (orderSource.equals("客户端")) {
			return OrderSourceEnum.APP;
		} else if (orderSource.equals("网站")) {
			return OrderSourceEnum.WEB;
		} 
		return null;
	}
	
	public String moreQuery(){
		QueryHelper helper = new QueryHelper("order_", "o");
		if(sn!=null && !sn.isEmpty())
			helper.addWhereCondition("o.sn like ?", "%"+sn+"%");
		if(driverId>0)
			helper.addWhereCondition("o.driver.id=?", driverId);
		if(customerOrganizationName!=null && !customerOrganizationName.isEmpty())
			helper.addWhereCondition("o.customerOrganization.name=?", customerOrganizationName);
		if(planBeginDate!=null)
			helper.addWhereCondition("TO_DAYS(?)<=TO_DAYS(o.planBeginDate)", planBeginDate);
		if(planEndDate!=null)
			helper.addWhereCondition("TO_DAYS(o.planBeginDate)<=TO_DAYS(?)", planEndDate);
		OrderStatusEnum statusEnum=getOrderStatus(status);
		if(statusEnum!=null)
			helper.addWhereCondition("o.status=?", statusEnum);
		if(customerName != null && !customerName.isEmpty())
			helper.addWhereCondition("o.customer.name=?", customerName);
		if(phone != null && !phone.isEmpty())
			helper.addWhereCondition("o.phone=?", phone);
		ChargeModeEnum chargeModeEnum = getChargeMode(chargeMode);
		if(chargeModeEnum!=null)
			helper.addWhereCondition("o.chargeMode=?", chargeModeEnum);
		if(planBeginDate1!=null)
			helper.addWhereCondition("TO_DAYS(?)<=TO_DAYS(o.planBeginDate)", planBeginDate1);
		if(planBeginDate2!=null)
			helper.addWhereCondition("TO_DAYS(o.planBeginDate)<=TO_DAYS(?)", planBeginDate2);
		if(planEndDate1!=null)
			helper.addWhereCondition("TO_DAYS(?)<=TO_DAYS(o.planEndDate)", planEndDate1);
		if(planEndDate2!=null)
			helper.addWhereCondition("TO_DAYS(o.planEndDate)<=TO_DAYS(?)", planEndDate2);
		if(actualBeginDate1!=null)
			helper.addWhereCondition("TO_DAYS(?)<=TO_DAYS(o.actualBeginDate)", actualBeginDate1);
		if(actualBeginDate2!=null)
			helper.addWhereCondition("TO_DAYS(o.actualBeginDate)<=TO_DAYS(?)", actualBeginDate2);
		if(actualEndDate1!=null)
			helper.addWhereCondition("TO_DAYS(?)<=TO_DAYS(o.actualEndDate)", actualEndDate1);
		if(actualEndDate2!=null)
			helper.addWhereCondition("TO_DAYS(o.actualEndDate)<=TO_DAYS(?)", actualEndDate2);
		if(serviceType != null && !serviceType.isEmpty())
			helper.addWhereCondition("o.serviceType.title=?", serviceType);
		if(fromAddress != null && !fromAddress.isEmpty())
			helper.addWhereCondition("o.fromAddress like ?", "%"+fromAddress+"%");
		if(toAddress != null && !toAddress.isEmpty())
			helper.addWhereCondition("o.toAddress like ?", "%"+toAddress+"%");
		if(orderMoney1 != null)
			helper.addWhereCondition("?<=o.orderMoney", orderMoney1);
		if(orderMoney2 != null)
			helper.addWhereCondition("o.orderMoney<=?", orderMoney2);
		if(actualMoney1 != null)
			helper.addWhereCondition("?<=o.actualMoney", actualMoney1);
		if(actualMoney2 != null)
			helper.addWhereCondition("o.actualMoney<=?", actualMoney2);
		if(queueTime1!=null)
			helper.addWhereCondition("TO_DAYS(?)<=TO_DAYS(o.queueTime)", queueTime1);
		if(queueTime2!=null)
			helper.addWhereCondition("TO_DAYS(o.queueTime)<=TO_DAYS(?)", queueTime2);
		if(scheduleTime1!=null)
			helper.addWhereCondition("TO_DAYS(?)<=TO_DAYS(o.scheduleTime)", scheduleTime1);
		if(scheduleTime2!=null)
			helper.addWhereCondition("TO_DAYS(o.scheduleTime)<=TO_DAYS(?)", scheduleTime2);
		if(plateNumber != null && !plateNumber.isEmpty())
			helper.addWhereCondition("o.car.plateNumber like ?", "%"+plateNumber+"%");
		if(schedulerId>0)
			helper.addWhereCondition("o.scheduler.id=?", schedulerId);
		OrderSourceEnum orderSourceEnum = getOrderSource(orderSource);
		if(orderSourceEnum != null)
			helper.addWhereCondition("o.orderSource=?", orderSourceEnum);
		if(callForOtherLabel == "是" || "是".equals(callForOtherLabel)){
			helper.addWhereCondition("o.callForOther =?", true);
		}
		if(callForOtherLabel == "否" || "否".equals(callForOtherLabel)){
			helper.addWhereCondition("o.callForOther =?", false);
		}
		if(otherPassengerName != null && !otherPassengerName.isEmpty())
			helper.addWhereCondition("o.otherPassengerName like ?", "%"+otherPassengerName+"%");
		if(otherPhoneNumber != null && !otherPhoneNumber.isEmpty())
			helper.addWhereCondition("o.otherPhoneNumber like ?", "%"+otherPhoneNumber+"%");
		if(callForOtherSendSMSLabel == "是" || "是".equals(callForOtherSendSMSLabel))
			helper.addWhereCondition("o.callForOtherSendSMS =?", true);
		if(callForOtherSendSMSLabel == "否" || "否".equals(callForOtherSendSMSLabel))
			helper.addWhereCondition("o.callForOtherSendSMS =?", false);
		if(grade >0)
			helper.addWhereCondition("o.grade = ?", grade);
		if(refuelMoney1 != null)
			helper.addWhereCondition("?<=o.refuelMoney", refuelMoney1);
		if(refuelMoney2 != null)
			helper.addWhereCondition("o.refuelMoney<=?", refuelMoney2);
		if(washingFee1 != null)
			helper.addWhereCondition("?<=o.washingFee", washingFee1);
		if(washingFee2 != null)
			helper.addWhereCondition("o.washingFee<=?", washingFee2);
		if(parkingFee1 != null)
			helper.addWhereCondition("?<=o.parkingFee", parkingFee1);
		if(parkingFee2 != null)
			helper.addWhereCondition("o.parkingFee<=?", parkingFee2);
		if(toll1 != null)
			helper.addWhereCondition("?<=o.toll", toll1);
		if(toll2 != null)
			helper.addWhereCondition("o.toll<=?", toll2);
		if(roomAndBoardFee1 != null)
			helper.addWhereCondition("?<=o.roomAndBoardFee", roomAndBoardFee1);
		if(roomAndBoardFee2 != null)
			helper.addWhereCondition("o.roomAndBoardFee<=?", roomAndBoardFee2);
		if(otherFee1 != null)
			helper.addWhereCondition("?<=o.otherFee", otherFee1);
		if(otherFee2 != null)
			helper.addWhereCondition("o.otherFee<=?", otherFee2);
		if(salerId>0)
			helper.addWhereCondition("o.saler.id=?", salerId);
		
		helper.addOrderByProperty("o.id", false);
		PageBean<Order> pageBean = orderService.queryOrder(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("orderManagerHelper", helper);
		/*driverId=0;
		customerOrganizationName=null;
		planBeginDate=null;
		planEndDate=null;
		status=null;*/
		return "list";
	}
	
	public String getCustomerOrganizationName() {
		return customerOrganizationName;
	}

	public void setCustomerOrganizationName(String customerOrganizationName) {
		this.customerOrganizationName = customerOrganizationName;
	}
	
	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}
	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}

	public Date getPlanBeginDate() {
		return planBeginDate;
	}

	public void setPlanBeginDate(Date planBeginDate) {
		this.planBeginDate = planBeginDate;
	}

	public Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getOrderId() {
		return orderId;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	
	
	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}	

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getBeforeTime() {
		return beforeTime;
	}

	public void setBeforeTime(String beforeTime) {
		this.beforeTime = beforeTime;
	}

	public String getAfterTime() {
		return afterTime;
	}

	public void setAfterTime(String afterTime) {
		this.afterTime = afterTime;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String cancelReason) {
		this.reason = cancelReason;
	}

	public String getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public long getCarId() {
		return carId;
	}

	public void setCarId(long carId) {
		this.carId = carId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderMileString() {
		return orderMileString;
	}

	public void setOrderMileString(String orderMileString) {
		this.orderMileString = orderMileString;
	}

	public void setOrderMoneyString(String orderMoneyString) {
		this.orderMoneyString = orderMoneyString;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Date getPostponeDate() {
		return postponeDate;
	}

	public void setPostponeDate(Date postponeDate) {
		this.postponeDate = postponeDate;
	}

	public String getPostponeReason() {
		return postponeReason;
	}

	public void setPostponeReason(String postponeReason) {
		this.postponeReason = postponeReason;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getRescheduleReason() {
		return rescheduleReason;
	}

	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}
	
	public float getBeginMile() {
		return beginMile;
	}

	public void setBeginMile(float beginMile) {
		this.beginMile = beginMile;
	}

	public float getCustomerGetonMile() {
		return customerGetonMile;
	}

	public void setCustomerGetonMile(float customerGetonMile) {
		this.customerGetonMile = customerGetonMile;
	}

	public float getCustomerGetoffMile() {
		return customerGetoffMile;
	}

	public void setCustomerGetoffMile(float customerGetoffMile) {
		this.customerGetoffMile = customerGetoffMile;
	}

	public float getEndMile() {
		return endMile;
	}

	public void setEndMile(float endMile) {
		this.endMile = endMile;
	}

	public BigDecimal getRefuelMoney() {
		return refuelMoney;
	}

	public void setRefuelMoney(BigDecimal refuelMoney) {
		this.refuelMoney = refuelMoney;
	}

	public BigDecimal getWashingFee() {
		return washingFee;
	}

	public void setWashingFee(BigDecimal washingFee) {
		this.washingFee = washingFee;
	}

	public BigDecimal getParkingFee() {
		return parkingFee;
	}

	public void setParkingFee(BigDecimal parkingFee) {
		this.parkingFee = parkingFee;
	}

	public BigDecimal getToll() {
		return toll;
	}

	public void setToll(BigDecimal toll) {
		this.toll = toll;
	}

	public float getTotalChargeMile() {
		return totalChargeMile;
	}

	public void setTotalChargeMile(float totalChargeMile) {
		this.totalChargeMile = totalChargeMile;
	}

	public BigDecimal getRoomAndBoardFee() {
		return roomAndBoardFee;
	}

	public void setRoomAndBoardFee(BigDecimal roomAndBoardFee) {
		this.roomAndBoardFee = roomAndBoardFee;
	}

	public BigDecimal getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(BigDecimal otherFee) {
		this.otherFee = otherFee;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getActualMoney() {
		return actualMoney;
	}

	public void setActualMoney(BigDecimal actualMoney) {
		this.actualMoney = actualMoney;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public IteratorStatus getIteratorStatus() {
		return iteratorStatus;
	}

	public void setIteratorStatus(IteratorStatus iteratorStatus) {
		this.iteratorStatus = iteratorStatus;
	}

	public List<DayOrderDetail> getDayDetails() {
		return dayDetails;
	}

	public void setDayDetails(List<DayOrderDetail> dayDetails) {
		this.dayDetails = dayDetails;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public long getDriverId() {
		return driverId;
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

	public Date getPlanBeginDate1() {
		return planBeginDate1;
	}

	public void setPlanBeginDate1(Date planBeginDate1) {
		this.planBeginDate1 = planBeginDate1;
	}

	public Date getPlanBeginDate2() {
		return planBeginDate2;
	}

	public void setPlanBeginDate2(Date planBeginDate2) {
		this.planBeginDate2 = planBeginDate2;
	}

	public Date getPlanEndDate1() {
		return planEndDate1;
	}

	public void setPlanEndDate1(Date planEndDate1) {
		this.planEndDate1 = planEndDate1;
	}

	public Date getPlanEndDate2() {
		return planEndDate2;
	}

	public void setPlanEndDate2(Date planEndDate2) {
		this.planEndDate2 = planEndDate2;
	}

	public Date getActualBeginDate1() {
		return actualBeginDate1;
	}

	public void setActualBeginDate1(Date actualBeginDate1) {
		this.actualBeginDate1 = actualBeginDate1;
	}

	public Date getActualBeginDate2() {
		return actualBeginDate2;
	}

	public void setActualBeginDate2(Date actualBeginDate2) {
		this.actualBeginDate2 = actualBeginDate2;
	}

	public Date getActualEndDate1() {
		return actualEndDate1;
	}

	public void setActualEndDate1(Date actualEndDate1) {
		this.actualEndDate1 = actualEndDate1;
	}

	public Date getActualEndDate2() {
		return actualEndDate2;
	}

	public void setActualEndDate2(Date actualEndDate2) {
		this.actualEndDate2 = actualEndDate2;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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

	public BigDecimal getOrderMoney1() {
		return orderMoney1;
	}

	public void setOrderMoney1(BigDecimal orderMoney1) {
		this.orderMoney1 = orderMoney1;
	}

	public BigDecimal getOrderMoney2() {
		return orderMoney2;
	}

	public void setOrderMoney2(BigDecimal orderMoney2) {
		this.orderMoney2 = orderMoney2;
	}

	public BigDecimal getActualMoney1() {
		return actualMoney1;
	}

	public void setActualMoney1(BigDecimal actualMoney1) {
		this.actualMoney1 = actualMoney1;
	}

	public BigDecimal getActualMoney2() {
		return actualMoney2;
	}

	public void setActualMoney2(BigDecimal actualMoney2) {
		this.actualMoney2 = actualMoney2;
	}

	public Date getQueueTime1() {
		return queueTime1;
	}

	public void setQueueTime1(Date queueTime1) {
		this.queueTime1 = queueTime1;
	}

	public Date getQueueTime2() {
		return queueTime2;
	}

	public void setQueueTime2(Date queueTime2) {
		this.queueTime2 = queueTime2;
	}

	public Date getScheduleTime1() {
		return scheduleTime1;
	}

	public void setScheduleTime1(Date scheduleTime1) {
		this.scheduleTime1 = scheduleTime1;
	}

	public Date getScheduleTime2() {
		return scheduleTime2;
	}

	public void setScheduleTime2(Date scheduleTime2) {
		this.scheduleTime2 = scheduleTime2;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public long getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(long schedulerId) {
		this.schedulerId = schedulerId;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getCallForOtherLabel() {
		return callForOtherLabel;
	}

	public void setCallForOtherLabel(String callForOtherLabel) {
		this.callForOtherLabel = callForOtherLabel;
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

	public String getCallForOtherSendSMSLabel() {
		return callForOtherSendSMSLabel;
	}

	public void setCallForOtherSendSMSLabel(String callForOtherSendSMSLabel) {
		this.callForOtherSendSMSLabel = callForOtherSendSMSLabel;
	}

	public long getSalerId() {
		return salerId;
	}

	public void setSalerId(long salerId) {
		this.salerId = salerId;
	}

	public BigDecimal getRefuelMoney1() {
		return refuelMoney1;
	}

	public void setRefuelMoney1(BigDecimal refuelMoney1) {
		this.refuelMoney1 = refuelMoney1;
	}

	public BigDecimal getRefuelMoney2() {
		return refuelMoney2;
	}

	public void setRefuelMoney2(BigDecimal refuelMoney2) {
		this.refuelMoney2 = refuelMoney2;
	}

	public BigDecimal getWashingFee1() {
		return washingFee1;
	}

	public void setWashingFee1(BigDecimal washingFee1) {
		this.washingFee1 = washingFee1;
	}

	public BigDecimal getWashingFee2() {
		return washingFee2;
	}

	public void setWashingFee2(BigDecimal washingFee2) {
		this.washingFee2 = washingFee2;
	}

	public BigDecimal getParkingFee1() {
		return parkingFee1;
	}

	public void setParkingFee1(BigDecimal parkingFee1) {
		this.parkingFee1 = parkingFee1;
	}

	public BigDecimal getParkingFee2() {
		return parkingFee2;
	}

	public void setParkingFee2(BigDecimal parkingFee2) {
		this.parkingFee2 = parkingFee2;
	}

	public BigDecimal getToll1() {
		return toll1;
	}

	public void setToll1(BigDecimal toll1) {
		this.toll1 = toll1;
	}

	public BigDecimal getToll2() {
		return toll2;
	}

	public void setToll2(BigDecimal toll2) {
		this.toll2 = toll2;
	}

	public BigDecimal getRoomAndBoardFee1() {
		return roomAndBoardFee1;
	}

	public void setRoomAndBoardFee1(BigDecimal roomAndBoardFee1) {
		this.roomAndBoardFee1 = roomAndBoardFee1;
	}

	public BigDecimal getRoomAndBoardFee2() {
		return roomAndBoardFee2;
	}

	public void setRoomAndBoardFee2(BigDecimal roomAndBoardFee2) {
		this.roomAndBoardFee2 = roomAndBoardFee2;
	}

	public BigDecimal getOtherFee1() {
		return otherFee1;
	}

	public void setOtherFee1(BigDecimal otherFee1) {
		this.otherFee1 = otherFee1;
	}

	public BigDecimal getOtherFee2() {
		return otherFee2;
	}

	public void setOtherFee2(BigDecimal otherFee2) {
		this.otherFee2 = otherFee2;
	}
}

class AbstractTrackVO{
	private String abstractTime;
	private String abstractAddress;
	public String getAbstractTime() {
		return abstractTime;
	}
	public void setAbstractTime(String abstractTime) {
		this.abstractTime = abstractTime;
	}
	public String getAbstractAddress() {
		return abstractAddress;
	}
	public void setAbstractAddress(String abstractAddress) {
		this.abstractAddress = abstractAddress;
	}	
}

class carJson {
	String id;
	String name;// 这里存放车牌号码

	/**
	 * Setter method for property <tt>id</tt>.
	 * 
	 * @param id
	 *            value to be assigned to property id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Getter method for property <tt>id</tt>.
	 * 
	 * @return property value of id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter method for property <tt>name</tt>.
	 * 
	 * @param name
	 *            value to be assigned to property name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method for property <tt>name</tt>.
	 * 
	 * @return property value of name
	 */
	public String getName() {
		return name;
	}
}
