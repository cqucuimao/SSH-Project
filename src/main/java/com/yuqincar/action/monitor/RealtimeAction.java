package com.yuqincar.action.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.tuple.ElementWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.monitor.CapcareMessage;
import com.yuqincar.domain.monitor.MonitorGroup;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.monitor.CapcareMessageService;
import com.yuqincar.service.monitor.MonitorGroupService;
import com.yuqincar.service.monitor.impl.CapcareMessageServiceImpl;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.Text;

import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
public class RealtimeAction extends BaseAction implements ModelDriven<Car>{
	
	private Car model=new Car();
	private String carId;
	private String baiduKey;
	private String carPlateNumber;//防止和car属性中的plateNumber重名
	private String superType;
	private String carsStatus;
	@Autowired
	private CarService carService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private MonitorGroupService monitorGroupService;
	@Autowired
	private CapcareMessageService capcareMessageService;
	
	private Car car;
	
	//监控分组相关的属性
	private String monitorGroupTitle;
	
	private String carString;
	
	private Long monitorGroupId;
	
	private Long[] selectedCarIds;
	
	private Long selectId;
	
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
		List<MonitorGroup> monitorGroupList = monitorGroupService.getAll();
		servicePointList.add(new ServicePoint());
		for(ServicePoint servicePoint:servicePointListInDatabase){
			servicePointList.add(servicePoint);
		}
		//前端驻车点选择需要一个空选项，所以重置servicePointList
		ActionContext.getContext().put("servicePointList", servicePointList);
		ActionContext.getContext().put("monitorGroupList", monitorGroupList);
		return "home";
	}
	
	/**
	 * 查询所有监控的车辆
	 */
	public void allNormalCars(){
		List<Car> cars=carService.getCarsForMonitoring();
		List<CarVO> carsVO=parseCars(cars);
		this.writeJson(JSON.toJSONString(carsVO));
	}
	
	/**
	 * 根据车型监控车辆
	 */
	public void getCarsBySuperType(){
		System.out.println("superType="+superType);
		List<Car> cars = carService.getCarsForMonitoringBySuperType(superType);
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
		//对应状态的车辆集合
		List<Car> carsByStatus = new ArrayList<Car>();
		//获取所有监控的车辆
		List<Car> allCars = carService.getCarsForMonitoring();
		Map<String, CapcareMessage> capcareMap = CapcareMessageServiceImpl.capcareMap;
		if(selectId == null || selectId.equals("")){
			//如果是全部，就不做处理
			if(carsStatus.equals("全部")){
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
				carsByStatus= carService.findByDriverNameAndPlateNumberAndServicePointName(null,plateNumber,servicePointName);
				
			}else if(carsStatus.equals("行驶")){
				for(Car car:allCars){
					CapcareMessage sm = capcareMap.get(car.getPlateNumber());
					if(sm != null){
						System.out.println("sm="+sm);
						String speed = sm.getSpeed();
						int status = Integer.parseInt(sm.getStatus());
						if(status == 1 && !speed.equals("0.0")){
							carsByStatus.add(car);
						}
					}
				}
			}else if(carsStatus.equals("静止")){
				for(Car car:allCars){
					CapcareMessage sm = capcareMap.get(car.getPlateNumber());
					if(sm != null){
						String speed = sm.getSpeed();
						int status = Integer.parseInt(sm.getStatus());
						if(status == 1 && speed.equals("0.0")){
							carsByStatus.add(car);
						}
					}
				}
			}else{//无网络
				for(Car car:allCars){
					CapcareMessage sm = capcareMap.get(car.getPlateNumber());
					if(sm != null){
						int status = Integer.parseInt(sm.getStatus());
						if(status == 2){
							carsByStatus.add(car);
						}
					}
				}
			}
		}else{
			carsByStatus = new ArrayList<Car>(monitorGroupService.getById(selectId).getCars());
		}
		
		//转为json数据
		List<CarVO> carsVO=null;
		if(carsByStatus.size()!=0){
			carsVO=parseCars(carsByStatus);
		}
		String jsonStr="{\"cars\":"+JSON.toJSONString(carsVO)+"}"; 
		System.out.println("jsonStr="+jsonStr);
		this.writeJson(jsonStr);
		
	}
	
	
	//监控车辆分组
	public String monitorGroup(){
		List<MonitorGroup> monitorGroups = monitorGroupService.getAll();
		ActionContext.getContext().put("monitorGroups", monitorGroups);
	
		return "list";
	}
	
	public String monitorGroupAddUI(){
		
		List<Car> carsList = monitorGroupService.sortCarByPlateNumber(carService.getCarsForMonitoring());
		ActionContext.getContext().put("carsList", carsList);

		List<Car> selectedList = new ArrayList<Car>();
		ActionContext.getContext().put("selectedList", selectedList);
		
		return "saveUI";
	}
	
	public String monitorGroupAdd(){
		MonitorGroup mg = new MonitorGroup();
		mg.setTitle(monitorGroupTitle);
		//处理分组的车辆
		List<Car> carsList = new ArrayList<Car>();
		System.out.println("carString="+carString);
		if(carString != null && !carString.equals("")){
			String[] carIds = carString.split(",");
			for(String id:carIds){
				Long longId = Long.parseLong(id);
				carsList.add(carService.getCarById(longId));
			}
			mg.setCars(new HashSet<Car>(carsList));
		}else{
			mg.setCars(null);
		}
		monitorGroupService.save(mg);
		
		return monitorGroup();
	}

	public String monitorGroupEditUI(){

		MonitorGroup mg = monitorGroupService.getById(monitorGroupId);
		monitorGroupTitle = mg.getTitle();
		List<Car> carsList = monitorGroupService.sortCarByPlateNumber(carService.getCarsForMonitoring());
		List<Car> selectedList = new ArrayList<Car>(mg.getCars());
		
		selectedCarIds = new Long[mg.getCars().size()];
		String selectedCarString = "";
		int index = 0;
		for (Car car : mg.getCars()) {
			carsList.remove(car);
			selectedCarString =selectedCarString+ car.getId() + ",";
			selectedCarIds[index++] = car.getId();
		}
		carString = selectedCarString;

		ActionContext.getContext().put("carsList", carsList);
		ActionContext.getContext().put("selectedList", selectedList);
		
		return "saveUI";
	}

	public String monitorGroupEdit(){
		MonitorGroup mg = monitorGroupService.getById(monitorGroupId);
		mg.setTitle(monitorGroupTitle);
		//处理所属的车辆
		if(carString != null && !carString.equals("")){
			String[] carIds = carString.split(",");
			List<Car> carList = new ArrayList<Car>();
			for(int i=0;i<carIds.length;i++){
				Long id = Long.parseLong(carIds[i]);
				carList.add(carService.getCarById(id));
			}
			mg.setCars(new HashSet<Car>(carList));
		}
		monitorGroupService.update(mg);
		return monitorGroup();
	}
	
	public String delete(){
		monitorGroupService.delete(monitorGroupId);
		return monitorGroup();
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

	public String getCarPlateNumber() {
		return carPlateNumber;
	}

	public void setCarPlateNumber(String carPlateNumber) {
		this.carPlateNumber = carPlateNumber;
	}

	public String getSuperType() {
		return superType;
	}

	public void setSuperType(String superType) {
		this.superType = superType;
	}

	public String getCarsStatus() {
		return carsStatus;
	}

	public void setCarsStatus(String carsStatus) {
		this.carsStatus = carsStatus;
	}

	public String getMonitorGroupTitle() {
		return monitorGroupTitle;
	}

	public void setMonitorGroupTitle(String monitorGroupTitle) {
		this.monitorGroupTitle = monitorGroupTitle;
	}

	public String getCarString() {
		return carString;
	}

	public void setCarString(String carString) {
		this.carString = carString;
	}
	
	public Long getMonitorGroupId() {
		return monitorGroupId;
	}

	public void setMonitorGroupId(Long monitorGroupId) {
		this.monitorGroupId = monitorGroupId;
	}

	public Long[] getSelectedCarIds() {
		return selectedCarIds;
	}

	public void setSelectedCarIds(Long[] selectedCarIds) {
		this.selectedCarIds = selectedCarIds;
	}

	public Long getSelectId() {
		return selectId;
	}

	public void setSelectId(Long selectId) {
		this.selectId = selectId;
	}

	public String getBaiduKey() {
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
	
	//获取凯步数据
	public void getCapcareData(){
		System.out.println("车牌号="+carPlateNumber);
		Map<String, CapcareMessage> capcareMap = CapcareMessageServiceImpl.capcareMap;
		System.out.println("capcareMap="+capcareMap.get(carPlateNumber));
		CapcareMessageVO cmvo = new CapcareMessageVO();
		cmvo.setPlateNumber(capcareMap.get(carPlateNumber).getPlateNumber());
		cmvo.setLongitude(capcareMap.get(carPlateNumber).getLongitude());
		cmvo.setLatitude(capcareMap.get(carPlateNumber).getLatitude());
		cmvo.setSpeed(capcareMap.get(carPlateNumber).getSpeed());
		cmvo.setStatus(capcareMap.get(carPlateNumber).getStatus());
		cmvo.setDirection(capcareMap.get(carPlateNumber).getDirection());
		
		String jsonString = JSON.toJSONString(cmvo);
		String added = "\"carId\""+":"+"\""+carId+"\",";
		String json = "{"+added+jsonString.substring(1);
		this.writeJson(json);
	}
	
	class CapcareMessageVO{
		private String plateNumber;
		private String longitude;
		private String latitude;
		private String speed;
		private String status;
		private String direction;
		public String getPlateNumber() {
			return plateNumber;
		}
		public void setPlateNumber(String plateNumber) {
			this.plateNumber = plateNumber;
		}
		public String getLongitude() {
			return longitude;
		}
		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}
		public String getLatitude() {
			return latitude;
		}
		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}
		public String getSpeed() {
			return speed;
		}
		public void setSpeed(String speed) {
			this.speed = speed;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getDirection() {
			return direction;
		}
		public void setDirection(String direction) {
			this.direction = direction;
		}
		
		
	}
	
	//初始化车辆位置
	public void initCapcareMessage(){
		capcareMessageService.initCapcareMessages();
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
