package com.yuqincar.action.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.Preparable;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRepair;
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
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.customer.CustomerService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;
/**
 * 用于调度APP中所有的请求。
 *
 */
@Controller
@Scope("prototype")
public class ScheduleAppAction  extends BaseAction implements Preparable {
	
	private User user;
	
	private String keyword;
	
	private String customerOrganization;
	
	private String customer;
	
	private String driver;
	
	private String plateNumber;
	
	private String chargeMode;
	
	private long fromDate;
	
	private long toDate;
	
	private long carId;
	
	private long date;
	
	private long carServiceTypeId;
	
	private long planBeginDate;
	
	private long planEndDate;
	
	private String fromAddressDescription;
	
	private String fromAddressDetail;
	
	private double fromAddressLongitude;
	
	private double fromAddressLatitude;
	
	private String toAddressDescription;
	
	private String toAddressDetail;
	
	private double toAddressLongitude;
	
	private double toAddressLatitude;
	
	private String phoneNumber;
	
	private int passengerNumber;
	
	private int copyOrderCount;
	
	private String memo;
	
	private long orderId;
	
	private int pageNum;
	
	private String orderSource;
	
	private long newPlanEndDate;
	
	private String reason;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerOrganizationService customerOrganizationService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CarService carService;
	
	public void prepare() throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("pwd");
		long companyId=Long.valueOf(request.getParameter("companyId"));
		user = userService.getByLoginNameAndMD5Password(username, password,companyId);
		//TODO 应该限制没有权限的用户来使用。
		//if(user!=null && !user.hasPrivilegeByUrl("/schedule"))
		//	user=null;
	}
	
	public void login(){
		if(user==null)
			writeJson("{\"status\":false}");
		else
			writeJson("{\"status\":true}");
	}
	
	public void queryCustomerOrganization(){
		if(user==null){
			writeJson("{\"status\":false}");
			return;
		}
		PageBean<CustomerOrganization> pageBean=customerOrganizationService.queryCustomerOrganizationByKeyword(keyword);
		List<String> names=new ArrayList<String>(pageBean.getRecordList().size());
		for(CustomerOrganization co:pageBean.getRecordList())
			names.add(co.getName());
		String json = JSON.toJSONString(names);
		writeJson(json);
	}
	
	public void queryCustomer(){
		if(user==null || customerOrganization==null || customerOrganization.length()==0){
			writeJson("{\"status\":false}");
			return;
		}
		QueryHelper helper = new QueryHelper(Customer.class, "c");
		helper.addWhereCondition(
				"c.customerOrganization.name=? and c.name like ?",
				customerOrganization, "%" + keyword + "%");
		List<String> names = new ArrayList<String>();
		for (Customer customer : customerService.queryCustomer(1, helper)
				.getRecordList()) {
			names.add(customer.getName());
		}
		String json = JSON.toJSONString(names);
		writeJson(json);
	}
	
	public void getAllPhoneNumbers(){
		if(user==null || customerOrganization==null || customerOrganization.length()==0
				|| customer==null || customer.length()==0){
			writeJson("{\"status\":false}");
			return;
		}
		QueryHelper helper = new QueryHelper(Customer.class, "c");
		helper.addWhereCondition("c.customerOrganization.name=? and c.name=?",
				customerOrganization, customer);
		List<Customer> customerList = (List<Customer>) customerService
				.queryCustomer(1, helper).getRecordList();
		List<String> list = new ArrayList<String>();
		for (Customer customer : customerList) {
			for (String phoneNumber : customer.getPhones()) {
				list.add(phoneNumber);
			}
		}
		String json = JSON.toJSONString(list);
		writeJson(json);
	}
	
	public void getAllCarServiceType(){
		if(user==null){
			writeJson("{\"status\":false}");
			return;
		}
		List<CarServiceType> carServiceTypeList=orderService.getAllCarServiceType();
		List<CarServiceTypeVO> voList=new ArrayList<CarServiceTypeVO>(carServiceTypeList.size());
		for(CarServiceType cst:carServiceTypeList){
			CarServiceTypeVO vo=new CarServiceTypeVO();
			vo.setId(cst.getId());
			vo.setTitle(cst.getTitle());
			voList.add(vo);
		}
		String json = JSON.toJSONString(voList);
		writeJson(json);
	}
	
	private List<CarTaskVO> getCarTaskVOList(List<Car> cars,Date fromDate, Date toDate){
		List<CarTaskVO> carList=new ArrayList<CarTaskVO>(cars.size());
		for(Car car: cars){
			CarTaskVO ctvo=new CarTaskVO();
			ctvo.setCarId(car.getId());
			ctvo.setDriver(car.getDriver().getName());
			ctvo.setPlateNumber(car.getPlateNumber());
			List<List<Order>> carUseInfo = orderService.getCarTask(car,
					fromDate, toDate);
			List<DayTaskVO> dayTaskList=new ArrayList<DayTaskVO>(carUseInfo.size());
			int i = 0;
			for(List<Order> daytask:carUseInfo){
				DayTaskVO dtvo=new DayTaskVO();
				dtvo.setDay(DateUtils.getOffsetDate(fromDate, i++).getTime());
				if(daytask.get(0)!=null)
					dtvo.setTask("任务");
				else
					dtvo.setTask("空闲");
				dayTaskList.add(dtvo);
			}
			ctvo.setTasks(dayTaskList);
			carList.add(ctvo);
		}
		return carList;
	}
	
	public void queryCarTasks(){
		if(user==null || ((driver==null || driver.length()==0) && (plateNumber==null || plateNumber.length()==0))
				|| chargeMode==null || chargeMode.length()==0  || (!chargeMode.equals("mile") && !chargeMode.equals("day")) 
				|| fromDate<=0 || (!chargeMode.equals("mile") && toDate<=0)){
			writeJson("{\"status\":false}");
			return;
		}
		QueryHelper helper = new QueryHelper(Car.class, "c");
		if (driver!=null && driver.length()>0 && plateNumber!=null && plateNumber.length()>0)
			helper.addWhereCondition("c.status=? and c.driver.name like ? and c.plateNumber like ?",
					CarStatusEnum.NORMAL,"%"+driver+"%", "%"+plateNumber+"%");
		else if (driver!=null && driver.length()>0)
			helper.addWhereCondition("c.status=? and c.driver.name like ?", CarStatusEnum.NORMAL,"%"+driver+"%");
		else
			helper.addWhereCondition("c.status=? and c.plateNumber like ?", CarStatusEnum.NORMAL,"%"+plateNumber+"%");
		List<Car> cars = carService.queryCar(1, helper).getRecordList();
		
		Date _fromDate = new Date();
		_fromDate.setTime(fromDate);
		_fromDate=DateUtils.getMinDate(_fromDate);
		Date _toDate=null;
		if (chargeMode.equals("mile"))
			_toDate=DateUtils.getMaxDate(DateUtils.getOffsetDate(_fromDate,4));
		else{
			_toDate=new Date();
			_toDate.setTime(toDate);
			_toDate=DateUtils.getMaxDate(_toDate);
		}
				
		String json = JSON.toJSONString(getCarTaskVOList(cars,_fromDate,_toDate));
		writeJson(json);
	}
	
	public void getCarTaskInfo(){
		if(user==null || carId<=0 || date<=0){
			writeJson("{\"status\":false}");
			return;
		}
		Date _date=new Date();
		_date.setTime(date);
		Car car=carService.getCarById(carId);
		List<List<Order>> taskList=orderService.getCarTask(car, DateUtils.getMinDate(_date), DateUtils.getMaxDate(_date));
		List<TaskVO> taskVOList=new ArrayList<TaskVO>(taskList.get(0).size());
		for(Order be:taskList.get(0)){
			TaskVO tvo=new TaskVO();
			if(be instanceof Order){	//只显示订单任务。忽略掉其它任务。
				Order order=(Order)be;
				tvo.setSn(order.getSn());
				tvo.setCustomerOrganization(order.getCustomerOrganization().getName());
				tvo.setCustomer(order.getCustomer().getName());
				tvo.setPhoneNumber(order.getPhone());
				tvo.setChargeMode(order.getChargeMode()==ChargeModeEnum.MILE ? "按里程计费" : "按天计费");
				if(order.getChargeMode()==ChargeModeEnum.MILE){
					tvo.setAddress("从 "+order.getFromAddress()+" 到 "+order.getToAddress());
					tvo.setTime(DateUtils.getYMDHMString(order.getPlanBeginDate()));
				}else{
					tvo.setAddress(order.getFromAddress());
					tvo.setTime("从 "+DateUtils.getYMDHMString(order.getPlanBeginDate())+" 到 "+DateUtils.getYMDString(order.getPlanEndDate()));
				}
				tvo.setStatus(order.getStatus().toString());;
				taskVOList.add(tvo);
			}
		}
		String json = JSON.toJSONString(taskVOList);
		writeJson(json);
	}
	
	public void recommendCarTasks(){
		if(user==null || carServiceTypeId<=0 || chargeMode==null || chargeMode.length()==0 || (!chargeMode.equals("mile") && !chargeMode.equals("day"))
				|| planBeginDate<=0 || (!chargeMode.equals("mile") && planEndDate<=0) || fromAddressLongitude<=0 || fromAddressLatitude<=0){
			writeJson("{\"status\":false}");
			return;
		}
				
		Date _fromDate = new Date();
		_fromDate.setTime(fromDate);
		_fromDate=DateUtils.getMinDate(_fromDate);
		Date _toDate=null;
		if (chargeMode.equals("mile"))
			_toDate=DateUtils.getMaxDate(DateUtils.getOffsetDate(_fromDate,4));
		else{
			_toDate=new Date();
			_toDate.setTime(toDate);
			_toDate=DateUtils.getMaxDate(_toDate);
		}
		List<Car> cars = orderService.getRecommandedCar(
				carService.getCarServiceTypeById(carServiceTypeId),ChargeModeEnum.fromString(chargeMode),
				_fromDate, _toDate, 1).getRecordList();
				
		String json = JSON.toJSONString(getCarTaskVOList(cars,_fromDate,_toDate));
		writeJson(json);
	}
		
	private int SSMOrder(Order order, Order toUpdateOrder, String scheduleMode){
		ChargeModeEnum chargeModeEnum = ChargeModeEnum.fromString(chargeMode);
		CarServiceType carServiceType = null;
		Date beginDate = null;
		Date endDate = null;
		Car car = null;
		car = carService.getCarById(carId);
		order.setChargeMode(chargeModeEnum);
		beginDate=new Date();
		beginDate.setTime(fromDate);
		order.setPlanBeginDate(beginDate);
		if (order.getChargeMode()!=ChargeModeEnum.MILE) {
			endDate = new Date();
			endDate.setTime(toDate);
			order.setPlanEndDate(endDate);
		}
		carServiceType = carService.getCarServiceTypeById(carServiceTypeId);
		order.setPhone(phoneNumber);
		
		if(order.getOrderMoney()==null)
			order.setOrderMoney(new BigDecimal(0));
		
		order.setMemo(memo);
		
		int result = 0;
		String str=null;
		if(scheduleMode==null || scheduleMode.isEmpty()){
			str=OrderService.SCHEDULE_FROM_NEW;
			order.setOrderSource(OrderSourceEnum.SCHEDULER);
		}else if(scheduleMode.equals(OrderService.SCHEDULE_FROM_QUEUE))
			str=OrderService.SCHEDULE_FROM_QUEUE;
		else if(scheduleMode.equals(OrderService.SCHEDULE_FROM_UPDATE))
			str=OrderService.SCHEDULE_FROM_UPDATE;
		//TODO
		//result = orderService.scheduleOrder(str, order,customerOrganization,customer,car,copyOrderCount,toUpdateOrder,user);
		return result;
	}
	
	public void scheduleOrder(){
		if(user==null || orderSource==null || orderSource.length()==0 || orderId<0
				|| customerOrganization==null || customerOrganization.length()==0
				|| customer==null || customer.length()==0 || phoneNumber==null || phoneNumber.length()==0 || phoneNumber.length()!=13
				|| carServiceTypeId<=0 || chargeMode==null || chargeMode.length()==0 || (!chargeMode.equals("mile") && !chargeMode.equals("day"))
				|| passengerNumber<=0 || planBeginDate<=0 || (!chargeMode.equals("mile") && planEndDate<=0)
				|| fromAddressDescription==null || fromAddressDescription.length()==0
				|| fromAddressDetail==null || fromAddressDetail.length()==0 
				|| fromAddressLongitude<=0 || fromAddressLatitude<=0 
				|| (chargeMode.equals("mile") &&
						( toAddressDescription==null || toAddressDescription.length()==0
						  || toAddressDetail==null || toAddressDetail.length()==0 
						  || toAddressLongitude<=0 || toAddressLatitude==0))
				|| carId<=0 || copyOrderCount<0){
			writeJson("{\"status\":false}");
			return;
		}
		
		int result=0;
		if(orderSource.equals("new"))
			result=SSMOrder(new Order(),null,OrderService.SCHEDULE_FROM_NEW);
		else if(orderSource.equals("queue"))
			result=SSMOrder(orderService.getOrderById(orderId),null,OrderService.SCHEDULE_FROM_QUEUE);
		else if(orderSource.equals("modify")){
			Order order,toUpdateOrder;
			order=orderService.getOrderById(orderId);
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
			toUpdateOrder.setCar(order.getCar());
			toUpdateOrder.setDriver(order.getDriver());
			result=SSMOrder(order,toUpdateOrder,OrderService.SCHEDULE_FROM_UPDATE);
		}
		
		writeJson("{\"status\":"+result+"}");
	}
	
	public void enqueueOrder(){
		if(user==null || customerOrganization==null || customerOrganization.length()==0
				|| customer==null || customer.length()==0 || phoneNumber==null || phoneNumber.length()==0 || phoneNumber.length()!=13
				|| carServiceTypeId<=0 || chargeMode==null || chargeMode.length()==0 || (!chargeMode.equals("mile") && !chargeMode.equals("day"))
				|| passengerNumber<=0 || planBeginDate<=0 || (!chargeMode.equals("mile") && planEndDate<=0)
				|| fromAddressDescription==null || fromAddressDescription.length()==0
				|| fromAddressDetail==null || fromAddressDetail.length()==0 
				|| fromAddressLongitude<=0 || fromAddressLatitude<=0 
				|| (chargeMode.equals("mile") &&
						( toAddressDescription==null || toAddressDescription.length()==0
						  || toAddressDetail==null || toAddressDetail.length()==0 
						  || toAddressLongitude<=0 || toAddressLatitude==0))
				|| copyOrderCount<0){
			writeJson("{\"status\":false}");
			return;
		}
		Order order = new Order();
		CustomerOrganization co = null;
		Customer c = null;
		ChargeModeEnum chargeModeEnum = ChargeModeEnum.fromString(chargeMode);
		co=new CustomerOrganization();
		co.setName(customerOrganization);
		order.setCustomerOrganization(co);	//到Service层去处理是否新建客户单位
		c=new Customer();
		c.setName(customer);
		order.setCustomer(c);							//到Service层去处理是否新建客户
		order.setChargeMode(chargeModeEnum);
		Date beginDate=new Date();
		beginDate.setTime(fromDate);
		order.setPlanBeginDate(beginDate);
		if (order.getChargeMode()!=ChargeModeEnum.MILE) {
			Date endDate = new Date();
			endDate.setTime(toDate);
			order.setPlanEndDate(endDate);
		}
		CarServiceType carServiceType = carService.getCarServiceTypeById(carServiceTypeId);
		order.setServiceType(carServiceType);
		order.setPhone(phoneNumber);
		order.setOrderMoney(new BigDecimal(0));
		order.setStatus(OrderStatusEnum.INQUEUE);
		
		//由于取消了Address类，所以注释下面的代码。
		/*
		List<Address> addresses=new ArrayList<Address>();
		Address fromAddress=new Address();
		fromAddress.setDescription(fromAddressDescription);
		fromAddress.setDetail(fromAddressDetail);
		fromAddress.setLocation(new Location());
		fromAddress.getLocation().setLongitude(fromAddressLongitude);
		fromAddress.getLocation().setLatitude(fromAddressLatitude);
		addresses.add(fromAddress);
		if (order.getChargeMode()==ChargeModeEnum.MILE) {
			Address toAddress=new Address();
			toAddress.setDescription(toAddressDescription);
			toAddress.setDetail(toAddressDetail);
			toAddress.setLocation(new Location());
			toAddress.getLocation().setLongitude(toAddressLongitude);
			toAddress.getLocation().setLatitude(toAddressLatitude);
			addresses.add(toAddress);
		}
		*/
		order.setMemo(memo);
		order.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderService.EnQueue(order,null,copyOrderCount);
	}
	
	public void getOrdersInQueue(){
		if(user==null || pageNum<0){
			writeJson("{\"status\":false}");
			return;
		}
		QueryHelper helper = new QueryHelper("order_", "o");
		helper.addWhereCondition("o.status=?", OrderStatusEnum.INQUEUE);
		helper.addWhereCondition("o.scheduling=?", false);
		List<Order> orders = (List<Order>)orderService.queryOrder(pageNum, helper).getRecordList();
		List<OrderInQueueVO> oiqList=new ArrayList<OrderInQueueVO>(orders.size());
		for(Order order:orders){
			OrderInQueueVO oiqvo=new OrderInQueueVO();
			oiqvo.setId(order.getId());
			oiqvo.setSn(order.getSn());
			oiqvo.setCustomerOrganization(order.getCustomerOrganization().getName());
			oiqvo.setFromDate(order.getPlanBeginDate().getTime());
			oiqvo.setCarServiceType(order.getServiceType().getTitle());
			oiqList.add(oiqvo);
		}
		String json = JSON.toJSONString(oiqList);
		writeJson(json);
	}
	
	private OrderVO getOrderVO(Order order){
		OrderVO ovo=new OrderVO();
		ovo.setId(order.getId());
		ovo.setCustomerOrganization(order.getCustomerOrganization().getName());
		ovo.setCustomer(order.getCustomer().getName());
		ovo.setPhoneNumber(order.getPhone());
		ovo.setCarServiceTypeId(order.getServiceType().getId());
		switch(order.getChargeMode()){
		case MILE:
			ovo.setChargeMode("mile");
			break;
		case DAY:
			ovo.setChargeMode("day");
			break;
		case PROTOCOL:
			ovo.setChargeMode("protocol");
			break;
		}
		ovo.setPlanBeginDate(order.getPlanBeginDate().getTime());
		if(order.getChargeMode()==ChargeModeEnum.DAY || order.getChargeMode()==ChargeModeEnum.PROTOCOL)
			ovo.setPlanEndDate(order.getPlanEndDate().getTime());
		ovo.setFromAddressDescription(order.getFromAddress());
		ovo.setFromAddressDetail(order.getFromAddress());
		if(order.getChargeMode()==ChargeModeEnum.MILE){
			ovo.setToAddressDescription(order.getToAddress());
			ovo.setToAddressDetail(order.getToAddress());
		}
		ovo.setMemo(order.getMemo());
		return ovo;
	}
	
	public void distributeOrder(){
		if(user==null){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.distributeOrder(user,null);		
		String json = JSON.toJSONString(getOrderVO(order));
		writeJson(json);
	}

	public void isOrderDeprived(){
		if(user==null || orderId<0){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderById(orderId);
		if(order.getStatus()==OrderStatusEnum.INQUEUE && order.isScheduling() 
				&& order.getScheduler().equals(user))
			writeJson("{\"status\":false}");
		else
			writeJson("{\"status\":true}");
	}
	
	public void hasDistributedOrder(){
		if(user==null){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderDistributed(user);
		if(order!=null)
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
	}
	
	public void getDistributedOrder(){
		if(user==null){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderDistributed(user);
		if(order==null){
			writeJson("{\"status\":false}");
			return;
		}
		String json = JSON.toJSONString(getOrderVO(order));
		writeJson(json);
	}
	
	public void queryOrders(){
		if(user==null || pageNum<=0){
			writeJson("{\"status\":false}");
			return;
		}
		
		QueryHelper helper = new QueryHelper("order_", "o");
		if(customerOrganization!=null && customerOrganization.length()>0)
			helper.addWhereCondition("o.customerOrganization.name like ?", "%"+customerOrganization+"%");
		if(driver!=null && driver.length()>0)
			helper.addWhereCondition("o.driver.name like ?", "%"+driver+"%");
		if(planBeginDate>0 && planEndDate>0){
			Date _planBeginDate=new Date();
			_planBeginDate.setTime(planBeginDate);
			Date _planEndDate=new Date();
			_planEndDate.setTime(planEndDate);
			helper.addWhereCondition("TO_DAYS(?)<=TO_DAYS(o.planBeginDate) and TO_DAYS(o.planBeginDate)<=TO_DAYS(?)",_planBeginDate,_planEndDate);
		}else if(planBeginDate>0){
			Date _planBeginDate=new Date();
			_planBeginDate.setTime(planBeginDate);
			helper.addWhereCondition("TO_DAYS(?)<=TO_DAYS(o.planBeginDate)",_planBeginDate);
		}else if(planEndDate>0){
			Date _planEndDate=new Date();
			_planEndDate.setTime(planEndDate);
			helper.addWhereCondition("TO_DAYS(o.planBeginDate)<=TO_DAYS(?)",_planEndDate);
		}
		List<Order> orders = (List<Order>)orderService.queryOrder(pageNum, helper).getRecordList();
		List<OrderInQueryListVO> orderVOList=new ArrayList<OrderInQueryListVO>(orders.size());
		for(Order order:orders){
			OrderInQueryListVO oov=new OrderInQueryListVO();
			oov.setId(order.getId());
			oov.setSn(order.getSn());
			oov.setCustomerOrganization(order.getCustomerOrganization().getName());
			if(order.getChargeMode()==ChargeModeEnum.MILE){
				if(order.getActualBeginDate()!=null)
					oov.setDate(DateUtils.getYMDHMString(order.getActualBeginDate()));
				else
					oov.setDate(DateUtils.getYMDHMString(order.getPlanBeginDate()));
			}else{
				if(order.getActualBeginDate()!=null && order.getActualEndDate()!=null)
					oov.setDate(DateUtils.getYMDString(order.getActualBeginDate())+ " 至 " +DateUtils.getYMDString(order.getActualEndDate()));
				else
					oov.setDate(DateUtils.getYMDString(order.getPlanBeginDate())+ " 至 " +DateUtils.getYMDString(order.getPlanEndDate()));
			}
			oov.setStatus(order.getStatus().toString());
			orderVOList.add(oov);
		}
		
		String json = JSON.toJSONString(orderVOList);
		writeJson(json);
	}
	
	public void getOrder(){
		if(user==null || orderId<=0){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderById(orderId);
		OrderViewVO ovvo=new OrderViewVO();
		ovvo.setSn(order.getSn());
		ovvo.setCustomerOrganization(order.getCustomerOrganization().getName());
		ovvo.setCustomer(order.getCustomer().getName());
		ovvo.setChargeMode(order.getChargeMode().toString());
		ovvo.setCarServiceType(order.getServiceType().getTitle());
		ovvo.setStatus(order.getStatus().toString());
		if(order.getChargeMode()==ChargeModeEnum.MILE)
			ovvo.setPlanDate(DateUtils.getYMDHMString(order.getPlanBeginDate()));
		else
			ovvo.setPlanDate(DateUtils.getYMDHMString(order.getPlanBeginDate())+ " 至 " + DateUtils.getYMDHMString(order.getPlanEndDate()));
		if(order.getActualBeginDate()!=null && order.getActualEndDate()!=null)
			ovvo.setActualDate(DateUtils.getYMDHMString(order.getActualBeginDate())+ " 至 " + DateUtils.getYMDHMString(order.getActualEndDate()));
		else
			ovvo.setActualDate(null);
		ovvo.setFromAddress(order.getFromAddress());
		if(order.getChargeMode()==ChargeModeEnum.MILE)
			ovvo.setToAddress(order.getToAddress());
		else
			ovvo.setToAddress(null);
		ovvo.setDriverName(order.getDriver().getName());
		ovvo.setCarPlateNumber(order.getCar().getPlateNumber());
		ovvo.setEnqueueDate(order.getQueueTime().getTime());
		ovvo.setScheduleDate(order.getScheduleTime().getTime());
		ovvo.setScheduler(order.getScheduler().getName());
		ovvo.setAcceptedDate(order.getAcceptedTime().getTime());
		ovvo.setActualMoney(order.getActualMoney());
		ovvo.setOrderMoney(order.getOrderMoney());
		ovvo.setMemo(order.getMemo());

		String json = JSON.toJSONString(ovvo);
		writeJson(json);
	}
	
	public void canModifyOrder(){
		if(user==null || orderId<=0){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderById(orderId);
		if(orderService.canUpdate(order))
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
	}
	
	public void canPostponeOrder(){
		if(user==null || orderId<=0){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderById(orderId);
		if(orderService.canOrderEndPostpone(order))
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
	}
	
	public void canRescheduleOrder(){
		if(user==null || orderId<=0){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderById(orderId);
		if(orderService.canOrderReschedule(order))
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
	}
	
	public void canCancelOrder(){
		if(user==null || orderId<=0){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderById(orderId);
		if(orderService.canCancelOrder(order))
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
	}
	
	public void getOrderForModify(){
		if(user==null || orderId<=0){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderById(orderId);
		OrderModifyVO omvo=new OrderModifyVO();
		omvo.setCustomerOrganization(order.getCustomerOrganization().getName());
		omvo.setCustomer(order.getCustomer().getName());
		omvo.setPhoneNumber(order.getPhone());
		switch(order.getChargeMode()){
		case MILE:
			omvo.setChargeMode("mile");
			break;
		case DAY:
			omvo.setChargeMode("day");
			break;
		case PROTOCOL:
			omvo.setChargeMode("protocol");
			break;
		}
		omvo.setCarServiceTypeId(order.getServiceType().getId());
		omvo.setPlanBeginDate(order.getPlanBeginDate().getTime());
		if(order.getChargeMode()!=ChargeModeEnum.MILE)
			omvo.setPlanEndDate(order.getPlanEndDate().getTime());
		omvo.setFromAddressDescription(order.getFromAddress());
		omvo.setFromAddressDetail(order.getFromAddress());
		if(order.getChargeMode()==ChargeModeEnum.MILE)
		{
			omvo.setToAddressDescription(order.getToAddress());
			omvo.setToAddressDetail(order.getToAddress());
		}
		omvo.setDriverName(order.getDriver().getName());
		omvo.setCarPlateNumber(order.getCar().getPlateNumber());
		omvo.setMemo(order.getMemo());
		String json = JSON.toJSONString(omvo);
		writeJson(json);
	}
	
	public void postponeOrder(){
		if(user==null || orderId<=0 || newPlanEndDate<=0 || reason==null || reason.length()==0){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderById(orderId);
		int result;
		Date postponeDate=new Date();
		postponeDate.setTime(newPlanEndDate);
		postponeDate=DateUtils.getMaxDate(postponeDate);
		if(postponeDate.before(order.getPlanEndDate())){
			result=1;
		}else{
			String str=orderService.orderEndPostpone(order, postponeDate, reason, user);
			if("OK".equals(str))
				result=0;
			else
				result=3;
		}
		writeJson("{\"status\":"+result+"}");
	}
	
	public void rescheduleOrder(){
		if(user==null || orderId<=0 || carId<=0 || reason==null || reason.length()==0){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderById(orderId);
		Car car=carService.getCarById(carId);
		//TODO 需要再传入一个driver
		//int result=orderService.orderReschedule(order, car, user, reason);
		//writeJson("{\"status\":"+result+"}");
	}
	
	public void cancelOrder(){
		if(user==null || orderId<=0 || reason==null || reason.length()==0){
			writeJson("{\"status\":false}");
			return;
		}
		Order order=orderService.getOrderById(orderId);
		int result=orderService.cancelOrder(order, user, reason);
		writeJson("{\"status\":"+result+"}");
	}
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCustomerOrganization() {
		return customerOrganization;
	}

	public void setCustomerOrganization(String customerOrganization) {
		this.customerOrganization = customerOrganization;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getChargeMode() {
		return chargeMode;
	}

	public void setChargeMode(String chargeMode) {
		this.chargeMode = chargeMode;
	}

	public long getFromDate() {
		return fromDate;
	}

	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}

	public long getToDate() {
		return toDate;
	}

	public void setToDate(long toDate) {
		this.toDate = toDate;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getCarId() {
		return carId;
	}

	public void setCarId(long carId) {
		this.carId = carId;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getCarServiceTypeId() {
		return carServiceTypeId;
	}

	public void setCarServiceTypeId(long carServiceTypeId) {
		this.carServiceTypeId = carServiceTypeId;
	}

	public long getPlanBeginDate() {
		return planBeginDate;
	}

	public void setPlanBeginDate(long planBeginDate) {
		this.planBeginDate = planBeginDate;
	}

	public long getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(long planEndDate) {
		this.planEndDate = planEndDate;
	}

	public String getFromAddressDescription() {
		return fromAddressDescription;
	}

	public void setFromAddressDescription(String fromAddressDescription) {
		this.fromAddressDescription = fromAddressDescription;
	}

	public String getFromAddressDetail() {
		return fromAddressDetail;
	}

	public void setFromAddressDetail(String fromAddressDetail) {
		this.fromAddressDetail = fromAddressDetail;
	}

	public double getFromAddressLongitude() {
		return fromAddressLongitude;
	}

	public void setFromAddressLongitude(double fromAddressLongitude) {
		this.fromAddressLongitude = fromAddressLongitude;
	}

	public double getFromAddressLatitude() {
		return fromAddressLatitude;
	}

	public void setFromAddressLatitude(double fromAddressLatitude) {
		this.fromAddressLatitude = fromAddressLatitude;
	}

	public String getToAddressDescription() {
		return toAddressDescription;
	}

	public void setToAddressDescription(String toAddressDescription) {
		this.toAddressDescription = toAddressDescription;
	}

	public String getToAddressDetail() {
		return toAddressDetail;
	}

	public void setToAddressDetail(String toAddressDetail) {
		this.toAddressDetail = toAddressDetail;
	}

	public double getToAddressLongitude() {
		return toAddressLongitude;
	}

	public void setToAddressLongitude(double toAddressLongitude) {
		this.toAddressLongitude = toAddressLongitude;
	}

	public double getToAddressLatitude() {
		return toAddressLatitude;
	}

	public void setToAddressLatitude(double toAddressLatitude) {
		this.toAddressLatitude = toAddressLatitude;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getPassengerNumber() {
		return passengerNumber;
	}

	public void setPassengerNumber(int passengerNumber) {
		this.passengerNumber = passengerNumber;
	}

	public int getCopyOrderCount() {
		return copyOrderCount;
	}

	public void setCopyOrderCount(int copyOrderCount) {
		this.copyOrderCount = copyOrderCount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}	
	
	public long getNewPlanEndDate() {
		return newPlanEndDate;
	}

	public void setNewPlanEndDate(long newPlanEndDate) {
		this.newPlanEndDate = newPlanEndDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}



	class CarServiceTypeVO{
		private long id;
		private String title;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		
	}

	class DayTaskVO{
		private long day;
		private String task;
		public long getDay() {
			return day;
		}
		public void setDay(long day) {
			this.day = day;
		}
		public String getTask() {
			return task;
		}
		public void setTask(String task) {
			this.task = task;
		}		
	}
	class CarTaskVO{
		private long carId;
		private String driver;
		private String plateNumber;
		private List<DayTaskVO> tasks;
		public long getCarId() {
			return carId;
		}
		public void setCarId(long carId) {
			this.carId = carId;
		}
		public String getDriver() {
			return driver;
		}
		public void setDriver(String driver) {
			this.driver = driver;
		}
		public String getPlateNumber() {
			return plateNumber;
		}
		public void setPlateNumber(String plateNumber) {
			this.plateNumber = plateNumber;
		}
		public List<DayTaskVO> getTasks() {
			return tasks;
		}
		public void setTasks(List<DayTaskVO> tasks) {
			this.tasks = tasks;
		}
		
	}
	
	class TaskVO{
		private String sn;
		private String customerOrganization;
		private String customer;
		private String phoneNumber;
		private String chargeMode;
		private String address;
		private String time;
		private String status;
		public String getSn() {
			return sn;
		}
		public void setSn(String sn) {
			this.sn = sn;
		}
		public String getCustomerOrganization() {
			return customerOrganization;
		}
		public void setCustomerOrganization(String customerOrganization) {
			this.customerOrganization = customerOrganization;
		}
		public String getCustomer() {
			return customer;
		}
		public void setCustomer(String customer) {
			this.customer = customer;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getChargeMode() {
			return chargeMode;
		}
		public void setChargeMode(String chargeMode) {
			this.chargeMode = chargeMode;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
	}
	
	class OrderInQueueVO{
		private long id;
		private String sn;
		private String customerOrganization;
		private long fromDate;
		private String carServiceType;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getSn() {
			return sn;
		}
		public void setSn(String sn) {
			this.sn = sn;
		}
		public long getFromDate() {
			return fromDate;
		}
		public void setFromDate(long fromDate) {
			this.fromDate = fromDate;
		}
		public String getCustomerOrganization() {
			return customerOrganization;
		}
		public void setCustomerOrganization(String customerOrganization) {
			this.customerOrganization = customerOrganization;
		}
		public String getCarServiceType() {
			return carServiceType;
		}
		public void setCarServiceType(String carServiceType) {
			this.carServiceType = carServiceType;
		}		
	}
	
	class OrderVO{
		private long id;
		private String customerOrganization;
		private String customer;
		private String phoneNumber;
		private long carServiceTypeId;
		private String chargeMode;
		private int passengerNumber;
		private long planBeginDate;
		private long planEndDate;
		private String fromAddressDescription;
		private String fromAddressDetail;
		private double fromAddressLongitude;
		private double fromAddressLatitude;
		private String toAddressDescription;
		private String toAddressDetail;
		private double toAddressLongitude;
		private double toAddressLatitude;
		private String memo;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getCustomerOrganization() {
			return customerOrganization;
		}
		public void setCustomerOrganization(String customerOrganization) {
			this.customerOrganization = customerOrganization;
		}
		public String getCustomer() {
			return customer;
		}
		public void setCustomer(String customer) {
			this.customer = customer;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public long getCarServiceTypeId() {
			return carServiceTypeId;
		}
		public void setCarServiceTypeId(long carServiceTypeId) {
			this.carServiceTypeId = carServiceTypeId;
		}
		public String getChargeMode() {
			return chargeMode;
		}
		public void setChargeMode(String chargeMode) {
			this.chargeMode = chargeMode;
		}
		public int getPassengerNumber() {
			return passengerNumber;
		}
		public void setPassengerNumber(int passengerNumber) {
			this.passengerNumber = passengerNumber;
		}
		public long getPlanBeginDate() {
			return planBeginDate;
		}
		public void setPlanBeginDate(long planBeginDate) {
			this.planBeginDate = planBeginDate;
		}
		public long getPlanEndDate() {
			return planEndDate;
		}
		public void setPlanEndDate(long planEndDate) {
			this.planEndDate = planEndDate;
		}
		public String getFromAddressDescription() {
			return fromAddressDescription;
		}
		public void setFromAddressDescription(String fromAddressDescription) {
			this.fromAddressDescription = fromAddressDescription;
		}
		public String getFromAddressDetail() {
			return fromAddressDetail;
		}
		public void setFromAddressDetail(String fromAddressDetail) {
			this.fromAddressDetail = fromAddressDetail;
		}
		public double getFromAddressLongitude() {
			return fromAddressLongitude;
		}
		public void setFromAddressLongitude(double fromAddressLongitude) {
			this.fromAddressLongitude = fromAddressLongitude;
		}
		public double getFromAddressLatitude() {
			return fromAddressLatitude;
		}
		public void setFromAddressLatitude(double fromAddressLatitude) {
			this.fromAddressLatitude = fromAddressLatitude;
		}
		public String getToAddressDescription() {
			return toAddressDescription;
		}
		public void setToAddressDescription(String toAddressDescription) {
			this.toAddressDescription = toAddressDescription;
		}
		public String getToAddressDetail() {
			return toAddressDetail;
		}
		public void setToAddressDetail(String toAddressDetail) {
			this.toAddressDetail = toAddressDetail;
		}
		public double getToAddressLongitude() {
			return toAddressLongitude;
		}
		public void setToAddressLongitude(double toAddressLongitude) {
			this.toAddressLongitude = toAddressLongitude;
		}
		public double getToAddressLatitude() {
			return toAddressLatitude;
		}
		public void setToAddressLatitude(double toAddressLatitude) {
			this.toAddressLatitude = toAddressLatitude;
		}
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
		}
	}
	
	class OrderInQueryListVO{
		private long id;
		private String sn;
		private String customerOrganization;
		private String date;
		private String status;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getSn() {
			return sn;
		}
		public void setSn(String sn) {
			this.sn = sn;
		}
		public String getCustomerOrganization() {
			return customerOrganization;
		}
		public void setCustomerOrganization(String customerOrganization) {
			this.customerOrganization = customerOrganization;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
	}
	
	class OrderViewVO{
		private String sn;
		private String customerOrganization;
		private String customer;
		private String phoneNumber;
		private String chargeMode;
		private int passengerNumber;
		private String carServiceType;
		private String status;
		private String planDate;
		private String actualDate;
		private String fromAddress;
		private String toAddress;
		private String driverName;
		private String carPlateNumber;
		private long enqueueDate;
		private long scheduleDate;
		private String scheduler;
		private long acceptedDate;
		private double actualMile;
		private double orderMile;
		private BigDecimal actualMoney;
		private BigDecimal orderMoney;
		private String memo;
		public String getSn() {
			return sn;
		}
		public void setSn(String sn) {
			this.sn = sn;
		}
		public String getCustomerOrganization() {
			return customerOrganization;
		}
		public void setCustomerOrganization(String customerOrganization) {
			this.customerOrganization = customerOrganization;
		}
		public String getCustomer() {
			return customer;
		}
		public void setCustomer(String customer) {
			this.customer = customer;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getChargeMode() {
			return chargeMode;
		}
		public void setChargeMode(String chargeMode) {
			this.chargeMode = chargeMode;
		}
		public int getPassengerNumber() {
			return passengerNumber;
		}
		public void setPassengerNumber(int passengerNumber) {
			this.passengerNumber = passengerNumber;
		}
		public String getCarServiceType() {
			return carServiceType;
		}
		public void setCarServiceType(String carServiceType) {
			this.carServiceType = carServiceType;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getPlanDate() {
			return planDate;
		}
		public void setPlanDate(String planDate) {
			this.planDate = planDate;
		}
		public String getActualDate() {
			return actualDate;
		}
		public void setActualDate(String actualDate) {
			this.actualDate = actualDate;
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
		public String getDriverName() {
			return driverName;
		}
		public void setDriverName(String driverName) {
			this.driverName = driverName;
		}
		public String getCarPlateNumber() {
			return carPlateNumber;
		}
		public void setCarPlateNumber(String carPlateNumber) {
			this.carPlateNumber = carPlateNumber;
		}
		public long getEnqueueDate() {
			return enqueueDate;
		}
		public void setEnqueueDate(long enqueueDate) {
			this.enqueueDate = enqueueDate;
		}
		public long getScheduleDate() {
			return scheduleDate;
		}
		public void setScheduleDate(long scheduleDate) {
			this.scheduleDate = scheduleDate;
		}
		public String getScheduler() {
			return scheduler;
		}
		public void setScheduler(String scheduler) {
			this.scheduler = scheduler;
		}
		public long getAcceptedDate() {
			return acceptedDate;
		}
		public void setAcceptedDate(long acceptedDate) {
			this.acceptedDate = acceptedDate;
		}
		public double getActualMile() {
			return actualMile;
		}
		public void setActualMile(double actualMile) {
			this.actualMile = actualMile;
		}
		public double getOrderMile() {
			return orderMile;
		}
		public void setOrderMile(double orderMile) {
			this.orderMile = orderMile;
		}
		public BigDecimal getActualMoney() {
			return actualMoney;
		}
		public void setActualMoney(BigDecimal actualMoney) {
			this.actualMoney = actualMoney;
		}
		public BigDecimal getOrderMoney() {
			return orderMoney;
		}
		public void setOrderMoney(BigDecimal orderMoney) {
			this.orderMoney = orderMoney;
		}
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
		}
	}
	
	class OrderModifyVO{
		private String customerOrganization;
		private String customer;
		private String phoneNumber;
		private String chargeMode;
		private int passengerNumber;
		private long carServiceTypeId;
		private long planBeginDate;
		private long planEndDate;
		private String fromAddressDescription;
		private String fromAddressDetail;
		private double fromAddressLongitude;
		private double fromAddressLatitude;
		private String toAddressDescription;
		private String toAddressDetail;
		private double toAddressLongitude;
		private double toAddressLatitude;
		private String driverName;
		private String carPlateNumber;
		private long carId;
		private String memo;
		public String getCustomerOrganization() {
			return customerOrganization;
		}
		public void setCustomerOrganization(String customerOrganization) {
			this.customerOrganization = customerOrganization;
		}
		public String getCustomer() {
			return customer;
		}
		public void setCustomer(String customer) {
			this.customer = customer;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getChargeMode() {
			return chargeMode;
		}
		public void setChargeMode(String chargeMode) {
			this.chargeMode = chargeMode;
		}
		public int getPassengerNumber() {
			return passengerNumber;
		}
		public void setPassengerNumber(int passengerNumber) {
			this.passengerNumber = passengerNumber;
		}
		public long getCarServiceTypeId() {
			return carServiceTypeId;
		}
		public void setCarServiceTypeId(long carServiceTypeId) {
			this.carServiceTypeId = carServiceTypeId;
		}
		public long getPlanBeginDate() {
			return planBeginDate;
		}
		public void setPlanBeginDate(long planBeginDate) {
			this.planBeginDate = planBeginDate;
		}
		public long getPlanEndDate() {
			return planEndDate;
		}
		public void setPlanEndDate(long planEndDate) {
			this.planEndDate = planEndDate;
		}
		public String getFromAddressDescription() {
			return fromAddressDescription;
		}
		public void setFromAddressDescription(String fromAddressDescription) {
			this.fromAddressDescription = fromAddressDescription;
		}
		public String getFromAddressDetail() {
			return fromAddressDetail;
		}
		public void setFromAddressDetail(String fromAddressDetail) {
			this.fromAddressDetail = fromAddressDetail;
		}
		public double getFromAddressLongitude() {
			return fromAddressLongitude;
		}
		public void setFromAddressLongitude(double fromAddressLongitude) {
			this.fromAddressLongitude = fromAddressLongitude;
		}
		public double getFromAddressLatitude() {
			return fromAddressLatitude;
		}
		public void setFromAddressLatitude(double fromAddressLatitude) {
			this.fromAddressLatitude = fromAddressLatitude;
		}
		public String getToAddressDescription() {
			return toAddressDescription;
		}
		public void setToAddressDescription(String toAddressDescription) {
			this.toAddressDescription = toAddressDescription;
		}
		public String getToAddressDetail() {
			return toAddressDetail;
		}
		public void setToAddressDetail(String toAddressDetail) {
			this.toAddressDetail = toAddressDetail;
		}
		public double getToAddressLongitude() {
			return toAddressLongitude;
		}
		public void setToAddressLongitude(double toAddressLongitude) {
			this.toAddressLongitude = toAddressLongitude;
		}
		public double getToAddressLatitude() {
			return toAddressLatitude;
		}
		public void setToAddressLatitude(double toAddressLatitude) {
			this.toAddressLatitude = toAddressLatitude;
		}
		public String getDriverName() {
			return driverName;
		}
		public void setDriverName(String driverName) {
			this.driverName = driverName;
		}
		public String getCarPlateNumber() {
			return carPlateNumber;
		}
		public void setCarPlateNumber(String carPlateNumber) {
			this.carPlateNumber = carPlateNumber;
		}
		public long getCarId() {
			return carId;
		}
		public void setCarId(long carId) {
			this.carId = carId;
		}
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
		}
		
	}
}
