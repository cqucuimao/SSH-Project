package com.yuqincar.action.car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.PlateTypeEnum;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.car.TransmissionTypeEnum;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.common.TreeNode;
import com.yuqincar.domain.monitor.Device;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.device.DeviceService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarAction extends BaseAction implements ModelDriven<Car>{
	
	private Car model = new Car();
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DeviceService deviceService;
	

	private Long carServiceTypeId;
	private Long servicePointId;
	private Long driverId;
	private Long deviceId;	
	private String driverName;
	private String carSelectorId;
	private String synchDriverName;
	private String synchDriverId;
	private int transmissionTypeId;
	private int plateTypeId;
	private String actionFlag;
	/** 查询 */
	public String queryList() {
		
		QueryHelper helper = new QueryHelper(Car.class, "c");
		/*车牌号查询*/
		if(model.getPlateNumber()!=null && !"".equals(model.getPlateNumber()))
			helper.addWhereCondition("c.plateNumber like ?", "%"+model.getPlateNumber()+"%");
		/*服务类型查询*/
		ActionContext.getContext().put("carServiceTypeList", carService.getAllCarServiceType());

		if(model.getServiceType()!=null&&model.getServiceType().getTitle()!=null && !"".equals(model.getServiceType().getTitle()))
			helper.addWhereCondition("c.serviceType.title like ?", "%"+model.getServiceType().getTitle()+"%");
		/*司机姓名查询*/
		if(model.getDriver()!=null&&model.getDriver().getName()!=null && !"".equals(model.getDriver().getName()))
			helper.addWhereCondition("c.driver.name like ?", "%"+model.getDriver().getName()+"%");
		helper.addOrderByProperty("c.id", false);
		PageBean<Car> pageBean = carService.queryCar(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);	
		ActionContext.getContext().getSession().put("carHelper", helper);
		
		return "list";
	}
	/*
	 * 列表
	 */
	public String list(){
		QueryHelper helper = new QueryHelper("Car", "c");
		helper.addOrderByProperty("c.id", false);
		ActionContext.getContext().put("carServiceTypeList", carService.getAllCarServiceType());
		PageBean<Car> pageBean = carService.queryCar(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carHelper");
		ActionContext.getContext().put("carServiceTypeList", carService.getAllCarServiceType());
		PageBean<Car> pageBean = carService.queryCar(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/** 删除 */
	public String delete() throws Exception {
		carService.deleteCarById(model.getId());
		return freshList();
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		ActionContext.getContext().put("actionFlag", actionFlag);
		// 准备数据：carServiceTypeList
		ActionContext.getContext().put("carServiceTypeList", carService.getAllCarServiceType());
		// 准备数据：servicePointList
	    ActionContext.getContext().put("servicePointList", carService.getAllServicePoint());	    
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {
		if(carService.isPlateNumberExist(0, model.getPlateNumber())){
				addFieldError("plateNumber", "车牌号已经存在！");
				return addUI();
		}
		if(carService.isVINExist(0, model.getVIN())){
				addFieldError("VIN", "车架号已经存在！");
				return addUI();
		}
		if(carService.isEngineSNExist(0, model.getEngineSN())){
				addFieldError("EngineSN", "发动机号已经存在！");
				return addUI();
		}
		// 封装对象
		//System.out.println(carService);
		model.setServiceType(carService.getCarServiceTypeById(carServiceTypeId));
		model.setServicePoint(carService.getServicePointById(servicePointId));
		model.setDriver(userService.getById(driverId));
		model.setStatus(CarStatusEnum.NORMAL);
		model.setPlateType(PlateTypeEnum.getById(plateTypeId));
		model.setTransmissionType(TransmissionTypeEnum.getById(transmissionTypeId));
		// 保存到数据库
		carService.saveCar(model);
		ActionContext.getContext().getValueStack().push(new Car());
		return freshList();
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
		ActionContext.getContext().put("actionFlag", actionFlag);
		// 准备回显的数据
		Car car = carService.getCarById(model.getId());
		ActionContext.getContext().getValueStack().push(car);
		//处理服务类型
		if (car.getServiceType() != null) {
			carServiceTypeId = car.getServiceType().getId();
		}
		//处理驻车点
		if (car.getServicePoint() != null) {
			servicePointId = car.getServicePoint().getId();
		}
		
		//处理默认司机
		if (car.getDriver() != null) {
			driverId = car.getDriver().getId();
		}

		// 准备数据：carServiceTypeList
		ActionContext.getContext().put("carServiceTypeList", carService.getAllCarServiceType());

		// 准备数据：servicePointList
	    ActionContext.getContext().put("servicePointList", carService.getAllServicePoint());
	    
		return "saveUI";
	}
	
	/*
	 * 显示详细信息
	 */
	public String detail(){
		
		Car car = carService.getCarById(model.getId());
		ActionContext.getContext().getValueStack().push(car);
		
		return "device";
	}
	
	/** 修改 */
	public String edit() throws Exception {
		
		if(carService.isPlateNumberExist(model.getId(), model.getPlateNumber())){
				addFieldError("plateNumber", "车牌号已经存在！");
				return editUI();
		}
		if(carService.isVINExist(model.getId(), model.getVIN())){
				addFieldError("VIN", "车架号已经存在！");
				return editUI();
		}
		if(carService.isEngineSNExist(model.getId(), model.getEngineSN())){
				addFieldError("EngineSN", "发动机号已经存在！");
				return editUI();
		}
		//从数据库中取出原对象
		Car car = carService.getCarById(model.getId());
		//设置要修改的属性
		car.setPlateNumber(model.getPlateNumber());
		car.setServiceType(carService.getCarServiceTypeById(carServiceTypeId));
		car.setBrand(model.getBrand());
		car.setModel(model.getModel());
		car.setVIN(model.getVIN());
		car.setEngineSN(model.getEngineSN());
		car.setEnrollDate(model.getEnrollDate());
		car.setSeatNumber(model.getSeatNumber());
		car.setPlateType(PlateTypeEnum.getById(plateTypeId));
		car.setTransmissionType(TransmissionTypeEnum.getById(transmissionTypeId));
		car.setRegistDate(model.getRegistDate());
		car.setDriver(userService.getById(driverId));
		car.setServicePoint(carService.getServicePointById(servicePointId));

		//更新到数据库
		carService.updateCar(car);
		ActionContext.getContext().getValueStack().push(new Car());
		return freshList();
	}
	/*
	 * 修改车载设备 
	 */
	public String editDevice() throws Exception {
		//从数据库中取出原对象
		Car car = carService.getCarById(model.getId());
		if(car.getDevice() != null){
			
			deviceId = car.getDevice().getId();
			Device device = deviceService.getDeviceById(deviceId);
			device.setPN(model.getDevice().getPN());
			device.setSN(model.getDevice().getSN());
			System.out.println("deviceId="+deviceId);
			
			deviceService.update(device);
			
		}else{
			Device device = new Device();
			device.setSN(model.getDevice().getSN());
			device.setPN(model.getDevice().getPN());	
			carService.saveDevice(device);
			car.setDevice(device);
			
			carService.updateCar(car);
		}
		ActionContext.getContext().getValueStack().push(new Car());
		return freshList();
	}
	public Car getModel() {
		return model;
	}
	
	public String popup() {
		List<TreeNode> nodes = new ArrayList() ;
		boolean synchDriver=false;
		if(!StringUtils.isEmpty(synchDriverName) && !StringUtils.isEmpty(synchDriverId))
			synchDriver=true;
		
		if(!StringUtils.isEmpty(model.getPlateNumber()))
			nodes= carService.getCarTree(model.getPlateNumber(),synchDriver);
		else if(!StringUtils.isEmpty(driverName)) {
			List<Car> cars = carService.findByDriverName(driverName);
			Map<ServicePoint,List<Car>> carMap=new HashMap<ServicePoint,List<Car>>();
			for(Car c : cars){
				if(!carMap.containsKey(c.getServicePoint()))
					carMap.put(c.getServicePoint(), new ArrayList<Car>());
				carMap.get(c.getServicePoint()).add(c);
			}
			for(ServicePoint sp : carMap.keySet()) {
				TreeNode parent = new TreeNode();
				parent.setOpen(true);
				parent.setName(sp.getName());
				parent.setChildren(new ArrayList<TreeNode>());
				for(Car c : carMap.get(sp)){
					TreeNode child = new TreeNode();
					child.setName(c.getPlateNumber());
					if(synchDriver){
						if(c.getDriver()!=null){
							Map<String,Object> param=new HashMap<String,Object>();
							param.put("driverName", c.getDriver().getName());
							param.put("driverId", c.getDriver().getId());
							child.setParam(param);
						}
					}
					parent.getChildren().add(child);
					
				}
				nodes.add(parent);
			}
		}
		//默认返回所有车辆
		if(StringUtils.isEmpty(model.getPlateNumber())&&StringUtils.isEmpty(driverName))
			nodes= carService.getCarTree(model.getPlateNumber(),synchDriver);
		Gson gson = new Gson();
		ActionContext.getContext().put("nodes", gson.toJson(nodes));
		System.out.println("nodes="+gson.toJson(nodes));
		ActionContext.getContext().put("carSelectorId", carSelectorId);
		ActionContext.getContext().put("synchDriverName", synchDriverName);
		ActionContext.getContext().put("synchDriverId", synchDriverId);
		return "popup";
	}
	//判断车辆能否删除
	public boolean isCanDeleteCar(){
		Car car = (Car) ActionContext.getContext().getValueStack().peek();
		if(carService.canDeleteCar(car.getId()))
			return true;
		else 
			return false;
	}


	
	public Long getCarServiceTypeId() {
		return carServiceTypeId;
	}

	public void setCarServiceTypeId(Long carServiceTypeId) {
		this.carServiceTypeId = carServiceTypeId;
	}

	public Long getServicePointId() {
		return servicePointId;
	}

	public void setServicePointId(Long servicePointId) {
		this.servicePointId = servicePointId;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getCarSelectorId() {
		return carSelectorId;
	}
	public void setCarSelectorId(String carSelectorId) {
		this.carSelectorId = carSelectorId;
	}
	public String getSynchDriverName() {
		return synchDriverName;
	}
	public void setSynchDriverName(String synchDriverName) {
		this.synchDriverName = synchDriverName;
	}
	public String getSynchDriverId() {
		return synchDriverId;
	}
	public void setSynchDriverId(String synchDriverId) {
		this.synchDriverId = synchDriverId;
	}
	
	public int getTransmissionTypeId() {
		return transmissionTypeId;
	}
	public void setTransmissionTypeId(int transmissionTypeId) {
		this.transmissionTypeId = transmissionTypeId;
	}
	public int getPlateTypeId() {
		return plateTypeId;
	}
	public void setPlateTypeId(int plateTypeId) {
		this.plateTypeId = plateTypeId;
	}
	public String getActionFlag() {
		return actionFlag;
	}
	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}
	
	
}
