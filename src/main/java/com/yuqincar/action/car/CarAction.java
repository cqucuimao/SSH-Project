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
	
	/** 列表 */
	public String list() throws Exception {
		
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
		
		PageBean<Car> pageBean = carService.queryCar(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);	
		ActionContext.getContext().getSession().put("carHelper", helper);
		
		return "list";
	}
	/*
	 * 翻页时保留查询条件 
	 */
	public String queryList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carHelper");
		ActionContext.getContext().put("carServiceTypeList", carService.getAllCarServiceType());
		PageBean<Car> pageBean = carService.queryCar(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/** 删除 */
	public String delete() throws Exception {
		carService.deleteCarById(model.getId());
		return "toList";
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		// 准备数据：carServiceTypeList
		ActionContext.getContext().put("carServiceTypeList", carService.getAllCarServiceType());
		// 准备数据：servicePointList
	    ActionContext.getContext().put("servicePointList", carService.getAllServicePoint());	    
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {
		if(model.isStandbyCar())
			model.setDriver(null);
		else{
			if(driverId==null){
				addFieldError("driver", "非备用车必须制定司机！");
				return addUI();
			}
		}	
		// 封装对象
		//System.out.println(carService);
		model.setServiceType(carService.getCarServiceTypeById(carServiceTypeId));
		model.setServicePoint(carService.getServicePointById(servicePointId));
		model.setDriver(userService.getById(driverId));
		model.setStatus(CarStatusEnum.NORMAL);
		// 保存到数据库
		carService.saveCar(model);

		return "toList";
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
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
		if(model.isStandbyCar())
			model.setDriver(null);
		else{
			if(driverId==null){
				addFieldError("driver", "非备用车必须制定司机！");
				return addUI();
			}
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
		car.setRegistDate(model.getRegistDate());
		car.setDriver(userService.getById(driverId));
		car.setServicePoint(carService.getServicePointById(servicePointId));

		//更新到数据库
		carService.updateCar(car);

		return "toList";
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
		return "toList";
	}
	public Car getModel() {
		// TODO Auto-generated method stub
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
			for(Car c : cars) {
				TreeNode parent = new TreeNode();
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
				parent.setOpen(true);
				parent.setName(c.getServicePoint().getName());
				parent.setChildren(new ArrayList());
				parent.getChildren().add(child);
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
	
	
}
