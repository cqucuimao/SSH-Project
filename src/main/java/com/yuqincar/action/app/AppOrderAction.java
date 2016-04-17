package com.yuqincar.action.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.Preparable;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Address;
import com.yuqincar.domain.order.ChargeModeEnum;
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
		user = userService.getByLoginNameAndPassword(username, pwd);
	}

	/**
	 * 获取所有待执行的订单的数量
	 */
	public void countUndoOrder() {
		if (user == null) {
			this.writeJson("{\"status\":false}");
			return;
		}
		int count = driverAPPService.getNumberOfUndoOrders(user);
		this.writeJson("{\"status\":" + count + "}");
	}

	
	public void acceptOrder() {
		if(user==null || orderId==null) {
			this.writeJson("{\"status\":false}");
		}
		Order order = orderService.getOrderById(orderId);
		if(driverAPPService.orderAccept(order) == 1) {
			this.writeJson("{\"status\":true}");
		} else {
			this.writeJson("{\"status\":false}");
		}
	}
	
	private AddressVO getAddressVO(Address address){
		LocationVO location=new LocationVO();
		location.setId(address.getLocation().getId());
		location.setLongitude(address.getLocation().getLongitude());
		location.setLatitude(address.getLocation().getLatitude());
		AddressVO addressVO=new AddressVO();
		addressVO.setId(address.getId());
		addressVO.setDescription(address.getDescription());
		addressVO.setDetail(address.getDetail());
		addressVO.setLocation(location);
		return addressVO;
	}
	
	/**
	 * 获取某个用户执行中的订单
	 * 应该只有一条记录返回
	 */
	public void getBeginOrder() { 
		if(user == null) {
			this.writeJson("{\"status\":false}");
			return;
		}
		Order o = driverAPPService.getBeginOrder(user);
		List<AppUndoOrderVo> orders = new ArrayList<AppUndoOrderVo>();
		
		if(o==null) {
			this.writeJson(JSON.toJSONString(orders));
			return;
		}
		AppUndoOrderVo vo = new AppUndoOrderVo();
		
		vo.orderId = o.getId();
		vo.chargeMode = o.getChargeMode();
		vo.fromAddress = getAddressVO(o.getFromAddress());
		vo.planBeginDate = o.getPlanBeginDate();
		vo.planEndDate = o.getPlanEndDate();
		vo.status = o.getStatus();
		vo.sn = o.getSn();
		

		if (o.getActualBeginLocation() != null) {
			vo.fromLatitude = o.getActualBeginLocation().getLatitude();
			vo.fromLongitue = o.getActualBeginLocation().getLongitude();
		}

		if (vo.chargeMode == ChargeModeEnum.MILE) {
			vo.toAddress = getAddressVO(o.getToAddress());
			if (o.getActualEndLocation() != null) {
				vo.toLatitude = o.getActualEndLocation().getLatitude();
				vo.toLongitude = o.getActualEndLocation().getLongitude();
			}
		}
		
		vo.customerName = o.getCustomer().getName();
		vo.customerOrganization = o.getCustomerOrganization().getName();
		vo.customerPhone = o.getPhone();
		orders.add(vo);
		this.writeJson(JSON.toJSONString(orders));
		
	}
	
	
	/*
	 * {orderId: **, chargeMode: **, planBeginDate: **， planEndDate:
	 * **，fromAddress: **，toAddress: **, fromLongitue:**, fromLatitude: **,
	 * toLongitude: **, toLatitude:
	 */
	public void listUndoOrder() {

		if (user == null) {
			this.writeJson("{\"status\":false}");
			return;
		}
		List<Order> orders = driverAPPService.getAllUndoOrders(user);
		System.out.println(orders.size());
		List<AppUndoOrderVo> appUndoOrders = new ArrayList<AppUndoOrderVo>();
		for (Order o : orders) {
			AppUndoOrderVo vo = new AppUndoOrderVo();

			vo.orderId = o.getId();
			vo.chargeMode = o.getChargeMode();
			vo.fromAddress = getAddressVO(o.getFromAddress());
			vo.planBeginDate = o.getPlanBeginDate();
			vo.planEndDate = o.getPlanEndDate();
			vo.sn = o.getSn();
			if (o.getActualBeginLocation() != null) {
				vo.fromLatitude = o.getActualBeginLocation().getLatitude();
				vo.fromLongitue = o.getActualBeginLocation().getLongitude();
			}

			if (vo.chargeMode == ChargeModeEnum.MILE) {
				vo.toAddress = getAddressVO(o.getToAddress());
				if (o.getActualEndLocation() != null) {
					vo.toLatitude = o.getActualEndLocation().getLatitude();
					vo.toLongitude = o.getActualEndLocation().getLongitude();
				}
			}
			
			vo.status = o.getStatus();
			vo.customerName = o.getCustomer().getName();
			vo.customerOrganization = o.getCustomerOrganization().getName();
			vo.customerPhone = o.getPhone();
			appUndoOrders.add(vo);
		
		}

		String json = JSON.toJSONString(appUndoOrders);
		this.writeJson(json);
	}

	public void getUndoOrder() {
		if (user == null || orderId == null) {
			this.writeJson("{\"status\":false}");
			return;
		}
		Order o = driverAPPService.getUndoOrder(user, orderId);

		AppUndoOrderVo vo = new AppUndoOrderVo();

		vo.orderId = o.getId();
		vo.chargeMode = o.getChargeMode();
		vo.fromAddress = getAddressVO(o.getFromAddress());
		vo.planBeginDate = o.getPlanBeginDate();
		vo.planEndDate = o.getPlanEndDate();
		vo.status = o.getStatus();
		vo.sn = o.getSn();
		if (o.getActualBeginLocation() != null) {
			vo.fromLatitude = o.getActualBeginLocation().getLatitude();
			vo.fromLongitue = o.getActualBeginLocation().getLongitude();
		}

		if (vo.chargeMode == ChargeModeEnum.MILE) {
			vo.toAddress = getAddressVO(o.getToAddress());
			if (o.getActualEndLocation() != null) {
				vo.toLatitude = o.getActualEndLocation().getLatitude();
				vo.toLongitude = o.getActualEndLocation().getLongitude();
			}
		}
		this.writeJson(JSON.toJSONString(vo));
	}

	public void beginOrder() {
		if (user == null || orderId == null) {
			this.writeJson("{\"status\":false}");
			return;
		}
		Order o = orderService.getOrderById(orderId);
		if (driverAPPService.orderBegin(o) == 0)
			this.writeJson("{\"status\":true}");
		else
			this.writeJson("{\"status\":false}");
	}

	public void endOrder() {
		if (user == null || orderId == null) {
			this.writeJson("{\"status\":false}");
			return;
		}
		Order o = orderService.getOrderById(orderId);
		if (driverAPPService.orderEnd(o) == 0)
			this.writeJson("{\"status\":true}");
		else
			this.writeJson("{\"status\":false}");
	}

	public void getOrderStatus() {
		if (user == null || orderId == null) {
			this.writeJson("{\"status\":false}");
			return;
		}
		Order o = orderService.getOrderById(orderId);
		AppOrderStatusVo vo = new AppOrderStatusVo();
		vo.status = o.getStatus();
		vo.driverName = o.getDriver().getLoginName();
		
		this.writeJson(JSON.toJSONString(vo));
	}

	public void listDownOrder() {
		if (user == null) {
			this.writeJson("{\"status\":false}");
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
		List<AppEndOrderVo> voOrders = new ArrayList();
		for(Order o:orders) {
			AppEndOrderVo vo = new AppEndOrderVo();
			vo.orderId = o.getId();
			vo.chargeMode = o.getChargeMode();
			vo.fromAddress = o.getFromAddress();
			vo.actualBeginDate= o.getActualBeginDate();
			vo.actualEndDate = o.getActualEndDate();
			vo.customerName = o.getCustomer().getName();
			vo.customerOrganization = o.getCustomerOrganization().getName();
			vo.customerPhone = o.getPhone();
			vo.sn = o.getSn();
			voOrders.add(vo);
		}
		pageBean.setRecordList(voOrders);
		
		this.writeJson(JSON.toJSONString(orderMap));
	}
	
	public void getDoneOrderDetail() {
		if (user == null || orderId == null) {
			this.writeJson("{\"status\":false}");
			return;
		}
		Order order = driverAPPService.getDoneOrderDetailById(orderId);
		if(order==null) {
			this.writeJson("{\"status\":not found}");
			return;
		}
		AppEndOrderVo vo = new AppEndOrderVo();
		vo.orderId = order.getId();
		vo.chargeMode = order.getChargeMode();
		vo.fromAddress = order.getFromAddress();
		vo.actualBeginDate= order.getActualBeginDate();
		vo.actualEndDate = order.getActualEndDate();
		vo.customerName = order.getCustomer().getName();
		vo.customerOrganization = order.getCustomerOrganization().getName();
		vo.customerPhone = order.getPhone();
		vo.sn = order.getSn();
		this.writeJson(JSON.toJSONString(vo));
	}
	
	public void updateDeviceToken(){
		System.out.println("in updateDeviceToken");
		System.out.println("user="+user);
		System.out.println("deviceType="+deviceType);
		System.out.println("Token="+deviceToken);
		if (user == null || deviceType==null || deviceType.length()==0 ||
				deviceToken == null || deviceToken.length()==0) {
			this.writeJson("{\"status\":false}");
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

class AppUndoOrderVo {
	public Long orderId;
	public ChargeModeEnum chargeMode;
	public Date planBeginDate;
	public Date planEndDate;
	public AddressVO fromAddress ;
	public AddressVO toAddress;
	public double fromLongitue;
	public double fromLatitude;
	public double toLongitude;
	public double toLatitude;
	public String customerOrganization = "";
	public String customerName = "";
	public String customerPhone = "";
	public OrderStatusEnum status;
	public String sn = "";

}

class AppEndOrderVo {
	public Long orderId;
	public ChargeModeEnum chargeMode;
	public Date actualBeginDate;
	public Date actualEndDate;
	public Address fromAddress;
	public Address toAddress;
	public String customerOrganization = "";
	public String customerName = "";
	public String customerPhone = "";
	public OrderStatusEnum status;
	public String sn = "";

}

class AppOrderStatusVo {
	public String driverName;
	public OrderStatusEnum status;
}