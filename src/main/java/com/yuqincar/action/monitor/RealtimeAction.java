package com.yuqincar.action.monitor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Order;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.DateUtils;

@Controller
@Scope("prototype")
public class RealtimeAction extends BaseAction implements ModelDriven<Car>{
	
	private Car model=new Car();
	private String carId;
	private String baiduKey;

	@Autowired
	private CarService carService;
	@Autowired
	private OrderService orderService;
	
	private Car car;
	
	public String mapWindow(){
		return "mapWindow";
	}
	
	
	/**
	 * 返回主页面列表信息
	 * @return
	 */
	public String home(){
		
		List<ServicePoint> servicePointListInDatabase = carService.getAllServicePoint();
		List<ServicePoint> servicePointList=new ArrayList<ServicePoint>();
		servicePointList.add(new ServicePoint());
		for(ServicePoint servicePoint:servicePointListInDatabase){
			servicePointList.add(servicePoint);
		}
		//前端驻车点选择需要一个空选项，所以重置servicePointList
		ActionContext.getContext().put("servicePointList", servicePointList);
		return "home";
	}
	
	/**
	 * 查询所有未报废车辆的信息
	 */
	public void allNormalCars(){
		List<Car> cars=carService.getCarsForMonitoring();
		List<CarVO> carsVO=parseCars(cars);
		this.writeJson(JSON.toJSONString(carsVO));
	}
	
	
	public void getCarInfo(){
		Long id=new Long(Long.parseLong(carId));
		Car car=carService.getCarById(id);
		CarVO carVO=parseCar(car);
		this.writeJson(JSON.toJSONString(carVO));
	}
	
	/**
	 * 返回订单详情json数据
	 */
	public void orderDetail(){
		Order order=orderService.getCurrentOrderByCarId(model.getId());
		String jsonStr=null;
		if(order!=null){
			OrderVO orderVO=parseOrder(order);
			List<String> messages=new ArrayList<String>();
			messages.add(orderVO.getSnum());
			messages.add(orderVO.getOrg());
			messages.add(orderVO.getCustomer());
			messages.add(orderVO.getCharge());
			messages.add(orderVO.getTime());
			messages.add(orderVO.getType());
			messages.add(orderVO.getNumber());
			messages.add(orderVO.getDriver());
			messages.add(orderVO.getFrom());
			messages.add(orderVO.getTo());
		    jsonStr="{\"order\":"+JSON.toJSONString(messages)+","+"\"status\":1}"; 
		}else{
			jsonStr="{\"order\":null,"+"\"status\":0}";
		}
		System.out.println("jsonStr="+jsonStr);
		this.writeJson(jsonStr);
		
	}
	
	/**
	 * 返回车辆json数据
	 * @return
	 */
	public void list(){
		
		String plateNumber=null;
		if(car!=null)
			plateNumber=car.getPlateNumber();
		String servicePointName=null;
		if(!"".equals(model.getServicePoint().getName())){
			//业务点id
			int servicePointId=Integer.parseInt(model.getServicePoint().getName());
			//根据业务点id获取相应的业务点名称,实际上不需要此步骤，直接在下面用id查询就行，之前已经写了此接口，所以不做改动了
			servicePointName=carService.getServicePointById(servicePointId).getName();
		}
		List<Car> cars= carService.findByDriverNameAndPlateNumberAndServicePointName(null,plateNumber,servicePointName);
		System.out.println("in list, cars.size="+cars.size());
		List<CarVO> carsVO=null;
		if(cars.size()!=0){
			carsVO=parseCars(cars);
		}
		String jsonStr="{\"cars\":"+JSON.toJSONString(carsVO)+"}"; 
		System.out.println("jsonStr="+jsonStr);
		this.writeJson(jsonStr);
	}

	public Car getModel() {
		return model;
	}
	
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	
	public String getBaiduKey() {
		System.out.println("&&&&&&&&&&");
		return Configuration.getBaiduKey();
	}
	public CarVO parseCar(Car car){
		CarVO carVO=new CarVO();
		carVO.setType(car.getModel());
		if(car.getDriver()!=null){
			carVO.setDriver(car.getDriver().getName());
			carVO.setPhone(car.getDriver().getPhoneNumber());
		}else{
			carVO.setDriver("");
			carVO.setPhone("");
		}
		carVO.setId(car.getId());
		carVO.setLocation(car.getServicePoint().getName());
		carVO.setNumber(car.getPlateNumber());
		carVO.setSn(car.getDevice().getSN());
		return carVO;
	}
	
	public List<CarVO> parseCars(List<Car> cars){		
		List<CarVO> carsVO=new ArrayList<CarVO>(cars.size());
		for(int i=0;i<cars.size();i++){
			CarVO carVO=new CarVO();
			carVO.setType(cars.get(i).getModel());
			carVO.setDriver(cars.get(i).getDriver()!=null ? cars.get(i).getDriver().getName() : "");
			carVO.setId(cars.get(i).getId());
			carVO.setLocation(cars.get(i).getServicePoint().getName());
			carVO.setNumber(cars.get(i).getPlateNumber());
			carVO.setPhone(cars.get(i).getDriver()!=null ? cars.get(i).getDriver().getPhoneNumber() : "");
			carVO.setSn(cars.get(i).getDevice().getSN());
			carsVO.add(carVO);
		}
		return carsVO;
	}
	
	public OrderVO parseOrder(Order order){
		OrderVO orderVo=new OrderVO();
		orderVo.setSnum(order.getSn());
		orderVo.setOrg(order.getCustomerOrganization().getName());
		if(order.isCallForOther())
			orderVo.setCustomer(order.getOtherPassengerName()+"（"+order.getOtherPhoneNumber()+"）");
		else
			orderVo.setCustomer(order.getCustomer().getName()+"（"+order.getPhone()+"）");
		orderVo.setCharge(order.getChargeMode().getLabel());
		if(order.getChargeMode()==ChargeModeEnum.DAY || order.getChargeMode()==ChargeModeEnum.PROTOCOL)
			orderVo.setTime(DateUtils.getYMDString(order.getPlanBeginDate())+" 到 "+DateUtils.getYMDString(order.getPlanEndDate()));
		else if(order.getChargeMode()==ChargeModeEnum.PLANE || order.getChargeMode()==ChargeModeEnum.MILE)
			orderVo.setTime(DateUtils.getYMDHMString(order.getPlanBeginDate()));
		orderVo.setType(order.getServiceType().getTitle());
		orderVo.setNumber(order.getCar().getPlateNumber());
		orderVo.setDriver(order.getDriver().getName()+"（"+order.getDriver().getPhoneNumber()+"）");
		orderVo.setFrom(order.getFromAddress());
		orderVo.setTo(order.getToAddress());
		
		return orderVo;
	}
	
	public Car getCar() {
		return car;
	}


	public void setCar(Car car) {
		this.car = car;
	}

	class OrderVO{
		
		private String snum;           //订单号
		private String org;            //客户单位
		private String customer;       //客户
		private String charge;         //计费方式
		private String time;           //开始时间
		private String type;           //车型
		private String number;         //车牌号
		private String driver;		   //司机
		private String from;           //始发地
		private String to;             //目的地
		
		public String getSnum() {
			return snum;
		}
		public void setSnum(String snum) {
			this.snum = snum;
		}
		public String getOrg() {
			return org;
		}
		public void setOrg(String org) {
			this.org = org;
		}
		public String getCustomer() {
			return customer;
		}
		public void setCustomer(String customer) {
			this.customer = customer;
		}
		public String getCharge() {
			return charge;
		}
		public void setCharge(String charge) {
			this.charge = charge;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		public String getDriver() {
			return driver;
		}
		public void setDriver(String driver) {
			this.driver = driver;
		}
		
	}
	
	class CarVO{
		private Long id;
		private String type;        //车型
		private String number;      //车牌号
		private String driver;      //司机
		private String phone;       //联系方式
		private String location;    //驻车点
		private String status;      //车辆状态
		private String sn;          //设备sn号
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getSn() {
			return sn;
		}
		public void setSn(String sn) {
			this.sn = sn;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public String getDriver() {
			return driver;
		}
		public void setDriver(String driver) {
			this.driver = driver;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
	}
}
