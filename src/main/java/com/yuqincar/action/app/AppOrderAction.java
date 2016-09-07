package com.yuqincar.action.app;

import java.math.BigDecimal;
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
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserAPPDeviceTypeEnum;
import com.yuqincar.service.app.DriverAPPService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.PrivilegeService;
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
	private PrivilegeService privilegeService;
	@Autowired
	private OrderService orderService;

	private int pageNum;
	private Long fromDate;
	private Long toDate;
	
	private Long orderId;

	private User user;
	
	private String deviceType;
	
	private String deviceToken;
	
	private BigDecimal refuelMoney;

	private BigDecimal washingFee;
	
	private BigDecimal parkingFee;
	
	private BigDecimal toll;
	
	private BigDecimal roomAndBoardFee;
	
	private BigDecimal otherFee;

	public void prepare() throws Exception {
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		user = userService.getByLoginNameAndMD5Password(username, pwd);
		
		if(user!=null && !privilegeService.canUserHasPrivilege(user, "/driver_app"))
			user=null;
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
		vo.planBeginDate = order.getPlanBeginDate();
		if (order.getChargeMode() == ChargeModeEnum.MILE || order.getChargeMode() == ChargeModeEnum.PLANE)
			vo.toAddress = order.getToAddress();
		else
			vo.planEndDate = order.getPlanEndDate();
		vo.actualBeginDate=order.getActualBeginDate();
		vo.actualEndDate=order.getActualEndDate();
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
		if (driverAPPService.orderBegin(o,null,null) == 0)
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
		o.setRefuelMoney(refuelMoney!=null ? refuelMoney : BigDecimal.ZERO);
		o.setWashingFee(washingFee!=null ? washingFee : BigDecimal.ZERO);
		o.setParkingFee(parkingFee!=null ? parkingFee : BigDecimal.ZERO);
		o.setToll(toll!=null ? toll : BigDecimal.ZERO);
		o.setRoomAndBoardFee(roomAndBoardFee!=null ? roomAndBoardFee : BigDecimal.ZERO);
		o.setOtherFee(otherFee!=null ? otherFee : BigDecimal.ZERO);
		
		if (driverAPPService.orderEnd(o,null,null) == 0)
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
		if(driverAPPService.canBeginOrder(order))
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
		if(driverAPPService.canEndOrder(order))
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
		if(driverAPPService.canCustomerGeton(order)){
			writeJson("{\"status\":true}");
			return;
		}else{
			writeJson("{\"status\":false}");
			return;
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
		if(driverAPPService.canCustomerGetoff(order))
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
		if(driverAPPService.customerGeton(order,null,null)==0)
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
				
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
		if(driverAPPService.customerGetoff(order,null,null)==0)
			writeJson("{\"status\":true}");
		else
			writeJson("{\"status\":false}");
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
		float getonMile=order.getCustomerGetonMile();
		float getoffMile=order.getCustomerGetoffMile();
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
	public Date planBeginDate;
	public Date planEndDate;
	public Date actualBeginDate;
	public Date actualEndDate;
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