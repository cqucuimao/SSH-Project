package com.yuqincar.action.app;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.common.VerificationCode;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderSourceEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.order.OtherPassenger;
import com.yuqincar.domain.privilege.UserAPPDeviceTypeEnum;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.common.DiskFileService;
import com.yuqincar.service.common.VerificationCodeService;
import com.yuqincar.service.customer.CustomerService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

/**
 * 用于处理客户下订单APP中所有的请求。
 *
 */
@Controller
@Scope("prototype")
public class OrderAppAction extends BaseAction{
	
	private String keyword;
	
	private String phoneNumber;
	
	private String customerName;
	
	private String customerOrganizationName;
	
	private String validationCode;
	
	private int carServiceTypeId;
	
	private String chargeMode;
	
	private long beginTime;
	
	private long endTime;
	
	private String fromAddress;
	
	private String toAddress;
	
	private String address;
	
	private String callForOther;
	
	private String callForOtherName;
	
	private String callForOtherPhoneNumber;
	
	private String callForOtherSendSMS;
	
	private String newCustomerName;
	
	private String newCustomerOrganizationName;
	
	private String newGender;
	
	private String orderStatus;
	
	private int pageNum;
	
	private int orderId;
	
	private int addressIndex;
	
	private int passengerIndex;
	
	private int grade;
	
	private String deviceType;
	
	private String deviceToken;
	
	@Autowired
	private CustomerOrganizationService customerOrganizationService;
	
	@Autowired
	private VerificationCodeService verificationCodeService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private DiskFileService diskFileService;
	
	public void searchCustomerOrganization(){
		PageBean<CustomerOrganization> pageBean=customerOrganizationService.queryCustomerOrganizationByKeyword(keyword);
		List<String> names=new ArrayList<String>(pageBean.getRecordList().size());
		for(CustomerOrganization co:pageBean.getRecordList())
			names.add(co.getName());
		String json = JSON.toJSONString(names);
		writeJson(json);		
	}
	
	private boolean checkPrivilege(){
		if(phoneNumber==null || phoneNumber.length()==0 || !StringUtils.isNumeric(phoneNumber) ||
				validationCode==null || validationCode.length()==0)
			return false;
		
		VerificationCode sendCode = verificationCodeService.getByPhoneNumber(phoneNumber);
		if(sendCode ==null ||  !sendCode.getCode().equals(validationCode))
			return false;
		else
			return true;
	}
	
	public void login(){		
		if(checkPrivilege())
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
	}
	
	public void registCustomerInfo(){
		System.out.println("in registCustomerInfo");
		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		List<BaseEntity> list=orderService.dealCCP(customerOrganizationName, customerName, phoneNumber);
		if(list.get(0)==null && list.get(1)==null)
			writeJson("{\"status\":false}");
		
		writeJson("{\"status\":true}");			
	}
	
	public void getAllCarServiceType(){
		System.out.println("in getAllCarServiceType");
		System.out.println("validationCode="+validationCode);
		System.out.println("phoneNumber="+phoneNumber);
		if(!checkPrivilege()){
			System.out.println("unauthorized");
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		System.out.println("1");
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);
		List<CarServiceTypeVO> list=new LinkedList<CarServiceTypeVO>();
		for(CarServiceType cst:carService.getAllCarServiceType()){
			CarServiceTypeVO cstvo=new CarServiceTypeVO();
			cstvo.setId(cst.getId().intValue());
			cstvo.setName(cst.getTitle());
			System.out.println(customer);
			System.out.println(customer.getCustomerOrganization());
			System.out.println(customer.getCustomerOrganization().getPriceTable());
			System.out.println(customer.getCustomerOrganization().getPriceTable().
					getCarServiceType());
			System.out.println(customer.getCustomerOrganization().getPriceTable().
					getCarServiceType().get(cst));
			System.out.println(customer.getCustomerOrganization().getPriceTable().
					getCarServiceType().get(cst).toPriceDescription());
			cstvo.setPriceDescription(customer.getCustomerOrganization().getPriceTable().
					getCarServiceType().get(cst).toPriceDescription());
			list.add(cstvo);
		}
		String json = JSON.toJSONString(list);
		System.out.println("json");
		writeJson(json);
	}
	
	public void getCarServiceTypeImage(){
		//由于接口废除，注释下面的代码
		/*
		if(carServiceTypeId==0){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		CarServiceType cst=carService.getCarServiceTypeById(carServiceTypeId);
		diskFileService.downloadDiskFile(cst.getPicture(), response);
		*/
	}
	
	public void getAllHistoryPassengers(){
		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);
		List<OtherPassengerVO> opvoList=new ArrayList<OtherPassengerVO>(customer.getOtherPassengers().size());
		for(int i=0;i<customer.getOtherPassengers().size();i++){
			if(!customer.getOtherPassengers().get(i).isDeleted()){
				OtherPassengerVO opvo=new OtherPassengerVO();
				opvo.setIndex(customer.getOtherPassengers().get(i).getId().intValue());
				opvo.setName(customer.getOtherPassengers().get(i).getName());
				opvo.setPhoneNumber(customer.getOtherPassengers().get(i).getPhoneNumber());
				opvoList.add(opvo);
			}
		}
		String json = JSON.toJSONString(opvoList);
		writeJson(json);
	}
	
	public void submitOrder(){
		if(carServiceTypeId==0 || chargeMode==null || chargeMode.length()==0 ||
				(!chargeMode.equals("DAY") && !chargeMode.equals("MILE") &&!chargeMode.equals("PLANE")) ||
				beginTime==0 || (chargeMode.equals("DAY") && endTime==0) ||
				fromAddress==null || fromAddress.length()==0 ||
				((chargeMode.equals("MILE") || chargeMode.equals("PLANE")) && (toAddress==null || toAddress.length()==0))){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		if(callForOther==null || callForOther.length()==0 ||
				(callForOther.equals("true") && (callForOtherName==null || callForOtherName.length()==0 ||
				callForOtherPhoneNumber==null || callForOtherPhoneNumber.length()==0 ||
				callForOtherSendSMS==null || callForOtherSendSMS.length()==0))){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		Order order = new Order();
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);
		ChargeModeEnum chargeModeEnum = null;
		order.setCustomerOrganization(customer.getCustomerOrganization());
		order.setCustomer(customer);
		
		if("true".equals(callForOther)){
			order.setCallForOther(true);
			order.setOtherPassengerName(callForOtherName);
			order.setOtherPhoneNumber(callForOtherPhoneNumber);
			if("true".equals(callForOtherSendSMS))
				order.setCallForOtherSendSMS(true);
			else
				order.setCallForOtherSendSMS(false);
		}else
			order.setCallForOther(false);
		
		if(chargeMode.equals("DAY"))
			chargeModeEnum = ChargeModeEnum.DAY;
		else if(chargeMode.equals("MILE"))
			chargeModeEnum = ChargeModeEnum.MILE;
		else if(chargeMode.equals("PLANE"))
			chargeModeEnum = ChargeModeEnum.PLANE;		
		order.setChargeMode(chargeModeEnum);
		
		order.setPlanBeginDate(new Date(beginTime));
		if (order.getChargeMode()==ChargeModeEnum.DAY)
			order.setPlanEndDate(new Date(endTime));
		
		order.setFromAddress(fromAddress);
		if(order.getChargeMode()==ChargeModeEnum.MILE || order.getChargeMode()==ChargeModeEnum.PLANE)
			order.setToAddress(toAddress);
		
		order.setServiceType(carService.getCarServiceTypeById(carServiceTypeId));
		order.setPhone(phoneNumber);
		order.setOrderMoney(new BigDecimal(0));
		order.setStatus(OrderStatusEnum.INQUEUE);
		order.setMemo("");		
		order.setOrderSource(OrderSourceEnum.APP);
		
		orderService.EnQueue(order, null,0);
		
		writeJson("{\"status\":true}");
	}
	
	public void getCustomerInfo(){
		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);
		CustomerInfoVO civo=new CustomerInfoVO();
		civo.setName(customer.getName());
		civo.setOrganizationName(customer.getCustomerOrganization().getName());
		civo.setGender(customer.isGender() ? "male" : "female");

		String json = JSON.toJSONString(civo);
		writeJson(json);
	}
	
	public void updateCustomerInfo(){
		if(newCustomerName==null || newCustomerName.length()==0 ||
				newCustomerOrganizationName==null || newCustomerOrganizationName.length()==0 ||
				newGender==null || newGender.length()==0){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}

		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		List<BaseEntity> list=orderService.dealCCP(newCustomerOrganizationName, newCustomerName, phoneNumber);
		Customer customer=(Customer)list.get(1);
		customer.setCustomerOrganization((CustomerOrganization)list.get(0));
		customer.setGender(newGender.equals("male"));
		customerService.updateCustomer(customer);
		
		writeJson("{\"status\":true}");
	}
	
	public void getOrdersByStatus(){
		if(orderStatus==null || orderStatus.length()==0 || pageNum==0 ||
				(!orderStatus.equals("WAIT") && !orderStatus.equals("BEGIN") && !orderStatus.equals("END") && !orderStatus.equals("CANCELLED"))){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}

		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		OrdersVO ordersVO=new OrdersVO();
		PageBean<Order> pageBean=null;
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);
		
		QueryHelper helper=new QueryHelper("order_","o");
		if(orderStatus.equals("WAIT")){			
			helper.addWhereCondition("o.customer=? and (o.status=? or o.status=? or o.status=?)", 
					customer,OrderStatusEnum.INQUEUE,OrderStatusEnum.SCHEDULED,OrderStatusEnum.ACCEPTED);
		}else if(orderStatus.equals("BEGIN")){
			helper.addWhereCondition("o.customer=? and (o.status=? or o.status=? or o.status=?)", customer, OrderStatusEnum.BEGIN, OrderStatusEnum.GETON, OrderStatusEnum.GETOFF);
		}else if(orderStatus.equals("END")){
			helper.addWhereCondition("o.customer=? and o.status=?", customer, OrderStatusEnum.END);
		}else if(orderStatus.equals("CANCELLED")){
			helper.addWhereCondition("o.customer=? and o.status=?", customer, OrderStatusEnum.CANCELLED);
		}
		pageBean=orderService.queryOrder(pageNum, helper);
		
		ordersVO.setCurrentPage(pageBean.getCurrentPage());
		ordersVO.setPageCount(pageBean.getPageCount());
		ordersVO.setPageSize(pageBean.getPageSize());
		ordersVO.setRecordCount(pageBean.getRecordCount());
		List<OrderVO> orderList=new ArrayList<OrderVO>(pageBean.getRecordList().size());
		for(Order order:pageBean.getRecordList()){
			OrderVO ovo=new OrderVO();
			ovo.setId(order.getId().intValue());
			
			StringBuffer time=new StringBuffer();
			if(order.getActualBeginDate()!=null)
				time.append(DateUtils.getYMDHMString(order.getActualBeginDate()));
			else
				time.append(DateUtils.getYMDHMString(order.getPlanBeginDate()));
			if(order.getActualEndDate()!=null)
				time.append(" 到 ").append(DateUtils.getYMDHMString(order.getActualEndDate()));
			else if(order.getPlanEndDate()!=null)
				time.append(" 到 ").append(DateUtils.getYMDHMString(order.getPlanEndDate()));
			ovo.setTime(time.toString());
			
			ovo.setFromAddress(order.getFromAddress());
			if(order.getChargeMode()==ChargeModeEnum.MILE || order.getChargeMode()==ChargeModeEnum.PLANE)
				ovo.setToAddress(order.getToAddress());
			
			if(order.isCallForOther())
				ovo.setOtherPassenger(order.getOtherPassengerName()+"（"+order.getOtherPhoneNumber()+"）");
			
			orderList.add(ovo);
		}
		ordersVO.setRecordList(orderList);
		String json = JSON.toJSONString(ordersVO);
		writeJson(json);
	}
	
	public void getOrdersInfo(){
		if(orderId<=0){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}

		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		Order order=orderService.getOrderById(orderId);
		if(order==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		OrderDetailVO odvo=new OrderDetailVO();
		
		odvo.setSN(order.getSn());
		
		StringBuffer passenger=new StringBuffer();
		passenger.append(order.getCustomer().getName()).append("（").append(order.getPhone()).append("）");
		if(order.isCallForOther())
			passenger.append("，实际乘车人：").append(order.getOtherPassengerName()).append("（").append(order.getOtherPhoneNumber()).append("）");
		odvo.setPassenger(passenger.toString());
		
		StringBuffer time=new StringBuffer();
		if(order.getActualBeginDate()!=null)
			time.append(DateUtils.getYMDHMString(order.getActualBeginDate()));
		else
			time.append(DateUtils.getYMDHMString(order.getPlanBeginDate()));
		if(order.getActualEndDate()!=null)
			time.append(" 到 ").append(DateUtils.getYMDHMString(order.getActualEndDate()));
		else if(order.getPlanEndDate()!=null)
			time.append(" 到 ").append(DateUtils.getYMDHMString(order.getPlanEndDate()));
		odvo.setTime(time.toString());
		
		odvo.setFromAddress(order.getFromAddress());
		if(order.getChargeMode()==ChargeModeEnum.MILE || order.getChargeMode()==ChargeModeEnum.PLANE)
			odvo.setToAddress(order.getToAddress());
		
		odvo.setCarServiceType(order.getServiceType().getTitle());
		
		switch(order.getStatus()){
		case INQUEUE:
		case SCHEDULED:
		case ACCEPTED:
			odvo.setOrderStatus("等待服务");
			break;
		case BEGIN:
		case GETON:
		case GETOFF:
			odvo.setOrderStatus("服务中");
			break;
		case END:
		case PAYED:
			odvo.setOrderStatus("服务结束");
			break;
		case CANCELLED:
			odvo.setOrderStatus("服务取消");
			break;
		}
		
		if(order.getDriver()!=null)
			odvo.setDriver(order.getDriver().getName()+"（"+order.getDriver().getPhoneNumber()+"）");
		
		String json = JSON.toJSONString(odvo);
		writeJson(json);
	}
	
	public void getAllFavoriateAddress(){
		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);
		String json = JSON.toJSONString(customer.getAddresses());
		writeJson(json);
	}
	
	public void deleteHistoryAddress(){
		if(address==null || address.length()==0){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		
		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);
		customer.getAddresses().remove(address);
		customerService.updateCustomer(customer);

		writeJson("{\"status\":true}");
	}
	
	public void deleteHistoryPassenger(){
		if(passengerIndex==0){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		
		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);
		for(OtherPassenger otherPassenger:customer.getOtherPassengers()){
			if(otherPassenger.getId()==passengerIndex && !otherPassenger.isDeleted())
				customerService.deleteOtherPassenger(customer, otherPassenger);
		}

		writeJson("{\"status\":true}");
	}
	
	public void needRegistUserInfo(){
		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);
		if(customer==null)
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");		
	}
	
	public void isOrderEvaluated(){
		if(orderId<=0){
			writeJson("{\"status\":\"badParameter\"}");
			return; 
		}
		
		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		Order order=orderService.getOrderById(orderId);
		if(order==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		if(order.isEvaluated())
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
	}
	
	public void getEvaluateGrade(){
		if(orderId<=0){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		
		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);		
		Order order=orderService.getOrderById(orderId);

		if(order==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		
		if(!order.getCustomer().equals(customer)){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		writeJson("{\"grade\":"+order.getGrade()+"}");
	}
	
	public void evaluateOrder(){
		if(orderId<=0 || grade<0 || grade>5){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}

		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);
		Order order=orderService.getOrderById(orderId);
		if(order==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		
		if(!order.getCustomer().equals(customer)){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		
		order.setGrade(grade);
		order.setEvaluated(true);
		orderService.update(order);
		
		writeJson("{\"status\":true}");
	}
	
	public void updateDeviceToken(){
		if(deviceType==null || deviceType.length()==0 || deviceToken==null || deviceToken.length()==0){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}

		if(!checkPrivilege()){
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		Customer customer=customerService.getCustomerByPhoneNumber(phoneNumber);
		if(deviceType.equals("ios"))
			customer.setAppDeviceType(UserAPPDeviceTypeEnum.IOS);
		else if(deviceType.equals("android"))
			customer.setAppDeviceType(UserAPPDeviceTypeEnum.ANDROID);
		customer.setAppDeviceToken(deviceToken);
		customerService.updateCustomer(customer);
		
		this.writeJson("{\"status\":true}");
	}
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) throws UnsupportedEncodingException {
		this.keyword = URLDecoder.decode(keyword,"UTF-8");
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) throws UnsupportedEncodingException{
		this.customerName = URLDecoder.decode(customerName,"UTF-8");
	}

	public String getCustomerOrganizationName() {
		return customerOrganizationName;
	}

	public void setCustomerOrganizationName(String customerOrganizationName) throws UnsupportedEncodingException{
		this.customerOrganizationName = URLDecoder.decode(customerOrganizationName,"UTF-8");
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}
	
	public int getCarServiceTypeId() {
		return carServiceTypeId;
	}

	public void setCarServiceTypeId(int carServiceTypeId) {
		this.carServiceTypeId = carServiceTypeId;
	}
	
	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getCallForOther() {
		return callForOther;
	}

	public void setCallForOther(String callForOther) {
		this.callForOther = callForOther;
	}

	public String getCallForOtherName() {
		return callForOtherName;
	}

	public void setCallForOtherName(String callForOtherName) throws UnsupportedEncodingException{
		this.callForOtherName = URLDecoder.decode(callForOtherName,"UTF-8");
	}

	public String getCallForOtherPhoneNumber() {
		return callForOtherPhoneNumber;
	}

	public void setCallForOtherPhoneNumber(String callForOtherPhoneNumber) {
		this.callForOtherPhoneNumber = callForOtherPhoneNumber;
	}

	public String getCallForOtherSendSMS() {
		return callForOtherSendSMS;
	}

	public void setCallForOtherSendSMS(String callForOtherSendSMS) {
		this.callForOtherSendSMS = callForOtherSendSMS;
	}

	public String getNewCustomerName() {
		return newCustomerName;
	}

	public void setNewCustomerName(String newCustomerName) throws UnsupportedEncodingException{
		this.newCustomerName = URLDecoder.decode(newCustomerName,"UTF-8");
	}

	public String getNewCustomerOrganizationName() {
		return newCustomerOrganizationName;
	}

	public void setNewCustomerOrganizationName(String newCustomerOrganizationName) throws UnsupportedEncodingException{
		this.newCustomerOrganizationName = URLDecoder.decode(newCustomerOrganizationName,"UTF-8");
	}

	public String getNewGender() {
		return newGender;
	}

	public void setNewGender(String newGender) {
		this.newGender = newGender;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}	

	public int getAddressIndex() {
		return addressIndex;
	}

	public void setAddressIndex(int addressIndex) {
		this.addressIndex = addressIndex;
	}

	public int getPassengerIndex() {
		return passengerIndex;
	}

	public void setPassengerIndex(int passengerIndex) {
		this.passengerIndex = passengerIndex;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) throws UnsupportedEncodingException{
		this.address = URLDecoder.decode(address,"UTF-8");
	}

	public String getChargeMode() {
		return chargeMode;
	}

	public void setChargeMode(String chargeMode) {
		this.chargeMode = chargeMode;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) throws UnsupportedEncodingException{
		this.fromAddress = URLDecoder.decode(fromAddress,"UTF-8");
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) throws UnsupportedEncodingException {
		this.toAddress = URLDecoder.decode(toAddress,"UTF-8");
	}

	class CarServiceTypeVO{
		private int id;
		private String name;
		private String priceDescription;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPriceDescription() {
			return priceDescription;
		}
		public void setPriceDescription(String priceDescription) {
			this.priceDescription = priceDescription;
		}	
	}
	
	class OtherPassengerVO{
		private int index;
		private String name;
		private String phoneNumber;
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
	}
	
	class OrderVO{
		private int id;
		private String time;
		private String fromAddress;
		private String toAddress;
		private String otherPassenger;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
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
		public String getOtherPassenger() {
			return otherPassenger;
		}
		public void setOtherPassenger(String otherPassenger) {
			this.otherPassenger = otherPassenger;
		}		
	}
	
	class OrdersVO{
		private int currentPage;
		private int pageCount;
		private int pageSize;
		private int recordCount;
		private List<OrderVO> recordList;
		public int getCurrentPage() {
			return currentPage;
		}
		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}
		public int getPageCount() {
			return pageCount;
		}
		public void setPageCount(int pageCount) {
			this.pageCount = pageCount;
		}
		public int getPageSize() {
			return pageSize;
		}
		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
		public int getRecordCount() {
			return recordCount;
		}
		public void setRecordCount(int recordCount) {
			this.recordCount = recordCount;
		}
		public List<OrderVO> getRecordList() {
			return recordList;
		}
		public void setRecordList(List<OrderVO> recordList) {
			this.recordList = recordList;
		}		
	}
	
	class OrderDetailVO{
		private String SN;
		private String passenger;
		private String time;
		private String fromAddress;
		private String toAddress;
		private String carServiceType;
		private String orderStatus;
		private String driver;
		public String getSN() {
			return SN;
		}
		public void setSN(String sN) {
			SN = sN;
		}
		public String getPassenger() {
			return passenger;
		}
		public void setPassenger(String passenger) {
			this.passenger = passenger;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
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
		public String getCarServiceType() {
			return carServiceType;
		}
		public void setCarServiceType(String carServiceType) {
			this.carServiceType = carServiceType;
		}
		public String getOrderStatus() {
			return orderStatus;
		}
		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}
		public String getDriver() {
			return driver;
		}
		public void setDriver(String driver) {
			this.driver = driver;
		}		
	}
	
	class CustomerInfoVO{
		private String name;
		private String organizationName;
		private String gender;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getOrganizationName() {
			return organizationName;
		}
		public void setOrganizationName(String organizationName) {
			this.organizationName = organizationName;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		
	}
}