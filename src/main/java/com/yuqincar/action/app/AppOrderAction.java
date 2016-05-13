package com.yuqincar.action.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.Preparable;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserAPPDeviceTypeEnum;
import com.yuqincar.service.app.DriverAPPService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;

/**
 * 用于处理司机APP中关于订单的请求。
 *
 */ 
@Controller
@Scope("prototype")
public class AppOrderAction extends BaseAction implements Preparable {
	@Autowired
	private LBSDao lbsDao;
	@Autowired
	private DriverAPPService driverAPPService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;

	private int pageNum;
	private Long fromDate;
	private Long toDate;
	
	private Long orderId;

	private User user;
	
	private String deviceType;
	
	private String deviceToken;

	public void prepare() throws Exception {
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		System.out.println("in prepare");
		System.out.println("username="+username+",pwd="+pwd);
		user = userService.getByLoginNameAndMD5Password(username, pwd);
	}

	/**
	 * 获取所有待执行的订单的数量
	 */
	public void countUndoOrder() {
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		int count = driverAPPService.getNumberOfUndoOrders(user);
		this.writeJson("{\"status\":" + count + "}");
	}

	
	public void acceptOrder() {
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order order = orderService.getOrderById(orderId);
		if(driverAPPService.orderAccept(order) == 1) {
			this.writeJson("{\"status\":true}");
		} else {
			this.writeJson("{\"status\":false}");
		}
	}
	
	private OrderVo getOrderVo(Order order){
		OrderVo vo = new OrderVo();
		vo.orderId = order.getId();
		vo.chargeMode = order.getChargeMode();
		vo.fromAddress = order.getFromAddress();
		vo.beginDate = order.getPlanBeginDate();
		if (order.getChargeMode() == ChargeModeEnum.MILE || order.getChargeMode() == ChargeModeEnum.PLANE)
			vo.toAddress = order.getToAddress();
		else
			vo.endDate = order.getPlanEndDate();
		vo.sn = order.getSn();			
		vo.status = order.getStatus();
		vo.customerName = order.getCustomer().getName();
		vo.customerOrganization = order.getCustomerOrganization().getName();
		vo.customerPhone = order.getPhone();
		return vo;
	}
		
	/**
	 * 获取某个用户执行中的订单
	 * 应该只有一条记录返回
	 */
	public void getBeginOrder() { 
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		Order o = driverAPPService.getBeginOrder(user);
		List<OrderVo> orders = new ArrayList<OrderVo>();
		
		if(o==null) {
			this.writeJson(JSON.toJSONString(orders));
			return;
		}
		
		orders.add(getOrderVo(o));
		this.writeJson(JSON.toJSONString(orders));
		
	}
	
	public void listUndoOrder() {
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		List<Order> orders = driverAPPService.getAllUndoOrders(user);
		System.out.println(orders.size());
		List<OrderVo> appUndoOrders = new ArrayList<OrderVo>();
		for (Order o : orders) {
			appUndoOrders.add(getOrderVo(o));
		
		}

		String json = JSON.toJSONString(appUndoOrders);
		this.writeJson(json);
	}

	public void getUndoOrder() {
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order o = driverAPPService.getUndoOrder(user, orderId);				
		this.writeJson(JSON.toJSONString(getOrderVo(o)));
	}

	public void beginOrder() {
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order o = orderService.getOrderById(orderId);
		if (driverAPPService.orderBegin(o) == 0)
			this.writeJson("{\"status\":true}");
		else
			this.writeJson("{\"status\":false}");
	}

	public void endOrder() {
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order o = orderService.getOrderById(orderId);
		if (driverAPPService.orderEnd(o) == 0)
			this.writeJson("{\"status\":true}");
		else
			this.writeJson("{\"status\":false}");
	}

	public void getOrderStatus() {
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order o = orderService.getOrderById(orderId);
		AppOrderStatusVo vo = new AppOrderStatusVo();
		vo.status = o.getStatus();
		vo.driverName = o.getDriver().getLoginName();
		
		this.writeJson(JSON.toJSONString(vo));
	}

	public void listDoneOrder() {
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(pageNum==0) pageNum = 1;
		
		Date start = null;
		Date end = null;
		if(fromDate!=null)
			start = new Date(fromDate);
		if(toDate!=null)
			end = new Date(toDate);

		Map<String, Object> orderMap = driverAPPService.queryEndOrder(pageNum, start, end, user);
		
		PageBean pageBean = (PageBean)orderMap.get("pageBean");
		List<Order> orders = pageBean.getRecordList();
		List<OrderVo> voOrders = new ArrayList();
		for(Order o:orders) 
			voOrders.add(getOrderVo(o));
		pageBean.setRecordList(voOrders);		
		this.writeJson(JSON.toJSONString(orderMap));
	}
	
	public void getDoneOrderDetail() {
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order order = driverAPPService.getDoneOrderDetailById(orderId);
		if(order==null) {
			this.writeJson("{\"status\":not found}");
			return;
		}
		this.writeJson(JSON.toJSONString(getOrderVo(order)));
	}
	
	public void updateDeviceToken(){
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(deviceType==null || deviceType.length()==0 ||
				deviceToken == null || deviceToken.length()==0){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		if(deviceType.equals("ios"))
			user.setAppDeviceType(UserAPPDeviceTypeEnum.IOS);
		else if(deviceType.equals("android"))
			user.setAppDeviceType(UserAPPDeviceTypeEnum.ANDROID);
		
		user.setAppDeviceToken(deviceToken);
		userService.update(user);
		
		this.writeJson("{\"status\":true}");
	}
	
	public void canBeginOrder(){
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order order = orderService.getOrderById(orderId);
		if(order.getStatus()==OrderStatusEnum.ACCEPTED)
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
	}
	
	public void canEndOrder(){
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order order = orderService.getOrderById(orderId);
		if(order.getStatus()==OrderStatusEnum.BEGIN || order.getStatus()==OrderStatusEnum.GETOFF)
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
	}
	
	public void canCustomerGeton(){
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order order = orderService.getOrderById(orderId);
		if(order.getChargeMode()==ChargeModeEnum.MILE || order.getChargeMode()==ChargeModeEnum.PLANE){
			if(order.getStatus()==OrderStatusEnum.BEGIN){
				writeJson("{\"status\":true}");
				return;
			}else{
				writeJson("{\"status\":false}");
				return;
			}
		}else{			
			if(order.getStatus()==OrderStatusEnum.BEGIN || order.getStatus()==OrderStatusEnum.GETOFF){
				DayOrderDetail dod=orderService.getDayOrderDetailByDate(order, new Date());
				if(dod!=null){
					writeJson("{\"status\":false}");
					return;
				}else{
					writeJson("{\"status\":true}");
					return;
				}
			}else{
				writeJson("{\"status\":false}");
				return;
			}
		}
	}
	
	public void canCustomerGetoff(){
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order order = orderService.getOrderById(orderId);
		if(order.getStatus()==OrderStatusEnum.GETON)
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
	}
	
	public void customerGeton(){
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order order = orderService.getOrderById(orderId);
		driverAPPService.customerGeton(order);
		writeJson("{\"status\":true}");
	}
	
	public void customerGetoff(){
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order order = orderService.getOrderById(orderId);
		driverAPPService.customerGetoff(order);
		writeJson("{\"status\":true}");
	}
	
	public void canCustomerSign(){
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order order = orderService.getOrderById(orderId);
		if(order.getStatus()==OrderStatusEnum.GETOFF)
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
	}
	
	public void getMileInfo(){
		if (user == null) {
			writeJson("{\"status\":\"unauthorized\"}");
			return;
		}
		if(orderId==null){
			writeJson("{\"status\":\"badParameter\"}");
			return;
		}
		Order order = orderService.getOrderById(orderId);
		Date beginDate=order.getDayDetails().get(0).getGetonDate();
		Date endDate=order.getDayDetails().get(order.getDayDetails().size()-1).getGetoffDate();
		float getonMile=lbsDao.getMileAtMoment(order.getCar().getDevice().getSN(), beginDate);
		float getoffMile=lbsDao.getMileAtMoment(order.getCar().getDevice().getSN(), endDate);
		float serviceMile=getoffMile-getonMile;
		Map<String,Float> map=new HashMap<String,Float>();
		map.put("getonMile", getonMile);
		map.put("getoffMile", getoffMile);
		map.put("serviceMile", serviceMile);
		this.writeJson(JSON.toJSONString(map));
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public Long getFromDate() {
		return fromDate;
	}

	public void setFromDate(Long fromDate) {
		this.fromDate = fromDate;
	}

	public Long getToDate() {
		return toDate;
	}

	public void setToDate(Long toDate) {
		this.toDate = toDate;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

}

class LocationVO{
	private double longitude;
	private double latitude;
	private Long id;
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
}

class AddressVO {
	private Long id;
	private String description;
	private String detail;
	private LocationVO location;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public LocationVO getLocation() {
		return location;
	}
	public void setLocation(LocationVO location) {
		this.location = location;
	}	
}

class OrderVo {
	public Long orderId;
	public ChargeModeEnum chargeMode;
	public Date beginDate;
	public Date endDate;
	public String fromAddress;
	public String toAddress;
	public String customerOrganization;
	public String customerName;
	public String customerPhone;
	public OrderStatusEnum status;
	public String sn;

}

class AppOrderStatusVo {
	public String driverName;
	public OrderStatusEnum status;
}