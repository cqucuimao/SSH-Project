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

import com.alibaba.fastjson.JSONArray;
import com.mysql.fabric.xmlrpc.base.Struct;
import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.DriverActionVO;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderOperationRecord;
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
	private long imageId;
	private long driverId;
	private Date planBeginDate;
	private Date planEndDate;
	private String status;
	private long orderId;
	private String actionId;
	private Date actionTime;
	private String time;
	private String inputTime;
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
	private BigDecimal washingFee;
	private BigDecimal parkingFee;
	private BigDecimal toll;
	private float totalChargeMile;
	private BigDecimal roomAndBoardFee;
	private BigDecimal otherFee;
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
		System.out.println("in print, orderId="+orderId);
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
		System.out.println("in print, orderId="+orderId);
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
		System.out.println("IDDDDD="+orderId);
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
		System.out.println("ID="+orderId);
		System.out.println(ActionContext.getContext().getValueStack().peek().getClass().toString());
		System.out.println("orderForView="+ActionContext.getContext().get("orderForView"));
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);
			
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			System.out.println("List="+driverActionVOList);
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		return "operate";
	}
	
	public String getStatusLabel(){
		DriverActionVO vo=(DriverActionVO)ActionContext.getContext().getValueStack().peek();
		
		return vo.getStatus().getLabel();
	}
	
	//添加  接收订单操作
	public String addAcceptAction(){

		User user = (User)ActionContext.getContext().getSession().get("user");
		//System.out.println("date="+DateUtils.getYMDHMSString(date));
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);
			
			orderService.addAcceptAction(order, user, actionTime);
			
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			//System.out.println("List="+driverActionVOList.get(1).getStatus().getLabel());
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		
		return "operate";
	}
	
	//添加  开始订单操作
	public String addBeginAction(){

		User user = (User)ActionContext.getContext().getSession().get("user");
		//System.out.println("date="+DateUtils.getYMDHMSString(date));
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);
			
			orderService.addBeginAction(order, user, actionTime);
			
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			//System.out.println("List="+driverActionVOList.get(1).getStatus().getLabel());
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		
		return "operate";
	}
	
	//添加  客户上车操作
	public String addGetonAction(){

		User user = (User)ActionContext.getContext().getSession().get("user");
		//System.out.println("date="+DateUtils.getYMDHMSString(date));
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);
			
			orderService.addGetonAction(order, user, actionTime);
			
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			//System.out.println("List="+driverActionVOList.get(1).getStatus().getLabel());
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		
		return "operate";
	}
	
	//添加  客户下车操作
	public String addGetoffAction(){

		User user = (User)ActionContext.getContext().getSession().get("user");
		//System.out.println("date="+DateUtils.getYMDHMSString(date));
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);
			
			orderService.addGetoffAction(order, user, actionTime);
			
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			//System.out.println("List="+driverActionVOList.get(1).getStatus().getLabel());
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		
		return "operate";
	}
	
	//添加  结束订单操作
	public String addEndAction(){

		User user = (User)ActionContext.getContext().getSession().get("user");
		//System.out.println("date="+DateUtils.getYMDHMSString(date));
		if(orderId>0){
			Order order=orderService.getOrderById(orderId);
			ActionContext.getContext().getValueStack().push(order);
			
			orderService.addEndAction(order, user, actionTime);
			
			List<DriverActionVO> driverActionVOList = orderService.getDriverActions(order);
			//System.out.println("List="+driverActionVOList.get(1).getStatus().getLabel());
			ActionContext.getContext().put("driverActionVOList", driverActionVOList);
		}
		
		return "operate";
	}
	
	//删除司机操作
	public String deleteDriverAction() throws Exception{
		
		User user = (User)ActionContext.getContext().getSession().get("user");
		System.out.println("actionId="+actionId);
		
		orderService.deleteDriverAction(actionId, user);
		orderId = Long.parseLong(actionId.substring(0, 1));
		System.out.println("orderId="+orderId);
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
		ActionContext.getContext().getValueStack().push(time);
		ActionContext.getContext().getValueStack().push(beforeTime);
		ActionContext.getContext().getValueStack().push(afterTime);
		return "popupModify";
	}
	
	//修改时间
	public void modifyDriverActionTime() throws ParseException{
		System.out.println("++++");
		User user = (User)ActionContext.getContext().getSession().get("user");
		System.out.println("actionId="+actionId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(inputTime);
		System.out.println("date="+date);
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
			ActionContext.getContext().getValueStack().push(order);
		}
		return "reschedule";
	}
	
	public String rescheduleDo(){
		System.out.println("rescheduleDo");
		System.out.println("orderId="+orderId);
		System.out.println("plateNumber="+plateNumber);
		System.out.println("rescheduleReason="+rescheduleReason);
		Order order=orderService.getOrderById(orderId);
		Car car=carService.getCarByPlateNumber(plateNumber);
		ActionContext.getContext().getValueStack().push(order);
		int result=0;
		//TODO 还应该多传一个driver对象
		//result=orderService.orderReschedule(order, car, (User)ActionContext.getContext().getSession().get("user"), rescheduleReason);
		if(result==1){
			addFieldError("rescheduleError", "车辆在此时间段不可用");
			return "reschedule";
		}else if(result==2){
			addFieldError("rescheduleError", "此订单当前状态不支持重新调度操作");
			return "reschedule";
		}
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

	public String getChargeModeString() {
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return order.getChargeMode().toString();
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
	
	public String getStatusString(){
		Order order=(Order)ActionContext.getContext().getValueStack().peek();
		return getOrderStatusString(order.getStatus());
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
	
	

	public String getInputTime() {
		return inputTime;
	}

	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
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
