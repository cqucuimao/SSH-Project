package com.yuqincar.action.car;

import java.util.ArrayList;
import java.util.Date;
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
import com.yuqincar.dao.car.CarCareDao;
import com.yuqincar.dao.car.CarViolationDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.CarViolation;
import com.yuqincar.domain.car.PlateTypeEnum;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.car.TransmissionTypeEnum;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.common.TreeNode;
import com.yuqincar.domain.monitor.Device;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarCareService;
import com.yuqincar.service.car.CarExamineService;
import com.yuqincar.service.car.CarRepairService;
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
	
	@Autowired 
	private CarCareService carCareService;
	
	@Autowired
	private CarCareDao carCareDao;
	
	@Autowired
	private CarViolationDao carViolationDao;
	
	@Autowired
	private CarRepairService carRepairService;

	@Autowired
	private CarExamineService carExamineService;
	

	private Long carServiceTypeId;
	private Long servicePointId;
	private Long deviceId;
	private String driverName;
	private String selectorName;
	private String synchDriver;
	private int transmissionTypeId;
	private int plateTypeId;
	private String actionFlag;
	private String statusLabel;
	private String plateTypeLabel;
	private String transmissionTypeLabel;
	private String careExpiredLabel;
	private String insuranceExpiredLabel;
	private String examineExpiredLabel;
	private String tollChargeExpiredLabel;
	private String standbyCarLabel;
	private CarRepair unDoneAppointRepair;
	private CarCare unDoneAppointCare;
	private CarExamine unDoneAppointExamine;
	
	private Date registDate1;
	private Date registDate2;
	private Date enrollDate1;
	private Date enrollDate2;
	private int mileage1;
	private int mileage2;
	private int seatNumber1;
	private int seatNumber2;
	
	private Car car;
	
	public String queryList() {
		QueryHelper helper = new QueryHelper(Car.class, "c");
		if(car!=null)
			helper.addWhereCondition("c.plateNumber=?", car.getPlateNumber());
		ActionContext.getContext().put("carServiceTypeList", carService.getAllCarServiceType());
		if(model.getServiceType()!=null&&model.getServiceType().getTitle()!=null && !"".equals(model.getServiceType().getTitle()))
			helper.addWhereCondition("c.serviceType.title like ?", "%"+model.getServiceType().getTitle()+"%");
		if(model.getDriver()!=null)
			helper.addWhereCondition("c.driver=?", model.getDriver());		
		helper.addWhereCondition("c.status=?", CarStatusEnum.NORMAL);
		helper.addOrderByProperty("c.id", false);
		PageBean<Car> pageBean = carService.queryCar(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);	
		ActionContext.getContext().getSession().put("carHelper", helper);
		
		return "list";
	}
	
	public String list(){
		QueryHelper helper = new QueryHelper("Car", "c");
		helper.addWhereCondition("c.status=?", CarStatusEnum.NORMAL);
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
	
	public String queryUI(){
		ActionContext.getContext().put("carServiceTypeList", carService.getAllCarServiceType());
		ActionContext.getContext().put("servicePointList", carService.getAllServicePoint());	
		return "queryUI";
	}
	
	/*详细查询*/
	public String moreQuery(){
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
		/*业务点查询*/
		if(model.getServicePoint() != null && model.getServicePoint().getName() != null && !"".equals(model.getServicePoint().getName()))
			helper.addWhereCondition("c.servicePoint.name like ?", "%"+model.getServicePoint().getName()+"%");
		//车辆状态
		if(statusLabel == "正常" || statusLabel.equals("正常")){
			helper.addWhereCondition("c.status=?", CarStatusEnum.NORMAL);
		}
		if(statusLabel == "报废" || statusLabel.equals("报废")){
			helper.addWhereCondition("c.status=?", CarStatusEnum.SCRAPPED);
		}
		//车牌类型
		if(plateTypeLabel == "蓝牌" || plateTypeLabel.equals("蓝牌")){
			helper.addWhereCondition("c.plateType=?", PlateTypeEnum.BLUE);
		}
		if(plateTypeLabel == "黄牌" || plateTypeLabel.equals("黄牌")){
			helper.addWhereCondition("c.plateType=?", PlateTypeEnum.YELLOW);
		}
		//变速箱类型
		if(transmissionTypeLabel == "自动" || transmissionTypeLabel.equals("自动")){
			helper.addWhereCondition("c.transmissionType=?", TransmissionTypeEnum.AUTO);
		}
		if(transmissionTypeLabel == "手动" || transmissionTypeLabel.equals("手动")){
			helper.addWhereCondition("c.transmissionType=?", TransmissionTypeEnum.MANNUAL);
		}
		if(transmissionTypeLabel == "不确定" || transmissionTypeLabel.equals("不确定")){
			helper.addWhereCondition("c.transmissionType=?", TransmissionTypeEnum.UNKNOWN);
		}
		//品牌
		if(model.getBrand() != null && !"".equals(model.getBrand())){
			helper.addWhereCondition("c.brand like ?","%"+model.getBrand()+"%" );
		}
		//型号
		if(model.getModel() != null && !"".equals(model.getModel())){
			helper.addWhereCondition("c.model like ?", "%"+model.getModel()+"%");
		}
		//车架号
		if(model.getVIN() != null && !"".equals(model.getVIN())){
			helper.addWhereCondition("c.VIN like ?", "%"+model.getVIN()+"%");
		}
		//发动机号
		if(model.getEngineSN() != null && !"".equals(model.getEngineSN())){
			helper.addWhereCondition("c.EngineSN like ?", "%"+model.getEngineSN()+"%");
		}
		//SN号
		if(model.getDevice() != null && model.getDevice().getSN() != null && !"".equals(model.getDevice().getSN())){
			helper.addWhereCondition("c.device.SN like ?", "%"+model.getDevice().getSN()+"%");
		}
		//路桥卡卡号
		if(model.getTollChargeSN() != null && !"".equals(model.getTollChargeSN())){
			System.out.println("路桥卡="+model.getTollChargeSN());
			helper.addWhereCondition("c.tollChargeSN like ?", "%"+model.getTollChargeSN()+"%");
		}
		//备注
		if(model.getMemo() != null && !"".equals(model.getMemo())){
			helper.addWhereCondition("c.memo like ?", "%"+model.getMemo()+"%");
		}
		//注册时间
		if(registDate1!=null && registDate2!=null)
			helper.addWhereCondition("(TO_DAYS(c.registDate)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(c.registDate))>=0", 
					registDate1 ,registDate2);
		else if(registDate1==null && registDate2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(c.registDate))>=0", registDate2);
		else if(registDate1!=null && registDate2==null)
			helper.addWhereCondition("(TO_DAYS(c.registDate)-TO_DAYS(?))>=0", registDate1);
		//登记时间
		if(enrollDate1!=null && enrollDate2!=null)
			helper.addWhereCondition("(TO_DAYS(c.enrollDate)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(c.enrollDate))>=0", 
					enrollDate1 ,enrollDate2);
		else if(enrollDate1==null && enrollDate2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(c.enrollDate))>=0", enrollDate2);
		else if(enrollDate1!=null && enrollDate2==null)
			helper.addWhereCondition("(TO_DAYS(c.enrollDate)-TO_DAYS(?))>=0", enrollDate1);
		//里程数
		if(mileage1!=0 && mileage2!=0)
			helper.addWhereCondition("(c.mileage-?)>=0 and (?-c.mileage)>=0", 
					mileage1 ,mileage2);
		else if(mileage1==0 && mileage2!=0)
			helper.addWhereCondition("(?-c.mileage)>=0", mileage2);
		else if(mileage1!=0 && mileage2==0)
			helper.addWhereCondition("(c.mileage-?)>=0", mileage1);
		//座位数
		if(seatNumber1!=0 && seatNumber2!=0)
			helper.addWhereCondition("(c.seatNumber-?)>=0 and (?-c.seatNumber)>=0", 
					seatNumber1 ,seatNumber2);
		else if(seatNumber1==0 && seatNumber2!=0)
			helper.addWhereCondition("(?-c.seatNumber)>=0", seatNumber2);
		else if(seatNumber1!=0 && seatNumber2==0)
			helper.addWhereCondition("(c.seatNumber-?)>=0", seatNumber1);
		//是否备用车
		if(standbyCarLabel == "是" || "是".equals(standbyCarLabel)){
			helper.addWhereCondition("c.standbyCar =?", true);
		}
		if(standbyCarLabel == "否" || "否".equals(standbyCarLabel)){
			helper.addWhereCondition("c.standbyCar =?", false);
		}
		//是否年检过期
		if(examineExpiredLabel == "是" || "是".equals(examineExpiredLabel)){
			helper.addWhereCondition("c.examineExpired =?", true);
		}
		if(examineExpiredLabel == "否" || "否".equals(examineExpiredLabel)){
			helper.addWhereCondition("c.examineExpired =?", false);
		}
		//是否保养过期
		if(careExpiredLabel == "是" || "是".equals(careExpiredLabel)){
			helper.addWhereCondition("c.careExpired =?", true);
		}
		if(careExpiredLabel == "否" || "否".equals(careExpiredLabel)){
			helper.addWhereCondition("c.careExpired =?", false);
		}
		//是否路桥费过期
		if(tollChargeExpiredLabel == "是" || "是".equals(tollChargeExpiredLabel)){
			helper.addWhereCondition("c.tollChargeExpired =?", true);
		}
		if(tollChargeExpiredLabel == "否" || "否".equals(tollChargeExpiredLabel)){
			helper.addWhereCondition("c.tollChargeExpired =?", false);
		}
		//是否保险过期
		if(insuranceExpiredLabel == "是" || "是".equals(insuranceExpiredLabel)){
			helper.addWhereCondition("c.insuranceExpired =?", true);
		}
		if(insuranceExpiredLabel == "否" || "否".equals(insuranceExpiredLabel)){
			helper.addWhereCondition("c.insuranceExpired =?", false);
		}
		helper.addOrderByProperty("c.id", false);
		PageBean<Car> pageBean = carService.queryCar(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);	
		ActionContext.getContext().getSession().put("carHelper", helper);
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
	
	/** 添加外调车页面 */
	public String borrowedUI() throws Exception {
		// 准备数据：carServiceTypeList
		ActionContext.getContext().put("carServiceTypeList", carService.getAllCarServiceType());
		// 准备数据：servicePointList
	    ActionContext.getContext().put("servicePointList", carService.getAllServicePoint());	    
		return "borrowedUI";
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
		model.setServiceType(carService.getCarServiceTypeById(carServiceTypeId));
		model.setServicePoint(carService.getServicePointById(servicePointId));
		model.setStatus(CarStatusEnum.NORMAL);
		model.setPlateType(PlateTypeEnum.getById(plateTypeId));
		model.setTransmissionType(TransmissionTypeEnum.getById(transmissionTypeId));
		if(StringUtils.isEmpty(model.getTollChargeSN()))
			model.setTollChargeSN(null);
		// 保存到数据库
		carService.saveCar(model);
		ActionContext.getContext().getValueStack().push(new Car());
		return freshList();
	}
	
	//外调车辆
	public String borrowed() throws Exception {
		if(carService.isPlateNumberExist(0, model.getPlateNumber())){
				addFieldError("plateNumber", "车牌号已经存在！");
				return borrowedUI();
		}
		// 封装对象
		model.setServiceType(carService.getCarServiceTypeById(carServiceTypeId));
		model.setServicePoint(carService.getServicePointById(servicePointId));
		model.setStatus(CarStatusEnum.NORMAL);
		model.setStandbyCar(true);
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
		car.setTollChargeSN(StringUtils.isEmpty(model.getTollChargeSN()) ? null : model.getTollChargeSN());
		car.setEnrollDate(model.getEnrollDate());
		car.setSeatNumber(model.getSeatNumber());
		car.setPlateType(PlateTypeEnum.getById(plateTypeId));
		car.setTransmissionType(TransmissionTypeEnum.getById(transmissionTypeId));
		car.setRegistDate(model.getRegistDate());
		car.setDriver(model.getDriver());
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
					child.setId(c.getId());
					if(!StringUtils.isEmpty(synchDriver)){
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
	
	public boolean isViolationExist(){
		Car car=(Car)ActionContext.getContext().getValueStack().peek();
		QueryHelper helper = new QueryHelper("CarViolation", "cv");
		helper.addWhereCondition("cv.car=?", car);
		List<CarViolation> list=carViolationDao.getAllQuerry(helper);
		boolean exist=false;
		if(list!=null && list.size()>0){
			for(CarViolation cv:list)
				if(!cv.isDealt()){
					exist=true;
					break;
				}
		}
		return exist;
	}
	
	public String carDetail(){
		Car car = carService.getCarById(model.getId());
		unDoneAppointRepair = carRepairService.getUnDoneAppointRepair(car);
		unDoneAppointExamine = carExamineService.getUnDoneAppointExamine(car);
		unDoneAppointCare = carCareService.getUnDoneAppointCare(car);
		ActionContext.getContext().getValueStack().push(car);
		return "detail";
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
	
	public String getSelectorName() {
		return selectorName;
	}
	public void setSelectorName(String selectorName) {
		this.selectorName = selectorName;
	}
	public String getSynchDriver() {
		return synchDriver;
	}
	public void setSynchDriver(String synchDriver) {
		this.synchDriver = synchDriver;
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
	
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	
	public String getPlateTypeLabel() {
		return plateTypeLabel;
	}
	public void setPlateTypeLabel(String plateTypeLabel) {
		this.plateTypeLabel = plateTypeLabel;
	}
	
	public String getTransmissionTypeLabel() {
		return transmissionTypeLabel;
	}
	public void setTransmissionTypeLabel(String transmissionTypeLabel) {
		this.transmissionTypeLabel = transmissionTypeLabel;
	}
	
	public String getCareExpiredLabel() {
		return careExpiredLabel;
	}
	public void setCareExpiredLabel(String careExpiredLabel) {
		this.careExpiredLabel = careExpiredLabel;
	}
	public String getInsuranceExpiredLabel() {
		return insuranceExpiredLabel;
	}
	public void setInsuranceExpiredLabel(String insuranceExpiredLabel) {
		this.insuranceExpiredLabel = insuranceExpiredLabel;
	}
	public String getExamineExpiredLabel() {
		return examineExpiredLabel;
	}
	public void setExamineExpiredLabel(String examineExpiredLabel) {
		this.examineExpiredLabel = examineExpiredLabel;
	}
	public String getTollChargeExpiredLabel() {
		return tollChargeExpiredLabel;
	}
	public void setTollChargeExpiredLabel(String tollChargeExpiredLabel) {
		this.tollChargeExpiredLabel = tollChargeExpiredLabel;
	}
	public String getStandbyCarLabel() {
		return standbyCarLabel;
	}
	public void setStandbyCarLabel(String standbyCarLabel) {
		this.standbyCarLabel = standbyCarLabel;
	}
	public CarRepair getUnDoneAppointRepair() {
		return unDoneAppointRepair;
	}
	public void setUnDoneAppointRepair(CarRepair unDoneAppointRepair) {
		this.unDoneAppointRepair = unDoneAppointRepair;
	}
	public CarCare getUnDoneAppointCare() {
		return unDoneAppointCare;
	}
	public void setUnDoneAppointCare(CarCare unDoneAppointCare) {
		this.unDoneAppointCare = unDoneAppointCare;
	}
	public CarExamine getUnDoneAppointExamine() {
		return unDoneAppointExamine;
	}
	public void setUnDoneAppointExamine(CarExamine unDoneAppointExamine) {
		this.unDoneAppointExamine = unDoneAppointExamine;
	}
	public Date getRegistDate1() {
		return registDate1;
	}
	public void setRegistDate1(Date registDate1) {
		this.registDate1 = registDate1;
	}
	public Date getRegistDate2() {
		return registDate2;
	}
	public void setRegistDate2(Date registDate2) {
		this.registDate2 = registDate2;
	}
	public Date getEnrollDate1() {
		return enrollDate1;
	}
	public void setEnrollDate1(Date enrollDate1) {
		this.enrollDate1 = enrollDate1;
	}
	public Date getEnrollDate2() {
		return enrollDate2;
	}
	public void setEnrollDate2(Date enrollDate2) {
		this.enrollDate2 = enrollDate2;
	}
	public int getMileage1() {
		return mileage1;
	}
	public void setMileage1(int mileage1) {
		this.mileage1 = mileage1;
	}
	public int getMileage2() {
		return mileage2;
	}
	public void setMileage2(int mileage2) {
		this.mileage2 = mileage2;
	}
	public int getSeatNumber1() {
		return seatNumber1;
	}
	public void setSeatNumber1(int seatNumber1) {
		this.seatNumber1 = seatNumber1;
	}
	public int getSeatNumber2() {
		return seatNumber2;
	}
	public void setSeatNumber2(int seatNumber2) {
		this.seatNumber2 = seatNumber2;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}	
}
